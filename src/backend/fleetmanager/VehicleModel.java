package backend.fleetmanager;

// Classe abstrata responsável por representar
// um modelo de veículo elétrico.
//
// Um VehicleModel descreve características
// compartilhadas entre vários veículos.
//
// Exemplos:
// - bateria
// - potência máxima
// - categoria
//
// Classes filhas podem especializar
// comportamentos específicos.
public abstract class VehicleModel {

    // =========================
    // ATRIBUTOS
    // =========================

    // Nome do modelo
    private final String name;

    // Capacidade máxima da bateria (kWh)
    private final double batteryCapacity;

    // Potência máxima suportada no carregamento (kW)
    private final double maxChargingPower;

    // Código do modelo
    //
    // Exemplo:
    // CMP, SUV, TRK
    private final String modelCode;

    // =========================
    // CONSTRUTOR
    // =========================

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

    // =========================
    // MÉTODOS ABSTRATOS
    // =========================


    // Cada modelo deve possuir
    // sua própria descrição.
    public abstract String
    getModelDescription();

    // =========================
// CONTADOR DE INSTÂNCIAS
// =========================

// Contador individual por modelo
// static garante que é compartilhado
// entre todas as instâncias do mesmo tipo
private int instanceCount = 0;

// Gera o próximo ID para um veículo deste modelo
//
// Exemplo com CompactModel (base 100):
//   1° veículo → 10001
//   2° veículo → 10002
//   3° veículo → 10003
public int generateNextId() {

    instanceCount++;

    int idBase = Integer.parseInt(modelCode);

    return (idBase * 100) + instanceCount;
}

// Reinicia contador do modelo
public void resetInstanceCount() {

    instanceCount = 0;
}

    // =========================
    // GETTERS
    // =========================

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