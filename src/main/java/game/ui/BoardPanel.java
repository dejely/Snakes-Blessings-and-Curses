/**
 * 
 */
package game.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * 
 */
public class BoardPanel extends JPanel{

	/**
	 * 
	 */
	public BoardPanel() {

		this.setLayout(new BorderLayout());

		this.add(new JLabel("Hello, user!", JLabel.CENTER), BorderLayout.NORTH);
		this.add(new JLabel("Welcome to Snake-Blessings and Curses", JLabel.CENTER), BorderLayout.CENTER);

	
	}

}
