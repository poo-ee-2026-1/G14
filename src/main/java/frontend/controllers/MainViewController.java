package frontend.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import backend.config.SimulationConfig;
import backend.core.AppController;
import backend.fleetmanager.Vehicle;
import backend.simulator.Simulator;
import backend.stationmanager.ChargingPoint;
import backend.stationmanager.StationManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainViewController {

    private AppController appController;

    private boolean childControllersConnected = false;

    @FXML
    private ImageView imgLogo;

    @FXML
    private StationStatusPanelController statusPanelController;

    @FXML
    private QueuePanelController queuePanelController;

    @FXML
    private EventPanelController eventPanelController;

    @FXML
    private GridPane gridPontos;

    @FXML
    private ControllersCardController controllersCardController;

    private final Map<Integer, ChargingPointCardController> cards =
            new HashMap<>();

    @FXML
    public void initialize() {
        carregarLogo();
        System.out.println("[DEBUG] MainViewController inicializado");
    }

    public void setAppController(AppController appController) {
        this.appController = appController;

        System.out.println("[DEBUG] AppController conectado na View");
        System.out.println("Status Controller = " + statusPanelController);
        System.out.println("Controls Controller = " + controllersCardController);

        Simulator sim = appController.getSimulator();
        montarGrid(sim.getStationManager());
        startLoop();
    }

    private void conectarControllersFilhos() {
        if (childControllersConnected) {
            return;
        }

        if (appController == null) {
            return;
        }

        if (controllersCardController == null) {
            return;
        }

        controllersCardController.setAppController(appController);
        childControllersConnected = true;
        System.out.println("[DEBUG] Controllers conectados.");
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void carregarLogo() {
        try (InputStream stream = getClass().getResourceAsStream("/assets/images/logo512x512.png")) {
            if (stream == null) {
                System.err.println("LOGO NÃO ENCONTRADA");
                return;
            }
            Image img = new Image(stream);
            imgLogo.setImage(img);
            imgLogo.setFitHeight(160);
            imgLogo.setPreserveRatio(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLoop() {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(SimulationConfig.FRAME_DELAY),
                        e -> update()
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void update() {
    conectarControllersFilhos();
    if (appController == null) return;

    appController.update(SimulationConfig.DEFAULT_REAL_DELTA_TIME);

    Simulator sim = appController.getSimulator();
    StationManager sm = sim.getStationManager();

    // 1. Atualiza Fila (SmartQueue)
    if (queuePanelController != null) {
        // getVehicles() da SmartQueue retorna a lista ordenada por prioridade
        queuePanelController.setQueueItems(sm.getWaitingQueue().getVehicles());
    }

    // 2. Atualiza Logs (EventLogger)
    if (eventPanelController != null) {
        // Passa a lista observável para o controller
        eventPanelController.setLogs(sim.getEventLogger().getEventLog());
    }

    atualizarDashboard(sim, sm);
    atualizarGrid(sm);
}

    @SuppressWarnings("CallToPrintStackTrace")
    private void montarGrid(StationManager sm) {
        gridPontos.getChildren().clear();
        cards.clear();

        int col = 0;
        int row = 0;

        try {
            for (ChargingPoint cp : sm.getChargingPoints()) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/components/chargingpoint/ChargingPointCard.fxml")
                );

                VBox cardView = loader.load();

                ChargingPointCardController controller = loader.getController();

                cards.put(cp.getId(), controller);

                gridPontos.add(cardView, col, row);

                col++;

                if (col == 4) {
                    col = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void atualizarGrid(StationManager sm) {
        for (ChargingPoint cp : sm.getChargingPoints()) {
            ChargingPointCardController card = cards.get(cp.getId());
            if (card == null) {
                continue;
            }

            Vehicle v = cp.getConnectedVehicle();

            card.atualizarCard(
                    cp.getId(),
                    cp.isOccupied() ? "CARREGANDO" : "LIVRE",
                    v != null ? String.valueOf(v.getId()) : null,
                    v != null && v.getModel() != null ? v.getModel().getName() : null,
                    v != null ? v.getStateOfCharge() : 0,
                    cp.getCurrentPower(),
                    v != null ? v.getColor() : null,
                    v != null && v.getModel() != null ? v.getModel().getVisualIndex() : 1
            );
        }
    }

    private void atualizarDashboard(Simulator sim, StationManager sm) {
        if (statusPanelController == null) {
            return;
        }

        int fila = sm.getWaitingQueue().size();
        int carregando = 0;

        double energiaTotal = 0;
        double potenciaAtual = 0;

        for (ChargingPoint cp : sm.getChargingPoints()) {
            if (cp.isOccupied()) {
                carregando++;
            }
            energiaTotal += cp.getEnergyDelivered();
            potenciaAtual += cp.getCurrentPower();
        }

        double receita = energiaTotal * sm.getEnergyPrice();

        System.out.println("[DEBUG] Atualizando Dashboard: fila=" + fila + ", carregando=" + carregando +
                ", energiaTotal=" + energiaTotal + ", potenciaAtual=" + potenciaAtual + ", receita=" + receita);

        long totalSegundos = (long) sim.getSimulationTime();
        long horas = totalSegundos / 3600;
        long minutos = (totalSegundos % 3600) / 60;
        long segundos = totalSegundos % 60;

        String tempo = String.format("%02d:%02d:%02d", horas, minutos, segundos);

        statusPanelController.atualizar(
                fila,
                carregando,
                energiaTotal,
                potenciaAtual,
                receita,
                tempo
        );
    }

    public Map<Integer, ChargingPointCardController> getCards() {
        return cards;
    }
}