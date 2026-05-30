package backend.core;

import backend.config.SimulationConfig;

// Gerenciador do relógio da simulação. Converte o tempo real decorrido em tempo simulado acelerado.

public class TimeSystem {

    private double accumulatedSimulationTime;
    private double deltaSimulationTime;

    public TimeSystem() {
        this.accumulatedSimulationTime = 0.0;
        this.deltaSimulationTime = 0.0;
    }

     // Atualiza o relógio mestre com base no passo de tempo real e na escala configurada.
    public void update(double deltaRealTime) {
        // Ex: 0.016 segundos reais * 60.0 (TIME_SCALE) = 0.96 segundos se passaram na simulação
        this.deltaSimulationTime = deltaRealTime * SimulationConfig.TIME_SCALE;
        this.accumulatedSimulationTime += this.deltaSimulationTime;
    }

    public double getDeltaSimulationTime() {
        return this.deltaSimulationTime;
    }

    public double getAccumulatedSimulationTime() {
        return this.accumulatedSimulationTime;
    }

    public void reset() {
        this.accumulatedSimulationTime = 0.0;
        this.deltaSimulationTime = 0.0;
    }
}