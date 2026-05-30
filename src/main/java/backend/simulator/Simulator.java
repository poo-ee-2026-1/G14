package backend.simulator;

import java.util.PriorityQueue;

import backend.config.SimulationConfig;
import backend.fleetmanager.FleetManager;
import backend.fleetmanager.Vehicle;
import backend.fleetmanager.VehicleModel;
import backend.stationmanager.ChargingPoint;
import backend.stationmanager.StationManager;

// Classe principal do motor de simulação discreta e contínua.
public class Simulator {

    // ATRIBUTOS
    private double simulationTime;
    private final PriorityQueue<Event> eventQueue;
    private final StationManager stationManager;
    private final FleetManager fleetManager;
    private boolean isRunning;

    // CONSTRUTOR


    public Simulator() {
        this.simulationTime = 0.0;
        this.eventQueue = new PriorityQueue<>();
        this.stationManager = new StationManager();
        this.fleetManager = new FleetManager();
        this.isRunning = false;
    }

    // INICIALIZAÇÃO

    public void initializeSimulation() {
        this.simulationTime = 0.0;
        this.eventQueue.clear();
        this.stationManager.reset();
        this.fleetManager.reset();
        this.isRunning = true;

        System.out.println("Simulador inicializado.");

        // 1. Agenda a frota vinda do FleetManager na linha do tempo
        scheduleArrivals();

        // 2. Injeção de segurança para carregar a UI com dados simulados reais logo no início
        try {
            if (!fleetManager.getAvailableModels().isEmpty()) {
                // Captura um modelo de veículo real existente no seu ecossistema para evitar erros de construtor
                VehicleModel modeloReal = fleetManager.getAvailableModels().get(0);
                
                Vehicle carroTeste1 = new Vehicle(99, modeloReal, "Vermelho", 20.0, 0.0);
                Vehicle carroTeste2 = new Vehicle(100, modeloReal, "Prata", 45.0, 0.0);

                ChargingPoint posto1 = this.stationManager.getPointById(1);
                ChargingPoint posto2 = this.stationManager.getPointById(2);

                if (posto1 != null) posto1.connectVehicle(carroTeste1);
                if (posto2 != null) posto2.connectVehicle(carroTeste2);

                this.stationManager.rebalancePower();
                System.out.println("[DEBUG] Cenário de teste acoplado nos postos com modelos reais.");
            }
        } catch (Exception e) {
            System.err.println("Aviso ao injetar dados iniciais na UI: " + e.getMessage());
        }
    }


    // Lógica padrão de agendamento 
    private void scheduleArrivals() {
        double time = 0.0;

        for (Vehicle vehicle : fleetManager.getVehicles()) {
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

    // CICLO DE EXECUÇÃO DO LOOP DE ATUALIZAÇÃO CONTÍNUA)
    
    public void update(double deltaTime) {
        if (!isRunning) return;

        // Avança o relógio mestre da simulação
        this.simulationTime += deltaTime;

        while (!eventQueue.isEmpty() && eventQueue.peek().getTimestamp() <= this.simulationTime) {
            Event event = eventQueue.poll();
            processEvent(event);
        }

        // Executa o algoritmo contínuo de divisão de potência da subestação
        this.stationManager.rebalancePower();

        // 3. Incrementa a recarga dos carros frame a frame
        updateContinuousCharging(deltaTime);
    }

     // Abastece continuamente as baterias dos veículos que estão conectados aos totens ativos.
    private void updateContinuousCharging(double deltaTime) {
        double deltaHours = deltaTime / 3600.0;

        for (int i = 1; i <= SimulationConfig.MAX_CHARGING_POINTS; i++) {
            ChargingPoint point = stationManager.getPointById(i);
            
            if (point != null && point.isOccupied()) {
                Vehicle vehicle = point.getConnectedVehicle();
                if (vehicle != null) {
                    double currentPower = point.getCurrentPower() > 0 
                            ? point.getCurrentPower() 
                            : SimulationConfig.DEFAULT_CHARGING_POWER;

                    double energyTransferred = currentPower * deltaHours;
                    vehicle.updateEnergy(energyTransferred);
                }
            }
        }
    }

    // PROCESSAMENTO DE EVENTOS
 
    private void processEvent(Event event) {
        if (event == null) return;

        switch (event.getType()) {
            case VEHICLE_ARRIVAL -> handleVehicleArrival(event.getVehicle());
                
            case START_CHARGING -> {
                stationManager.processChargingStart(event);
                
                Vehicle vehicle = event.getVehicle();
                ChargingPoint point = stationManager.getPointById(event.getChargingPointId());
                
                if (point == null || vehicle == null) return;

                vehicle.setStatus(backend.fleetmanager.VehicleStatus.CHARGING);
                
                double power = point.getCurrentPower() > 0
                        ? point.getCurrentPower() 
                        : SimulationConfig.DEFAULT_CHARGING_POWER;

                double chargingTime = vehicle.calculateRemainingChargingTime(power);
                
                // CORREÇÃO: Trocado getTime() por getTimestamp()
                double finishTimestamp = event.getTimestamp() + chargingTime;

                scheduleEvent(new Event(finishTimestamp, EventType.FINISH_CHARGING, vehicle, point.getId()));
            }

            case FINISH_CHARGING -> {
                stationManager.processChargingEnd(event);
                checkQueueAndCharge();
            }
                
            default -> {
            }
        }
    }

    private void handleVehicleArrival(Vehicle vehicle) {
        if (vehicle == null) return;

        if (stationManager.hasCapacityFor(vehicle)) {
            ChargingPoint availablePoint = stationManager.getAvailablePoint();
            if (availablePoint != null) {
                Event startEvent = new Event(this.simulationTime, EventType.START_CHARGING, vehicle, availablePoint.getId());
                processEvent(startEvent);
                return;
            }
        }
        vehicle.setStatus(backend.fleetmanager.VehicleStatus.WAITING);
        stationManager.addVehicleToQueue(vehicle);
    }

    private void checkQueueAndCharge() {
        while (!stationManager.getWaitingQueue().isEmpty()) {
            Vehicle nextVehicle = stationManager.getNextInQueue();
            if (nextVehicle == null) break;

            if (stationManager.hasCapacityFor(nextVehicle)) {
                ChargingPoint availablePoint = stationManager.getAvailablePoint();
                if (availablePoint != null) {
                    Event startEvent = new Event(this.simulationTime, EventType.START_CHARGING, nextVehicle, availablePoint.getId());
                    processEvent(startEvent);
                } else {
                    nextVehicle.setStatus(backend.fleetmanager.VehicleStatus.WAITING);
                    stationManager.addVehicleToQueue(nextVehicle);
                    break;
                }
            } else {
                nextVehicle.setStatus(backend.fleetmanager.VehicleStatus.WAITING);
                stationManager.addVehicleToQueue(nextVehicle);
                break;
            }
        }
    }

  
    // GETTERS & UTILS

    public void scheduleEvent(Event event) {
        if (event != null) {
            eventQueue.add(event);
        }
    }

    public StationManager getStationManager() { return this.stationManager; }
    public double getSimulationTime() { return this.simulationTime; }
    public boolean isRunning() { return this.isRunning; }
    public FleetManager getFleetManager() { return this.fleetManager; }
}