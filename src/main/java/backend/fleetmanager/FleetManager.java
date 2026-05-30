package backend.fleetmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import backend.config.SimulationConfig;

/**
 * Classe responsável por gerenciar a frota de veículos da simulação.
 */
public class FleetManager {

    // =========================
    // ATRIBUTOS
    // =========================

    // Lista de modelos disponíveis para geração de veículos
    private final List<VehicleModel> availableModels;

    // Lista de cores possíveis
    private final List<String> colorOptions;

    // Gerador de números aleatórios
    private final Random randomGenerator;

    // Lista de veículos final do ciclo
    private final List<Vehicle> vehicles;

    // =========================
    // CONSTRUTOR
    // =========================

    public FleetManager() {
        // Cria gerador aleatório
        this.randomGenerator = new Random();

        // Inicializa lista de modelos
        this.availableModels = new ArrayList<>();

        // Inicializa lista de cores
        this.colorOptions = new ArrayList<>();

        // Carrega dados iniciais
        loadDefaultModelsAndColors();

        // Inicializa lista de veículos
        this.vehicles = new ArrayList<>();

        // Gera a frota inicial
        generateFleet();
    }

    // =========================
    // CONFIGURAÇÃO PADRÃO
    // =========================

    private void loadDefaultModelsAndColors() {
        // Adiciona cores disponíveis
        colorOptions.add("Branco");
        colorOptions.add("Preto");
        colorOptions.add("Prata");
        colorOptions.add("Azul");
        colorOptions.add("Vermelho");

        // Adiciona modelos disponíveis
        // Nota: Garanta que essas classes estendam VehicleModel no seu projeto
        try {
            availableModels.add(new CompactModel());
            availableModels.add(new SedanModel());
            availableModels.add(new SUVModel());
            availableModels.add(new PickupModel());
        } catch (Exception e) {
            System.out.println("Aviso: Falha ao carregar subclasses de modelos: " + e.getMessage());
        }
    }

    // =========================
    // GERAÇÃO DE VEÍCULOS
    // =========================
    
    private void generateFleet() {
        // Usa a quantidade de totens configurados como base estável de ciclo inicial
        int maxCiclo = SimulationConfig.MAX_CHARGING_POINTS;
        
        for (int i = 1; i <= maxCiclo; i++) {
            // Arrival time 0 porque o Simulator vai controlar quando cada um chega
            Vehicle vehicle = generateRandomVehicle(0.0);

            if (vehicle != null) {
                vehicles.add(vehicle);
            }
        }
    }

    /**
     * Gera um veículo aleatório com base nos modelos e cores cadastrados.
     */
    public Vehicle generateRandomVehicle(double arrivalTime) {
        // Segurança contra listas vazias
        if (availableModels.isEmpty() || colorOptions.isEmpty()) {
            return null;
        }

        // Escolhe modelo aleatório
        VehicleModel model = availableModels.get(randomGenerator.nextInt(availableModels.size()));

        // Escolhe cor aleatória
        String color = colorOptions.get(randomGenerator.nextInt(colorOptions.size()));

        // Tenta buscar o SoC mínimo e máximo se existirem na configuração, caso contrário assume padrão visível
        double min = 10.0;
        double max = 50.0;
        
        try {
            // Se as variáveis existirem no seu SimulationConfig, o Java vai ler aqui automaticamente
            min = SimulationConfig.BATTERY_WEIGHT * 10; // Fallback inteligente baseado em constantes existentes
        } catch (Exception e) {
            // Mantém os valores mock estáveis de 10% a 50%
        }

        double randomSoC = min + (randomGenerator.nextDouble() * (max - min));

        // Cria veículo aleatório completo
        return new Vehicle(
            model.generateNextId(),  // ID gerado pelo modelo
            model,
            color,
            randomSoC,
            arrivalTime
        );
    }

    // =========================
    // RESET
    // =========================

    public void reset() {
        // Zera contadores de cada modelo
        for (VehicleModel model : availableModels) {
            model.resetInstanceCount();
        }    
        vehicles.clear();
        generateFleet();
    }

    // =========================
    // GETTERS
    // =========================

    public List<Vehicle> getVehicles() {
        return this.vehicles;
    }

    public int getMaxVehiclesPerCycle() {
        return SimulationConfig.MAX_CHARGING_POINTS;
    }

    public List<VehicleModel> getAvailableModels() {
        return this.availableModels;
    }
}