package backend.config;

public final class SimulationConfig {

    // =========================
    // TEMPO
    // =========================

    /**
     * Fator de escala de tempo da simulação.
     *
     * Controla quantos segundos simulados passam
     * para cada segundo real.
     *
     * Exemplos:
     *   1.0   → tempo real (1s real = 1s simulado)
     *   60.0  → 1s real = 1min simulado
     *   480.0 → 1s real = 8min simulado
     *            (24h simuladas em ~3min reais)
     *
     * ⚠ Se a animação estiver rápida demais → diminuir
     * ⚠ Se a animação estiver lenta demais  → aumentar
     */
    public static final double TIME_SCALE = 480.0;

    /**
     * Pausa entre frames em milissegundos.
     * Controla a suavidade da animação visual.
     *
     * Exemplos:
     *   16  → ~60fps (muito suave)
     *   33  → ~30fps (suave)
     *   100 → ~10fps (mais lento, mais didático)
     *
     * ⚠ Se a animação estiver rápida demais → aumentar
     * ⚠ Se a animação estiver lenta demais  → diminuir
     */
    public static final int FRAME_DELAY = 16;

    public static final double
            DEFAULT_REAL_DELTA_TIME = 1.0;

    /**
     * Duração máxima da simulação em segundos simulados.
     *
     * Exemplos:
     *   3600.0  →  1h simulada
     *   86400.0 → 24h simuladas
     *
     * Com TIME_SCALE = 480.0:
     *   86400.0 → termina em ~3 minutos reais
     */
    public static final double
            MAX_SIMULATION_TIME = 86400.0;

    // =========================
    // ESTAÇÃO
    // =========================

    public static final int
            MAX_CHARGING_POINTS = 8;

    public static final double
            STATION_MAX_POWER = 80.0;

    public static final double
            ENERGY_PRICE = 1.50;

    /**
     * Potência padrão por ponto de carregamento em kW.
     * Usada como fallback quando o ponto não
     * retorna uma potência válida.
     */
    public static final double
            DEFAULT_CHARGING_POWER = 50.0;

    // =========================
    // FILA
    // =========================

    public static final double
            BATTERY_WEIGHT = 0.6;

    public static final double
            WAITING_WEIGHT = 0.4;

    public static final double
            WAITING_TIME_NORMALIZATION = 1000.0;

    // =========================
    // FROTA
    // =========================

    public static final int
            MAX_VEHICLES_PER_CYCLE = 8;

    public static final double
            MIN_INITIAL_SOC = 10.0;

    public static final double
            MAX_INITIAL_SOC = 50.0;

    /**
     * Média do intervalo entre chegadas de veículos
     * em segundos simulados.
     *
     * Usado na distribuição exponencial:
     *   intervalo = -MEAN * ln(1 - random)
     *
     * Exemplos:
     *   300.0  → média de 1 veículo a cada 5min simulados
     *   1800.0 → média de 1 veículo a cada 30min simulados
     *
     * Com TIME_SCALE = 480.0 e MEAN = 1800.0:
     *   → um veículo chega a cada ~3.75s reais
     *
     * ⚠ Se os veículos chegarem rápido demais → aumentar
     * ⚠ Se os veículos chegarem devagar demais → diminuir
     */
   public static final double
        ARRIVAL_INTERVAL = 1800.0;

    // =========================
    // CONVERSÕES
    // =========================

    public static final double
            SECONDS_PER_HOUR = 3600.0;

    // =========================
    // DEBUG / TESTE
    // =========================

    public static final boolean
            DEBUG_MODE = true;

    private SimulationConfig() {
    }

    // =========================
// MODO DE TESTE
// =========================

/**
 * Ativa modo de teste de estresse.
 *
 * true  → todos os veículos chegam ao mesmo tempo
 * false → veículos chegam em intervalos normais
 */
public static final boolean
        STRESS_TEST_MODE = false;
}