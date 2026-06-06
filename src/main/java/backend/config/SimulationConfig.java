package backend.config;

public class SimulationConfig {
    
    public static final double FRAME_DELAY = 16.0;
    public static final double DEFAULT_REAL_DELTA_TIME = 0.016;
    
    public static final double TIME_SCALE = 120.0;

    // =========================================================================
    // CONFIGURAÇÕES DE FLUXO E AGENDAMENTO ORIGINAL
    // =========================================================================
    
    public static final boolean STRESS_TEST_MODE = false;
    public static final double ARRIVAL_INTERVAL = 1.0;

    // =========================================================================
    //  PARÂMETROS DA ESTAÇÃO E POTÊNCIA
    // =========================================================================
    
    public static final int MAX_CHARGING_POINTS = 8;
    
    /**
     * Ampliamos a subestação de 80.0 kW para 350.0 kW (Padrão de postos Ultra-fast da Ionity/Porsche).
     * Agora a estação tem energia de sobra para alimentar múltiplos totens com alta performance.
     */
    public static final double STATION_MAX_POWER = 350.0;
    
    /**
     * Potência padrão de cada totem elevada para 50.0 kW (Antes era 11.0 kW).
     * Com 50 kW, uma bateria compacta ou de sedan vai do zero ao topo em frações de minutos na simulação.
     */
    public static final double DEFAULT_CHARGING_POWER = 50.0;
    
    public static final double ENERGY_PRICE = 0.45;

    // =========================================================================
    // CONFIGURAÇÕES DA FILA INTELIGENTE
    // =========================================================================
    
    public static final double BATTERY_WEIGHT = 0.6;
    public static final double WAITING_WEIGHT = 0.4;
    public static final double WAITING_TIME_NORMALIZATION = 1800.0;
}