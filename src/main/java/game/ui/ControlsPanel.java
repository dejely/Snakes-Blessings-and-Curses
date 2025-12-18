package game.ui;

import game.engine.Dice;
import game.engine.Game;
import game.engine.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ControlsPanel extends JPanel {

    private GameWindow gameWindow;
    private BoardPanel boardPanel;
    
    // UI Components
    private JPanel playerListPanel; 
    private JTextArea historyArea;
    private JLabel die1Label, die2Label; 
    private JButton rollButton;
    private Timer animationTimer; 

    public ControlsPanel(GameWindow window, BoardPanel board, int playerCount) {
        this.gameWindow = window;
        this.boardPanel = board;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // --- 1. TOP: PLAYER INFO PANEL ---
        playerListPanel = new JPanel();
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        JScrollPane playerScroll = new JScrollPane(playerListPanel);
        playerScroll.setPreferredSize(new Dimension(0, 200)); 
        playerScroll.setBorder(new TitledBorder("Player Status"));
        add(playerScroll, BorderLayout.NORTH);

        // --- 2. CENTER: HISTORY LOG ---
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setText("--- GAME START ---\n");

        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setBorder(new TitledBorder("Action History"));
        historyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(historyScroll, BorderLayout.CENTER);

        // --- 3. BOTTOM: DICE & CONTROLS ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBorder(new TitledBorder("Controls"));
        bottomPanel.setPreferredSize(new Dimension(0, 120)); 

        // Dice Container 
        JPanel diceContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        die1Label = createDieLabel();
        die2Label = createDieLabel();
        
        diceContainer.add(die1Label);
        diceContainer.add(die2Label);
        
        // Wrapper for Title + Dice
        JPanel diceWrapper = new JPanel(new BorderLayout());
        JLabel diceTitle = new JLabel("Last Roll", SwingConstants.CENTER);
        diceWrapper.add(diceTitle, BorderLayout.NORTH);
        diceWrapper.add(diceContainer, BorderLayout.CENTER);
        
        // Roll Button
        rollButton = new JButton("Roll");
        rollButton.setFont(new Font("Arial", Font.BOLD, 24));
        rollButton.addActionListener(e -> startTurnSequence());

        bottomPanel.add(diceWrapper, BorderLayout.WEST);
        bottomPanel.add(rollButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Initial Refresh
        refreshUI();
    }
    
    private JLabel createDieLabel() {
        // Use a large font to make the Unicode characters look like graphics
        // If the dice look like empty boxes, change "SansSerif" to "Segoe UI Symbol" or "Dialog"
        JLabel lbl = new JLabel("\u2680", SwingConstants.CENTER); 
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 60)); 
        lbl.setPreferredSize(new Dimension(70, 70));
        lbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        return lbl;
    }
    
    // Helper to convert number 1-6 to Unicode Pips
    private String getDicePips(int value) {
        switch (value) {
            case 1: return "\u2680"; 
            case 2: return "\u2681"; 
            case 3: return "\u2682"; 
            case 4: return "\u2683"; 
            case 5: return "\u2684"; 
            case 6: return "\u2685"; 
            default: return "?";
        }
    }

    private void startTurnSequence() {
        Game game = gameWindow.getGame();
        if (game == null || game.isGameOver()) return;
        
        Player current = game.getCurrentPlayer();
        
        // 1. Foretold Fate (Instant Input)
        if (current.hasForetoldFate) {
            String input = JOptionPane.showInputDialog(this, 
                current.getName() + ": Foretold Fate! Choose steps (1-10):");
            int forcedRoll = 1;
            try {
                forcedRoll = Integer.parseInt(input);
            } catch (Exception ex) { forcedRoll = 1; }
            
            finalizeTurn(forcedRoll);
            return;
        }

        // 2. Normal Roll (Animation)
        rollButton.setEnabled(false); 
        
        long startTime = System.currentTimeMillis();
        animationTimer = new Timer(50, e -> {
            // Scramble using Unicode Pips
            die1Label.setText(getDicePips(Dice.rollSingleDie()));
            die2Label.setText(getDicePips(Dice.rollSingleDie()));
            
            if (System.currentTimeMillis() - startTime > 500) {
                ((Timer)e.getSource()).stop();
                finalizeTurn(-1); 
            }
        });
        animationTimer.start();
    }

    private void finalizeTurn(int forcedRoll) {
        Game game = gameWindow.getGame();
        String turnLog = game.processTurn(forcedRoll);
        
        historyArea.append(turnLog + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
        
        refreshUI();

        if (game.isGameOver()) {
            rollButton.setEnabled(false);
            rollButton.setText("GAME OVER");
            JOptionPane.showMessageDialog(this, "Game Over! " + game.getWinner().getName() + " Wins!");
            gameWindow.returnToMenu();
        } else {
            rollButton.setEnabled(true);
        }
    }

    private void refreshUI() {
        Game game = gameWindow.getGame();
        if (game == null) return;

        // 1. Update Board
        List<Integer> positions = new ArrayList<>();
        for (Player p : game.getPlayers()) {
            positions.add(p.getPosition());
        }
        boardPanel.updatePositions(positions);

        // 2. Update Dice Pips
        die1Label.setText(getDicePips(game.getLastDie1()));
        die2Label.setText(getDicePips(game.getLastDie2()));

        // 3. Update Player List
        playerListPanel.removeAll();
        Player currentPlayer = game.getCurrentPlayer();

        for (Player p : game.getPlayers()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            if (p == currentPlayer) {
                card.setBackground(new Color(220, 255, 220)); 
                card.setBorder(new CompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 150, 0), 2),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
            } else {
                card.setBackground(Color.WHITE);
            }

            String statusText = "Position: " + p.getPosition();
            
            if (p.isShackled) statusText += " [SHACKLED]";
            if (p.skipNextTurn) statusText += " [SKIP]";
            if (p.hasForetoldFate) statusText += " [FORETOLD]";
            if (p.hasSwitcheroo) statusText += " [SWITCH]";
            if (p.danielBlessingTurns > 0) statusText += " [DANIEL]";
            if (p.jacobsLadderCharges > 0) statusText += " [JACOB]";

            JLabel nameLbl = new JLabel(p.getName(), SwingConstants.LEFT);
            nameLbl.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel statsLbl = new JLabel(statusText, SwingConstants.LEFT);
            statsLbl.setFont(new Font("Arial", Font.PLAIN, 12));
            if (statusText.contains("[")) statsLbl.setForeground(Color.RED);

            card.add(nameLbl, BorderLayout.NORTH);
            card.add(statsLbl, BorderLayout.SOUTH);
            
            playerListPanel.add(card);
            playerListPanel.add(Box.createRigidArea(new Dimension(0, 5))); 
        }
        
        playerListPanel.revalidate();
        playerListPanel.repaint();
    }
}