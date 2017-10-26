package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * Deck class.
 */

public class Deck {
	private ArrayList<Card> deck = new ArrayList<Card>();
	private int top;

	private static final int SHUFFLE_COUNT = 1000;

	/**
	 * Construct the deck.
	 */
	public Deck() {
		deck.addAll(Card.getAllCards());
		top = 0;
	}
	
	/**
	 * Get the size.
	 * @return int
	 */
	public int size() {
		return deck.size();
	}
	
	/** 
	 * Get all the cards.
	 * @return ArrayList<Card>
	 */
	public ArrayList<Card> getCards() {
		return deck;
	}

	/**
	 * Shuffle the deck.
	 */
	public void shuffle() {
		top = 0;
		Random rand = new Random(System.currentTimeMillis()); 
		for (int i = 0; i < SHUFFLE_COUNT; i++) {
			// Select random values in the deck to shuffle.
			int pos1 = rand.nextInt(52);
			int pos2 = rand.nextInt(52);

			// Shuffle/Swap the deck.
			Collections.swap(deck, pos1, pos2);
		}
	}

	/**
	 * Is the deck empty?
	 * This will check if the deck is empty.
	 * @return boolean
	 */
	public boolean empty() {
		return top == deck.size();
	}

	/**
	 * Deal the next card from the top of the deck.
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