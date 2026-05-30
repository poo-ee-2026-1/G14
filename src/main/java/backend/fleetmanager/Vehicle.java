package backend.fleetmanager;


 // Classe que representa um veículo elétrico dentro da simulação.

public class Vehicle implements Comparable<Vehicle> {

    // ATRIBUTOS

    private final int id;
    private final VehicleModel model;
    private final String color;
    private double stateOfCharge;
    private VehicleStatus status;
    private final double arrivalTime;
    private double waitingTime;
    private double priorityScore;

    // CONSTRUTOR

    public Vehicle(
            int id,
            VehicleModel model,
            String color,
            double stateOfCharge,
            double arrivalTime
    ) {
        this.id = id;
        this.model = model;
        this.color = color;
        
        // Garante SoC válido
        this.stateOfCharge = Math.max(0.0, Math.min(stateOfCharge, 100.0));
        this.status = VehicleStatus.ARRIVING;
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0.0;
        this.priorityScore = 0.0;
    }

     // CONSTRUTOR DE SOBRECARGA PARA TESTES

    public Vehicle(int id, double stateOfCharge) {
        this.id = id;
        // Instancia um modelo compacto de fallback para não dar NullPointerException
        this.model = new CompactModel(); 
        this.color = "Prata";
        this.stateOfCharge = Math.max(0.0, Math.min(stateOfCharge, 100.0));
        this.status = VehicleStatus.ARRIVING;
        this.arrivalTime = 0.0;
        this.waitingTime = 0.0;
        this.priorityScore = 0.0;
    }

    // ORDENAÇÃO

    @Override
    public int compareTo(Vehicle other) {
        // Compara score de prioridade (maior prioridade vem primeiro)
        int priorityComparison = Double.compare(other.getPriorityScore(), this.priorityScore);

        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // Desempata pelo horário de chegada antigo (Quem chegou antes tem prioridade)
        return Double.compare(this.arrivalTime, other.arrivalTime);
    }

    // TEMPO DE ESPERA

    public void updateWaitingTime(double currentTime) {
        if (this.status == VehicleStatus.WAITING) {
            this.waitingTime = currentTime - this.arrivalTime;
        }
    }

    // CARREGAMENTO

    public void startCharging(double currentTime) {
        this.status = VehicleStatus.CHARGING;
        this.waitingTime = currentTime - this.arrivalTime;
    }

    public double calculateRemainingChargingTime(double chargingPower) {
        if (chargingPower <= 0) {
            return Double.POSITIVE_INFINITY;
        }

        // Energia restante em kWh
        double energyNeeded = getEnergyNeeded();

        // Tempo em horas = energia / potência
        double hours = energyNeeded / chargingPower;

        // Retorna o equivalente em segundos (assume 3600 segundos por hora se não houver a constante)
        return hours * 3600.0;
    }

    public void finishCharging() {
        this.stateOfCharge = 100.0;
        this.status = VehicleStatus.FINISHED;
    }

    // ENERGIA

    public double getEnergyNeeded() {
        if (this.model == null) {
            return 0.0;
        }

        // Energia atual em kWh
        double currentEnergy = (this.stateOfCharge / 100.0) * this.model.getBatteryCapacity();

        // Retorna energia restante
        return this.model.getBatteryCapacity() - currentEnergy;
    }

    public void updateEnergy(double actualEnergyKwh) {
        if (this.model == null || this.model.getBatteryCapacity() <= 0) {
            return;
        }

        // Energia atual em kWh
        double currentEnergy = (this.stateOfCharge / 100.0) * this.model.getBatteryCapacity();

        // Nova energia após carregamento
        double newEnergy = currentEnergy + actualEnergyKwh;

        // Converte para porcentagem
        double newSoC = (newEnergy / this.model.getBatteryCapacity()) * 100.0;

        // Limita entre 0% e 100%
        this.stateOfCharge = Math.min(newSoC, 100.0);

        if (this.stateOfCharge >= 100.0) {
            finishCharging();
        }
    }

    // GETTERS E SETTERS

    public int getId() { return id; }
    public VehicleModel getModel() { return model; }
    public String getColor() { return color; }
    public double getStateOfCharge() { return stateOfCharge; }
    public VehicleStatus getStatus() { return status; }
    public double getWaitingTime() { return waitingTime; }
    public double getPriorityScore() { return priorityScore; }

    public void setStateOfCharge(double stateOfCharge) {
        this.stateOfCharge = Math.max(0.0, Math.min(stateOfCharge, 100.0));
    }

    public void setPriorityScore(double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}