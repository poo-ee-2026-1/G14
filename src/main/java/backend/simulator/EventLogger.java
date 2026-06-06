package backend.simulator;

// Importações necessárias para manipulação de arquivos
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Classe responsável por registrar eventos da simulação
public class EventLogger {
    private static final String LOG_PREFIX = "[LOG]: ";
    
    // Mude para ObservableList
    private final ObservableList<String> eventLog = FXCollections.observableArrayList();

    public void log(String message) {
        // Platform.runLater é necessário se o log vier de uma thread de background
        javafx.application.Platform.runLater(() -> {
            eventLog.add(0, message); // Adiciona no topo
            System.out.println(LOG_PREFIX + message);
        });
    }

    public ObservableList<String> getEventLog() {
        return eventLog;
    }

// MÉTODOS PRINCIPAIS

    // Remove todos os logs armazenados
    public void clearLog() {

        eventLog.clear();
    }

    // Exporta os logs para um arquivo
    public void exportLog(String filename) {

        try (
                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(filename)
                        )
        ) {

            // Percorre todos os logs
            for (String logEntry : eventLog) {

                // Escreve linha no arquivo
                writer.write(logEntry);

                // Quebra de linha
                writer.newLine();
            }

            // Mensagem de sucesso
            System.out.println(
                    "Logs exportados com sucesso."
            );

        } catch (IOException e) {

            // Exibe erro de exportação
            System.err.println(
                    "Erro ao exportar o log: "
                            + e.getMessage()
            );
        }
    }

// GETTERS

    // Retorna uma cópia da lista de logs
    public List<String> getLog() {

        // Retorna cópia para proteger encapsulamento
        return new ArrayList<>(eventLog);
    }
}