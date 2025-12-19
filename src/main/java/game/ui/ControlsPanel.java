package game.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;
import game.engine.Dice;
import game.engine.Game;
import game.engine.Player;

public class ControlsPanel extends JPanel {
    private GameWindow gameWindow;
    private BoardPanel boardPanel;
    private JPanel playerListPanel; 
    private JTextArea historyArea;
    private JLabel die1Label, die2Label; 
    private JButton rollButton;
    private Timer animationTimer; 

    // Synchronized dice values
    private int officialDie1 = 1;
    private int officialDie2 = 1;

    // Theme Colors
    private final Color OBSI_BLACK = new Color(20, 20, 22);
    private final Color LAVA_ORANGE = new Color(230, 126, 34);
    private final Color FIRE_RED = new Color(192, 57, 43);
    private final Color TEXT_GOLD = new Color(241, 196, 15);

    public ControlsPanel(GameWindow window, BoardPanel board, int playerCount) {
        this.gameWindow = window;
        this.boardPanel = board;
        
        setOpaque(false);
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // 1. TOP: PLAYER STATUS
        playerListPanel = new JPanel();
        playerListPanel.setOpaque(false);
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        JScrollPane pScroll = new JScrollPane(playerListPanel);
        pScroll.setPreferredSize(new Dimension(0, 230)); 
        pScroll.setOpaque(false);
        pScroll.getViewport().setOpaque(false);
        pScroll.setBorder(createRelicBorder("ASCENDANTS"));
        add(pScroll, BorderLayout.NORTH);

        // 2. CENTER: HISTORY
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setBackground(new Color(30, 30, 35)); 
        historyArea.setForeground(Color.WHITE);
        historyArea.setFont(new Font("Dialog", Font.BOLD, 14));
        historyArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane hScroll = new JScrollPane(historyArea);
        hScroll.setOpaque(true);
        hScroll.getViewport().setOpaque(true);
        hScroll.setBorder(createRelicBorder("THE CHRONICLE"));
        add(hScroll, BorderLayout.CENTER);

        // 3. BOTTOM: DICE & BUTTON
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel diceContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        diceContainer.setOpaque(true); 
        diceContainer.setBackground(new Color(25, 25, 30)); 
        diceContainer.setBorder(new LineBorder(LAVA_ORANGE, 2));

        die1Label = createDieLabel();
        die2Label = createDieLabel();
        diceContainer.add(die1Label);
        diceContainer.add(die2Label);
        
        rollButton = new JButton("ROLL");
        rollButton.setFont(new Font("Serif", Font.BOLD, 26));
        rollButton.setBackground(FIRE_RED);
        rollButton.setForeground(Color.WHITE);
        rollButton.setAlignmentX(Component.CENTER_ALIGNMENT); 
        rollButton.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(LAVA_ORANGE, 2), new EmptyBorder(10, 60, 10, 60)));
        
        rollButton.addActionListener(e -> handleRollButtonClick());

        bottomPanel.add(diceContainer);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bottomPanel.add(rollButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        refreshUI();
    }

    private void handleRollButtonClick() {
        Game game = gameWindow.getGame();
        Player p = game.getCurrentPlayer();
        
        if (p.hasForetoldFate) {
            String input = JOptionPane.showInputDialog(this, 
                p.getName() + ", choose your steps (1-12):", "Foretold Fate", JOptionPane.PLAIN_MESSAGE);
            try {
                int steps = Integer.parseInt(input);
                if (steps >= 1 && steps <= 12) {
                    // Manually set visual dice for prophesied move
                    officialDie1 = steps / 2;
                    officialDie2 = steps - officialDie1;
                    die1Label.setText(getDicePips(officialDie1));
                    die2Label.setText(getDicePips(officialDie2));
                    finalizeTurn(steps); 
                    return;
                }
            } catch (Exception e) { }
        }
        startRollAnimation();
    }

    private void startRollAnimation() {
        rollButton.setEnabled(false);
        
        // PRE-DETERMINE THE MOVE HERE
        officialDie1 = Dice.rollSingleDie();
        officialDie2 = Dice.rollSingleDie();
        int totalMove = officialDie1 + officialDie2;

        long startTime = System.currentTimeMillis();
        animationTimer = new Timer(50, e -> {
            // Visual "blur" during animation
            die1Label.setText(getDicePips(Dice.rollSingleDie()));
            die2Label.setText(getDicePips(Dice.rollSingleDie()));
            
            if (System.currentTimeMillis() - startTime > 600) {
                ((Timer)e.getSource()).stop();
                
                // LOCK IN THE VISUALS TO MATCH THE MOVE
                die1Label.setText(getDicePips(officialDie1));
                die2Label.setText(getDicePips(officialDie2));
                
                finalizeTurn(totalMove);
            }
        });
        animationTimer.start();
    }

    private void finalizeTurn(int forcedMove) {
        Game game = gameWindow.getGame();
        Player current = game.getCurrentPlayer();
        int start = current.getPosition();
        
        // Pass the pre-calculated total to the engine
        String result = game.processTurn(forcedMove);
        int end = current.getPosition();

        historyArea.append("> " + result + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());

        final int[] visualPos = {start};
        Timer moveTimer = new Timer(100, e -> {
            if (visualPos[0] < end) visualPos[0]++;
            else if (visualPos[0] > end) visualPos[0]--;
            
            updateBoardPositions(game, current, visualPos[0]);

            if (visualPos[0] == end) {
                ((Timer)e.getSource()).stop();
                refreshUI();
                
                if (game.isGameOver()) {
                    showGameSummary(game);
                    gameWindow.returnToMenu();
                } else {
                    rollButton.setEnabled(true);
                }
            }
        });
        moveTimer.start();
    }

    private void updateBoardPositions(Game g, Player active, int vPos) {
        List<Integer> positions = new ArrayList<>();
        for (Player p : g.getPlayers()) {
            positions.add(p == active ? vPos : p.getPosition());
        }
        boardPanel.updatePositions(positions);
    }

    private void refreshUI() {
        Game game = gameWindow.getGame();
        if (game == null) return;
        playerListPanel.removeAll();
        Player current = game.getCurrentPlayer();
        Color[] pColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        List<Player> allPlayers = game.getPlayers();

        for (int i = 0; i < allPlayers.size(); i++) {
            Player p = allPlayers.get(i);
            Color playerColor = pColors[i % pColors.length];

            JPanel card = new JPanel(new BorderLayout());
            card.setOpaque(true);
            card.setBackground(p == current ? new Color(60, 45, 35) : new Color(40, 40, 45, 180));
            card.setBorder(new CompoundBorder(
                new LineBorder(p == current ? LAVA_ORANGE : Color.DARK_GRAY, 2), 
                new EmptyBorder(5, 10, 5, 10)
            ));

            JLabel name = new JLabel(p.getName().toUpperCase());
            name.setForeground(playerColor);
            name.setFont(new Font("Serif", Font.BOLD, 14));

            JLabel status = new JLabel("TILE: " + p.getPosition() + " " + p.getStatusDisplay());
            status.setFont(new Font("SansSerif", Font.PLAIN, 11));
            status.setForeground(Color.LIGHT_GRAY);

            card.add(name, BorderLayout.NORTH);
            card.add(status, BorderLayout.SOUTH);
            
            playerListPanel.add(card);
            playerListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        playerListPanel.revalidate(); 
        playerListPanel.repaint();
    }

    private void showGameSummary(Game game) {
        List<Player> standings = new ArrayList<>(game.getPlayers());
        standings.sort((p1, p2) -> Integer.compare(p2.getPosition(), p1.getPosition()));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(25, 25, 30)); 
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        try {
            URL imgUrl = getClass().getResource("/GameEnd.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image scaledImg = icon.getImage().getScaledInstance(420, 240, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainPanel.add(imageLabel);
                mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        } catch (Exception e) {
            System.err.println("Error loading victory image: " + e.getMessage());
        }

        StringBuilder summaryText = new StringBuilder("THE ASCENSION IS COMPLETE\n\n");
        for (int i = 0; i < standings.size(); i++) {
            Player p = standings.get(i);
            String rank = (i == 0) ? "🏆 VICTOR: " : (i + 1) + ". ";
            summaryText.append(rank).append(p.getName())
                       .append(" — Tile ").append(p.getPosition()).append("\n");
        }

        JTextArea area = new JTextArea(summaryText.toString());
        area.setFont(new Font("Serif", Font.BOLD, 18));
        area.setForeground(TEXT_GOLD);
        area.setBackground(new Color(35, 35, 40));
        area.setEditable(false);
        area.setMargin(new Insets(15, 15, 15, 15));
        area.setBorder(new LineBorder(LAVA_ORANGE, 2));
        
        mainPanel.add(area);
        JOptionPane.showMessageDialog(this, mainPanel, "The Chronicle Ends", JOptionPane.PLAIN_MESSAGE);
    }

    private JLabel createDieLabel() {
        JLabel lbl = new JLabel("\u2680", SwingConstants.CENTER); 
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 60)); 
        lbl.setPreferredSize(new Dimension(80, 80));
        lbl.setOpaque(true); lbl.setBackground(Color.WHITE); 
        lbl.setForeground(OBSI_BLACK);
        lbl.setBorder(new LineBorder(LAVA_ORANGE, 2));
        return lbl;
    }

    private String getDicePips(int v) {
        return switch (v) {
            case 1 -> "\u2680"; case 2 -> "\u2681"; case 3 -> "\u2682";
            case 4 -> "\u2683"; case 5 -> "\u2684"; case 6 -> "\u2685";
            default -> "?";
        };
    }

    private TitledBorder createRelicBorder(String t) {
        TitledBorder tb = BorderFactory.createTitledBorder(new LineBorder(LAVA_ORANGE, 1), t);
        tb.setTitleColor(TEXT_GOLD); tb.setTitleFont(new Font("Serif", Font.BOLD, 13));
        return tb;
    }
}