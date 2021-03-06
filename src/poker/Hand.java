package poker;

import java.util.Collection;
import java.util.TreeSet;

import poker.enums.HandType;
import poker.enums.Rank;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * A played Hand.
 * Provides hand evaluation and validation methods.
 */

public class Hand implements Comparable<Hand> {
	private TreeSet<Card> cards;
	private long score;
	private HandType type;
	
	/**
     * Construct from Set.
     */
    public Hand(TreeSet<Card> startingCards) {    	
    	cards = startingCards;
    	validate();
    }
    
    /**
     * Construct from Collection.
     */
    public Hand(Collection<Card> startingCards) {
    	this(new TreeSet<Card>(startingCards));
    }
    
    /**
     * Construct from string.
     * @param cardStr String
     * @return Hand
     */
    public Hand(String cardStr) {
    	this(Utils.getCardsFromString(cardStr));
    }

    /**
     * Hand Type.
     */
    public HandType getType() {
    	return type;
    }
    
    /**
     * Hand size.
     */
    public int getSize() {
    	return cards.size();
    }
    
    /**
     * Get Validity.
     */
    public boolean isValid() {
    	return type != HandType.INVALID;
    }
    
    /**
     * Get the relative hand rank within type.
     */
    public long getScore() {
    	return score;
    }
    
    /**
     * Get the cards in hand.
     * @return TreeSet<Card>
     */
    public TreeSet<Card> getCards() {
    	return cards;
    }
    
    /**
     * Get the highest card in hand.
     * @return Card
     */
    public Card getHighCard() {
    	return cards.first();
    }
    
    /**
     * Get string format.
     */
    @Override
    public String toString() {
    	return Utils.getCardString(cards);
    }
    
    /**
     * Hand sorting is done by score.
     */
	@Override
	public int compareTo(Hand o) {
		long ret = o.getScore() - score;
		return (ret < 0) ? -1 : (ret == 0 ? 0 : 1);
	}
	
	/**
	 * Two hands are equal if they have the same score.
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
     * Validation and scoring.
     */
    private void validate() {
    	type = HandType.INVALID;
    	score = 0;
    	
    	int handSize = cards.size();
    	    	
    	// A player can play between 1 to 5 card per turn.
    	if (handSize < 1 || handSize > Rules.MAX_HAND_SIZE) {
    		return;
    	}

    	// Hands that are less than 5 cards may only be X-of-a-kind.
    	// X-of-a-kind score is the value of the highest card in the hand
    	if (handSize < 5) {
    		validateOfAKind();
    	}
    	
    	// 5 card hand must be one of the following:
    	//    - 4 of a kind with kicker,
    	//    - straight,
    	//    - flush,
    	//    - full house.
    	else {
        	long ranksAndSuits = 0L;
        	int r1 = 0, r2 = 0;
        	Rank r = null;
        	
        	for (Card c : cards) {
        		ranksAndSuits |= c.getScore();
        		
        		if (r == null) {
        			r = c.getRank();
        			++r1;
        		} else if (c.getRank() == r) {
        			++r1;
        		} else {
        			++r2;
        		}
        	}
        	
        	long ranks = ranksAndSuits & 0xFFFF0;
        	long suits = ranksAndSuits & 0xF;
        	
    		// Full house or quads with kicker.
    		if (Long.bitCount(ranks) == 2) {
    			validateFullHouseOrQuads(ranks, suits, r1, r2);
    		}
    		
    		else {
    			validateStraightsAndFlushes(ranks, suits);
    		}    		
    	}
    }
    
    /**
     * Set the validity and score for the high card, pair, trips, quads (no kicker).
     */
    private void validateOfAKind() {
    	Card first = cards.first();
    	Card last = cards.last();
    	
		if (first.getRank() == last.getRank()) {
			int sz = cards.size();
	    	type = HandType.valueOf(sz);
			
			if (sz < 4) {
				score = type.getScore() | first.getScore();
			} else {
				score = type.getScore()  | (first.getRank().getScore() << 17);
			}
		}
    }
    
    /**
     * Set the validity and score for full houses and quads with kicker.
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
			
			score = type.getScore() | (main.getRank().getScore() << 17) | kicker.getScore();					
		} else {
			type = HandType.FULL_HOUSE;			
			
			if (r1 == 3) {
				main = cards.first();
				kicker = cards.last();
			} else {
				main = cards.last();
				kicker = cards.first();
			}

			score = type.getScore() | (main.getRank().getScore() << 17) | kicker.getRank().getScore();
		}
    }
    
    /**
     * Validate for straight or flush or straight flush
     */
    private void validateStraightsAndFlushes(long ranks, long suits) {
		// Straight.
		if (Long.bitCount(ranks) == 5) {
			if ((cards.first().getRank().getScore() >> 4) == cards.last().getRank().getScore()) {
				type = HandType.STRAIGHT;
				score = type.getScore() | getHighCard().getScore();
			}
		}
		
		// Flush or straight flush.
		if (Long.bitCount(suits) == 1) {
			long flushValue = getHighCard().getScore();
			
			if (type == HandType.STRAIGHT) {
				type = HandType.STRAIGHT_FLUSH;
			} else {
				type = HandType.FLUSH;
			}
			
			score = type.getScore() | flushValue;
		}
    }
}