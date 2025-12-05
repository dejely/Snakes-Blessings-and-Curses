/**
 * 
 */
package game.ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * 
 */
public class ControlsPanel extends JPanel{

	/**
	 * 
	 */
	public ControlsPanel() {

		/* 
		JLabel wrapper = new JLabel();

		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout(4, 1, 10, 10));

		menu.add(new JButton("Hello")); //add actionlistener

		JPanel leftWrapper = new JPanel(new GridBagLayout());
        leftWrapper.add(wrapper);
        leftWrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0)); 

        this.add(leftWrapper, BorderLayout.WEST);

		this.setVisible(true);
		*/

		setLayout(new GridLayout(4, 1, 10, 10));

        add(new JButton("Hello"));
        add(new JButton("Start"));
        add(new JButton("Reset"));
        JButton _Exit = new JButton("Exit");

		add(_Exit);



		
	}

}
