package game.engine;

import java.util.ArrayList;
import java.util.List;

/*
 * Game loop insert here
 */

public class Game {
	
	private Board board;
	private List<Player> players;
	private Dice dice; //Implemented a new dice logic
	private int numPlayers, currentPlayerIndex;
	private boolean gameRunning;
	private Player winner;

	public Game(int numPlayers){
		
		this.board = new Board();
		this.players = new ArrayList<>();
		this.dice = new Dice();
		this.currentPlayerIndex = 0; //always start at zero
		this.gameRunning = false;
		
		// __init__ players
		
		for (int i = 0; i < numPlayers; i++) {
			players.add(new Player());
		}
		
	}
	
	//Game loop starts
	public void startGame(){
		//TODO: Code Block here
	}
	
	public void gameLoop() {
		//TODO: Code Block here
	}
	
	public void processTurn(Player player) {
		/*
		 * Method with Player type player var params 
		 * TODO: Code Block here
		 */
		
		
		
		
	}
	
	public boolean isGameOver() {
		//TODO: Code Block here
		
		
		return false;
	}
	
	//TODO: Import other game methods here
	

}
