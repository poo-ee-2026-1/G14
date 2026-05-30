package frontend.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import backend.config.SimulationConfig;
import backend.core.AppController;
import backend.fleetmanager.Vehicle;
import backend.simulator.Simulator;
import backend.stationmanager.ChargingPoint;
import backend.stationmanager.StationManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;


public class MainViewController {

    @FXML private ImageView imgLogo;
    @FXML private Label lblPotenciaTotal;
    @FXML private GridPane gridPontos;
    @FXML private TableView<Vehicle> tabelaFila;
    @FXML private TableColumn<Vehicle, Integer> colVeiculo;
    @FXML private TableColumn<Vehicle, Double> colBateria;

    private AppController appController;
    private final List<ChargingPointCardController> cardControllersCache = new ArrayList<>();
    private boolean cardsCarregados = false;

    @FXML
    public void initialize() {
        
        try {
            
            InputStream imgStream = getClass().getResourceAsStream("/assets/images/logo.png");
            
            if (imgStream == null) {
                imgStream = getClass().getClassLoader().getResourceAsStream("assets/images/logo.png");
            }
            
            if (imgStream != null) {
                Image logo = new Image(imgStream);
                if (imgLogo != null) {
                    imgLogo.setImage(logo);
                    
                   
                    imgLogo.setFitHeight(180);
                    imgLogo.setFitWidth(360);
                    imgLogo.setPreserveRatio(true);
                    imgLogo.setSmooth(true); 
                }
            } else {
                System.err.println("[AVISO] Arquivo logo.png não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar a logo: " + e.getMessage());
        }

        // Configuração das tabelas
        colVeiculo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBateria.setCellValueFactory(new PropertyValueFactory<>("stateOfCharge"));
        colBateria.setCellFactory(column -> new javafx.scene.control.TableCell<Vehicle, Double>() {
            @Override
            protected void updateItem(Double soc, boolean empty) {
                super.updateItem(soc, empty);
                if (empty || soc == null) setText(null);
                else setText(String.format("%.1f%%", soc));
            }
        });
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
        atualizarInterfaceGeral();
        ligarLoopDeAtualizacao();
    }

    private void ligarLoopDeAtualizacao() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(SimulationConfig.FRAME_DELAY), event -> {
            if (appController != null) {
                appController.update();
                appController.render();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void atualizarInterfaceGeral() {
        if (appController == null) return;
        
        Simulator simulator = appController.getSimulator();
        StationManager stationManager = simulator.getStationManager();

        double totalSegundos = simulator.getSimulationTime();
        long horas = (long) (totalSegundos / 3600);
        long minutos = (long) ((totalSegundos % 3600) / 60);
        long segundos = (long) (totalSegundos % 60);
        
        lblPotenciaTotal.setText(String.format("[%02d:%02d:%02d] Potência Atual: %.1f kW / %.1f kW", 
                horas, minutos, segundos, getPotenciaTotalConsumida(stationManager), SimulationConfig.STATION_MAX_POWER));

        renderizarCards(stationManager);
        atualizarFila(stationManager, simulator);
    }

    private double getPotenciaTotalConsumida(StationManager sm) {
        double total = 0.0;
        if (sm.getChargingPoints() != null) {
            for (ChargingPoint cp : sm.getChargingPoints()) {
                if (cp.isOccupied()) total += cp.getCurrentPower();
            }
        }
        return total;
    }

    private void renderizarCards(StationManager sm) {
        if (!cardsCarregados) {
            gridPontos.getChildren().clear();
            int col = 0, lin = 0;
            for (int i = 0; i < SimulationConfig.MAX_CHARGING_POINTS; i++) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/charginpoint/ChargingPointCard.fxml"));
                    Parent card = loader.load();
                    cardControllersCache.add(loader.getController());
                    gridPontos.add(card, col++, lin);
                    if (col > 3) { col = 0; lin++; }
                } catch (IOException e) {}
            }
            cardsCarregados = true;
        }

        double delta = appController.getTimeSystem().getDeltaSimulationTime();
        for (int i = 0; i < SimulationConfig.MAX_CHARGING_POINTS; i++) {
            if (i >= cardControllersCache.size()) break;
            ChargingPointCardController cc = cardControllersCache.get(i);
            ChargingPoint cp = (sm.getChargingPoints() != null && i < sm.getChargingPoints().size()) ? sm.getChargingPoints().get(i) : null;
            
            if (cp != null) {
                cp.deliverEnergy(delta);
                cc.atualizarCard(cp.getId(), cp.isOccupied() ? "CARREGANDO" : "LIVRE", cp.getCurrentPower());
            } else {
                cc.atualizarCard(i + 1, "LIVRE", 0.0);
            }
        }
    }

    private void atualizarFila(StationManager sm, Simulator sim) {
        if (sm.getWaitingQueue() != null) {
            sm.getWaitingQueue().updatePriorities(sim.getSimulationTime());
            tabelaFila.getItems().setAll(sm.getWaitingQueue().getVehicles());
        }
    }
}