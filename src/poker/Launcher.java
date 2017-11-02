package poker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import poker.io.UserInput;
import poker.player.Player;
import poker.player.bots.PlayerAIBase;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * Main game engine
 */

public class Launcher {
	public static void main(String[] args) {
		// prompt user for game setup
		int playerCount = UserInput.getPlayerCount(Rules.MAX_PLAYERS);
		int roundCount = 0;
		
		ArrayList<Player> players = new ArrayList<Player>();
		HashMap<Player, GameResults> results = new HashMap<Player, GameResults>();
		boolean haveHumanPlayers = false;
		
		for (int i = 1; i <= playerCount; i++) {
			int type = UserInput.getNextPlayerType(i);
			String name = UserInput.getNextPlayerName(i);
			PlayerAIBase ai = null;
			
			if (type == Player.BOT) {
				ai = UserInput.getNextPlayerAI(i);
			} else {
				haveHumanPlayers = true;
			}
			
			Player p = new Player(type, i, name, ai);
			players.add(p);
			results.put(p, new GameResults(p));
		}
		
		if (!haveHumanPlayers) {
			roundCount = UserInput.getRoundCount();
		}
		
		// run...
		UserInput.postMessage("Starting %d game%s with %d players!\n", 
			roundCount, (roundCount > 1 ? "s" :""), playerCount);
		
		for (int round = 0; round < roundCount; round++) {
			Game game = new Game(players);
			ArrayList<Player> ret = game.run();
			
			for (int i = 0; i < ret.size(); i++) {
				Player next = ret.get(i);
				results.get(next).processGameResult(i);
			}
		}
		
		// tabulate and post results
		List<GameResults> playerResults = results.values().stream()
				.sorted((a, b) -> b.getScore() - a.getScore())
				.collect(Collectors.toList());
		
		UserInput.postResults(playerResults);
	}
}