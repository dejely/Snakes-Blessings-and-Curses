package game.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class ControlsPanel extends JPanel {

    // Helper method to let GameWindow access the Dice panel if needed
    public final DicePanel dicePanel;
    public final PlayerInfoPanel playerInfoPanel;

    public ControlsPanel() {
        // Layout: Stack items vertically
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize sub-components
        dicePanel = new DicePanel();
        playerInfoPanel = new PlayerInfoPanel();

        // Add them to this panel
        this.add(dicePanel);
        this.add(Box.createVerticalStrut(20)); // Spacing
        this.add(playerInfoPanel);
        this.add(Box.createVerticalGlue());    // Push everything up
    }

    // ================== DICE PANEL ==================
    public static class DicePanel extends JPanel {
        private static final String[] DIE_FACES = {
            "\u2680", "\u2681", "\u2682", "\u2683", "\u2684", "\u2685"
        };

        private final Random rnd = new Random();
        private final JLabel die1 = new JLabel("", SwingConstants.CENTER);
        private final JLabel die2 = new JLabel("", SwingConstants.CENTER);

        public DicePanel() {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createTitledBorder("Dice"));
            setBackground(new Color(245, 245, 245));

            GridBagConstraints c = new GridBagConstraints();
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 48);
            
            die1.setFont(font);
            die2.setFont(font);
            die1.setPreferredSize(new Dimension(70, 70));
            die2.setPreferredSize(new Dimension(70, 70));

            // Dice Setup
            c.gridx = 0; c.gridy = 0; c.insets = new Insets(5, 5, 5, 5);
            add(die1, c);

            c.gridx = 1;
            add(die2, c);

            // Roll Button
            JButton roll = new JButton("Roll");
            roll.addActionListener(e -> rollDice());

            c.gridx = 0; c.gridy = 1; 
            c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            add(roll, c);

            rollDice(); // Initial roll
        }

        private void rollDice() {
            int v1 = rnd.nextInt(6);
            int v2 = rnd.nextInt(6);
            die1.setText(DIE_FACES[v1]);
            die2.setText(DIE_FACES[v2]);
        }
    }

    // ================== PLAYER INFO PANEL ==================
    // CHANGED: Made 'static' to fix instantiation errors
    public static class PlayerInfoPanel extends JPanel {

        private final ArrayList<JLabel> labels = new ArrayList<>();
        private final ArrayList<String> names = new ArrayList<>();

        private final JPanel listPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // 0 rows means dynamic
        private final JButton addBtn = new JButton("Add Player");
        private final JButton removeBtn = new JButton("Remove Player");

        private int currentPlayer = 0;

        public PlayerInfoPanel() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createTitledBorder("Players"));

            // Default starting players
            names.add("Player 1");
            names.add("Player 2");
            refreshPlayerList();

            // Button panel
            JPanel btnPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            btnPanel.add(addBtn);
            btnPanel.add(removeBtn);

            add(listPanel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> addPlayer());
            removeBtn.addActionListener(e -> removePlayer());

            updateButtonStates();
        }

        private void refreshPlayerList() {
            listPanel.removeAll();
            labels.clear();

            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                JLabel lbl = new JLabel(name + " — Pos: 1");
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createEtchedBorder());
                labels.add(lbl);
                listPanel.add(lbl);
            }

            highlightCurrentPlayer();
            revalidate();
            repaint();
        }

        private void highlightCurrentPlayer() {
            for (int i = 0; i < labels.size(); i++) {
                JLabel lbl = labels.get(i);
                if (i == currentPlayer) {
                    lbl.setBackground(new Color(255, 230, 150)); // Highlight color
                } else {
                    lbl.setBackground(new Color(240, 240, 240));
                }
            }
        }

        private void addPlayer() {
            if (names.size() >= 4) return;
            names.add("Player " + (names.size() + 1));
            refreshPlayerList();
            updateButtonStates();
        }

        private void removePlayer() {
            if (names.size() <= 2) return;
            names.remove(names.size() - 1);
            if (currentPlayer >= names.size()) currentPlayer = 0;
            refreshPlayerList();
            updateButtonStates();
        }

        private void updateButtonStates() {
            addBtn.setEnabled(names.size() < 4);
            removeBtn.setEnabled(names.size() > 2);
        }
    }
}