package backend.core;

import backend.config.SimulationConfig;

// Classe responsável pelo
// gerenciamento temporal
public class TimeSystem {

    // =========================
    // ATRIBUTOS
    // =========================

    // Tempo acumulado
    private double simulationTime;

    // Escala temporal
    private double timeScale;

    // Delta do último frame
    private double deltaSimulationTime;

    // Meta temporal
    private double timeTarget;

    // =========================
    // CONSTRUTOR
    // =========================

    public TimeSystem() {

        this.timeScale =
                SimulationConfig.TIME_SCALE;

        this.simulationTime =
                0.0;

        this.deltaSimulationTime =
                0.0;

        this.timeTarget =
                SimulationConfig.TIME_SCALE;
    }

    // =========================
    // UPDATE
    // =========================

    // Atualiza tempo da simulação
    public void update(
            double deltaRealTime
    ) {

        // Converte tempo real
        // em tempo simulado
        this.deltaSimulationTime =
                deltaRealTime
                        *
                        timeScale;

        // Acumula tempo
        this.simulationTime +=
                deltaSimulationTime;
    }

    // =========================
    // CONFIGURAÇÃO TEMPORAL
    // =========================

    // Define escala temporal
    public void setTimeScale(
            double scale
    ) {

        // Evita escalas inválidas
        if (scale > 0) {

            this.timeScale =
                    scale;

            this.timeTarget =
                    scale;
        }
    }

    // =========================
    // RESET
    // =========================

    // Reinicia relógio
    public void reset() {

        this.simulationTime =
                0.0;

        this.deltaSimulationTime =
                0.0;
    }

    // =========================
    // GETTERS
    // =========================

    public double
    getSimulationTime() {

        return simulationTime;
    }

    public double
    getDeltaSimulationTime() {

        return deltaSimulationTime;
    }

    public double
    getTimeScale() {

        return timeScale;
    }

    public double
    getTimeTarget() {

        return timeTarget;
    }
}