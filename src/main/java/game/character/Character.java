package game.character;

public abstract class Character {
    protected Skill passiveSkill;
    protected Skill activeSkill;

    public Skill getPassiveSkill() {
        return passiveSkill;
    }

    public Skill getActiveSkill() {
        return activeSkill;
    }
}
