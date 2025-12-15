package game.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Player {
    private final String name;
    private int position = 1;
    private final List<Effect> effects = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void move(int steps) {
        position = Math.min(position + steps, 100);
    }

    public void addEffect(Effect e, Game g) {
        effects.add(e);
        e.onApply(this, g);
    }

    public boolean hasEffect(EffectType type) {
        return effects.stream().anyMatch(e -> e.getType() == type);
    }

    public List<Effect> getEffects() {
        return Collections.unmodifiableList(effects);
    }

    public int applyDiceModifiers(int base) {
        int modified = base;
        for (Effect e : effects) {
            modified = e.modifyDice(modified);
        }
        return modified;
    }

    public void startTurn(Game g) {
        effects.forEach(e -> e.onStartTurn(this, g));
    }

    public void endTurn(Game g) {
        Iterator<Effect> it = effects.iterator();
        while (it.hasNext()) {
            Effect e = it.next();
            e.onEndTurn(this, g);
            e.decrement();
            if (e.getRemainingTurns() <= 0) {
                e.onExpire(this, g);
                it.remove();
            }
        }
    }
}
