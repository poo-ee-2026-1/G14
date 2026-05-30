package backend.simulator;

// Enum responsável por representar todos os tipos possíveis de eventos da simulação
public enum EventType {

    // Evento disparado quando um veículo chega à estação de carregamento
    VEHICLE_ARRIVAL,

    // Evento disparado quando um carregamento inicia
    START_CHARGING,

    // Evento disparado quando um carregamento termina
    FINISH_CHARGING,

    // Evento responsável por redistribuir potência entre os carregadores ativos
    POWER_REBALANCE,

    // Evento que representa o encerramento da simulação
    SIMULATION_END
}