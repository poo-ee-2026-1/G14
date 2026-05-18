package backend.core;

// Classe responsável por inicializar e iniciar o sistema principal
public class Application {

// =========================
// ATRIBUTOS
// =========================

    // Loop principal do sistema
    private final GameLoop gameLoop;

    // Controlador principal
    private final AppController appController;

// =========================
// CONSTRUTOR
// =========================

    public Application() {

        // Cria controlador central
        this.appController =
                new AppController();

        // Cria game loop
        this.gameLoop =
                new GameLoop(appController);
    }

// =========================
// MÉTODOS
// =========================

    // Inicia aplicação
    public void start() {

        System.out.println(
                "Aplicação iniciando..."
        );

        // Inicia loop principal
        gameLoop.start();
    }

// =========================
// GETTERS
// =========================

    public AppController
    getAppController() {

        return appController;
    }

    public GameLoop
    getGameLoop() {

        return gameLoop;
    }
}