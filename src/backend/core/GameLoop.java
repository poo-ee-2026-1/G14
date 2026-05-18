package backend.core;

import backend.config.SimulationConfig;

// Classe responsável pelo loop principal de execução
public class GameLoop {

// =========================
// ATRIBUTOS
// =========================

    // Estado do loop
    private boolean running;

    // Controlador central
    private final AppController controller;

    // Delay entre frames
    private final int FRAME_DELAY = SimulationConfig.FRAME_DELAY;

// =========================
// CONSTRUTOR
// =========================

    public GameLoop(
            AppController controller
    ) {

        this.controller =
                controller;

        this.running =
                false;
    }

// =========================
// START
// =========================

    // Inicia execução
    public void start() {

        running = true;

        System.out.println(
                "Sistema iniciado."
        );

        run();
    }

// =========================
// LOOP PRINCIPAL
// =========================

    private void run() {

        while (running) {

            update();

            render();

            sleep();
        }
    }

// =========================
// UPDATE
// =========================

    private void update() {

        controller.update();
    }

// =========================
// RENDER
// =========================

    private void render() {

        controller.render();
    }

// =========================
// DELAY
// =========================

    private void sleep() {

        try {

            Thread.sleep(
                    FRAME_DELAY
            );

        } catch (
                InterruptedException e
        ) {
        }
    }

// =========================
// ENCERRAMENTO
// =========================

    public void stop() {

        running = false;

        System.out.println(
                "Sistema encerrado."
        );
    }
}