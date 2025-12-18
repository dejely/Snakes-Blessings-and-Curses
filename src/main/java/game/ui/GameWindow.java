package game.ui;

import game.engine.Game;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class GameWindow extends JFrame {

    private Game game; // <--- ADD THIS FIELD

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

    // <--- ADD THIS GETTER METHOD
    public Game getGame() {
        return this.game;
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
    
    public void returnToMenu() {
        showWelcomeScreen();
    }

    public void startGame(int numPlayers) {
        // <--- INITIALIZE THE GAME HERE
        this.game = new Game(numPlayers);

        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // 1. Create Board
        BoardPanel board = new BoardPanel();
        add(board, BorderLayout.CENTER);

        // 2. Create Controls
        ControlsPanel controls = new ControlsPanel(this, board, numPlayers);
        controls.setPreferredSize(new Dimension(300, 800)); 
        add(controls, BorderLayout.EAST);

        // 3. Refresh
        revalidate();
        repaint();
    }
}