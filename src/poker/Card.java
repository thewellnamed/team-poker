package poker;

import java.util.Objects;

import poker.enums.*;

public class Card implements Comparable<Card> {
		
	private Rank rank;
	private Suit suit;
	   
	/**
     * Construct from rank and suit
     */
	public Card(Rank r, Suit s) {
		rank = r;
		suit = s;
	}
	
	/**
	 * Construct from string
	 */
	public Card(String c) {
		this(Rank.fromFormatString(c.substring(0, 1)), Suit.fromFormatString(c.substring(1, 2)));
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
	public int getValue() {
		return (rank.getValue() * 4) + suit.getValue();
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
		if (rank.getValue() == o.getRank().getValue()) {
			return o.getSuit().getValue() - suit.getValue();
		}
			
		return o.getRank().getValue() - rank.getValue();
	}
}