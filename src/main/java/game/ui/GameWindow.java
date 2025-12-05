/**
 * 
 */
package game.ui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * 
 */
public class GameWindow extends JFrame{

	/**
	 * 
	 */
	public GameWindow() {

		super("Snakes: Blessings and Curses");

		ImageIcon image = new ImageIcon("BoardPanel.jpg");

		JLabel picture = new JLabel(image);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(2000, 1000);
		this.setResizable(true);


		this.add(picture);

		//BoardPanel board = new BoardPanel();
		//this.add(board);

		//ControlsPanel control = new ControlsPanel();
		//this.add(control);

		this.pack();
		this.setVisible(true);
	}

}
