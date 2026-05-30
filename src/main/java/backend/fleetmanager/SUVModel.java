package backend.fleetmanager;

// Classe que representa um veículo do tipo SUV.
public class SUVModel
        extends VehicleModel {

// CONSTANTES DO MODELO

    // Nome do modelo
    private static final String MODEL_NAME =
            "SUV";

    // Capacidade da bateria em kWh
    private static final double BATTERY_KWH =
            108.8;

    // Potência máxima de carregamento em kW
    private static final double MAX_POWER_KW =
            170.0;

    // Código identificador do modelo
    private static final String MODEL_CODE =
            "300";

// CONSTRUTOR

    // Construtor padrão do SUV
    public SUVModel() {

        // Inicializa os dados básicos através da superclasse
        super(
                MODEL_NAME,
                BATTERY_KWH,
                MAX_POWER_KW,
                MODEL_CODE
        );
    }

// POLIMORFISMO

    // Retorna descrição especializada para veículos SUV.
    @Override
    public String getModelDescription() {

        return
                "SUV espaçoso — "
                        + "bateria de 108,8 kWh, "
                        + "suporte a carregamento "
                        + "rápido DC de até 170 kW.";
    }
}