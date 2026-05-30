package backend.stationmanager;

import java.util.ArrayList;
import java.util.List;

import backend.config.SimulationConfig;
import backend.fleetmanager.Vehicle;
import backend.fleetmanager.VehicleStatus;
import backend.simulator.Event;

public class StationManager {

// ATRIBUTOS

    private final List<ChargingPoint> chargingPoints;
    private final SmartQueue waitingQueue;
    private final EnergyManager energyManager;
    private final double energyPrice;

// CONSTRUTOR

    public StationManager() {

        this.chargingPoints = new ArrayList<>();

        for (int i = 1; i <= SimulationConfig.MAX_CHARGING_POINTS; i++) {
            chargingPoints.add(new ChargingPoint(i));
        }

        this.waitingQueue = new SmartQueue();

        this.energyPrice = SimulationConfig.ENERGY_PRICE;

        this.energyManager = new EnergyManager(
                chargingPoints,
                SimulationConfig.STATION_MAX_POWER
        );
    }

// FILA

    public void addVehicleToQueue(Vehicle vehicle) {

        if (vehicle == null) return;

        vehicle.setStatus(VehicleStatus.WAITING);

        waitingQueue.addVehicle(vehicle);
    }

    public Vehicle getNextInQueue() {
        return waitingQueue.getNextVehicle();
    }

// CAPACIDADE

    public boolean hasCapacityFor(Vehicle vehicle) {
        return energyManager.canAcceptVehicle(chargingPoints, vehicle);
    }


// PONTOS
    public ChargingPoint getAvailablePoint() {

        for (ChargingPoint point : chargingPoints) {
            if (!point.isOccupied()) {
                return point;
            }
        }

        return null;
    }

    public ChargingPoint getPointById(int id) {

        for (ChargingPoint point : chargingPoints) {
            if (point.getId() == id) {
                return point;
            }
        }

        return null;
    }

// PROCESSAMENTO

    public void processChargingStart(Event event) {

        ChargingPoint point = getPointById(event.getChargingPointId());
        Vehicle vehicle = event.getVehicle();

        if (point == null || vehicle == null) return;

        point.connectVehicle(vehicle);
        vehicle.setStatus(VehicleStatus.CHARGING);
    }

    public void processChargingEnd(Event event) {

        ChargingPoint point = getPointById(event.getChargingPointId());
        Vehicle vehicle = event.getVehicle();

        if (point == null || vehicle == null) return;

        vehicle.setStatus(VehicleStatus.FINISHED);
        point.disconnectVehicle();
    }

// REBALANCEAMENTO

   public boolean rebalancePower() {
    return energyManager.rebalancePower(chargingPoints);
}

// RESET

    public void reset() {

        waitingQueue.clear();

        for (ChargingPoint point : chargingPoints) {
            point.disconnectVehicle();
            point.release();
        }
    }

// GETTERS


    public double getEnergyPrice() {
        return energyPrice;
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }
    public SmartQueue getWaitingQueue() {
        return this.waitingQueue;
    }
}