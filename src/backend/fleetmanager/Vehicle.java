package backend.fleetmanager;

import backend.config.SimulationConfig;

// Classe que representa um veículo elétrico dentro da simulação.
public class Vehicle
        implements Comparable<Vehicle> {

// =========================
// ATRIBUTOS
// =========================

    // Identificador único do veículo
    private final int id;

    // Modelo do veículo
    private final VehicleModel model;

    // Cor do veículo
    private final String color;

    // Estado de carga da bateria (0% a 100%)
    private double stateOfCharge;

    // Estado atual do veículo
    private VehicleStatus status;

    // Momento de chegada na estação
    private final double arrivalTime;

    // Tempo acumulado de espera
    private double waitingTime;

    // Score usado na fila de prioridade
    private double priorityScore;

// =========================
// CONSTRUTOR
// =========================

    // Construtor completo do veículo
    public Vehicle(
            int id,
            VehicleModel model,
            String color,
            double stateOfCharge,
            double arrivalTime
    ) {

        // Define ID
        this.id = id;

        // Define modelo
        this.model = model;

        // Define cor
        this.color = color;

        // Garante SoC válido
        this.stateOfCharge =
                Math.max(
                        0.0,
                        Math.min(stateOfCharge, 100.0)
                );

        // Veículo inicia chegando
        this.status = VehicleStatus.ARRIVING;

        // Define tempo de chegada
        this.arrivalTime = arrivalTime;

        // Tempo inicial de espera
        this.waitingTime = 0.0;

        // Prioridade inicial
        this.priorityScore = 0.0;
    }

// =========================
// ORDENAÇÃO
// =========================

    // Maior prioridade vem primeiro.
    @Override
    public int compareTo(
            Vehicle other
    ) {

        // Compara score de prioridade
        int priorityComparison =
                Double.compare(
                        other.getPriorityScore(),
                        this.priorityScore
                );

        // Se prioridades forem diferentes
        if (priorityComparison != 0) {

            return priorityComparison;
        }

        // Desempata pelo horário de chegada
        return Double.compare(
                this.arrivalTime,
                other.arrivalTime
        );
    }

// =========================
// TEMPO DE ESPERA
// =========================

    // Atualiza tempo de espera do veículo
    public void updateWaitingTime(
            double currentTime
    ) {

        // Apenas veículos esperando
        // acumulam tempo de espera
        if (this.status
                == VehicleStatus.WAITING) {

            this.waitingTime =
                    currentTime
                            - this.arrivalTime;
        }
    }

// =========================
// CARREGAMENTO
// =========================

    // Define início do carregamento
    public void startCharging(
        double currentTime
) {

    this.status =
            VehicleStatus.CHARGING;

    // Atualiza tempo de espera final
    this.waitingTime =
            currentTime
                    -
                    this.arrivalTime;
}
// Calcula quanto tempo falta para completar
// o carregamento com base na potência disponível
public double calculateRemainingChargingTime(
        double chargingPower
) {

    // Segurança contra potência inválida
    if (chargingPower <= 0) {
        return Double.POSITIVE_INFINITY;
    }

    // Energia restante em kWh
    double energyNeeded = getEnergyNeeded();

    // Tempo em horas = energia / potência
    double hours = energyNeeded / chargingPower;

    // Converte para segundos
    return hours * SimulationConfig.SECONDS_PER_HOUR;
}
    // Finaliza carregamento
public void finishCharging() {

    this.stateOfCharge = 100.0;

    this.status = VehicleStatus.FINISHED;
}

    // =========================
    // ENERGIA
    // =========================

    // Calcula quanta energia ainda falta
    // para atingir 100% da bateria.
    public double getEnergyNeeded() {

        // Segurança contra modelo inválido
        if (this.model == null) {

            return 0.0;
        }

        // Energia atual em kWh
        double currentEnergy =
                (
                        this.stateOfCharge
                                / 100.0
                )
                        * this.model
                        .getBatteryCapacity();

        // Retorna energia restante
        return this.model
                .getBatteryCapacity()
                - currentEnergy;
    }

    // Atualiza bateria após receber energia
    public void updateEnergy(
            double actualEnergyKwh
    ) {

        // Segurança contra modelo inválido
        if (
                this.model == null
                        ||
                        this.model
                                .getBatteryCapacity()
                                <= 0
        ) {

            return;
        }

        // Energia atual em kWh
        double currentEnergy =
                (
                        this.stateOfCharge
                                / 100.0
                )
                        * this.model
                        .getBatteryCapacity();

        // Nova energia após carregamento
        double newEnergy =
                currentEnergy
                        + actualEnergyKwh;

        // Converte para porcentagem
        double newSoC =
                (
                        newEnergy
                                / this.model
                                .getBatteryCapacity()
                ) * 100.0;

        // Limita entre 0% e 100%
        this.stateOfCharge =
                Math.min(newSoC, 100.0);

        // Se carregou totalmente
        if (this.stateOfCharge >= 100.0) {

            finishCharging();
        }
    }

// =========================
// GETTERS
// =========================

    public int getId() {

        return id;
    }

    public VehicleModel getModel() {

        return model;
    }

    public String getColor() {

        return color;
    }

    public double getStateOfCharge() {

        return stateOfCharge;
    }

    public VehicleStatus getStatus() {

        return status;
    }

    public double getWaitingTime() {

        return waitingTime;
    }

    public double getPriorityScore() {

        return priorityScore;
    }

// =========================
// SETTERS
// =========================

    // Atualiza estado de carga
    public void setStateOfCharge(
            double stateOfCharge
    ) {

        // Garante intervalo válido
        this.stateOfCharge =
                Math.max(
                        0.0,
                        Math.min(stateOfCharge, 100.0)
                );
    }

    // Atualiza score de prioridade
    public void setPriorityScore(
            double priorityScore
    ) {

        this.priorityScore =
                priorityScore;
    }

    // Atualiza status manualmente
    public void setStatus(
            VehicleStatus status
    ) {

        this.status = status;
    }
}