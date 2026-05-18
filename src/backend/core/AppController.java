package backend.core;

import backend.config.SimulationConfig;
import backend.simulator.Simulator;

// Classe responsável por coordenar os principais sistemas do projeto
public class AppController {

// =========================
// ATRIBUTOS
// =========================

    // Simulador principal
    private final Simulator simulator;

    // Sistema de tempo
    private final TimeSystem timeSystem;

// =========================
// CONSTRUTOR
// =========================

    public AppController() {

        // Inicializa sistema temporal
        this.timeSystem =
                new TimeSystem();

        // Inicializa simulador
        this.simulator =
                new Simulator();

        // Inicializa simulação
        simulator.initializeSimulation();
    }

// =========================
// UPDATE
// =========================

    // Atualiza todos os sistemas
    public void update() {

        // Delta de tempo real
        double deltaRealTime = SimulationConfig.DEFAULT_REAL_DELTA_TIME;


        // Atualiza relógio
        timeSystem.update(
                deltaRealTime
        );

        // Obtém delta simulado
        double deltaSimulationTime =
                timeSystem
                        .getDeltaSimulationTime();

        // Atualiza simulador
        simulator.update(
                deltaSimulationTime
        );
    }

// =========================
// RENDER
// =========================

    // Renderização futura da UI
    public void render() {

        // UI será implementada depois
    }

    // =========================
    // GETTERS
    // =========================

    public Simulator
    getSimulator() {

        return simulator;
    }

    public TimeSystem
    getTimeSystem() {

        return timeSystem;
    }
}