package backend.simulator;

// Importações para escrita de arquivos
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Classe responsável por armazenar
// os resultados e estatísticas da simulação.
//
// O relatório funciona como um resumo final
// da execução do sistema.
public final class SimulationReport {

    // =========================
    // ATRIBUTOS ESTATÍSTICOS
    // =========================

    // Número do ciclo analisado
    private int cycleNumber;

    // Quantidade de veículos atendidos
    private int vehiclesServed;

    // Energia total fornecida
    private double totalEnergyDelivered;

    // Receita total gerada
    private double totalRevenue;

    // Duração total da simulação
    private double simulationDuration;

    // Lista contendo resumo dos veículos
    //
    // Temporariamente utilizando String
    // até a classe VehicleSummary existir
    private List<String> vehicleSummaries = new ArrayList<>();;

    // =========================
    // CONSTRUTOR PADRÃO
    // =========================

    // Cria relatório vazio
    public SimulationReport() {

        // Reinicia todos os dados
        reset();
    }

    // =========================
    // CONSTRUTOR COMPLETO
    // =========================

    // Cria relatório já preenchido
    public SimulationReport(
            int cycleNumber,
            int vehiclesServed,
            double totalEnergyDelivered,
            double totalRevenue,
            double simulationDuration
    ) {

        // Define número do ciclo
        this.cycleNumber = cycleNumber;

        // Define quantidade de veículos
        this.vehiclesServed = vehiclesServed;

        // Define energia fornecida
        this.totalEnergyDelivered =
                totalEnergyDelivered;

        // Define receita total
        this.totalRevenue = totalRevenue;

        // Define duração da simulação
        this.simulationDuration =
                simulationDuration;

    }

    // =========================
    // GERAÇÃO DE RELATÓRIO
    // =========================

    // Gera relatório textual formatado
    public String generateReport() {

        // StringBuilder é mais eficiente
        // do que concatenar Strings diretamente
        StringBuilder report =
                new StringBuilder();

        // Cabeçalho
        report.append(
                "====================================\n"
        );

        // Número do ciclo
        report.append(
                "Simulation Report - Cycle "
        ).append(cycleNumber).append("\n");

        // Veículos atendidos
        report.append(
                "Vehicles Served: "
        ).append(vehiclesServed).append("\n");

        // Energia total
        report.append(
                "Total Energy Delivered: "
        ).append(totalEnergyDelivered)
                .append(" kWh\n");

        // Receita total
        report.append(
                "Total Revenue: $"
        ).append(totalRevenue).append("\n");

        // Duração da simulação
        report.append(
                "Simulation Duration: "
        ).append(simulationDuration)
                .append(" seconds\n");

        // Rodapé
        report.append(
                "===================================="
        );

        // Retorna relatório final
        return report.toString();
    }

    // =========================
    // VEÍCULOS
    // =========================

    // Adiciona resumo de um veículo
    public void addVehicleSummary(
            String summary
    ) {

        vehicleSummaries.add(summary);
    }

    // =========================
    // EXPORTAÇÃO
    // =========================

    // Exporta relatório para arquivo
    public void exportToFile(
            String filename
    ) {

        // try-with-resources:
        // fecha arquivo automaticamente
        try (
                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(
                                        filename
                                )
                        )
        ) {

            // Escreve relatório no arquivo
            writer.write(generateReport());

            // Mensagem de sucesso
            System.out.println(
                    "Relatório exportado com sucesso."
            );

        } catch (IOException e) {

            // Mensagem de erro amigável
            System.err.println(
                    "Erro ao exportar relatório: "
                            + e.getMessage()
            );
        }
    }

    // =========================
    // RESET
    // =========================

    // Reinicia todos os dados do relatório
    public void reset() {

        // Reinicia ciclo
        this.cycleNumber = 0;

        // Reinicia veículos atendidos
        this.vehiclesServed = 0;

        // Reinicia energia total
        this.totalEnergyDelivered = 0;

        // Reinicia receita
        this.totalRevenue = 0;

        // Reinicia duração
        this.simulationDuration = 0;

        // Limpa lista de veículos
        this.vehicleSummaries.clear();
    }

    public List<String> getVehicleSummaries() {
        return vehicleSummaries;
    }

    public void setVehicleSummaries(List<String> vehicleSummaries) {
        this.vehicleSummaries = vehicleSummaries;
    }
}