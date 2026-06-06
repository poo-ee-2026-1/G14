package backend.stationmanager;

import java.util.List;

import backend.fleetmanager.Vehicle;

public class EnergyManager {

    private final double stationMaxPower;

    public EnergyManager(double stationMaxPower) {
        this.stationMaxPower = stationMaxPower;
    }

    public boolean canAcceptVehicle(List<ChargingPoint> points, Vehicle newVehicle) {

        double total = 0;

        for (ChargingPoint p : points) {
            if (p.isOccupied() && p.getConnectedVehicle() != null) {
                total += p.getConnectedVehicle().getModel().getMaxChargingPower();
            }
        }

        total += newVehicle.getModel().getMaxChargingPower();

        return total <= stationMaxPower;
    }

    public boolean rebalancePower(List<ChargingPoint> points) {

        int active = 0;

        for (ChargingPoint p : points) {
            if (p.isOccupied()) active++;
        }

        if (active == 0) return false;

        double fairShare = stationMaxPower / active;

        for (ChargingPoint p : points) {

            if (!p.isOccupied()) {
                p.setCurrentPower(0);
                continue;
            }

            Vehicle v = p.getConnectedVehicle();

            double max = v.getModel().getMaxChargingPower();

            p.setCurrentPower(Math.min(max, fairShare));
        }

        return true;
    }
}