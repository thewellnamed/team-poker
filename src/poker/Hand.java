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
	private int score;
	private HandType type;
	
	private static final int MAX_HAND_SIZE = 5;
	
	// scoring stratification
	public static final int HIGH_CARD_BASE_SCORE = 100;
	public static final int PAIR_BASE_SCORE = 200;
	public static final int TRIPS_BASE_SCORE = 300;
	public static final int STRAIGHT_BASE_SCORE = 400;
	public static final int FLUSH_BASE_SCORE = 500;
	public static final int FULL_HOUSE_BASE_SCORE = 600;
	public static final int QUADS_BASE_SCORE = 1000;
	public static final int STRAIGHT_FLUSH_BASE_SCORE = 2000;
	
	public static final int QUADS_RANK_MULTIPLIER = 56; // 13 * 4 + 4. Allows for kicker values
												  		// used for quads and quads with kicker
	
	private static final List<Integer> scoreMap = 
			Arrays.asList(0, HIGH_CARD_BASE_SCORE, PAIR_BASE_SCORE, TRIPS_BASE_SCORE, QUADS_BASE_SCORE);
	
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
    public int getScore() {
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
		return score - o.getScore();
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
    	
    	// may only play 1 to 5 cards
    	if (cards.size() < 1 || cards.size() > MAX_HAND_SIZE) {
    		return;
    	}
    	
    	// hands of less than 5 cards may only be X-of-a-kind
    	// X-of-a-kind score is the value of the highest card
    	else if (cards.size() < 5) {
    		validateOfAKind();
    	}
    	
    	// 5 card hand must be one of
    	//    - 4 of a kind with kicker
    	//    - straight
    	//    - flush
    	//    - full house
    	else {
    		Map<Rank, List<Card>> ranks = cards.stream().collect(Collectors.groupingBy(Card::getRank));
    		List<Suit> suits = cards.stream().map(c -> c.getSuit()).distinct().collect(Collectors.toList());
    		
    		// full house or quads with kicker
    		if (ranks.size() == 2) {
    			validateFullHouseOrQuads(ranks, suits);
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
    	int sz = cards.size();
		if (cards.first().getRank() == cards.last().getRank()) {
			type = typeMap.get(sz);
			
			if (sz < 3) {
				score = scoreMap.get(sz) + cards.first().getValue();
			} else if (sz == 3) {
				score = scoreMap.get(sz) + cards.first().getRank().getValue();
			} else {
				score = scoreMap.get(cards.size()) + (cards.first().getRank().getValue() * QUADS_RANK_MULTIPLIER);
			}
		}
    }
    
    /**
     * Set validity and score for full houses and quads with kicker
     */
    private void validateFullHouseOrQuads(Map<Rank, List<Card>> ranks, List<Suit> suits) {
		Iterator<Rank> iter = ranks.keySet().iterator();
		Rank r2, r1 = iter.next();
		List<Card> cardsForRank = ranks.get(r1);
		int rankSize = cardsForRank.size();
		
		if (rankSize == 4 || rankSize == 1) {
			type = HandType.QUADS_WITH_KICKER;
			
			if (rankSize == 1) {
				r2 = r1;
				r1 = iter.next();
			} else {
				r2 = iter.next();
			}
			
			Card kicker = ranks.get(r2).get(0);
			score = QUADS_BASE_SCORE + (r1.getValue() * QUADS_RANK_MULTIPLIER) + kicker.getValue();					
		} else {
			type = HandType.FULL_HOUSE;			
			
			if (rankSize == 2) {
				r2 = r1; r1 = iter.next(); 
			} else {
				r2 = iter.next();
			}
			
			score = FULL_HOUSE_BASE_SCORE + (r1.getValue() * 13) + r2.getValue();
		}
    }
    
    /**
     * Validate for straight or flush or straight flush
     */
    private void validateStraightsAndFlushes(Map<Rank, List<Card>> ranks, List<Suit> suits) {
		// straight
		if (ranks.size() == 5) {
			TreeSet<Rank> r = new TreeSet<Rank>(ranks.keySet());
			if (r.last().getValue() - r.first().getValue() == 4) {
				type = HandType.STRAIGHT;
				score = STRAIGHT_BASE_SCORE + getHighCard().getValue();
			}
		}
		
		// flush or straight flush
		if (suits.size() == 1) {
			int flushValue = getHighCard().getValue();
			int baseScore;
			
			if (type == HandType.STRAIGHT) {
				type = HandType.STRAIGHT_FLUSH;
				baseScore = STRAIGHT_FLUSH_BASE_SCORE;
			} else {
				type = HandType.FLUSH;
				baseScore = FLUSH_BASE_SCORE;
			}
			
			score = baseScore + flushValue;
		}
    }
}