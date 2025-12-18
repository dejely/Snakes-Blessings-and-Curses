package game.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Blessing extends Tile {

    private final BlessingType blessingType;

    public Blessing(int index, BlessingType blessingType) {
        super(index, TileType.BLESSING);
        this.blessingType = blessingType;
    }

    public Blessing(int index) {
        super(index, TileType.BLESSING);
        this.blessingType = randomType();
    }

    private BlessingType randomType() {
        BlessingType[] values = BlessingType.values();
        return values[new Random().nextInt(values.length)];
    }

    @Override
    public String applyEffect(Player player, Game game) {
        StringBuilder msg = new StringBuilder();
        msg.append(" -> Landed on BLESSING: ").append(blessingType).append("! ");

        switch (blessingType) {
            case FORETOLD_FATE -> {
                player.hasForetoldFate = true;
                msg.append("\n(Foretold Fate) On your next turn, choose from 1-10 steps.");
            }
            case DANIELS_BLESSING -> {
                player.danielBlessingTurns = 2;
                // Fixed text encoding here
                msg.append("\n(Daniel's Blessing) For the next two turns, snakes won't bite you.");
            }
            case SWITCHEROO -> {
                player.hasSwitcheroo = true;
                msg.append("\n(Switcheroo) Swap places with a random player higher than you.");
            }
            case JACOBS_LADDER -> {
                player.jacobsLadderCharges = 2;
                msg.append("\n(Jacob's Ladder) Nullifies the next 2 cursed effects you step on.");
            }
            case SHACKLED -> {
                for (Player p : game.getPlayers()) {
                    if (p != player) {
                        p.isShackled = true;
                    }
                }
                msg.append("\n(Shackled) Chains OTHER players! Subtract 2 pips from their next roll.");
            }
            case SEMENTED -> {
                // LOGIC FIX: Check who is actually ahead
                List<Player> targets = new ArrayList<>();
                for (Player p : game.getPlayers()) {
                    if (p.getPosition() > player.getPosition()) {
                        targets.add(p);
                    }
                }

                if (targets.isEmpty()) {
                    // Case 0: No one ahead. DO NOT trigger the effect.
                    player.hasSemented = false; 
                    msg.append("\n(Semented) You reached for a bond, but no one is ahead! The effect fizzles.");
                } 
                else if (targets.size() == 1) {
                    // Case 1: Only one person. Auto-link.
                    player.hasSemented = true;
                    player.sementedTurns = 3; 
                    Player target = targets.get(0);
                    player.sementedTarget = target; 
                    
                    msg.append("\n(Semented) Automatically bonded with ").append(target.getName()).append(" for 3 turns.");
                } 
                else {
                    // Case 2: Multiple people. User must choose.
                    player.hasSemented = true;
                    player.sementedTurns = 3;
                    player.sementedTarget = null; // UI must fill this
                    msg.append("\n(Semented) Multiple players ahead! Choose one to bond with for 3 turns.");
                }
            }
        }
        return msg.toString();
    }
}