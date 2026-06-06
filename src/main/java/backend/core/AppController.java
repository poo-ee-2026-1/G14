package backend.core;

import backend.simulator.Simulator;

public class AppController {

    private final Simulator simulator;
    private boolean paused;
    private boolean started;

    public AppController() {
        this.simulator = new Simulator();
        this.started = false;
        this.paused = false;
        System.out.println("[DEBUG] AppController criado -> " + this);
    }

    public void startSimulation() {
        System.out.println("[APP] START -> " + simulator);
        if (!started) {
            simulator.initializeSimulation();
            started = true;
        }
        paused = false;
        System.out.println("[DEBUG] start");
    }

    public void togglePause() {
        if (!started) {
            return;
        }
        paused = !paused;
        System.out.println(paused ? "[SIM] PAUSED" : "[SIM] RUNNING");
    }

    public void resetSimulation() {
        System.out.println("[APP] RESET -> " + simulator);
        simulator.reset();
        simulator.initializeSimulation();
        started = false;
        paused = false;
    }

    public void update(double deltaTime) {
        if (!started || paused) {
            return; // Se não começou ou está pausado, não faz nada
        }
        simulator.update(deltaTime);
    }

    public Simulator getSimulator() {
        return simulator;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isStarted() {
        return started;
    }
}