package game.engine;

import game.character.*;
import game.character.Character;
import game.effects.*;
import java.util.*;

public class Player {
    private final String name;
    private final Character character;
    private int position = 0;
    private final List<Effect> effects = new ArrayList<>();

    public Player(String name, Character character) {
        this.name = name;
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public Character getCharacter() {
        return character;
    }

    public void move(int steps) {
        position = Math.min(Math.max(1, position + steps), 100);
        System.out.println(name + " moved to " + position);

    }

    public void addEffect(Effect e, Game g) {
        effects.add(e);
        e.onApply(this, g);
    }

    public void startTurn(Game g) {
        character.getPassiveSkill().onTurnStart(this, g);
        effects.forEach(e -> e.onTurnStart(this, g));
    }

    public RollContext buildRollContext(Game g, int baseRoll) {
        RollContext ctx = new RollContext();
        ctx.rollValue = baseRoll;
        effects.forEach(e -> e.onBeforeRoll(this, g, ctx));
        return ctx;
    }

    public void afterMove(Game g) {
        effects.forEach(e -> e.onAfterMove(this, g));
    }

    public void endTurn(Game g) {
        if (position >= 100) return;

        Iterator<Effect> it = effects.iterator();
        while (it.hasNext()) {
            Effect e = it.next();
            e.onTurnEnd(this, g);
            e.decrementDuration();
            if (e.isExpired()) it.remove();
        }

        Skill active = character.getActiveSkill();
        if (active instanceof ActiveSkill as) {
            as.tickCooldown();
        }
    }

    public boolean canActivateSkill(Game g) {
        return character.getActiveSkill().canActivate(this, g);
    }

    public void activateSkill(Game g) {
        character.getActiveSkill().activate(this, g);
    }
}
