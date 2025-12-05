/**
 * 
 */
package game.ui;

import java.awt.BorderLayout;

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

		this.setLayout(new BorderLayout());

		ImageIcon image = new ImageIcon(getClass().getResource("BoardPanel.jpg"));

		JLabel picture = new JLabel(image);
		

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setSize(2000, 1000);
		this.setResizable(true);


		this.add(picture, BorderLayout.CENTER);

		this.add(new ControlsPanel(), BorderLayout.WEST);

		//BoardPanel board = new BoardPanel();
		//this.add(board);

		this.pack();
		this.setVisible(true);
	}

}
