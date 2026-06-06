package frontend.app;

import java.io.IOException;
import java.util.Objects;

import backend.core.AppController;
import frontend.controllers.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void start(Stage primaryStage) {

        try {
            backend.core.Application backendCore = new backend.core.Application();
            AppController appController = backendCore.getAppController();

            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource("/views/MainView.fxml"))
            );

            Parent root = loader.load();

            MainViewController controller = loader.getController();
            controller.setAppController(appController);

            primaryStage.setTitle("Smart Charge Station");

            primaryStage.getIcons().addAll(

    new Image(
        Objects.requireNonNull(
            getClass().getResourceAsStream("/assets/images/icon16x16.png")
        )
    ),

    new Image(
        Objects.requireNonNull(
            getClass().getResourceAsStream("/assets/images/icon32x32.png")
        )
    ),

    new Image(
        Objects.requireNonNull(
            getClass().getResourceAsStream("/assets/images/icon144x144.png")
        )
    ),

    new Image(
        Objects.requireNonNull(
            getClass().getResourceAsStream("/assets/images/icon512x512.png")
        )
    )
);

            primaryStage.setScene(new Scene(root, 1400, 750));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}