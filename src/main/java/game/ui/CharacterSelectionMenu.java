package game.ui;

import game.character.CharacterType;
import game.config.BoardConfig;
import game.engine.Player;
import game.engine.Board;
import game.engine.BoardFactory;
import game.engine.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionMenu extends JFrame {

    private final JComboBox<CharacterType> player1Box;
    private final JComboBox<CharacterType> player2Box;

    public CharacterSelectionMenu() {
        setTitle("Select Characters");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        CharacterType[] types = CharacterType.values();

        player1Box = new JComboBox<>(types);
        player2Box = new JComboBox<>(types);

        JPanel center = new JPanel(new GridLayout(2, 2, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        center.add(new JLabel("Player 1:"));
        center.add(player1Box);
        center.add(new JLabel("Player 2:"));
        center.add(player2Box);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());

        add(center, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startGame() {
        CharacterType p1Type = (CharacterType) player1Box.getSelectedItem();
        CharacterType p2Type = (CharacterType) player2Box.getSelectedItem();

        Player p1 = new Player("Alice", p1Type.create());
        Player p2 = new Player("Bob", p2Type.create());

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        
        Board board = BoardFactory.createRandomBoard(
        	    12345L, 
        	    BoardConfig.defaultConfig()
        	);

        Game game = new Game(players, board);

        GameWindow window = new GameWindow(game);
        game.addListener(window);
        game.startTurn();

        dispose(); // close menu
    }
}
