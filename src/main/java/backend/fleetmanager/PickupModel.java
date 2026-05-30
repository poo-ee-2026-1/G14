package backend.fleetmanager;

// Classe que representa um veículo do tipo Caminhonete.
public class PickupModel
        extends VehicleModel {

// CONSTANTES DO MODELO


    // Nome do modelo
    private static final String MODEL_NAME =
            "Caminhonete";

    // Capacidade da bateria em kWh
    private static final double BATTERY_KWH =
            131.0;

    // Potência máxima de carregamento em kW
    private static final double MAX_POWER_KW =
            175.0;

    // Código identificador do modelo
    private static final String MODEL_CODE =
            "400";

// CONSTRUTOR


    // Construtor padrão da caminhonete
    public PickupModel() {

        // Inicializa os dados herdados da superclasse VehicleModel
        super(
                MODEL_NAME,
                BATTERY_KWH,
                MAX_POWER_KW,
                MODEL_CODE
        );
    }


// POLIMORFISMO


    // Retorna uma descrição específica da caminhonete.
    @Override
    public String getModelDescription() {

        return
                "Pickup robusta — "
                        + "bateria de 131 kWh, "
                        + "ultra-carregamento DC "
                        + "de até 175 kW para "
                        + "longas distâncias.";
    }
}