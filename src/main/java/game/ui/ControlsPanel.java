package game.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

public class ControlsPanel extends JPanel {

    private BoardPanel boardPanel;
    private GameWindow gameWindow; 
    
    private List<Integer> positions;
    private List<String> playerNames;
    private int currentPlayerIndex = 0; 
    private boolean isAnimating = false; 

    public final DicePanel dicePanel;
    public final PlayerInfoPanel playerInfoPanel;
    
    // --- NEW: Map to store where Snakes and Ladders lead ---
    private final Map<Integer, Integer> portalDestinations = new HashMap<>();

    public ControlsPanel(GameWindow window, BoardPanel board, int initialPlayerCount) {
        this.gameWindow = window; 
        this.boardPanel = board;
        this.positions = new ArrayList<>();
        this.playerNames = new ArrayList<>();

        // Initialize Portals (Start Tile -> End Tile)
        initializePortals();

        for (int i = 1; i <= initialPlayerCount; i++) {
            playerNames.add("Player " + i);
            positions.add(1); 
        }
        
        boardPanel.updatePositions(positions);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dicePanel = new DicePanel();
        playerInfoPanel = new PlayerInfoPanel();

        add(dicePanel);
        add(Box.createVerticalStrut(20));
        add(playerInfoPanel);
        add(Box.createVerticalGlue()); 
        
        // --- EXIT BUTTON ---
        JButton exitBtn = new JButton("Exit to Menu");
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setBackground(new Color(200, 50, 50)); // Red
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setMaximumSize(new Dimension(200, 40));
        
        exitBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to quit the current game?", 
                "Exit Game", JOptionPane.YES_NO_OPTION);
                
            if (choice == JOptionPane.YES_OPTION) {
                gameWindow.returnToMenu(); 
            }
        });
        
        add(Box.createVerticalStrut(20));
        add(exitBtn);
        // -------------------
        
        playerInfoPanel.refreshUI("");
    }

    // --- NEW: DEFINE SNAKE & LADDER DESTINATIONS ---
    private void initializePortals() {
        // --- LADDERS (Go UP) ---
        // Based on your tileMap array indexes:
        portalDestinations.put(6, 25);   // Row 1 (Index 5) -> Row 3
        portalDestinations.put(28, 50);  // Row 3 (Index 7) -> Row 5
        portalDestinations.put(39, 60);  // Row 4 (Index 1) -> Row 6
        portalDestinations.put(47, 75);  // Row 5 (Index 6) -> Row 8
        portalDestinations.put(64, 85);  // Row 7 (Index 3) -> Row 9
        portalDestinations.put(68, 89);  // Row 7 (Index 7) -> Row 9

        // --- SNAKES (Go DOWN) ---
        portalDestinations.put(97, 66);  // Row 10 (Index 3) -> Row 7
        portalDestinations.put(73, 52);  // Row 8 (Index 7) -> Row 6
        portalDestinations.put(44, 22);  // Row 5 (Index 3) -> Row 3
        portalDestinations.put(27, 5);   // Row 3 (Index 6) -> Row 1
    }

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
                int v1 = new Random().nextInt(6) + 1;
                int v2 = new Random().nextInt(6) + 1;
                dicePanel.setFinalFace(v1, v2);
                startPlayerMovement(v1 + v2);
            }
        });
        diceTimer.start();
    }

    private void startPlayerMovement(int diceValue) {
        int currentPos = positions.get(currentPlayerIndex);
        int targetPos = currentPos + diceValue;
        if (targetPos > 100) targetPos = 100;

        final int finalTarget = targetPos;
        Timer moveTimer = new Timer(300, null);
        
        moveTimer.addActionListener(e -> {
            int current = positions.get(currentPlayerIndex);
            
            if (current < finalTarget) {
                positions.set(currentPlayerIndex, current + 1);
                boardPanel.updatePositions(positions);
            } else {
                ((Timer)e.getSource()).stop();
                finishTurnLogic(finalTarget);
            }
        });
        moveTimer.start();
    }

    // --- UPDATED LOGIC FOR SNAKES AND LADDERS ---
    private void finishTurnLogic(int landedPos) {
        int tileType = boardPanel.getTileType(landedPos);
        String message = "";
        
        // 1. Check for Win First
        if (tileType == 3 || landedPos == 100) {
            JOptionPane.showMessageDialog(this, 
                "CONGRATULATIONS!\n" + playerNames.get(currentPlayerIndex) + " has won!", 
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
            gameWindow.returnToMenu();
            return;
        }

        // 2. Check for Snakes (5) or Ladders (4)
        if (portalDestinations.containsKey(landedPos)) {
            int newDest = portalDestinations.get(landedPos);
            
            if (newDest > landedPos) {
                // It's a Ladder
                JOptionPane.showMessageDialog(this, "You found a Ladder! Climbing up to Tile " + newDest + "!");
                message = " (CLIMBED LADDER)";
            } else {
                // It's a Snake
                JOptionPane.showMessageDialog(this, "Oh no! A Snake! Sliding down to Tile " + newDest + "...");
                message = " (SLID DOWN SNAKE)";
            }
            
            // Move player instantly to new spot
            landedPos = newDest;
            positions.set(currentPlayerIndex, landedPos);
            boardPanel.updatePositions(positions);
        }
        else if (tileType == 1) message = " (GOOD TILE)";
        else if (tileType == 2) message = " (BAD TILE)";

        // 3. End Turn
        currentPlayerIndex = (currentPlayerIndex + 1) % positions.size();
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
            for (int i = 0; i < playerNames.size(); i++) {
                String name = playerNames.get(i);
                int pos = positions.get(i);
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
                else if (i == (currentPlayerIndex - 1 + playerNames.size()) % playerNames.size()) {
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