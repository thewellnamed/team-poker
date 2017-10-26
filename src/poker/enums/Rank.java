package poker.enums;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * Card Rank (Two -> Ace)
 */

public enum Rank {
	// (Card label, Card Value).
	TWO		("2", 1, 1L << 4),
	THREE	("3", 2, 1L << 5),
	FOUR	("4", 3, 1L << 6),
	FIVE	("5", 4, 1L << 7),
	SIX		("6", 5, 1L << 8),
	SEVEN	("7", 6, 1L << 9),
	EIGHT	("8", 7, 1L << 10),
	NINE	("9", 8, 1L << 11),
	TEN		("T", 9, 1L << 12),
	JACK	("J", 10, 1L << 13),
	QUEEN	("Q", 11, 1L << 14),
	KING	("K", 12, 1L << 15),
	ACE		("A", 13, 1L << 16);
	
	// ------------
	
	private int rankId;
	private long scoreValue;
	private String label;
	
	/**
	 * Private constructor.
	 * @param lbl Used for string formatting
	 * @param val Relative value
	 */
	private Rank(String lbl, int id, long score) {
		label = lbl;
		rankId = id;
		scoreValue = score;
	}
	
	/**
	 * Get the ID of the rank.
	 */
	public int getId() {
		return rankId;
	}
	
	/**
	 * Get the score value of the rank.
	 * @return long
	 */
	public long getScore() {
		return scoreValue;
	}
	
	/**
	 * Get the string.
	 */
	public String toString() {
		return label;
	}
	
	// Helper methods for creating Ranks from the values or the string format.
	
	public static Rank ofValue(int id) {
		Rank r = valMap.get(id);
		
		if (r == null) {
			throw new InvalidParameterException("Requested unknown rank");
		}
		
		return r;
	}
	
	public static Rank ofValue(String lbl) {
		Rank r = lblMap.get(lbl.toUpperCase());
		
		if (r == null) {
			throw new InvalidParameterException("Requested unknown rank");
		}
		
		return r;
	}
	
	private static HashMap<String, Rank> lblMap = new HashMap<String, Rank>();
	private static HashMap<Integer, Rank> valMap = new HashMap<Integer, Rank>();
	static {
		for (Rank r : Rank.values()) {
			valMap.put(r.rankId, r);
			lblMap.put(r.label, r);
		}
	}
}