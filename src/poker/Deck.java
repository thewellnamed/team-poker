package poker;

import java.util.ArrayList;
import java.util.Random;
import poker.enums.*;

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
		for (int rank = 1; rank < 14; rank++) {
			for (int suit = 1; suit < 5; suit++) {
				deck.add(new Card(Rank.fromValue(rank), Suit.fromValue(suit)));
			}
		}

		top = 0;
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
			Card tmp = deck.get(pos1);
			deck.set(pos1, deck.get(pos2));
			deck.set(pos2, tmp);
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