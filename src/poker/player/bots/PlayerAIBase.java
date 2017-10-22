package poker.player.bots;

import java.util.ArrayList;
import java.util.TreeSet;
import poker.Card;
import poker.Hand;

public abstract class PlayerAIBase implements IPokerBot {
	
	/**
	 * Main API interface
	 * @param cards Cards held by player
	 * @param previous Hands previously played in this game
	 * @return Hand to play
	 */
	public abstract Hand getNextHand(TreeSet<Card> cards, Hand last, ArrayList<Hand> previous);
}
