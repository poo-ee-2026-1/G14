package frontend.app;

import java.io.IOException;

import backend.core.AppController;
import frontend.controllers.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Inicializando ecossistema Smart Charge Station...");

            // 1. INICIALIZA O MOTOR DO BACKEND
            backend.core.Application backendCore = new backend.core.Application();
            AppController appController = backendCore.getAppController();

            // 2. CARREGA O ARQUIVO FXML DA VIEW PRINCIPAL
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();

            // 3. CAPTURA O CONTROLLER DA TELA
            MainViewController viewController = loader.getController();

            // 4. FAZ O ACOPLAMENTO EM MÃO DUPLA
            viewController.setAppController(appController);
            appController.setViewController(viewController);

            // 5. CONFIGURA E EXIBE A JANELA PRINCIPAL
            primaryStage.setTitle("Smart Charge Station - MVP Simulation");
            primaryStage.setScene(new Scene(root, 1024, 768));
            
            primaryStage.setOnCloseRequest(event -> {
                System.out.println("Interface fechada pelo usuário. Encerrando simulação...");
                System.exit(0);
            });

            primaryStage.show();
            System.out.println("Janela carregada e loop de simulação ativo.");

        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO AO INICIALIZAR A APLICAÇÃO:");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}