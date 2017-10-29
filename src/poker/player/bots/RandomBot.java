package poker.player.bots;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Collectors;

import poker.Card;
import poker.Hand;
import poker.Logger;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * 
 * Random Bot.
 * Basic algorithm: 
 *    Play a random hand if no previous.
 *    Play a random better hand if previous.
 */

public class RandomBot extends PlayerAIBase {
	
	private int cardHash;
	private Random rand;
	
	public RandomBot() {
		super();		
		cardHash = 0;
		rand = new Random(System.currentTimeMillis());
	}
	
	public Hand getNextHand(TreeSet<Card> cards, Hand last, ArrayList<Hand> previous) {
		if (cards.hashCode() != cardHash) {
			cardHash = cards.hashCode();
			populateValidHands(cards);
		}
		
		if (last == null) {
			Hand h = null;
			do {
				int type = rand.nextInt(5) + 1;
				TreeSet<Hand> hands = validHands.get(type);
				
				if (hands.size() > 0) {
					if (hands.size() == 1) {
						h = hands.first();
					} else {
						Iterator<Hand> iter = hands.iterator();
						int i = 0, numToSkip = rand.nextInt(hands.size() - 1);
						while (i++ < numToSkip) iter.next();
						h = iter.next();
					}
				}
			} while (h == null);
			
			return h;			
		} else {
			TreeSet<Hand> hands = validHands.get(last.getSize());
			
			if (hands.isEmpty()) {
				return null;
			}
			
			List<Hand> valid = hands.stream().filter(h -> h.getScore() > last.getScore()).collect(Collectors.toList());
			if (valid.size() > 0) {
				if (valid.size() == 1) {
					return valid.get(0);
				} else {
					return valid.get(rand.nextInt(valid.size()));
				}
			}
		}
		
		return null;
	}
	
	/**
	 * To string.
	 */
	@Override 
	public String toString() {
		return String.format("RandomBot(%s)", playerName);
	}
}