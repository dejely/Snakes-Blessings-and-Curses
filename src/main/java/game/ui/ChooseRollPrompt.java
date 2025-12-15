package game.ui;

import javax.swing.*;

public class ChooseRollPrompt implements SkillPrompt<Integer> {

    @Override
    public Integer prompt() {
        Integer[] options = {1, 2, 3, 4, 5, 6};

        return (Integer) JOptionPane.showInputDialog(
                null,
                "Choose your roll value:",
                "Choose Exact Roll",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }
}
