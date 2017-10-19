package poker.player;

import java.util.TreeSet;
import java.security.InvalidParameterException;
import poker.Card;
import poker.Hand;
import poker.player.bots.PlayerAIBase;

/**
 * Poker Player
 * Provides interface to AI Engine if type == BOT
 */
public class Player {
	private int type;
	private String name;
	private TreeSet<Card> cards;
	private PlayerAIBase ai;
	
	private static final int HUMAN = 1;
	private static final int BOT = 2;
	
	/**
	 * Constructor
	 */
	public Player(int playerType, String playerName, PlayerAIBase bot) {
		type = playerType;
		name = playerName;
		ai = bot;
		
		if (type == BOT && ai == null) {
			throw new InvalidParameterException("bot player requires AI engine");
		}
	}

	/**
	 * Get Player name
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Update cards held by Player
	 */
	public void setCards(TreeSet<Card> c) {
		cards = c;
	}
	
	/**
	 * Main game action 
	 * @param lastHand Last played hand in round
	 * @return Hand to play
	 */
	public Hand getNextHand(Hand lastHand) {
		if (type == BOT) {
			return ai.getNextHand(cards, lastHand);
		}
		
		// Player todo
		return null;
	}
}