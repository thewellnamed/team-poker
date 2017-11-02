package poker;

import poker.player.Player;

public class GameResults {
	private Player player;
	private int[] results;
	private int score;
	
	public GameResults(Player p) {
		player = p;
		results = new int[] { 0, 0, 0, 0 };
	}
	
	public void processGameResult(int place) {
		results[place]++;
		score += (4 - place);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int[] getResults() {
		return results;
	}
	
	public int getScore() {
		return score;
	}
}
