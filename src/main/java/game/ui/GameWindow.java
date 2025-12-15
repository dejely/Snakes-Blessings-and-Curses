package game.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class GameWindow extends JFrame {

    public GameWindow() {
        super("Snakes: Blessings and Curses");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Start by showing the Welcome Screen
        showWelcomeScreen();
        
        // Basic Window Settings
        setResizable(true);
        setSize(1100, 850); 
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    private void showWelcomeScreen() {
        // Clear old stuff (like the game board)
        getContentPane().removeAll();
        
        // Create fresh Welcome Panel
        WelcomePanel welcome = new WelcomePanel(this);
        add(welcome);
        
        // Refresh visuals
        revalidate();
        repaint();
    }
    
    // --- NEW: Method to go back to Main Menu ---
    public void returnToMenu() {
        showWelcomeScreen();
    }

    public void startGame(int numPlayers) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // 1. Create Board
        BoardPanel board = new BoardPanel();
        add(board, BorderLayout.CENTER);

        // 2. Create Controls
        // CHANGED: We now pass 'this' (GameWindow) so ControlsPanel can call returnToMenu()
        ControlsPanel controls = new ControlsPanel(this, board, numPlayers);
        controls.setPreferredSize(new Dimension(300, 800)); 
        add(controls, BorderLayout.WEST);

        revalidate();
        repaint();
    }
}