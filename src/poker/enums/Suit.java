package poker.enums;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Card Suit
 */
public enum Suit {
	CLUBS		("c", 1),
	DIAMONDS	("d", 2),
	HEARTS		("h", 3),
	SPADES		("s", 4);
	
	// ------------
	
	private String label;
	private int value;
	
	/** 
	 * Private constructor
	 * @param lbl Used for string formatting
	 * @param val Relative value
	 */
	private Suit(String lbl, int val) {
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
	 * Get string label
	 */
	public String toString() {
		return label;
	}
	
	// Helper methods for creating Suits
	
	public static Suit fromValue(int val) {
		Suit s = valMap.get(val);
		
		if (s == null) {
			throw new InvalidParameterException("Requested unknown suit");
		}
		
		return s;
	}
	
	public static Suit fromFormatString(String lbl) {
		Suit s = lblMap.get(lbl.toLowerCase());
		
		if (s == null) {
			throw new InvalidParameterException("Requested unknown suit");
		}
		
		return s;
	}
	
	private static HashMap<String, Suit> lblMap = new HashMap<String, Suit>();
	private static HashMap<Integer, Suit> valMap = new HashMap<Integer, Suit>();
	static {
		for (Suit s : Suit.values()) {
			valMap.put(s.value, s);
			lblMap.put(s.label, s);
		}
	}
}
