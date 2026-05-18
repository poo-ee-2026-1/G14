package backend.stationmanager;

import backend.fleetmanager.Vehicle;

// Classe responsável por representar
// um ponto físico de carregamento.
//
// Cada ChargingPoint:
// - conecta um veículo
// - entrega energia
// - controla potência
// - acompanha consumo energético
public class ChargingPoint {

    // =========================
    // ATRIBUTOS
    // =========================

    // Identificador único do ponto
    private final int id;

    // Indica se o ponto está ocupado
    private boolean occupied;

    // Veículo atualmente conectado
    private Vehicle connectedVehicle;

    // Potência atual fornecida (kW)
    private double currentPower;

    // Energia total entregue (kWh)
    private double energyDelivered;

    // =========================
    // CONSTRUTOR
    // =========================

    // Cria um novo ponto de carregamento
    public ChargingPoint(int id) {

        this.id = id;

        this.occupied = false;

        this.connectedVehicle = null;

        this.currentPower = 0.0;

        this.energyDelivered = 0.0;
    }

    // =========================
// RESERVA
// =========================

// Reserva o ponto sem conectar veículo ainda
// Evita que dois eventos peguem o mesmo ponto
public void reserve() {

    this.occupied = true;
}
public void release() {

    this.occupied = false;
}
    // =========================
    // DISPONIBILIDADE
    // =========================


    // Verifica se o ponto está livre
    public boolean isAvailable() {

        return !occupied;
    }

    // =========================
    // CONEXÃO DE VEÍCULO
    // =========================

    // Conecta um veículo ao ponto
// Conecta um veículo ao ponto
    public void connectVehicle(
        Vehicle vehicle ) {

    // Permite conexão mesmo após reserve()
    // desde que não haja veículo já conectado
    if (connectedVehicle == null && vehicle != null) {

        this.connectedVehicle = vehicle;

        this.occupied = true;
    }
}

    // =========================
    // ENTREGA DE ENERGIA
    // =========================

    // Entrega energia ao veículo
    // com base no tempo da simulação
    public void deliverEnergy(
            double deltaSimulationTime
    ) {

        // Só funciona se houver veículo
        if (
                occupied
                        &&
                        connectedVehicle != null
        ) {

            // Converte segundos para horas
            double hours =
                    deltaSimulationTime / 3600.0;

            // Calcula energia entregue
            //
            // kWh = kW × h
            double energy =
                    currentPower * hours;

            // Descobre energia faltante
            double missingEnergy =
                    connectedVehicle
                            .getEnergyNeeded();

            // Garante que não ultrapasse
            // a carga máxima do veículo
            double actualEnergy =
                    Math.min(
                            energy,
                            missingEnergy
                    );

            // Atualiza bateria do veículo
            connectedVehicle
                    .updateEnergy(
                            actualEnergy
                    );

            // Acumula energia total entregue
            this.energyDelivered +=
                    actualEnergy;
        }
    }

    // =========================
    // DESCONEXÃO
    // =========================

    // Remove veículo conectado
    public void disconnectVehicle() {

        this.connectedVehicle = null;

        this.occupied = false;

        this.currentPower = 0.0;
    }

    // =========================
    // RESET
    // =========================

    // Reinicia o ponto
    public void reset() {

        disconnectVehicle();

        this.energyDelivered = 0.0;
    }

    // =========================
    // GETTERS E SETTERS
    // =========================

    // Retorna ID do ponto
    public int getId() {

        return id;
    }

    // Retorna estado de ocupação
    public boolean isOccupied() {

        return occupied;
    }

    // Retorna veículo conectado
    public Vehicle getConnectedVehicle() {

        return connectedVehicle;
    }

    // Retorna potência atual
    public double getCurrentPower() {

        return currentPower;
    }

    // Define potência atual
    public void setCurrentPower(
            double power
    ) {

        // Impede potência negativa
        this.currentPower =
                Math.max(power, 0.0);
    }

    // Retorna energia total entregue
    public double getEnergyDelivered() {

        return energyDelivered;
    }
}