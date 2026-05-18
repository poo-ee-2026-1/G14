package backend;

import backend.core.Application;

// Classe responsável apenas por iniciar o sistema
public class Main {

    // Ponto de entrada oficial da aplicação
    public static void main(String[] args) {

        // Cria aplicação principal
        Application application =
                new Application();

        // Inicia sistema
        application.start();
    }
}