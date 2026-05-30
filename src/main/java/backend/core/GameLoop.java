package backend.core;
public class GameLoop {

// ATRIBUTOS

    // Estado do loop
    private boolean running;

    // Controlador central
    private final AppController controller;

// CONSTRUTOR

    public GameLoop(
            AppController controller
    ) {

        this.controller =
                controller;

        this.running =
                false;
    }


// MÉTODO START

    // Inicia execução
    public void start() {

        running = true;

        System.out.println(
                "Sistema iniciado."
        );

        run();
    }

// LOOP PRINCIPAL

    private void run() {

        while (running) {

            update();

            render();
        }
    }

    private void update() {

        controller.update();
    }

    private void render() {

        controller.render();
    }

    public void stop() {

        running = false;

        System.out.println(
                "Sistema encerrado."
        );
    }
}