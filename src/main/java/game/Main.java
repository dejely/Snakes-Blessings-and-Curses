package game;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel; 

/**
 * This would be our Caller for
 * All the Subsystems we made per packages(file sub directories)
 * As mentioned in Design Patterns: Facade
 */

public class Main { 



	public static void main(String[] args) {
		JFrame window = new JFrame("Snake-Blessings and Curses");
		JPanel panel = new JPanel(new BorderLayout());
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(400, 400);
		window.setResizable(true);
		
        JLabel greeting = new JLabel("Hello, user!", JLabel.CENTER);
        JLabel message = new JLabel("Welcome to Snake-Blessings and Curses", JLabel.CENTER);
        
        panel.add(greeting, BorderLayout.NORTH);
        panel.add(message, BorderLayout.CENTER);
		
		window.add(panel);
		window.pack();
		window.setVisible(true);
		
		
	}
	
	

}
