package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Deck
 */
public class Deck {
	private ArrayList<Card> deck = new ArrayList<Card>();
	private int top;

	private static final int SHUFFLE_COUNT = 1000;

	/**
	 * Construct
	 */
	public Deck() {
		deck.addAll(Card.getAllCards());
		top = 0;
	}
	
	/**
	 * Get size
	 * @return int
	 */
	public int size() {
		return deck.size();
	}
	
	/** 
	 * Get all cards
	 * @return ArrayList<Card>
	 */
	public ArrayList<Card> getCards() {
		return deck;
	}

	/**
	 * Shuffle deck
	 */
	public void shuffle() {
		top = 0;
		Random rand = new Random(System.currentTimeMillis()); 
		for (int i = 0; i < SHUFFLE_COUNT; i++) {
			// select random values to swap
			int pos1 = rand.nextInt(52);
			int pos2 = rand.nextInt(52);

			// swap
			Collections.swap(deck, pos1, pos2);
		}
	}

	/**
	 * Is deck empty?
	 * 
	 * @return boolean
	 */
	public boolean empty() {
		return top == deck.size();
	}

	/**
	 * Deal next card from top of deck
	 * 
	 * @return Card
	 */
	public Card getNextCard() {
		if (empty()) {
			return null;
		}

		return deck.get(top++);
	}
}