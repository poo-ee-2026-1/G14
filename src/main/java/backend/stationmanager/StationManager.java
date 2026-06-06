package backend.stationmanager;

import java.util.ArrayList;
import java.util.List;

import backend.config.SimulationConfig;
import backend.fleetmanager.Vehicle;
import backend.fleetmanager.VehicleStatus;
import backend.simulator.Event;

public class StationManager {

    private final List<ChargingPoint> chargingPoints;
    private final SmartQueue waitingQueue;
    private final EnergyManager energyManager;
    private final double energyPrice;

    public StationManager() {

        this.chargingPoints = new ArrayList<>();

        for (int i = 1; i <= SimulationConfig.MAX_CHARGING_POINTS; i++) {
            chargingPoints.add(new ChargingPoint(i));
        }

        this.waitingQueue = new SmartQueue();

        this.energyPrice = SimulationConfig.ENERGY_PRICE;

        this.energyManager = new EnergyManager(
                SimulationConfig.STATION_MAX_POWER
        );
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    public boolean hasCapacityFor(Vehicle vehicle) {
        return energyManager.canAcceptVehicle(chargingPoints, vehicle);
    }

    public ChargingPoint getAvailablePoint() {

        for (ChargingPoint point : chargingPoints) {
            if (!point.isOccupied()) {
                return point;
            }
        }

        return null;
    }

    public ChargingPoint getPointById(int id) {
        return chargingPoints.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void processChargingStart(Event event) {

        ChargingPoint point = getPointById(event.getChargingPointId());
        Vehicle vehicle = event.getVehicle();

        if (point == null || vehicle == null) return;

        point.connectVehicle(vehicle);
        vehicle.setStatus(VehicleStatus.CHARGING);

        rebalancePower();

        System.out.println(
                "[START] Veiculo " + vehicle.getId() +
                " conectado no ponto " + point.getId()
        );
    }

    public boolean rebalancePower() {
        boolean ok = energyManager.rebalancePower(chargingPoints);
        printPowerDistribution();
        return ok;
    }

   public void addVehicleToQueue(Vehicle vehicle, double currentTime) {

    if (vehicle == null) return;

    vehicle.setStatus(VehicleStatus.WAITING);

    waitingQueue.addVehicle(vehicle, currentTime);
}
public void addVehicleToQueue(Vehicle vehicle) {
    addVehicleToQueue(vehicle, 0);
}
    public Vehicle getNextInQueue() {
        return waitingQueue.getNextVehicle();
    }

    public SmartQueue getWaitingQueue() {
        return waitingQueue;
    }

   public void reset() {

    waitingQueue.clear();

    for (ChargingPoint point : chargingPoints) {
        point.disconnectVehicle();
    }
}

    private void printPowerDistribution() {

        System.out.println("\n===== POWER =====");

        for (ChargingPoint point : chargingPoints) {
            if (point.getConnectedVehicle() != null) {
                System.out.println(
                        "Ponto " + point.getId() +
                        " -> " + point.getCurrentPower()
                );
            }
        }

        System.out.println("=================\n");
    }

    public double getEnergyPrice() {
        return energyPrice;
    }
}