package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StationStatusPanelController {

    @FXML private ImageView iconFila;
    @FXML private ImageView iconCarregando;
    @FXML private ImageView iconEnergia;
    @FXML private ImageView iconPotencia;
    @FXML private ImageView iconReceita;
    @FXML private ImageView iconTempo;

    @FXML private Label lblFila;
    @FXML private Label lblCarregando;
    @FXML private Label lblEnergia;
    @FXML private Label lblPotencia;
    @FXML private Label lblReceita;
    @FXML private Label lblTempo;

    @FXML
    public void initialize() {
        carregarIcones();
    }

    private void carregarIcones() {

        try {

            iconFila.setImage(
                new Image(getClass().getResourceAsStream(
                    "/assets/images/icon_fila.png"))
            );

            iconCarregando.setImage(
                new Image(getClass().getResourceAsStream(
                    "/assets/images/icon_carregando.png"))
            );

            iconEnergia.setImage(
                new Image(getClass().getResourceAsStream(
                    "/assets/images/icon_energia.png"))
            );

            iconPotencia.setImage(
                new Image(getClass().getResourceAsStream(
                    "/assets/images/icon_potencia.png"))
            );

            iconReceita.setImage(
                new Image(getClass().getResourceAsStream(
                    "/assets/images/icon_receita.png"))
            );

            iconTempo.setImage(
                new Image(getClass().getResourceAsStream(
                    "/assets/images/icon_tempo.png"))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizar(
            int fila,
            int carregando,
            double energia,
            double potencia,
            double receita,
            String tempo
    ) {

        lblFila.setText(String.valueOf(fila));

        lblCarregando.setText(String.valueOf(carregando));

        lblEnergia.setText(
                String.format("%.1f kWh", energia)
        );

        lblPotencia.setText(
                String.format("%.1f kW", potencia)
        );

        lblReceita.setText(
                String.format("R$ %.2f", receita)
        );

        lblTempo.setText(tempo);
    }
}