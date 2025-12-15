package game.config;

public class BoardConfig {

    public final double normalProb;
    public final double boonProb;
    public final double curseProb;

    public BoardConfig(double normalProb, double boonProb, double curseProb) {
        this.normalProb = normalProb;
        this.boonProb = boonProb;
        this.curseProb = curseProb;
    }

    public static BoardConfig defaultConfig() {
        return new BoardConfig(0.70, 0.15, 0.15);
    }
}
