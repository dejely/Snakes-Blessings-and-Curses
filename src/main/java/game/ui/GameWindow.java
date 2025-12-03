/**
 * 
 */
package game.ui;

import javax.swing.JFrame;
/**
 * 
 */
public class GameWindow extends JFrame{

	/**
	 * 
	 */
	public GameWindow() {

		super("Snakes: Blessings and Curses");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 500);
		this.setResizable(true);

		BoardPanel board = new BoardPanel();
		this.add(board);

		this.pack();
		this.setVisible(true);
	}

}
