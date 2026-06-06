package frontend.controllers;

import backend.core.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ControllersCardController {

    // BOTÕES

    @FXML
    private VBox btnStart;

    @FXML
    private VBox btnPause;

    @FXML
    private VBox btnReset;

    @FXML
    private VBox btnReport;

    // STATUS

    @FXML
    private Label lblStatus;

    private AppController appController;

    @FXML
    public void initialize() {

        atualizarStatus("PARADO");

        btnStart.setOnMouseClicked(e -> onStart());

        btnPause.setOnMouseClicked(e -> onPause());

        btnReset.setOnMouseClicked(e -> onReset());

        btnReport.setOnMouseClicked(e -> onGenerateReport());
    }

    public void setAppController(AppController appController) {

        this.appController = appController;
    }

    private void onStart() {

        if (appController == null) {
            System.err.println("[ERRO] AppController não conectado.");
            return;
        }

        appController.startSimulation();

        atualizarStatus("RODANDO");

        System.out.println("[UI] START");
    }

    private void onPause() {

        if (appController == null) {
            System.err.println("[ERRO] AppController não conectado.");
            return;
        }

        appController.togglePause();

        if (appController.isPaused()) {
            atualizarStatus("PAUSADO");
        } else {
            atualizarStatus("RODANDO");
        }

        System.out.println("[UI] PAUSE");
    }

    private void onReset() {

        if (appController == null) {
            System.err.println("[ERRO] AppController não conectado.");
            return;
        }

        appController.resetSimulation();

        atualizarStatus("RESETADO");

        System.out.println("[UI] RESET");
    }

    private void onGenerateReport() {

        System.out.println("[UI] GERAR RELATÓRIO");

        atualizarStatus("RELATÓRIO");
    }

    private void atualizarStatus(String status) {

        lblStatus.setText(status);

        switch (status) {

            case "RODANDO" -> lblStatus.setStyle(
                    "-fx-text-fill:#39FF14;" +
                    "-fx-font-weight:bold;"
            );

            case "PAUSADO" -> lblStatus.setStyle(
                    "-fx-text-fill:#ffcc00;" +
                    "-fx-font-weight:bold;"
            );

            case "RESETADO" -> lblStatus.setStyle(
                    "-fx-text-fill:#ff4d4d;" +
                    "-fx-font-weight:bold;"
            );

            case "RELATÓRIO" -> lblStatus.setStyle(
                    "-fx-text-fill:#8A2BE2;" +
                    "-fx-font-weight:bold;"
            );

            default -> lblStatus.setStyle(
                    "-fx-text-fill:#aaaaaa;"
            );
        }
    }
}