package poker.enums;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Card Rank (Two -> Ace)
 */
public enum Rank {
	// label, value
	TWO		("2", 1),
	THREE	("3", 2),
	FOUR	("4", 3),
	FIVE	("5", 4),
	SIX		("6", 5),
	SEVEN	("7", 6),
	EIGHT	("8", 7),
	NINE	("9", 8),
	TEN		("T", 9),
	JACK	("J", 10),
	QUEEN	("Q", 11),
	KING	("K", 12),
	ACE		("A", 13);
	
	// ------------
	
	private int value;
	private String label;
	
	/**
	 * Private constructor
	 * @param lbl Used for string formatting
	 * @param val Relative value
	 */
	private Rank(String lbl, int val) {
		label = lbl;
		value = val;
	}
	
	/**
	 * Get value
	 * @return int
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Get string
	 */
	public String toString() {
		return label;
	}
	
	// Helper methods for creating Ranks from value or string format
	
	public static Rank fromValue(int val) {
		Rank r = valMap.get(val);
		
		if (r == null) {
			throw new InvalidParameterException("Requested unknown rank");
		}
		
		return r;
	}
	
	public static Rank fromFormatString(String lbl) {
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
			valMap.put(r.value, r);
			lblMap.put(r.label, r);
		}
	}
}