package game.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * ControlsPanel: exposes simple API so other classes don't need to reference nested types.
 */
public class ControlsPanel extends JPanel {

    // Internal panels (nested to keep UI code together)
    private final DicePanel dicePanel;
    private final PlayerInfoPanel playerInfoPanel;

    public ControlsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dicePanel = new DicePanel();
        playerInfoPanel = new PlayerInfoPanel();

        add(dicePanel);
        add(Box.createVerticalStrut(20));
        add(playerInfoPanel);
        add(Box.createVerticalGlue());
    }

    // ----- Dice API -----
    public void addRollListener(ActionListener l) {
        dicePanel.addRollListener(l);
    }

    public void setDiceFaces(int d1, int d2) {
        dicePanel.setDiceFaces(d1, d2);
    }

    // ----- Players API -----
    /**
     * Register a Runnable callback to be invoked whenever the player list changes.
     */
    public void setOnPlayersChanged(Runnable callback) {
        playerInfoPanel.setOnChange(callback);
    }

    /**
     * Return a copy of the current player names.
     */
    public List<String> getPlayerNames() {
        return new ArrayList<>(playerInfoPanel.getNames());
    }

    /**
     * Add a player via the controls panel UI (keeps UI and model in sync).
     */
    public void addPlayerViaUI(String name) {
        playerInfoPanel.addPlayer(name);
    }

    /**
     * Remove last player via UI.
     */
    public void removeLastPlayerViaUI() {
        playerInfoPanel.removeLastPlayer();
    }

    // ================== DICE PANEL ==================
    private static class DicePanel extends JPanel {
        private static final String[] DIE_FACE = {
                "\u2680", "\u2681", "\u2682", "\u2683", "\u2684", "\u2685"
        };

        private final JLabel die1 = new JLabel("", SwingConstants.CENTER);
        private final JLabel die2 = new JLabel("", SwingConstants.CENTER);
        private final JButton roll = new JButton("Roll");

        public DicePanel() {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createTitledBorder("Dice"));
            setBackground(Color.WHITE);

            GridBagConstraints c = new GridBagConstraints();
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 48);

            die1.setFont(font);
            die2.setFont(font);
            die1.setPreferredSize(new Dimension(70, 70));
            die2.setPreferredSize(new Dimension(70, 70));

            c.gridx = 0; c.gridy = 0;
            add(die1, c);

            c.gridx = 1;
            add(die2, c);

            c.gridx = 0; c.gridy = 1; c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            add(roll, c);

            setDiceFaces(1, 1);
        }

        public void addRollListener(ActionListener l) {
            roll.addActionListener(l);
        }

        public void setDiceFaces(int d1, int d2) {
            d1 = Math.max(1, Math.min(6, d1));
            d2 = Math.max(1, Math.min(6, d2));
            die1.setText(DIE_FACE[d1 - 1]);
            die2.setText(DIE_FACE[d2 - 1]);
        }
    }

    // ================== PLAYER INFO PANEL (internal) ==================
    // Kept private so external classes don't need to reference the nested type.
    private static class PlayerInfoPanel extends JPanel {

        private final java.util.List<String> names = new ArrayList<>();
        private final JPanel listPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        private Runnable onChange;

        public PlayerInfoPanel() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createTitledBorder("Players"));

            JButton addBtn = new JButton("Add Player");
            JButton removeBtn = new JButton("Remove Player");

            JPanel btns = new JPanel(new GridLayout(1, 2, 5, 5));
            btns.add(addBtn);
            btns.add(removeBtn);

            add(new JScrollPane(listPanel), BorderLayout.CENTER);
            add(btns, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> {
                String name = JOptionPane.showInputDialog(this, "Player name:");
                if (name != null && !name.trim().isEmpty()) {
                    addPlayer(name.trim());
                }
            });

            removeBtn.addActionListener(e -> removeLastPlayer());
        }

        public void setOnChange(Runnable r) { this.onChange = r; }

        public java.util.List<String> getNames() { return names; }

        public void addPlayer(String name) {
            names.add(name);
            JLabel lbl = new JLabel(name);
            listPanel.add(lbl);
            revalidate(); repaint();
            if (onChange != null) onChange.run();
        }

        public void removeLastPlayer() {
            if (names.isEmpty()) return;
            names.remove(names.size() - 1);
            int lastIdx = listPanel.getComponentCount() - 1;
            if (lastIdx >= 0) listPanel.remove(lastIdx);
            revalidate(); repaint();
            if (onChange != null) onChange.run();
        }
    }
}
