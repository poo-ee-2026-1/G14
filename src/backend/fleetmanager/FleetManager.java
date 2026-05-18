package backend.fleetmanager;

import backend.config.SimulationConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Classe responsável por gerenciar a frota de veículos da simulação.
public class FleetManager {

// =========================
// ATRIBUTOS
// =========================

    // Lista de modelos disponíveis
    // para geração de veículos
    private final List<VehicleModel>
            availableModels;

    // Lista de cores possíveis
    private final List<String>
            colorOptions;

    // Quantidade máxima de veículos
    // permitidos por ciclo
    private final int maxVehiclesPerCycle;

    // Gerador de números aleatórios
    private final Random randomGenerator;


    //Lista de veículos final do ciclo
    private final List<Vehicle> vehicles;

// =========================
// CONSTRUTOR
// =========================

    // Inicializa o FleetManager
    public FleetManager() {

        // Cria gerador aleatório
        this.randomGenerator =
                new Random();

        // Inicializa lista de modelos
        this.availableModels =
                new ArrayList<>();

        // Inicializa lista de cores
        this.colorOptions =
                new ArrayList<>();

        // Define limite padrão
        this.maxVehiclesPerCycle = SimulationConfig.MAX_VEHICLES_PER_CYCLE;

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

    // Carrega modelos e cores padrão
    private void loadDefaultModelsAndColors() {

        // Adiciona cores disponíveis
        colorOptions.add("Branco");
        colorOptions.add("Preto");
        colorOptions.add("Prata");
        colorOptions.add("Azul");
        colorOptions.add("Vermelho");

        // Adiciona modelos disponíveis

        availableModels.add(
                new CompactModel()
        );

        availableModels.add(
                new SedanModel()
        );

        availableModels.add(
                new SUVModel()
        );

        availableModels.add(
                new PickupModel()
        );
    }

// =========================
// GERAÇÃO DE VEÍCULOS
// =========================
        private void generateFleet() {

    for (int i = 1; i <= maxVehiclesPerCycle; i++) {

        // Arrival time 0 porque o Simulator
        // vai controlar quando cada um chega
        Vehicle vehicle = generateRandomVehicle(0.0);

        if (vehicle != null) {
            vehicles.add(vehicle);
        }
    }
}
    // Gera um veículo aleatório
    public Vehicle generateRandomVehicle( double arrivalTime) {

        // Segurança contra listas vazias
        if (
                availableModels.isEmpty()
                        ||
                        colorOptions.isEmpty()
        ) {

            return null;
        }

        // Escolhe modelo aleatório
        VehicleModel model =
                availableModels.get(
                        randomGenerator.nextInt(
                                availableModels.size()
                        )
                );

        // Escolhe cor aleatória
        String color =
                colorOptions.get(
                        randomGenerator.nextInt(
                                colorOptions.size()
                        )
                );

        // Gera SoC aleatório entre
        // 10% e 50%
       
        double min = SimulationConfig.MIN_INITIAL_SOC;

        double max =  SimulationConfig.MAX_INITIAL_SOC;

        double randomSoC = min +  ( randomGenerator.nextDouble() * (max - min));

        // Cria veículo aleatório
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

    // Reinicia dados do FleetManager
    //
    // Atualmente vazio porque
    // os modelos e cores são fixos.
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

    // Retorna lista de veículos da frota
public List<Vehicle> getVehicles() {
    return this.vehicles;
}

    // Retorna limite máximo
    public int getMaxVehiclesPerCycle() {

        return this.maxVehiclesPerCycle;
    }

    // Retorna modelos disponíveis
    public List<VehicleModel>
    getAvailableModels() {

        return this.availableModels;
    }
}