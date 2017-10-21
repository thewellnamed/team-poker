package poker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import poker.enums.*;

public class Card implements Comparable<Card> {
		
	private Rank rank;
	private Suit suit;
	private long score;
	   
	/**
     * Construct from rank and suit
     */
	public Card(Rank r, Suit s) {
		rank = r;
		suit = s;
		score = rank.getScore() | suit.getScore();
	}
	
	/**
	 * Construct from string
	 */
	public Card(String c) {
		this(Rank.ofValue(c.substring(0, 1)), Suit.ofValue(c.substring(1, 2)));
	}
	 
	/**
	 * Get suit
	 */
	public Suit getSuit() {
		return suit;
	}
	   
	/**
	 * Get rank
	 */
	public Rank getRank() {
		return rank;
	}
	   
	/**
	 * Get relative card value
	 */
	public long getScore() {
		return score;
	}
	   
	/**
	 * Get string
	 */
	public String toString() {
		return rank.toString() + suit.toString();
	}
	   
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
   
		if (!(o instanceof Card)) {
			return false;
		}
   
		Card c = (Card) o;
		return c.getSuit() == suit && c.getRank() == rank;
	}
	   
	/**
	 * Override hashCode in order to enforce 
	 * card uniqueness in Set<Card>, i.e. only one 5c
	 */
	@Override
	public int hashCode() {
		return Objects.hash(suit, rank);
	}
	
	/**
	 * For TreeSet ordering (high card first)
	 */
	@Override
	public int compareTo(Card o) {
		if (rank.getScore() == o.getRank().getScore()) {
			return (int)(o.getSuit().getScore() - suit.getScore());
		}
			
		return (int)(o.getRank().getScore() - rank.getScore());
	}
	
	/**
	 * Factory methods
	 * Dictionary of cards to avoid overhead
	 */
	
	public static Collection<Card> getAllCards() {
		return cards.values();
	}
	
	public static Card ofValue(String cardStr) {
		Card c = cards.get(cardStr);
		
		if (c == null) {
			return new Card(cardStr);
		}
		
		return c;
	}
	
	private static HashMap<String, Card> cards = new HashMap<String, Card>();
	static {
		for (int rank = 1; rank <= Rank.values().length; rank++) {
			for (int suit = 1; suit <= Suit.values().length; suit++) {
				Card c = new Card(Rank.ofValue(rank), Suit.ofValue(suit));
				cards.put(c.toString(), c);
			}
		}
	}
}