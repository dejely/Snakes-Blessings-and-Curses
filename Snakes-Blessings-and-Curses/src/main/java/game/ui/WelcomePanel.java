package game.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class WelcomePanel extends JPanel {

    private GameWindow mainFrame;
    private Image backgroundImage;

    public WelcomePanel(GameWindow frame) {
        this.mainFrame = frame;
        
        try {
            URL imgUrl = getClass().getResource("/background.jpg");
            if (imgUrl != null) {
                backgroundImage = new ImageIcon(imgUrl).getImage();
            }
        } catch (Exception e) {
            System.err.println("Error loading background: " + e.getMessage());
        }

        setLayout(new GridBagLayout());

        // FIX: Create a panel that handles transparency correctly
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        card.setOpaque(false); // Prevents the "white box" artifacting
        card.setBackground(new Color(255, 255, 255, 210)); 
        card.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 10, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;

        JLabel title = new JLabel("Snakes", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        card.add(title, c);

        c.gridy++;
        JLabel subtitle = new JLabel("Blessings & Curses", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.ITALIC, 25));
        subtitle.setForeground(new Color(52, 73, 94));
        subtitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        card.add(subtitle, c);

        c.gridy++;
        String[] options = {"2 Players", "3 Players", "4 Players"};
        JComboBox<String> playerSelect = new JComboBox<>(options);
        playerSelect.setFont(new Font("SansSerif", Font.ITALIC, 20));

        playerSelect.setPreferredSize(new Dimension(250, 45));
        playerSelect.addActionListener(e -> repaint());

        card.add(playerSelect, c);

        c.gridy++;
        c.insets = new Insets(30, 0, 0, 0);
        JButton startBtn = createStyledButton("START GAME");
        startBtn.addActionListener(e -> {
            int count = playerSelect.getSelectedIndex() + 2;
            mainFrame.startGame(count);
        });
        
        card.add(startBtn, c);
        add(card);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(46, 204, 113));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(39, 174, 96)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(46, 204, 113)); }
        });

        return btn;
    }
}