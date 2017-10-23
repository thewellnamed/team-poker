package poker.player;

import java.util.ArrayList;
import java.util.TreeSet;
import java.security.InvalidParameterException;
import poker.Card;
import poker.Hand;
import poker.player.bots.PlayerAIBase;

/**
 * Poker Player
 * Provides interface to AI Engine if type == BOT
 */
public class Player implements Comparable<Player> {
	private int type;
	private String name;
	private int position;
	private TreeSet<Card> cards;
	private PlayerAIBase ai;
	
	public static final int HUMAN = 1;
	public static final int BOT = 2;
	
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int SOUTH = 3;
	public static final int WEST = 4;
	
	/**
	 * Constructor
	 */
	public Player(int playerType, int pos, String playerName, PlayerAIBase bot) {
		type = playerType;
		name = playerName;
		position = pos;
		ai = bot;
		
		if (type == BOT) {
			if (ai == null) {
				throw new InvalidParameterException("bot player requires AI engine");	
			}
			
			bot.setPlayerName(playerName);
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
	 * Get Player position
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Player is a bot?
	 * @return boolean
	 */
	public boolean isBot() {
		return type == BOT;
	}
	
	/** 
	 * Get cards held by player
	 * @return TreeSet<Card>
	 */
	public TreeSet<Card> getCards() {
		return cards;
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
	public Hand getNextHand(Hand last, ArrayList<Hand> previous) {
		if (type == BOT) {
			return ai.getNextHand(cards, last, previous);
		}
		
		// Player todo
		return null;
	}

	/** 
	 * Player order (N->E->S->W)
	 */
	@Override
	public int compareTo(Player o) {
		// TODO Auto-generated method stub
		return position - o.position;
	}
	
	@Override
	public String toString() {
		return String.format("Player(%s, %s)", name, type == HUMAN ? "HUMAN" : "BOT");
	}
}