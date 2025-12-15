package game;

import game.ui.CharacterSelectionMenu;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(CharacterSelectionMenu::new);
    }
}
