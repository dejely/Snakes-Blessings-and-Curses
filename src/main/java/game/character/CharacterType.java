package game.character;

import game.character.types.*;

public enum CharacterType {

    SEER("Seer"),
    SURVIVOR("Survivor"),
    TRICKSTER("Trickster");

    private final String displayName;

    CharacterType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Character create() {
        return switch (this) {
            case SEER -> new Seer();
            case SURVIVOR -> new Survivor();
            case TRICKSTER -> new Trickster();
        };
    }
}
