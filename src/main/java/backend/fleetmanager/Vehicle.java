package backend.fleetmanager;

// Classe que representa um veículo elétrico dentro da simulação.
public class Vehicle implements Comparable<Vehicle> {

    // ATRIBUTOS

    private final int id;
    private final VehicleModel model;
    private final CarColor color;

    private double stateOfCharge;
    private VehicleStatus status;

    private final double arrivalTime;

    private double waitingTime;
    private double priorityScore;

    // CONSTRUTOR PRINCIPAL
    public Vehicle(
            int id,
            VehicleModel model,
            CarColor color,
            double stateOfCharge,
            double arrivalTime
    ) {
        this.id = id;
        this.model = model;
        this.color = color;

        this.stateOfCharge = Math.max(
                0.0,
                Math.min(stateOfCharge, 100.0)
        );

        this.status = VehicleStatus.ARRIVING;

        this.arrivalTime = arrivalTime;

        this.waitingTime = 0.0;
        this.priorityScore = 0.0;
    }

    // CONSTRUTOR AUXILIAR PARA TESTES
    public Vehicle(
            int id,
            double stateOfCharge
    ) {
        this.id = id;

        this.model = new CompactModel();
        this.color = CarColor.PRATA;

        this.stateOfCharge = Math.max(
                0.0,
                Math.min(stateOfCharge, 100.0)
        );

        this.status = VehicleStatus.ARRIVING;

        this.arrivalTime = 0.0;

        this.waitingTime = 0.0;
        this.priorityScore = 0.0;
    }

    // ORDENAÇÃO

    @Override
    public int compareTo(Vehicle other) {
        int priorityComparison = Double.compare(
                other.getPriorityScore(),
                this.priorityScore
        );

        if (priorityComparison != 0) {
            return priorityComparison;
        }

        return Double.compare(
                this.arrivalTime,
                other.arrivalTime
        );
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

        double energyNeeded = getEnergyNeeded();

        double hours = energyNeeded / chargingPower;

        return hours * 3600.0;
    }

    public void finishCharging() {
        this.stateOfCharge = 100.0;
        this.status = VehicleStatus.FINISHED;
        System.out.println("[DEBUG] Veículo " + id + " finalizou o carregamento. SoC: " + stateOfCharge);
    }

    // ENERGIA

    public double getEnergyNeeded() {
        if (this.model == null) {
            return 0.0;
        }

        double currentEnergy = (this.stateOfCharge / 100.0) * this.model.getBatteryCapacity();
        return this.model.getBatteryCapacity() - currentEnergy;
    }

    public void updateEnergy(double actualEnergyKwh) {
        if (this.model == null || this.model.getBatteryCapacity() <= 0) {
            return;
        }
        System.out.println(
    "[SOC] Veículo "
    + id
    + " | SoC atual="
    + stateOfCharge
    + "%"
    + " | Energia adicionada="
    + actualEnergyKwh
);

        double currentEnergy = (this.stateOfCharge / 100.0) * this.model.getBatteryCapacity();
        double newEnergy = currentEnergy + actualEnergyKwh;
        double newSoC = (newEnergy / this.model.getBatteryCapacity()) * 100.0;

        System.out.println("[DEBUG] Veículo " + id + " atualizando energia: atual=" + currentEnergy + ", acrescido=" + actualEnergyKwh + ", SoC=" + newSoC);

        this.stateOfCharge = Math.min(newSoC, 100.0);

        if (this.stateOfCharge >= 100.0) {
            finishCharging();
        }
    }

    // GETTERS

    public int getId() {
        return id;
    }

    public VehicleModel getModel() {
        return model;
    }

    public CarColor getColor() {
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

    // SETTERS

    public void setStateOfCharge(double stateOfCharge) {
        this.stateOfCharge = Math.max(0.0, Math.min(stateOfCharge, 100.0));
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setPriorityScore(double priorityScore) {
        this.priorityScore = priorityScore;
    }

}
   