package game.ui;

import game.character.Skill;
import game.character.ActiveSkills.ChooseExactRoll;
import game.engine.Game;
import game.engine.GameListener;
import game.engine.Player;
import game.engine.TurnPhase;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel implements GameListener {

    private final Game game;
    private final JButton rollBtn = new JButton("Roll Dice");
    private final JButton skillBtn = new JButton("Use Skill");
    
    private final JLabel status = new JLabel(" ");

    public ControlPanel(Game game) {
        this.game = game;

        setLayout(new FlowLayout());

        add(rollBtn);
        add(skillBtn);
        skillBtn.addActionListener(e -> {
            Player player = game.getCurrentPlayer();
            Skill skill = player.getCharacter().getActiveSkill();

            if (!skill.canActivate(player, game)) return;

            if (skill instanceof ChooseExactRoll chooseRollSkill) {
                ChooseRollPrompt prompt = new ChooseRollPrompt();
                Integer roll = prompt.prompt();

                if (roll == null) return;

                chooseRollSkill.setChosenRoll(roll);
            }

            game.activateSkill();
            update();
        });
    }

    public void update() {
        Player p = game.getCurrentPlayer();
        TurnPhase phase = game.getPhase();

        skillBtn.setEnabled(
            phase == TurnPhase.SKILL &&
            p.getCharacter().getActiveSkill().canActivate(p, game)
        );

        add(status);

        rollBtn.addActionListener(e -> game.rollDice());
        skillBtn.addActionListener(e -> game.activateSkill());
    }

    @Override
    public void onTurnStarted(Player p) {
        status.setText("Turn: " + p.getName());
        skillBtn.setEnabled(
            p.getCharacter()
             .getActiveSkill()
             .canActivate(p, game)
        );
        rollBtn.setEnabled(true);
    }

    @Override
    public void onDiceRolled(Player p, int value) {
        rollBtn.setEnabled(false);
    }
    
    

}
