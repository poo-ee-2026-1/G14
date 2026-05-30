package backend.core;

import backend.config.SimulationConfig;
import backend.simulator.Simulator;
import frontend.controllers.MainViewController;

public class AppController {

    // Simulador principal
    private final Simulator simulator;

    // Sistema de tempo
    private final TimeSystem timeSystem;

    // Referência para o controlador da Interface Gráfica (JavaFX)
    private MainViewController viewController;

    public AppController() {
        // Inicializa sistema temporal
        this.timeSystem = new TimeSystem();

        // Inicializa simulador
        this.simulator = new Simulator();

        // Inicializa simulação
        simulator.initializeSimulation();
    }

    public void setViewController(MainViewController viewController) {
        this.viewController = viewController;
    }


     // Atualiza a física do tempo e processa os eventos do simulador.
    public void update() {
        // Delta de tempo real
        double deltaRealTime = SimulationConfig.DEFAULT_REAL_DELTA_TIME;

        // Atualiza relógio
        timeSystem.update(deltaRealTime);

        // Obtém delta simulado
        double deltaSimulationTime = timeSystem.getDeltaSimulationTime();

        // Atualiza simulador
        simulator.update(deltaSimulationTime);
    }

     // Delega a atualização visual para a interface JavaFX caso ela esteja conectada.
 
    public void render() {
        if (viewController != null) {
            viewController.atualizarInterfaceGeral();
        }
    }
    // GETTERS

    public Simulator getSimulator() {
        return simulator;
    }

    public TimeSystem getTimeSystem() {
        return timeSystem;
    }
}