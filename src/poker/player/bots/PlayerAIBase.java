package poker.player.bots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import poker.Card;
import poker.Hand;
import poker.enums.Rank;
import poker.enums.Suit;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 */

public abstract class PlayerAIBase implements IPokerBot {
	
	protected String playerName;
	protected HashMap<Integer, TreeSet<Hand>> validHands;
	
	public PlayerAIBase() {
		// Initialize hand map.
		validHands = new HashMap<Integer, TreeSet<Hand>>();
		validHands.put(1, new TreeSet<Hand>());
		validHands.put(2, new TreeSet<Hand>());
		validHands.put(3, new TreeSet<Hand>());
		validHands.put(4, new TreeSet<Hand>());
		validHands.put(5, new TreeSet<Hand>()); // Grouping all 5 card hands.
	}
	
	/**
	 * Set the bot name.
	 * @param name
	 */
	public void setPlayerName(String name) {
		playerName = name;
	}
	
	/**
	 * Main API interface.
	 * @param cards Cards held by player
	 * @param previous Hands previously played in this game
	 * @return Hand to play
	 */
	public abstract Hand getNextHand(TreeSet<Card> cards, Hand last, ArrayList<Hand> previous);
	
	/**
	 * Used for testing.
	 */
	public HashMap<Integer, TreeSet<Hand>> getHands() {
		return validHands;
	}
	
	/**
	 * To string.
	 */
	@Override 
	public String toString() {
		return String.format("Bot(%s)", playerName);
	}
	
	/**
	 * Enumerate all valid hands
	 * Todo: does not actually enumerate all straights and flushes...
	 * @param cards Cards held by Player
	 * @return Map from HandType -> Available valid hands
	 */
	@SuppressWarnings("unchecked")
	protected void populateValidHands(TreeSet<Card> cards) {
		Rank lastRank = null;
		ArrayList<Card> ofAKind = new ArrayList<Card>();
		ArrayList<Card> straight = new ArrayList<Card>();
		HashMap<Suit, TreeSet<Card>> suits;
		
		// Initialize suit map.
		suits = new HashMap<Suit, TreeSet<Card>>();
		suits.put(Suit.CLUBS, new TreeSet<Card>());
		suits.put(Suit.DIAMONDS, new TreeSet<Card>());
		suits.put(Suit.HEARTS, new TreeSet<Card>());
		suits.put(Suit.SPADES, new TreeSet<Card>());
		
		// Clear existing hands.
		for (int t : validHands.keySet()) {
			TreeSet<Hand> hands = validHands.get(t);
			hands.clear();
		}
				
		Iterator<Card> iter = cards.iterator();
		while (iter.hasNext()) {
			Card c = iter.next();
			
			// High card
			validHands.get(1).add(new Hand(Arrays.asList(c)));
					
			// If rank is changing, update of-a-kind hands and check for straights
			if (lastRank == null || c.getRank() != lastRank) {
				if (lastRank != null && ((lastRank.getScore() >> 1) != c.getRank().getScore())) {
					straight.clear();
				} 
				
				straight.add(c);
				lastRank = c.getRank();
				
				// X of a kind.
				if (ofAKind.size() > 1) {
					validHands.get(ofAKind.size()).add(new Hand(ofAKind));
				}
				
				ofAKind.clear();
				
				// Straight or straight flush
				if (straight.size() == 5) {
					validHands.get(5).add(new Hand(straight));
					
					// Keep rolling list of last 5 consecutive ranks...
					straight.remove(0);
				}
			} 
			
			// Rolling X-of-a-kind
			ofAKind.add(c);
			if (ofAKind.size() > 1) {
				validHands.get(ofAKind.size()).add(new Hand(ofAKind));
			}
			
			// Remember the suits
			TreeSet<Card> ofSuit = suits.get(c.getSuit());
			ofSuit.add(c);
		}
		
		// Flushes
		for (Suit s : suits.keySet()) {
			TreeSet<Card> suitCards = suits.get(s);
			
			// Make a flush with the highest top card and then the lowest kickers.
			if (suitCards.size() > 5) {
				if (suitCards.size() == 5) {
					validHands.get(5).add(new Hand(suitCards));
				} else {
					TreeSet<Card> flush = new TreeSet<Card>();
					flush.add(suitCards.first());
					
					for (int i = 0; i < 4; i++) {
						Card next = suitCards.last();
						flush.add(next);
						suitCards.remove(next);
					}
					
					validHands.get(5).add(new Hand(flush));
				}
			}
		}
		
		// Full Houses
		TreeSet<Hand> trips = validHands.get(3);
		TreeSet<Hand> pairs = validHands.get(2);
		
		if (trips.size() > 0 && (trips.size() > 1 || pairs.size() > trips.size())) {
			Iterator<Hand> tripsIter = trips.iterator();
			while (tripsIter.hasNext()) {
				Hand t = tripsIter.next();
				Iterator<Hand> pairIter = pairs.iterator();
				while (pairIter.hasNext()) {
					Hand p = pairIter.next();
					
					if (t.getHighCard().getRank() != p.getHighCard().getRank()) {
						TreeSet<Card> fh = new TreeSet<Card>(t.getCards());
						fh.addAll(p.getCards());
						validHands.get(5).add(new Hand(fh));
					}
				}
			}
		}
		
		// Quads with Kicker
		TreeSet<Hand> quads = validHands.get(4);
		if (quads.size() > 0) {
			Iterator<Hand> quadsIter = quads.iterator();
			while (quadsIter.hasNext()) {
				Hand quadHand = quadsIter.next();

				TreeSet<Hand> highCardHands = validHands.get(1);
				if (highCardHands.size() > 0) {
					Iterator<Hand> highCardIter = validHands.get(1).iterator();
					while (highCardIter.hasNext()) {
						Hand h = highCardIter.next();
						if (h.getHighCard().getRank() != quadHand.getHighCard().getRank()) {
							TreeSet<Card> qk = new TreeSet<Card>(quadHand.getCards());
							qk.addAll(h.getCards());
							validHands.get(5).add(new Hand(qk));
						}
					}
				}
			}
		}
	}
}
