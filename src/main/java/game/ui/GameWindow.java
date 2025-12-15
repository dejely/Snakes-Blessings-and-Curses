package game.ui;

import game.engine.*;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame implements GameListener {

    private final BoardPanel boardPanel;
    private final ControlPanel controlPanel;

    public GameWindow(Game game) {
        setTitle("Snakes: Blessings and Curses");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel(game);
        controlPanel = new ControlPanel(game);

        // ✅ ONLY GameWindow is the listener
        game.addListener(this);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ================= GameListener =================

    @Override
    public void onTurnStarted(Player p) {
        boardPanel.repaint();
        controlPanel.update();
    }

    public void onDiceRolled(int value) {
        controlPanel.update();
    }

    public void onPlayerMoved(Player p) {
        boardPanel.repaint();
        controlPanel.update();
        System.out.println("UI repaint after move");
    }

    @Override
    public void onEffectsUpdated(Player p) {
        controlPanel.update();
    }

    @Override
    public void onGameEnded(Player winner) {
        JOptionPane.showMessageDialog(
            this,
            winner.getName() + " wins!",
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
