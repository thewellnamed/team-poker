package poker.enums;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Type of poker hand
 */
public enum HandType {
						// number of cards, base score
	INVALID				(0, 0L),
	HIGH_CARD			(1, 1L << 34),
	PAIR				(2, 1L << 35),
	TRIPS				(3, 1L << 36),
	STRAIGHT			(5, 1L << 37),
	FLUSH				(5, 1L << 38),
	FULL_HOUSE			(5, 1L << 39),
	QUADS				(4, 1L << 40),
	QUADS_WITH_KICKER   (5, 1L << 40), // same as quads 
	STRAIGHT_FLUSH		(5, 1L << 41);	
	
	private int numCards;
	private long score;
	
	private HandType(int cards, long s) {
		numCards = cards;
		score = s;
	}
	
	public long getScore() {
		return score;
	}
	
	public static HandType valueOf(int numCards) {
		HandType type = cardMap.get(numCards);
		
		if (type == null) {
			throw new InvalidParameterException("Unable to find HandType");
		}
		
		return type;
	}
	
	private static HashMap<Integer, HandType> cardMap = new HashMap<Integer, HandType>();
	static {
		cardMap.put(0, HandType.INVALID);
		cardMap.put(1, HandType.HIGH_CARD);
		cardMap.put(2, HandType.PAIR);
		cardMap.put(3, HandType.TRIPS);
		cardMap.put(4, HandType.QUADS);
	}
}
