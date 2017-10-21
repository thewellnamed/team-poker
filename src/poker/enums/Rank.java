package poker.enums;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Card Rank (Two -> Ace)
 */
public enum Rank {
	// label, value
	TWO		("2", 1, 0x10),
	THREE	("3", 2, 0x20),
	FOUR	("4", 3, 0x40),
	FIVE	("5", 4, 0x80),
	SIX		("6", 5, 0x100),
	SEVEN	("7", 6, 0x200),
	EIGHT	("8", 7, 0x400),
	NINE	("9", 8, 0x800),
	TEN		("T", 9, 0x1000),
	JACK	("J", 10, 0x2000),
	QUEEN	("Q", 11, 0x4000),
	KING	("K", 12, 0x8000),
	ACE		("A", 13, 0x10000);
	
	// ------------
	
	private int rankId;
	private long scoreValue;
	private String label;
	
	/**
	 * Private constructor
	 * @param lbl Used for string formatting
	 * @param val Relative value
	 */
	private Rank(String lbl, int id, long score) {
		label = lbl;
		rankId = id;
		scoreValue = score;
	}
	
	/**
	 * Get ID
	 */
	public int getId() {
		return rankId;
	}
	
	/**
	 * Get score value
	 * @return long
	 */
	public long getScore() {
		return scoreValue;
	}
	
	/**
	 * Get string
	 */
	public String toString() {
		return label;
	}
	
	// Helper methods for creating Ranks from value or string format
	
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