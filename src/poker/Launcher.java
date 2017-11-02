package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import poker.player.Player;
import poker.player.bots.MatthewBot;
import poker.player.bots.RandomBot;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * Main game engine
 */

public class Launcher {
	public static void main(String[] args) {
		
		HashMap<String, Integer> wins = new HashMap<String, Integer>();
		wins.put("Bob", 0);
		wins.put("Sally", 0);
		wins.put("Alice", 0);
		wins.put("Mallory", 0);
		
		Player a = new Player(Player.BOT, Player.NORTH, "Sally", new MatthewBot());
		Player b = new Player(Player.BOT, Player.EAST, "Bob", new RandomBot()); 
		
		for (int i = 0; i < 100000; i++) {
			Game game = new Game(Arrays.asList(a, b));
			ArrayList<Player> results = game.run();			
			wins.put(results.get(0).getName(), wins.get(results.get(0).getName()) + 1);
		}
		
		System.out.println("Alice wins: " + wins.get("Alice"));
		System.out.println("Bob wins: " + wins.get("Bob"));
		System.out.println("Mallory wins: " + wins.get("Mallory"));
		System.out.println("Sally wins: " + wins.get("Sally"));
	}
}