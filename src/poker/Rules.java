package poker;

import poker.player.Player;

public class Rules {
	// basic parameters
	public static final int MAX_PLAYERS = 4;
	public static final int CARDS_PER_PLAYER = 13;
	public static final int MAX_HAND_SIZE = 5;
	
	// Hand validity results
	public static final int HAND_VALID = 0;
	public static final int HAND_INVALID = -1;
	public static final int HAND_INVALID_PLAYER_CARDS = -1;
	public static final int HAND_INVALID_LOW_SCORE = -2;
	
	public static int checkHand(Player player, Hand hand, Hand last) {
		// You must play a valid collection of cards.
		if (!hand.isValid()) {
			return HAND_INVALID;
		}		
		
		// You have to play the correct cards in your hand. 
		if (!player.getCards().containsAll(hand.getCards())) {
			return HAND_INVALID_PLAYER_CARDS;
		}
				
		// The next hand must beat the previous hand.
		if (last != null) {
			if (hand.getSize() != last.getSize() || 
			    hand.getScore() < last.getScore()) {
				return HAND_INVALID_LOW_SCORE;
			}
		}
		
		return HAND_VALID;
	}
}
