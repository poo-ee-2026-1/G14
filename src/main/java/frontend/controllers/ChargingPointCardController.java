package frontend.controllers;

import backend.fleetmanager.CarColor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChargingPointCardController {

    @FXML private Label lblNomePosto;
    @FXML private Label lblStatusPosto;
    @FXML private Label lblPotenciaPosto;

    @FXML private Label lblVehicleName;
    @FXML private Label lblVehicleModel;
    @FXML private Label lblBattery;

    @FXML private ImageView imgCar;
    @FXML private ImageView imgPlug;
    @FXML private ProgressBar progressBattery;

    public void initialize() {

        imgPlug.setImage(
                new Image(getClass()
                .getResourceAsStream("/assets/images/icon_plug.png"))
        );
    }

    public void atualizarCard(
            int numero,
        String status,
        String vehicleName,
        String vehicleModel,
        double battery,
        double potencia,
        CarColor color,
        int visualIndex) {

        lblNomePosto.setText("Posto " + numero);

// STATUS
if ("CARREGANDO".equalsIgnoreCase(status)) {

    lblStatusPosto.setText("CARREGANDO");

    lblStatusPosto.setStyle(
        "-fx-background-color: #16233b;" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: #204a78;" +
        "-fx-border-radius: 10;" +
        "-fx-padding: 4 12 4 12;" +
        "-fx-text-fill: #00e5ff;" +
        "-fx-font-weight: bold;"
    );

} else {

    lblStatusPosto.setText("LIVRE");

    lblStatusPosto.setStyle(
        "-fx-background-color: #162a1d;" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: #24552f;" +
        "-fx-border-radius: 10;" +
        "-fx-padding: 4 12 4 12;" +
        "-fx-text-fill: #39FF14;" +
        "-fx-font-weight: bold;"
    );
}
        // VEÍCULO
if (color != null) {

    String folder = switch (color) {

        case PRETO -> "black";
        case AZUL -> "blue";
        case VERMELHO -> "red";
        case PRATA -> "silver";
        case BRANCO -> "white";
    };

    String imagePath =
            "/assets/cars/"
            + folder
            + "/"
            + visualIndex
            + ".png";

    var stream =
            getClass().getResourceAsStream(imagePath);

    if (stream == null) {

        System.err.println(
                "[ERRO] Imagem não encontrada: "
                + imagePath
        );

    } else {

        imgCar.setImage(
                new Image(stream)
        );
    }

    lblVehicleName.setText(vehicleModel);
lblVehicleModel.setText("Veículo: " + vehicleName);

    lblBattery.setText(
            String.format("%.0f%%", battery)
    );

    progressBattery.setProgress(
            battery / 100.0
    );

} else {

    var stream =
            getClass().getResourceAsStream(
                    "/assets/images/empty.png"
            );

    if (stream != null) {

        imgCar.setImage(
                new Image(stream)
        );
    }

    lblVehicleName.setText("—");
    lblVehicleModel.setText("Sem veículo");
    lblBattery.setText("--");
    progressBattery.setProgress(0);
}
        }}