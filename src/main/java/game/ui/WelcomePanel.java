package game.ui;

import java.awt.*;
import javax.swing.*;

public class WelcomePanel extends JPanel {

    private GameWindow mainFrame;

    public WelcomePanel(GameWindow frame) {
        this.mainFrame = frame;
        setLayout(new GridBagLayout());
        setBackground(new Color(230, 240, 255)); 

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0; c.gridy = 0;

        JLabel title = new JLabel("Snakes: Blessings & Curses");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        add(title, c);

        c.gridy++;
        JLabel label = new JLabel("How many players?");
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        add(label, c);

        c.gridy++;
        String[] options = {"2 Players", "3 Players", "4 Players"};
        JComboBox<String> playerSelect = new JComboBox<>(options);
        playerSelect.setFont(new Font("Arial", Font.PLAIN, 16));
        add(playerSelect, c);

        c.gridy++;
        JButton startBtn = new JButton("START GAME");
        startBtn.setFont(new Font("Arial", Font.BOLD, 20));
        startBtn.setBackground(new Color(50, 200, 50));
        startBtn.setForeground(Color.WHITE);
        
        startBtn.addActionListener(e -> {
            int count = playerSelect.getSelectedIndex() + 2;
            mainFrame.startGame(count);
        });
        
        add(startBtn, c);
    }
}