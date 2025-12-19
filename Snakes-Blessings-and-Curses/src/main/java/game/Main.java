package game;

import game.ui.GameWindow;

/**
 * This would be our Caller for
 * All the Subsystems we made per packages(file sub directories)
 * As mentioned in Design Patterns: Facade
 */
public class Main { 

	static int size = 0;
	public static void main(String[] args) {
		new GameWindow();
		
	}
	
	

}
