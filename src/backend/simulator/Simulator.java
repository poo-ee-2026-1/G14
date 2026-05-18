package backend.simulator;

import backend.config.SimulationConfig;
import backend.fleetmanager.FleetManager;
import backend.fleetmanager.Vehicle;
import backend.stationmanager.ChargingPoint;
import backend.stationmanager.StationManager;

// Classe principal responsável por controlar toda a simulação.
public class Simulator {

// =========================
// ATRIBUTOS DE ESTADO
// =========================

    // Tempo atual acumulado da simulação
    private double simulationTime;

    // Número do ciclo atual
    private int cycleNumber;

    // Quantidade de veículos atendidos
    private int vehiclesServed;

    // Quantidade total de energia fornecida
    @SuppressWarnings("unused")
    private double totalEnergyDelivered;

// =========================
// MÓDULOS DO SISTEMA
// =========================

    // Gerenciador da frota de veículos
    private final FleetManager fleetManager;

    // Gerenciador das estações de carregamento
    private final StationManager stationManager;

    // Fila temporal de eventos
    private final EventQueue eventQueue;

    // Sistema de logs da simulação
    private final EventLogger eventLogger;

    // Relatório atual da simulação
    private final SimulationReport currentReport;

    // Indica se o ciclo está ativo
    private boolean cycleActive;

// =========================
// CONSTRUTOR
// =========================

    public Simulator() {

        this.simulationTime = 0.0;
        this.cycleNumber = 0;
        this.vehiclesServed = 0;
        this.totalEnergyDelivered = 0.0;

        this.fleetManager = new FleetManager();
        this.stationManager = new StationManager();
        this.eventQueue = new EventQueue();
        this.eventLogger = new EventLogger();
        this.currentReport = new SimulationReport();

        this.cycleActive = false;
    }

// =========================
// LOOP PRINCIPAL DA SIMULAÇÃO
// =========================

    // Avança a simulação processando todos os eventos
    public void update(double deltaSimulationTime) {

        // Avança o relógio da simulação
        this.simulationTime += deltaSimulationTime;

        // Processa todos os eventos pendentes até o tempo atual
        while (
                !eventQueue.isEmpty()
                &&
                eventQueue.peekNextEvent()
                        .getTimestamp()
                        <= this.simulationTime
        ) {
            Event nextEvent = eventQueue.nextEvent();
            processEvent(nextEvent);
        }
    }

// =========================
// CONTROLE DA SIMULAÇÃO
// =========================

    // Inicializa a simulação e agenda chegadas
    public void initializeSimulation() {

        System.out.println("Simulador inicializado.");

        // Agenda chegadas de todos os veículos
        scheduleArrivals();

        // Inicia primeiro ciclo
        startCycle();
    }

    // Inicia um novo ciclo
    public void startCycle() {

        if (cycleActive) {
            System.out.println("Um ciclo já está em andamento.");
            return;
        }

        cycleActive = true;
        cycleNumber++;
    }

    // Finaliza o ciclo atual
    public void endCycle() {

        if (!cycleActive) {
            System.out.println("Nenhum ciclo ativo para finalizar.");
            return;
        }

        eventLogger.log(
                "Ciclo "
                        + cycleNumber
                        + " finalizado. Veículos atendidos: "
                        + vehiclesServed
        );

        cycleActive = false;
    }

// =========================
// AGENDAMENTO DE CHEGADAS
// =========================

    // Agenda a chegada de todos os veículos
    private void scheduleArrivals() {

    double time = 0;

    for (Vehicle vehicle : fleetManager.getVehicles()) {

        // No modo stress todos chegam no mesmo instante
        if (!SimulationConfig.STRESS_TEST_MODE) {
            time += SimulationConfig.ARRIVAL_INTERVAL;
        }

        scheduleEvent(new Event(
                time,
                EventType.VEHICLE_ARRIVAL,
                vehicle
        ));
    }
    }

// =========================
// PROCESSAMENTO DE EVENTOS
// =========================

    public void processEvent(Event event) {

        if (event == null) return;

        Vehicle vehicle = event.getVehicle();

        if (vehicle == null) return;

        switch (event.getType()) {

            case VEHICLE_ARRIVAL -> {

                // Registra chegada no log
                eventLogger.log(
                        String.format(
                                "[Chegada] Veículo #%d (%s, %s) chegou no tempo %.1fs (Bateria: %.1f%%)",
                                vehicle.getId(),
                                vehicle.getModel().getName(),
                                vehicle.getColor(),
                                event.getTimestamp(),
                                vehicle.getStateOfCharge()
                        )
                );

                // Adiciona veículo na fila
                stationManager.addVehicleToQueue(vehicle);

                // Tenta conectar imediatamente
                // se houver ponto livre
                tryStartCharging(vehicle, event.getTimestamp());
            }

            case START_CHARGING -> {

                // Registra início do carregamento
                eventLogger.log(
                        String.format(
                                "[Carga] Veículo #%d iniciou carregamento no totem #%d.",
                                vehicle.getId(),
                                event.getChargingPointId()
                        )
                );

                // Executa início do carregamento
                stationManager.processChargingStart(event);

                // Busca o ponto de carregamento
                ChargingPoint point = stationManager
                        .getPointById(event.getChargingPointId());

                if (point == null) return;

                // Usa potência do ponto ou padrão da config
                double power = point.getCurrentPower() > 0
                        ? point.getCurrentPower()
                        : SimulationConfig.DEFAULT_CHARGING_POWER;

                // Calcula duração do carregamento
                double chargingTime =
                        vehicle.calculateRemainingChargingTime(power);

                // Calcula timestamp de término
                double finishTimestamp =
                        event.getTimestamp() + chargingTime;

                // Agenda fim do carregamento
                scheduleEvent(new Event(
                        finishTimestamp,
                        EventType.FINISH_CHARGING,
                        vehicle,
                        point.getId()

                
                ));
                // Agenda rebalanceamento
             scheduleEvent(new Event(
            event.getTimestamp(),
            EventType.POWER_REBALANCE,
            null));
            }

            case FINISH_CHARGING -> {

                // Registra finalização
                eventLogger.log(
                        String.format(
                                "[Sucesso] Veículo #%d carregou 100%%",
                                vehicle.getId()
                        )
                );

                // Finaliza carregamento
                stationManager.processChargingEnd(event);

                // Incrementa contador
                vehiclesServed++;

                // Tenta conectar próximo da fila no ponto que ficou livre
                Vehicle next = stationManager.getNextInQueue();

                if (next != null) {
                    scheduleEvent(new Event(
                            this.simulationTime,
                            EventType.START_CHARGING,
                            next,
                            event.getChargingPointId()
                    ));

                     // Agenda rebalanceamento
    scheduleEvent(new Event(
            this.simulationTime,
            EventType.POWER_REBALANCE,
            null
    ));
                }
            }
            case POWER_REBALANCE -> {

    boolean rebalanced =
            stationManager.rebalancePower();

    if (rebalanced) {

        eventLogger.log(
            "[Rebalanceamento] Potência redistribuída:"
        );

        eventLogger.log(
            "  Estado dos totens atualizado."
        );

    } else {

        eventLogger.log(
            "[Estável] Distribuição de potência inalterada."
        );
    }
}

            default -> System.out.println(
                    "Evento desconhecido: " + event.getType()
            );
        }
    }

// =========================
// AGENDAMENTO DE EVENTOS
// =========================

    public void scheduleEvent(Event event) {

        eventQueue.addEvent(event);

        eventLogger.log(
                "Evento agendado: "
                        + event.getType()
                        + " para o tempo "
                        + event.getTimestamp()
        );
    }

// =========================
// CONEXÃO IMEDIATA
// ========================

    // Tenta agendar START_CHARGING imediatamente
private void tryStartCharging(
        Vehicle vehicle,
        double currentTime
) {

    // Verifica se há totem livre
    ChargingPoint point =
            stationManager.getAvailablePoint();

    if (point == null) return;

    // Verifica se a estação tem
    // capacidade de potência para o veículo
    if (!stationManager.hasCapacityFor(vehicle)) {

        // Sem capacidade — permanece na fila
        eventLogger.log(
                String.format(
                        "[Fila] Veículo #%d aguarda — estação sem capacidade de potência.",
                        vehicle.getId()
                )
        );

        return;
    }

    // Totem livre e capacidade disponível
    point.reserve();

    scheduleEvent(new Event(
            currentTime,
            EventType.START_CHARGING,
            vehicle,
            point.getId()
    ));
}
// =========================
// RELATÓRIOS
// =========================

    public void generateReport() {

        if (currentReport != null) {
            System.out.println(currentReport.generateReport());
        }
    }

// =========================
// RESET DO SISTEMA
// =========================

    public void reset() {

        this.simulationTime = 0.0;
        this.cycleNumber = 0;
        this.vehiclesServed = 0;
        this.totalEnergyDelivered = 0.0;

        this.fleetManager.reset();
        this.stationManager.reset();
        this.eventQueue.clear();
        this.eventLogger.clearLog();

        if (this.currentReport != null) {
            this.currentReport.reset();
        }

        this.cycleActive = false;
    }
}