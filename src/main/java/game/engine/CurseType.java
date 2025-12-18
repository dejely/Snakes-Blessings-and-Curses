package game.engine;

public enum CurseType {
    WHAT_ARE_THE_ODDS,    // must roll even to move forward
    BARRED_HEAVEN,        // next 3 turns, ladders won't work
    UNMOVABLE_MAN,        // skip next turn
    PILLAR_OF_SALT,       // next turn lost if eaten by snake
    JOBS_SUFFERING        // apply all curses
}