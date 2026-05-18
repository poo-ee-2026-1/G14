package backend.fleetmanager;

// Enum responsável por representar
// os possíveis estados operacionais
// de um veículo na simulação.
//
// O enum limita os estados válidos,
// evitando uso de Strings soltas.
public enum VehicleStatus {

    // =========================
    // VEÍCULO CHEGANDO
    // =========================

    // Veículo ainda está entrando
    // na estação.
    ARRIVING,

    // =========================
    // VEÍCULO AGUARDANDO
    // =========================

    // Veículo está esperando
    // um ponto de carregamento.
    WAITING,

    // =========================
    // VEÍCULO CARREGANDO
    // =========================

    // Veículo conectado
    // e recebendo energia.
    CHARGING,

    // =========================
    // CARREGAMENTO FINALIZADO
    // =========================

    // Veículo concluiu
    // o carregamento.
    FINISHED
}