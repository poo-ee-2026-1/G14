package backend.fleetmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import backend.config.SimulationConfig;

public class FleetManager {

    // MODELOS DISPONÍVEIS
    private final List<VehicleModel> availableModels;

    // CORES DISPONÍVEIS
    private final List<CarColor> colorOptions;

    // GERADOR ALEATÓRIO
    private final Random randomGenerator;

    // FROTA ATUAL
    private final List<Vehicle> vehicles;

    public FleetManager() {

        this.randomGenerator = new Random();

        this.availableModels = new ArrayList<>();
        this.colorOptions = new ArrayList<>();

        loadDefaultModelsAndColors();

        this.vehicles = new ArrayList<>();

        generateFleet();
    }

    private void loadDefaultModelsAndColors() {

        // CORES

        colorOptions.add(CarColor.BRANCO);
        colorOptions.add(CarColor.PRETO);
        colorOptions.add(CarColor.PRATA);
        colorOptions.add(CarColor.AZUL);
        colorOptions.add(CarColor.VERMELHO);

        // MODELOS

        try {

            availableModels.add(new CompactModel());
            availableModels.add(new SedanModel());
            availableModels.add(new SUVModel());
            availableModels.add(new PickupModel());

        } catch (Exception e) {

            System.out.println(
                    "Erro ao carregar modelos: "
                    + e.getMessage()
            );
        }
    }

    private void generateFleet() {

        int maxCiclo = 20; 

        for (int i = 1; i <= maxCiclo; i++) {

            Vehicle vehicle =
                    generateRandomVehicle(0.0);

            if (vehicle != null) {
                vehicles.add(vehicle);
            }
        }
    }

    public Vehicle generateRandomVehicle(
            double arrivalTime
    ) {

        if (
                availableModels.isEmpty()
                ||
                colorOptions.isEmpty()
        ) {
            return null;
        }

        VehicleModel model =
                availableModels.get(
                        randomGenerator.nextInt(
                                availableModels.size()
                        )
                );

        CarColor color =
                colorOptions.get(
                        randomGenerator.nextInt(
                                colorOptions.size()
                        )
                );

        double min = 10.0;
        double max = 50.0;

        double randomSoC =
                min +
                (
                        randomGenerator.nextDouble()
                        *
                        (max - min)
                );

        return new Vehicle(
                model.generateNextId(),
                model,
                color,
                randomSoC,
                arrivalTime
        );
    }

    public void reset() {

        for (VehicleModel model : availableModels) {

            model.resetInstanceCount();
        }

        vehicles.clear();

        generateFleet();
    }

    // GETTERS

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public int getMaxVehiclesPerCycle() {
        return SimulationConfig.MAX_CHARGING_POINTS;
    }

    public List<VehicleModel> getAvailableModels() {
        return availableModels;
    }
}