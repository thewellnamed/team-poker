package poker.player.bots;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import poker.Card;
import poker.Hand;
import poker.Logger;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * Matthew's bot AI.
 */

public class MatthewBot extends PlayerAIBase {
	
	private int cardHash;
	
	public MatthewBot() {
		super();		
		cardHash = 0;
	}
	
	public Hand getNextHand(TreeSet<Card> cards, Hand last, ArrayList<Hand> previous) {
		if (cards.hashCode() != cardHash) {
			cardHash = cards.hashCode();
			populateValidHands(cards);
		}
		
		if (last == null) {
			// Todo implement hand selection logic...
			// Currently playing lowest possible hand.
			for (int i = 1; i <= 5; i++) {
				TreeSet<Hand> hands = validHands.get(i);
				if (hands.size() > 0) {
					return hands.last();
				}
			}
		} else {
			TreeSet<Hand> hands = validHands.get(last.getSize());
			
			if (hands.isEmpty()) {
				return null;
			}
			
			TreeSet<Hand> valid = new TreeSet<Hand>(hands.stream().filter(h -> h.getScore() > last.getScore()).collect(Collectors.toList()));
			if (valid.size() > 0) {
				return valid.last();
			}
			
		}

		return null;
	}
	
	/**
	 * To string.
	 */
	@Override 
	public String toString() {
		return String.format("MatthewBot(%s)", playerName);
	}
}
