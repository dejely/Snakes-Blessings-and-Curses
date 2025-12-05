package game.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GameWindow extends JFrame {

    public GameWindow() {
        super("Snakes: Blessings and Curses");
        
        // 1. Setup Main Layout
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2. Setup the Board (Center)
        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setBackground(Color.DARK_GRAY);
        
        // Try to load image safely
        URL imgUrl = getClass().getResource("BoardPanel1.jpg");
        if (imgUrl != null) {
            ImageIcon image = new ImageIcon(imgUrl);
            Image scaledImage = image.getImage().getScaledInstance(800, 800, Image.SCALE_SMOOTH);
            JLabel picture = new JLabel(new ImageIcon(scaledImage));
            boardContainer.add(picture, BorderLayout.CENTER);
        } else {
            // Fallback if image is missing
            JLabel errorLabel = new JLabel("Board Image Not Found", SwingConstants.CENTER);
            errorLabel.setForeground(Color.WHITE);
            errorLabel.setPreferredSize(new Dimension(800, 800));
            boardContainer.add(errorLabel, BorderLayout.CENTER);
        }

        this.add(boardContainer, BorderLayout.CENTER);

        // 3. Setup the Controls (Left Side / West)
        ControlsPanel controls = new ControlsPanel();
        // Give the side panel a preferred width
        controls.setPreferredSize(new Dimension(250, 800)); 
        this.add(controls, BorderLayout.WEST);

        // 4. Finalize Window
        this.setResizable(true);
        this.pack();
        this.setLocationRelativeTo(null); // Center on screen
        this.setVisible(true);
    }
}
