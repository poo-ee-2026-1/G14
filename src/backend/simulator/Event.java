package backend.simulator;

// Importa a entidade Vehicle do módulo fleetmanager
import backend.fleetmanager.Vehicle;

// Classe que representa um evento ocorrido na simulação.
// Eventos são utilizados para controlar ações importantes
// dentro do sistema.
public class Event implements Comparable<Event>{

    // =========================
    // CONSTANTES
    // =========================

    // Valor padrão indicando ausência de ponto de carregamento
    private static final int NO_CHARGING_POINT = -1;

    // =========================
    // ATRIBUTOS
    // =========================

    // Momento em que o evento ocorreu
    private final double timestamp;

    // Tipo do evento
    private final EventType type;

    // Veículo associado ao evento
    private final Vehicle vehicle;

    // ID do ponto de carregamento relacionado ao evento
    private final int chargingPointId;

    // =========================
    // CONSTRUTORES
    // =========================

    // Construtor para eventos sem ponto de carregamento
    public Event(
            double timestamp,
            EventType type,
            Vehicle vehicle
    ) {

        // Reaproveita o construtor principal
        this(
                timestamp,
                type,
                vehicle,
                NO_CHARGING_POINT
        );
    }

    // Construtor principal do evento
    public Event(
            double timestamp,
            EventType type,
            Vehicle vehicle,
            int chargingPointId
    ) {

        // Define o momento do evento
        this.timestamp = timestamp;

        // Define o tipo do evento
        this.type = type;

        // Define o veículo relacionado
        this.vehicle = vehicle;

        // Define o ponto de carregamento relacionado
        this.chargingPointId = chargingPointId;
    }

    // =========================
    // GETTERS
    // =========================

    // Retorna o timestamp do evento
    public double getTimestamp() {
        return timestamp;
    }

    // Retorna o tipo do evento
    public EventType getType() {
        return type;
    }

    // Retorna o veículo associado
    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean hasChargingPoint() {
    return chargingPointId != NO_CHARGING_POINT;
}
    // Retorna o ID do ponto de carregamento
    public int getChargingPointId() {
        return chargingPointId;
    }

   @Override
    public int compareTo(Event other) {
        return Double.compare(
        this.timestamp,
        other.timestamp
    );

}
}