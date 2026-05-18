package backend.stationmanager;

import backend.fleetmanager.Vehicle;
import java.util.ArrayList;
import java.util.List;

// Classe responsável pela
// distribuição de potência
public class EnergyManager {

    // =========================
    // ATRIBUTOS
    // =========================

    // Potência máxima da estação
    private final double stationMaxPower;

    // Pontos ativos
    private final List<ChargingPoint> activePoints;

    // =========================
    // CONSTRUTOR
    // =========================

    public EnergyManager(
            List<ChargingPoint> points,
            double stationMaxPower
    ) {

        this.stationMaxPower = stationMaxPower;

        this.activePoints = new ArrayList<>();
    }
// =========================
// VERIFICAÇÃO DE CAPACIDADE
// =========================

// Verifica se a estação consegue aceitar
// mais um veículo sem prejudicar os atuais.
//
// Simula o rebalanceamento com N+1 carros
// e verifica se todos receberiam pelo menos
// a potência mínima viável.
public boolean canAcceptVehicle(
        List<ChargingPoint> points,
        Vehicle newVehicle
) {

    double totalDemand = 0;

    for (ChargingPoint point : points) {

        if (
            point.isOccupied()
            &&
            point.getConnectedVehicle() != null
        ) {

            totalDemand +=
                point.getConnectedVehicle()
                     .getModel()
                     .getMaxChargingPower();
        }
    }

    totalDemand +=
        newVehicle.getModel()
                  .getMaxChargingPower();

    return totalDemand <= stationMaxPower;
}
    // =========================
    // REBALANCEAMENTO
    // =========================

    // Redistribui potência aproveitando
    // excedente de carros com limite baixo
    public boolean rebalancePower(List<ChargingPoint> points) {

    activePoints.clear();

    for (ChargingPoint point : points) {

        if (point.isOccupied() && point.getConnectedVehicle() != null) {
            activePoints.add(point);
        }
    }

    if (activePoints.isEmpty()) {
        return false;
    }

        // Potência disponível para distribuir
        double remainingPower = stationMaxPower;

        // Pontos que ainda podem receber mais
        List<ChargingPoint> needsMore =
                new ArrayList<>(activePoints);

        // Distribui em loop até não sobrar excedente
        while (!needsMore.isEmpty() && remainingPower > 0) {

            // Parte justa para cada ponto restante
            double fairShare =
                    remainingPower / needsMore.size();

            // Pontos que não conseguem absorver
            // a parte justa — vão devolver excedente
            List<ChargingPoint> saturated =
                    new ArrayList<>();

            for (ChargingPoint point : needsMore) {

                Vehicle vehicle =
                        point.getConnectedVehicle();

                double maxPower =
                        vehicle.getModel()
                                .getMaxChargingPower();

                // Carro não consegue absorver
                // a parte justa — satura
                if (maxPower <= fairShare) {

                    point.setCurrentPower(maxPower);

                    remainingPower -= maxPower;

                    saturated.add(point);
                }
            }

            // Se nenhum saturou, distribui
            // o restante igualmente e encerra
            if (saturated.isEmpty()) {

                for (ChargingPoint point : needsMore) {

                    point.setCurrentPower(fairShare);
                }

                break;
            }

            // Remove saturados da próxima rodada
            needsMore.removeAll(saturated);

        }
        return false;
    }
}