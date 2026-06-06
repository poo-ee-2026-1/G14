package backend.stationmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import backend.config.SimulationConfig;
import backend.fleetmanager.Vehicle;

// Classe responsável por gerenciar a fila inteligente de veículos.
public class SmartQueue {

    // ATRIBUTOS
  
    // Fila de prioridade
    private final PriorityQueue<Vehicle>
            queue;

    // Peso da bateria descarregada
    private final double weightBattery;

    // Peso do tempo de espera
    private final double weightWaitingTime;

    // CONSTRUTOR

         // Inicializa fila inteligente
        public SmartQueue() {

         this.queue = new PriorityQueue<>();

         // Pesos vindos da config
         this.weightBattery =
            SimulationConfig.BATTERY_WEIGHT;

        this.weightWaitingTime =
            SimulationConfig.WAITING_WEIGHT;
}

    // Adiciona veículo na fila
    // Adiciona veículo na fila
public void addVehicle(Vehicle vehicle, double currentTime) {

    if (vehicle == null) return;

    calculatePriority(vehicle, currentTime);

    queue.add(vehicle);
}
public void addVehicle(Vehicle vehicle) {
    addVehicle(vehicle, 0);
}
    // Retorna veículo de maior prioridade
    public Vehicle getNextVehicle() {

        return queue.poll();
    }

// CÁLCULO DE PRIORIDADE

    // Calcula score de prioridade
    public void calculatePriority(
            Vehicle vehicle,
            double currentTime
    ) {

        // Atualiza tempo de espera
        vehicle.updateWaitingTime(
                currentTime
        );

        // Quanto menor o SoC, maior a prioridade
        double batteryFactor =
                (
                        100.0
                                -
                                vehicle.getStateOfCharge()
                )
                        / 100.0;

        // Tempo de espera normalizado
        double waitingFactor =
        vehicle.getWaitingTime()
                / SimulationConfig.WAITING_TIME_NORMALIZATION;

        // Score ponderado final
        double score =
                (
                        batteryFactor
                                * weightBattery
                )
                        +
                        (
                                waitingFactor
                                        * weightWaitingTime
                        );

        // Atualiza score do veículo
        vehicle.setPriorityScore(
                score
        );
    }

// REORDENAÇÃO

    // Atualiza prioridades de todos os veículos
    public void updatePriorities(
            double currentTime
    ) {

        // Verifica se a fila está vazia
        if (queue.isEmpty()) {

            return;
        }

        // Lista temporária
        List<Vehicle> temp =
                new ArrayList<>();

        // Remove todos os veículos
        while (!queue.isEmpty()) {

            Vehicle vehicle =
                    queue.poll();

            // Recalcula score
            calculatePriority(
                    vehicle,
                    currentTime
            );

            // Guarda temporariamente
            temp.add(vehicle);
        }

        // Reinsere todos

        queue.clear();
        queue.addAll(temp);
    }

// UTILITÁRIOS

    // Verifica se fila está vazia
    public boolean isEmpty() {

        return queue.isEmpty();
    }

    // Limpa fila
    public void clear() {

        queue.clear();
    }
    public int size() {
    return queue.size();
}

// RETORNA COPIA DA FILA ORDENADA

public List<Vehicle> getVehicles() {
    // Criamos uma cópia para preservar a estrutura original da PriorityQueue
    PriorityQueue<Vehicle> copy = new PriorityQueue<>(this.queue);
    List<Vehicle> sortedList = new ArrayList<>();
    
    // Descarrega a fila mantendo a ordenação de prioridade
    while (!copy.isEmpty()) {
        sortedList.add(copy.poll());
    }
    return sortedList;
}
public void refresh(double currentTime) {
    updatePriorities(currentTime);
}
}