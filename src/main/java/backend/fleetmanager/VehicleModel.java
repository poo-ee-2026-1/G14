package backend.fleetmanager;

// Classe abstrata responsável por representar um modelo de veículo elétrico.

public abstract class VehicleModel {

// ATRIBUTOS

    // Nome do modelo
    private final String name;

    // Capacidade máxima da bateria (kWh)
    private final double batteryCapacity;

    // Potência máxima suportada no carregamento (kW)
    private final double maxChargingPower;

    // Código do modelo
    private final String modelCode;

// CONSTRUTOR

    // Construtor base do modelo
    public VehicleModel(
            String name,
            double batteryCapacity,
            double maxChargingPower,
            String modelCode
    ) {

        // Define nome
        this.name = name;

        // Garante capacidade válida
        this.batteryCapacity =
                Math.max(
                        batteryCapacity,
                        0.0
                );

        // Garante potência válida
        this.maxChargingPower =
                Math.max(
                        maxChargingPower,
                        0.0
                );

        // Define código do modelo
        this.modelCode = modelCode;
    }

// MÉTODOS 

    // Cada modelo deve possuir sua própria descrição.
    public abstract String
    getModelDescription();

// CONTADOR DE INSTÂNCIAS

private int instanceCount = 0;

public int generateNextId() {

    instanceCount++;

    int idBase = Integer.parseInt(modelCode);

    return (idBase * 100) + instanceCount;
}

// Reinicia contador do modelo
public void resetInstanceCount() {

    instanceCount = 0;
}

// GETTERS

    // Retorna nome do modelo
    public String getName() {

        return name;
    }

    // Retorna capacidade da bateria
    public double getBatteryCapacity() {

        return batteryCapacity;
    }

    // Retorna potência máxima
    public double getMaxChargingPower() {

        return maxChargingPower;
    }

    // Retorna código do modelo
    public String getModelCode() {

        return modelCode;
    }
}