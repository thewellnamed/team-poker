package poker.enums;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Card Suit
 */
public enum Suit {
	CLUBS		("c", 1, 0x1),
	DIAMONDS	("d", 2, 0x2),
	HEARTS		("h", 3, 0x4),
	SPADES		("s", 4, 0x8);
	
	// ------------
	
	private String label;
	private int suitId;
	private long scoreValue;
	
	/** 
	 * Private constructor
	 * @param lbl Used for string formatting
	 * @param val Relative value
	 */
	private Suit(String lbl, int id, long score) {
		suitId = id;
		label = lbl;
		scoreValue = score;
	}
	
	/**
	 * Get ID
	 */
	public int getId() {
		return suitId;
	}
	
	/**
	 * Get score value
	 * @return long
	 */
	public long getScore() {
		return scoreValue;
	}
	
	/**
	 * Get string label
	 */
	public String toString() {
		return label;
	}
	
	// Helper methods for creating Suits
	
	public static Suit ofValue(int id) {
		Suit s = valMap.get(id);
		
		if (s == null) {
			throw new InvalidParameterException("Requested unknown suit");
		}
		
		return s;
	}
	
	public static Suit ofValue(String lbl) {
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
			valMap.put(s.suitId, s);
			lblMap.put(s.label, s);
		}
	}
}
