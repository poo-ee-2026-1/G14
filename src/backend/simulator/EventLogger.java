package backend.simulator;

// Importações necessárias para manipulação de arquivos
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Classe responsável por registrar eventos da simulação
public class EventLogger {

// =========================
// CONSTANTES
// =========================

    // Prefixo padrão das mensagens
    private static final String LOG_PREFIX = "[LOG]: ";

// =========================
// ATRIBUTOS
// =========================

    // Lista que armazena os registros da simulação
    private final List<String> eventLog;

// =========================
// CONSTRUTOR
// =========================

    // Construtor padrão do logger
    public EventLogger() {

        // Inicializa lista vazia
        this.eventLog = new ArrayList<>();
    }

// =========================
// MÉTODOS PRINCIPAIS
// =========================

    // Registra uma nova mensagem no sistema
    public void log(String message) {

        // Adiciona mensagem à lista
        eventLog.add(message);

        // Exibe mensagem no console
        System.out.println(LOG_PREFIX + message);
    }

    // Remove todos os logs armazenados
    public void clearLog() {

        eventLog.clear();
    }

    // Exporta os logs para um arquivo
    public void exportLog(String filename) {

        // try-with-resources:
        // fecha automaticamente o writer
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

// =========================
// GETTERS
// =========================

    // Retorna uma cópia da lista de logs
    public List<String> getLog() {

        // Retorna cópia para proteger encapsulamento
        return new ArrayList<>(eventLog);
    }
}