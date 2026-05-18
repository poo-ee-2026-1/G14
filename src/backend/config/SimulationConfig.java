package backend.config;

public final class SimulationConfig {

// =========================
// TEMPO
// =========================

        //Fator de escala de tempo
        public static final double TIME_SCALE = 480.0;

        //Pausa entre frames em milissegundos.
        public static final int FRAME_DELAY = 16;

        public static final double DEFAULT_REAL_DELTA_TIME = 1.0;

        //Duração máxima da simulação em segundos simulados.
        public static final double  MAX_SIMULATION_TIME = 86400.0;

// =========================
// ESTAÇÃO
// =========================

    public static final int MAX_CHARGING_POINTS = 8;

    public static final double STATION_MAX_POWER = 80.0;

    public static final double ENERGY_PRICE = 1.50;

    //Potência padrão por ponto de carregamento em kW.

    public static final double DEFAULT_CHARGING_POWER = 50.0;

// =========================
// FILA
// =========================

    public static final double BATTERY_WEIGHT = 0.6;

    public static final double WAITING_WEIGHT = 0.4;

    public static final double WAITING_TIME_NORMALIZATION = 1000.0;

// =========================
// FROTA
// =========================

    public static final int MAX_VEHICLES_PER_CYCLE = 8;

    public static final double MIN_INITIAL_SOC = 10.0;

    public static final double MAX_INITIAL_SOC = 50.0;

    //Média do intervalo entre chegadas de veículos em segundos simulados.
   public static final double ARRIVAL_INTERVAL = 1800.0;

// =========================
// CONVERSÕES
// =========================

    public static final double
            SECONDS_PER_HOUR = 3600.0;

// =========================
// DEBUG / TESTE
// =========================

    public static final boolean DEBUG_MODE = true;

    private SimulationConfig() {
    }

// =========================
// MODO DE TESTE
// =========================

//Ativa modo de teste de estresse.
public static final boolean STRESS_TEST_MODE = false;
}