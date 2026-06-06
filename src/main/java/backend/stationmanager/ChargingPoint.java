package backend.stationmanager;

import backend.fleetmanager.Vehicle;

public class ChargingPoint {

    private final int id;
    private boolean occupied;
    private Vehicle connectedVehicle;

    private double currentPower;
    private double energyDelivered;

    public ChargingPoint(int id) {
        this.id = id;
        this.occupied = false;
        this.currentPower = 0.0;
        this.energyDelivered = 0.0;
    }

    public void connectVehicle(Vehicle vehicle) {
        if (vehicle == null) return;

        this.connectedVehicle = vehicle;
        this.occupied = true;
    }

    public void disconnectVehicle() {
        this.connectedVehicle = null;
        this.occupied = false;
        this.currentPower = 0.0;
    }

    public void deliverEnergy(double deltaSimSeconds) {

        if (!occupied || connectedVehicle == null || currentPower <= 0) {
            return;
        }

        double hours = deltaSimSeconds / 3600.0;
        double energy = currentPower * hours;

        double needed = connectedVehicle.getEnergyNeeded();
        double actual = Math.min(energy, needed);

        connectedVehicle.updateEnergy(actual);

        energyDelivered += actual;
    }
    public void reset() {
    disconnectVehicle();
    energyDelivered = 0.0;
    currentPower = 0.0;
}
    public double getEnergyDelivered() {
        return energyDelivered;
    }
    
    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getConnectedVehicle() {
        return connectedVehicle;
    }

    public double getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(double power) {
        this.currentPower = Math.max(0.0, power);
    }

    public int getId() {
        return id;
    }
}