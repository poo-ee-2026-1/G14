package backend.fleetmanager;

public enum CarColor {

    BRANCO("white"),
    PRETO("black"),
    PRATA("silver"),
    AZUL("blue"),
    VERMELHO("red");

    private final String assetName;

    CarColor(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }
}