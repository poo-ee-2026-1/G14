package backend.fleetmanager;

// Classe que representa um veículo compacto.
public class CompactModel
        extends VehicleModel {

// CONSTANTES DO MODELO

    // Nome do modelo
    private static final String MODEL_NAME =
            "Compacto";

    // Capacidade da bateria em kWh
    private static final double BATTERY_KWH =
            38.0;

    // Potência máxima de carregamento
    private static final double MAX_POWER_KW =
            40.0;

    // Código identificador do modelo
    private static final String MODEL_CODE =
            "100";

// CONSTRUTOR


    // Construtor padrão do modelo compacto
    public CompactModel() {

        // Envia dados para a superclasse
        super(
                MODEL_NAME,
                BATTERY_KWH,
                MAX_POWER_KW,
                MODEL_CODE
        );
    }


@Override
public int getVisualIndex() {
    return 1;
}

    @Override
    public String getModelDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}