package game.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame {

<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
=======
>>>>>>> Stashed changes
    private Game game;
    private Image backgroundImage;

>>>>>>> Stashed changes
    public GameWindow() {
        super("Snakes: Blessings and Curses");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Maven-specific background loading
        loadBackground();
        
        // Set a custom content pane to draw the background across the whole window
        setContentPane(new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Scales the image to fill the entire window
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        });

        // Start by showing the Welcome Screen
        showWelcomeScreen();
        
        // Basic Window Settings
        setResizable(false);
        setSize(1100, 850); 
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
=======
>>>>>>> Stashed changes
    private void loadBackground() {
        try {
            // Looks into src/main/resources inside the project jar
            URL imgUrl = getClass().getResource("/background.jpg");
            if (imgUrl != null) {
                backgroundImage = new ImageIcon(imgUrl).getImage();
            }
        } catch (Exception e) {
            System.err.println("Error loading window background: " + e.getMessage());
        }
    }

    public Game getGame() {
        return this.game;
    }

>>>>>>> Stashed changes
    private void showWelcomeScreen() {
        getContentPane().removeAll();
        
        // Create fresh Welcome Panel
        // Ensure WelcomePanel is non-opaque to see the GameWindow background through it
        WelcomePanel welcome = new WelcomePanel(this);
        welcome.setOpaque(false); 
        add(welcome);
        
        revalidate();
        repaint();
    }
    
    // --- NEW: Method to go back to Main Menu ---
    public void returnToMenu() {
        showWelcomeScreen();
    }

    public void startGame(int numPlayers) {
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
=======
>>>>>>> Stashed changes
        this.game = new Game(numPlayers);

>>>>>>> Stashed changes
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // 1. Create Board
        BoardPanel board = new BoardPanel();
        // Make sure board and controls don't completely hide the background if desired
        board.setOpaque(false); 
        add(board, BorderLayout.CENTER);

        // 2. Create Controls
        // CHANGED: We now pass 'this' (GameWindow) so ControlsPanel can call returnToMenu()
        ControlsPanel controls = new ControlsPanel(this, board, numPlayers);
        controls.setOpaque(false);
        controls.setPreferredSize(new Dimension(300, 800)); 
        add(controls, BorderLayout.WEST);

        revalidate();
        repaint();
    }
}