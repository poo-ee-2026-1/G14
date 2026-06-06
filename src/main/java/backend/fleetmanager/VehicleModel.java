package backend.fleetmanager;

// Classe abstrata responsável por representar um modelo de veículo elétrico.

public abstract class VehicleModel {

    // ATRIBUTOS

    private final String name;
    private final double batteryCapacity;
    private final double maxChargingPower;
    private final String modelCode;
  

    // CONTADOR DE INSTÂNCIAS

    private int instanceCount = 0;

    // CONSTRUTOR

    public VehicleModel(
            String name,
            double batteryCapacity,
            double maxChargingPower,
            String modelCode
    ) {

        this.name = name;

        this.batteryCapacity =
                Math.max(
                        batteryCapacity,
                        0.0
                );

        this.maxChargingPower =
                Math.max(
                        maxChargingPower,
                        0.0
                );

        this.modelCode = modelCode;
    }

    // MÉTODOS ABSTRATOS

    public abstract String getModelDescription();

    // Índice visual usado para carregar PNG correto
    public abstract int getVisualIndex();

    // CONTADOR

    public int generateNextId() {

        instanceCount++;

        int idBase =
                Integer.parseInt(modelCode);

        return
                (idBase * 100)
                        +
                        instanceCount;
    }

    public void resetInstanceCount() {

        instanceCount = 0;
    }

    // GETTERS

    public String getName() {
        return name;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public double getMaxChargingPower() {
        return maxChargingPower;
    }

    public String getModelCode() {
        return modelCode;
    }
}