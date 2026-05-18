package backend.fleetmanager;

// Classe que representa um veículo do tipo Sedan.
//
// Herda as características básicas da classe VehicleModel
// e define os dados específicos de um Sedan.
public class SedanModel
        extends VehicleModel {

    // =========================
    // CONSTANTES DO MODELO
    // =========================

    // Nome do modelo
    private static final String MODEL_NAME =
            "Sedan";

    // Capacidade da bateria em kWh
    private static final double BATTERY_KWH =
            55.0;

    // Potência máxima de carregamento em kW
    private static final double MAX_POWER_KW =
            100.0;

    // Código identificador do modelo
    private static final String MODEL_CODE =
            "200";

    // =========================
    // CONSTRUTOR
    // =========================

    // Construtor padrão do Sedan
    public SedanModel() {

        // Envia os dados do modelo
        // para a superclasse VehicleModel
        super(
                MODEL_NAME,
                BATTERY_KWH,
                MAX_POWER_KW,
                MODEL_CODE
        );
    }

    // =========================
    // POLIMORFISMO
    // =========================

    // Sobrescreve o método da superclasse
    // para fornecer uma descrição específica
    // do modelo Sedan.
    @Override
    public String getModelDescription() {

        return
                "Sedan executivo — "
                        + "bateria de 55 kWh, "
                        + "suporte a carregamento "
                        + "rápido AC/DC de até 100 kW.";
    }
}