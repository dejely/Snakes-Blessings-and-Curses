package game.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import game.engine.Game;
import game.engine.Player;

public class GameWindow extends JFrame {

    private final Game game;
    private final BoardPanel boardPanel;
    private final ControlsPanel controls;

    public GameWindow() {
        super("Snakes: Blessings and Curses");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // model
        game = new Game();

        // view
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        controls = new ControlsPanel();
        controls.setPreferredSize(new Dimension(300, 800));
        add(controls, BorderLayout.WEST);

        // sync UI -> Model when player list changes
        controls.setOnPlayersChanged(() -> {
            // clear and re-add players from UI panel
            game.clearPlayers();
            List<String> names = controls.getPlayerNames();
            for (String n : names) {
                game.addPlayer(n);
            }
            boardPanel.setModel(game.getBoard(), game.getPlayers(), game.getCurrentPlayerIndex());
        });

        // roll button behavior
        controls.addRollListener(e -> {
            if (game.getPlayers().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please add players first.");
                return;
            }

            int[] dice = game.rollForCurrentPlayer(); // {d1, d2, total}
            controls.setDiceFaces(dice[0], dice[1]);

            game.processCurrentPlayerTurn(dice[2]);

            boardPanel.setModel(game.getBoard(), game.getPlayers(), game.getCurrentPlayerIndex());

            if (game.isGameOver()) {
                // find winner
                Player winner = game.getPlayers().stream()
                        .filter(p -> p.getPosition() >= game.getBoard().getSize() - 1)
                        .findFirst()
                        .orElse(null);
                String msg = (winner != null) ? winner.getName() + " wins!" : "Game over";
                JOptionPane.showMessageDialog(this, msg);
            }
        });

        pack();
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // launch
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameWindow::new);
    }
}
