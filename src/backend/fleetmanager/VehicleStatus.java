package backend.fleetmanager;

// Enum responsável por representar os possíveis estados operacionais de um veículo na simulação

public enum VehicleStatus {

    // =========================
    // VEÍCULO CHEGANDO
    // =========================

    // Veículo ainda está entrando na estação
    ARRIVING,

    // =========================
    // VEÍCULO AGUARDANDO
    // =========================

    // Veículo está esperando em um ponto de carregamento
    WAITING,

    // =========================
    // VEÍCULO CARREGANDO
    // =========================

    // Veículo conectado e recebendo energia
    CHARGING,

    // =========================
    // CARREGAMENTO FINALIZADO
    // =========================

    // Veículo concluiu o carregamento
    FINISHED
}