package backend.core;

// Classe responsável por inicializar o núcleo da aplicação
public class Application {

    // Controlador principal
    private final AppController appController;

    public Application() {

        this.appController =
                new AppController();

        System.out.println(
                "[DEBUG] Backend Application criada"
        );
    }

    public AppController getAppController() {

        return appController;
    }
}