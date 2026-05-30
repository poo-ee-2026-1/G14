package backend.fleetmanager;

// Enum responsável por representar os possíveis estados operacionais de um veículo na simulação

public enum VehicleStatus {

    // Veículo ainda está entrando na estação
    ARRIVING,

    // Veículo está esperando em um ponto de carregamento
    WAITING,

    // Veículo conectado e recebendo energia
    CHARGING,

    // Veículo concluiu o carregamento
    FINISHED
}