package poker;

import java.util.Arrays;

import poker.player.Player;
import poker.player.bots.MatthewBot;

/**
 * Main game engine
 */
public class Launcher {
	public static void main(String[] args) {
		Player a = new Player(Player.BOT, Player.NORTH, "Bob", new MatthewBot());
		Player b = new Player(Player.BOT, Player.EAST, "Sally", new MatthewBot());
		
		Game game = new Game(Arrays.asList(a, b));
		game.run();
	}
}