package game.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

import game.engine.Blessing;
import game.engine.Curse;
import game.engine.Dice;
import game.engine.Game;
import game.engine.Ladder;
import game.engine.Player;
import game.engine.Snake;
import game.engine.Tile;

public class ControlsPanel extends JPanel {

    private BoardPanel boardPanel;
    // We need a reference to the Main Window so we can tell it to "Exit"
    private GameWindow gameWindow; 
    
    private final List<Player> players;
    private Game game;
    private int currentPlayerIndex = 0; 
    private boolean isAnimating = false; 

    public final DicePanel dicePanel;
    public final PlayerInfoPanel playerInfoPanel;

    // CHANGED: Constructor now accepts GameWindow
    public ControlsPanel(GameWindow window, BoardPanel board, int initialPlayerCount) {
        this.gameWindow = window; // Save the reference
        this.boardPanel = board;
        this.game = new Game(initialPlayerCount);
        this.players = game.getPlayers();
        boardPanel.updatePositionsFromPlayers(players);


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dicePanel = new DicePanel();
        playerInfoPanel = new PlayerInfoPanel();

        add(dicePanel);
        add(Box.createVerticalStrut(20));
        add(playerInfoPanel);
        add(Box.createVerticalGlue()); // Pushes everything up
        
        // --- NEW: EXIT BUTTON ---
        JButton exitBtn = new JButton("Exit to Menu");
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setBackground(new Color(200, 50, 50)); // Red
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setMaximumSize(new Dimension(200, 40));
        
        exitBtn.addActionListener(e -> {
            // Confirm before quitting
            int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to quit the current game?", 
                "Exit Game", JOptionPane.YES_NO_OPTION);
                
            if (choice == JOptionPane.YES_OPTION) {
                gameWindow.returnToMenu(); // Call the method in GameWindow
            }
        });
        
        add(Box.createVerticalStrut(20));
        add(exitBtn);
        // ------------------------
        
        playerInfoPanel.refreshUI("");
    }

    private void updatePlayerPositions() {
        List<Integer> pos = new ArrayList<>();
        for (Player p : players) {
            pos.add(p.getPosition());
        }
        boardPanel.updatePositions(pos);
    }

    // --- ANIMATION PHASE 1: DICE FLICKER ---
    public void startDiceAnimation() {
        if (isAnimating) return;
        isAnimating = true;
        dicePanel.toggleButtons(false);

        Timer diceTimer = new Timer(50, null); 
        final int[] ticks = {0}; 

        diceTimer.addActionListener(e -> {
            dicePanel.showRandomFace();
            ticks[0]++;

            if (ticks[0] >= 20) {
                diceTimer.stop();

                Player currentPlayer = players.get(currentPlayerIndex);
                int roll = Dice.DiceRollRNG(currentPlayer); // roll using engine dice
                dicePanel.setFinalFace((roll + 1)/2, (roll + 1)/2); // optional visual adjustment

                // Move player via engine
                currentPlayer.move(roll, game.getBoard()); // engine handles curses/blessings
                updatePlayerPositions();

                finishTurnLogic();
            }
        });
        diceTimer.start();
    }

    // --- ANIMATION PHASE 2: PLAYER HOPPING ---
    // removed for the meantime

    // --- PHASE 3: LOGIC CHECKS ---
    private void finishTurnLogic() {
        Player currentPlayer = players.get(currentPlayerIndex);
        int landedPos = currentPlayer.getPosition();
        Tile landedTile = game.getBoard().getTile(landedPos);

        String message = "";

        if (landedTile instanceof Blessing) {
            message = " (BLESSING!)";
        } else if (landedTile instanceof Curse) {
            message = " (CURSE!)";
        } else if (landedTile instanceof Snake) {
            message = " (SNAKE!)";
        } else if (landedTile instanceof Ladder) {
            message = " (LADDER!)";
        }

        // Win check
        if (landedPos >= game.getBoard().getSize() - 1) {
            JOptionPane.showMessageDialog(
                this,
                "CONGRATULATIONS!\n" + currentPlayer.getName() + " has won!",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
            );
            gameWindow.returnToMenu();
            return;
        }

        // Next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        playerInfoPanel.refreshUI(message);

        isAnimating = false;
        dicePanel.toggleButtons(true);
    }

    // ================== DICE PANEL ==================
    public class DicePanel extends JPanel {
        private static final String[] DIE_FACES = { "\u2680", "\u2681", "\u2682", "\u2683", "\u2684", "\u2685" };
        private final Random rnd = new Random();
        private final JLabel die1 = new JLabel(DIE_FACES[0], SwingConstants.CENTER);
        private final JLabel die2 = new JLabel(DIE_FACES[0], SwingConstants.CENTER);
        private final JButton rollBtn = new JButton("Roll Dice");

        public DicePanel() {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createTitledBorder("Dice"));
            setBackground(new Color(245, 245, 245));
            
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 48);
            die1.setFont(font); die2.setFont(font);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 0; add(die1, c);
            c.gridx = 1; add(die2, c);

            rollBtn.addActionListener(e -> startDiceAnimation());

            c.gridx = 0; c.gridy = 1; c.gridwidth = 2; c.fill = GridBagConstraints.HORIZONTAL;
            add(rollBtn, c);
        }

        public void toggleButtons(boolean enabled) {
            rollBtn.setEnabled(enabled);
        }

        public void showRandomFace() {
            die1.setText(DIE_FACES[rnd.nextInt(6)]);
            die2.setText(DIE_FACES[rnd.nextInt(6)]);
        }

        public void setFinalFace(int v1, int v2) {
            die1.setText(DIE_FACES[v1 - 1]);
            die2.setText(DIE_FACES[v2 - 1]);
        }
    }

    // ================== PLAYER INFO PANEL ==================
    public class PlayerInfoPanel extends JPanel {
        private final JPanel listPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        public PlayerInfoPanel() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createTitledBorder("Current Turn"));
            add(listPanel, BorderLayout.CENTER);
        }

        public void refreshUI(String lastActionMessage) {
            listPanel.removeAll();
            for (int i = 0; i < players.size(); i++) {
                String name = players.get(i).getName();
                int pos = players.get(i).getPosition();;
                String text = name + " (Tile " + pos + ")";
                JLabel lbl = new JLabel(text);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createEtchedBorder());
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setPreferredSize(new Dimension(180, 30));
                
                if (i == currentPlayerIndex) {
                    lbl.setBackground(new Color(255, 230, 150)); 
                    lbl.setText("--> " + text);
                    lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
                } 
                else if (i == (currentPlayerIndex - 1 + players.size()) % players.size()) {
                    lbl.setBackground(new Color(240, 240, 240));
                    lbl.setText(text + lastActionMessage);
                }
                else {
                    lbl.setBackground(new Color(240, 240, 240));
                }
                listPanel.add(lbl);
            }
            listPanel.revalidate();
            listPanel.repaint();
        }
    }
}