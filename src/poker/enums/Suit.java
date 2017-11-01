package poker.enums;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * The card suit.
 */

public enum Suit {
	// (Suit label, Suit Value)
	CLUBS		("c", 1, 1L),
	DIAMONDS	("d", 2, 1L << 1),
	HEARTS		("h", 3, 1L << 2),
	SPADES		("s", 4, 1L << 3);
	
	// --------------------------------------------------
	
	private String label;
	private int suitId;
	private long scoreValue;
	
	/** 
	 * Private constructor.
	 * @param lbl Used for string formatting
	 * @param val Relative value
	 */
	private Suit(String lbl, int id, long score) {
		suitId = id;
		label = lbl;
		scoreValue = score;
	}
	
	/**
	 * Get ID of the suit.
	 */
	public int getId() {
		return suitId;
	}
	
	/**
	 * Get score value of the suit.
	 * @return long
	 */
	public long getScore() {
		return scoreValue;
	}
	
	/**
	 * Get string label of the suit.
	 */
	public String toString() {
		return label;
	}
	
	// Helper methods for creating the suits.
	public static Suit ofValue(int id) {
		return valMap.get(id);
	}
	
	public static Suit ofValue(String lbl) {
		return lblMap.get(lbl.toLowerCase());
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
