package frontend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import backend.fleetmanager.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class QueuePanelController {

    @FXML
    private ListView<String> queueList;

    public void setQueueItems(List<Vehicle> vehicles) {
        // Formata a lista de objetos para uma lista de Strings legíveis
        List<String> displayList = vehicles.stream()
            .map(v -> "Veículo " + v.getId() + " | SoC: " + String.format("%.1f", v.getStateOfCharge()) + "%")
            .collect(Collectors.toList());
        
        queueList.getItems().setAll(displayList);
    }
}