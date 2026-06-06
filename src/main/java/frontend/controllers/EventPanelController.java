package frontend.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class EventPanelController {

    @FXML
    private ListView<String> listEvents;

    public void setLogs(ObservableList<String> logs) {
        // Vincula a lista observável diretamente. 
        // O JavaFX cuidará de atualizar a tela quando novos logs entrarem.
        listEvents.setItems(logs);
    }
}