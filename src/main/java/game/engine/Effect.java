package game.engine;

enum EffectType {
    BOON,
    CURSE,
    CONTROL,
    BUFF,
    DEBUFF
}

public interface Effect {
    EffectType getType();
    int getRemainingTurns();
    void decrement();

    default void onApply(Player p, Game g) {}
    default void onStartTurn(Player p, Game g) {}
    default int modifyDice(int base) { return base; }
    default void onEndTurn(Player p, Game g) {}
    default void onExpire(Player p, Game g) {}
}
