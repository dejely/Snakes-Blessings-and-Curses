package game;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.engine.Board;
import game.ui.*;

/**
 * This would be our Caller for
 * All the Subsystems we made per packages(file sub directories)
 * As mentioned in Design Patterns: Facade
 */

public class Main { 



	public static void main(String[] args) {

		new GameWindow();
		
	}
	
	

}
