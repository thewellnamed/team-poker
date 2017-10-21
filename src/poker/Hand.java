package poker;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import poker.enums.HandType;
import poker.enums.Rank;
import poker.enums.Suit;

/**
 * A played Hand
 * Provides hand evaluation and validation methods
 */
public class Hand implements Comparable<Hand> {
	private TreeSet<Card> cards;
	private long score;
	private HandType type;
	
	private static final int MAX_HAND_SIZE = 5;
	
	// scoring stratification
	public static final long SCORE_HIGH_CARD 		= 0x1L << 35;
	public static final long SCORE_PAIR				= 0x1L << 36;
	public static final long SCORE_TRIPS 			= 0x1L << 37;
	public static final long SCORE_STRAIGHT 		= 0x1L << 38;
	public static final long SCORE_FLUSH 			= 0x1L << 39;
	public static final long SCORE_FULL_HOUSE 		= 0x1L << 40;
	public static final long SCORE_QUADS 			= 0x1L << 41;
	public static final long SCORE_STRAIGHT_FLUSH 	= 0x1L << 42;
	
	private static final List<Long> scoreMap = Arrays.asList(0L, 
			SCORE_HIGH_CARD, SCORE_PAIR, SCORE_TRIPS, SCORE_QUADS);
	
	private static final List<HandType> typeMap = 
			Arrays.asList(HandType.INVALID, HandType.HIGH_CARD, HandType.PAIR, HandType.TRIPS, HandType.QUADS);

	/**
     * Construct from Set
     */
    public Hand(TreeSet<Card> startingCards) {    	
    	cards = startingCards;
    	validate();
    }
    
    /**
     * Construct from string
     * @param cardStr String
     * @return Hand
     */
    public Hand(String cardStr) {
    	this(Utils.getCardsFromString(cardStr));
    }

    /**
     * Hand Type
     */
    public HandType getType() {
    	return type;
    }
    
    /**
     * Get Validity
     */
    public boolean isValid() {
    	return type != HandType.INVALID;
    }
    
    /**
     * Get relative hand rank within type
     */
    public long getScore() {
    	return score;
    }
    
    /**
     * Get highest card in hand
     * @return Card
     */
    public Card getHighCard() {
    	return cards.first();
    }
    
    /**
     * Get string format
     */
    @Override
    public String toString() {
    	return Utils.getCardString(cards);
    }
    
    /**
     * Hand sorting by score
     */
	@Override
	public int compareTo(Hand o) {
		long ret = score - o.getScore();
		return (ret < 0) ? -1 : (ret == 0 ? 0 : 1);
	}
	
	/**
	 * Two hands are equal if they have the same score
	 */
	@Override
	public boolean equals(Object o) {
       if (o == this) {
    	   return true;
       }
       
       if (!(o instanceof Hand)) {
           return false;
       }
       
       Hand h = (Hand) o;
       if (h.getType() != type) {
    	   return false;
       }
       
       return h.getScore() == score;       
	}
        
    /**
     * Validation and scoring
     */
    private void validate() {
    	type = HandType.INVALID;
    	score = 0;
    	
    	int handSize = cards.size();
    	    	
    	// may only play 1 to 5 cards
    	if (handSize < 1 || handSize > MAX_HAND_SIZE) {
    		return;
    	}

    	// hands of less than 5 cards may only be X-of-a-kind
    	// X-of-a-kind score is the value of the highest suit
    	if (handSize < 5) {
    		validateOfAKind();
    	}
    	
    	// 5 card hand must be one of
    	//    - 4 of a kind with kicker
    	//    - straight
    	//    - flush
    	//    - full house
    	else {
        	long ranksAndSuits = 0L;
        	int r1 = 0, r2 = 0;
        	Rank r = null;
        	
        	for (Card c : cards) {
        		ranksAndSuits |= c.getScore();
        		
        		if (r == null) {
        			r = c.getRank();
        			r1++;
        		} else if (c.getRank() == r) {
        			++r1;
        		} else {
        			++r2;
        		}
        	}
        	
        	long ranks = ranksAndSuits & 0xFFFF0;
        	long suits = ranksAndSuits & 0xF;
        	
    		// full house or quads with kicker
    		if (Long.bitCount(ranks) == 2) {
    			validateFullHouseOrQuads(ranks, suits, r1, r2);
    		}
    		
    		else {
    			validateStraightsAndFlushes(ranks, suits);
    		}    		
    	}
    }
    
    /**
     * Set validity and score for high card, pair, trips, quads (no kicker)
     */
    private void validateOfAKind() {
    	Card first = cards.first();
    	Card last = cards.last();
    	
		if (first.getRank() == last.getRank()) {
	    	int sz = cards.size();
			type = typeMap.get(sz);
			
			if (sz < 4) {
				score = scoreMap.get(sz) | first.getScore();
			} else {
				score = scoreMap.get(sz) | (first.getRank().getScore() << 17);
			}
		}
    }
    
    /**
     * Set validity and score for full houses and quads with kicker
     */
    private void validateFullHouseOrQuads(long ranks, long suits, int r1, int r2) {
    	Card main, kicker;
    	
		if (r1 == 4 || r1 == 1) {
			type = HandType.QUADS_WITH_KICKER;
			
			if (r1 == 4) {
				main = cards.first();
				kicker = cards.last();
			} else {
				main = cards.last();
				kicker = cards.first();
			}
			
			score = SCORE_QUADS | (main.getRank().getScore() << 17) | kicker.getScore();					
		} else {
			type = HandType.FULL_HOUSE;			
			
			if (r1 == 3) {
				main = cards.first();
				kicker = cards.last();
			} else {
				main = cards.last();
				kicker = cards.first();
			}

			score = SCORE_FULL_HOUSE | (main.getRank().getScore() << 17) | kicker.getRank().getScore();
		}
    }
    
    /**
     * Validate for straight or flush or straight flush
     */
    private void validateStraightsAndFlushes(long ranks, long suits) {
		// straight
		if (Long.bitCount(ranks) == 5) {
			if ((cards.first().getRank().getScore() >> 4) == cards.last().getRank().getScore()) {
				type = HandType.STRAIGHT;
				score = SCORE_STRAIGHT | getHighCard().getScore();
			}
		}
		
		// flush or straight flush
		if (Long.bitCount(suits) == 1) {
			long flushValue = getHighCard().getScore();
			long baseScore;
			
			if (type == HandType.STRAIGHT) {
				type = HandType.STRAIGHT_FLUSH;
				baseScore = SCORE_STRAIGHT_FLUSH;
			} else {
				type = HandType.FLUSH;
				baseScore = SCORE_FLUSH;
			}
			
			score = baseScore | flushValue;
		}
    }
}