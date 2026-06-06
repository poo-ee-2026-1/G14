package backend.simulator;

import java.util.PriorityQueue;

import backend.config.SimulationConfig;
import backend.core.TimeSystem;
import backend.fleetmanager.FleetManager;
import backend.fleetmanager.Vehicle;
import backend.stationmanager.ChargingPoint;
import backend.stationmanager.StationManager;

public class Simulator {

    private double simulationTime;
    private boolean started = false;

    private final PriorityQueue<Event> eventQueue;
    private final StationManager stationManager;
    private final FleetManager fleetManager;
    private final TimeSystem timeSystem;
    private final EventLogger eventLogger; // Novo atributo

    public Simulator() {
        this.simulationTime = 0.0;
        this.eventQueue = new PriorityQueue<>();
        this.stationManager = new StationManager();
        this.fleetManager = new FleetManager();
        this.timeSystem = new TimeSystem();
        this.eventLogger = new EventLogger(); // Inicializado
    }

    // ----------------------------
    // CONTROLE PRINCIPAL
    // ----------------------------

    public void startSimulation() {
        if (!started) {
            initializeSimulation();
            started = true;
            eventLogger.log("Simulação iniciada.");
        }
    }

    public void initializeSimulation() {
        reset();
        scheduleArrivals();
        eventLogger.log("Simulação inicializada e cenário montado.");
    }

    public void reset() {
        simulationTime = 0.0;
        eventQueue.clear();
        stationManager.reset();
        fleetManager.reset();
        started = false;
        eventLogger.log("Sistema resetado.");
    }

    // ----------------------------
    // LOOP PRINCIPAL
    // ----------------------------

    public void update(double deltaRealTime) {
        timeSystem.update(deltaRealTime);
        double deltaSim = timeSystem.getDeltaSimulationTime();
        simulationTime = timeSystem.getAccumulatedSimulationTime();

        while (!eventQueue.isEmpty() && eventQueue.peek().getTimestamp() <= simulationTime) {
            processEvent(eventQueue.poll());
        }

        stationManager.getWaitingQueue().updatePriorities(simulationTime);
        updateContinuousCharging(deltaSim);
    }

    private void processEvent(Event event) {
        switch (event.getType()) {
            case VEHICLE_ARRIVAL -> handleVehicleArrival(event.getVehicle());
            case START_CHARGING -> {
                stationManager.processChargingStart(event);
                eventLogger.log("Veículo " + event.getVehicle().getId() + " iniciou recarga.");
            }
        }
    }

    private void handleVehicleArrival(Vehicle vehicle) {
        if (vehicle == null) return;
        
        eventLogger.log("Veículo " + vehicle.getId() + " chegou à estação.");

        if (stationManager.hasCapacityFor(vehicle)) {
            ChargingPoint point = stationManager.getAvailablePoint();
            if (point != null) {
                processEvent(new Event(simulationTime, EventType.START_CHARGING, vehicle, point.getId()));
                return;
            }
        }
        
        eventLogger.log("Veículo " + vehicle.getId() + " entrou na fila de espera.");
        stationManager.addVehicleToQueue(vehicle);
    }

    private void updateContinuousCharging(double deltaTime) {
        for (ChargingPoint point : stationManager.getChargingPoints()) {
            if (!point.isOccupied()) continue;
            point.deliverEnergy(deltaTime);
            Vehicle v = point.getConnectedVehicle();

            if (v != null && v.getStateOfCharge() >= 100.0) {
                eventLogger.log("Veículo " + v.getId() + " concluiu a recarga.");
                point.disconnectVehicle();
                stationManager.rebalancePower();
                checkQueueAndCharge();
            }
        }
    }

    private void checkQueueAndCharge() {
        while (!stationManager.getWaitingQueue().isEmpty()) {
            Vehicle v = stationManager.getNextInQueue();
            if (v == null) break;

            if (!stationManager.hasCapacityFor(v)) {
                stationManager.addVehicleToQueue(v);
                break;
            }

            ChargingPoint p = stationManager.getAvailablePoint();
            if (p == null) {
                stationManager.addVehicleToQueue(v);
                break;
            }

            processEvent(new Event(simulationTime, EventType.START_CHARGING, v, p.getId()));
        }
    }

    // ----------------------------
    // GETTERS E LOGGERS
    // ----------------------------

    public StationManager getStationManager() { return stationManager; }
    public double getSimulationTime() { return simulationTime; }
    public FleetManager getFleetManager() { return fleetManager; }
    public EventLogger getEventLogger() { return eventLogger; } // Agora existe!

    public void scheduleEvent(Event event) {
        if (event != null) eventQueue.add(event);
    }
    
    private void scheduleArrivals() {
        double time = 0.0;
        for (Vehicle vehicle : fleetManager.getVehicles()) {
            if (!SimulationConfig.STRESS_TEST_MODE) time += SimulationConfig.ARRIVAL_INTERVAL;
            scheduleEvent(new Event(time, EventType.VEHICLE_ARRIVAL, vehicle));
        }
    }
}