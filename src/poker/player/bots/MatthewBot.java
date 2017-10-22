package poker.player.bots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import poker.Card;
import poker.Hand;
import poker.enums.HandType;
import poker.enums.Rank;
import poker.enums.Suit;

/**
 * Matthew's AI
 * Basic algorithm: 
 *    
 *    Find the hand which maximizes the odds that every other player must pass
 *    Take into account hands played previously for card removal effects
 *    
 *    1. Find the best hand for each type that we can play
 *    2. Find the hand which is the closest to being the best possible hand playable by any player
 *       given the cards that have already been played
 */
public class MatthewBot extends PlayerAIBase {
	
	private TreeSet<Card> cards;
	private HashMap<Integer, TreeSet<Hand>> validHands;
	private HashMap<Suit, TreeSet<Card>> suits;
	
	public MatthewBot() {
		// initialize hand map
		validHands = new HashMap<Integer, TreeSet<Hand>>();
		validHands.put(1, new TreeSet<Hand>());
		validHands.put(2, new TreeSet<Hand>());
		validHands.put(3, new TreeSet<Hand>());
		validHands.put(4, new TreeSet<Hand>());
		validHands.put(5, new TreeSet<Hand>()); // grouping all 5 card hands
		
		// initialize suit map
		suits = new HashMap<Suit, TreeSet<Card>>();
		suits.put(Suit.CLUBS, new TreeSet<Card>());
		suits.put(Suit.DIAMONDS, new TreeSet<Card>());
		suits.put(Suit.HEARTS, new TreeSet<Card>());
		suits.put(Suit.SPADES, new TreeSet<Card>());
		
		cards = null;
	}
	
	public Hand getNextHand(TreeSet<Card> newCards, Hand last, ArrayList<Hand> previous) {
		if (cards == null || cards.size() != newCards.size()) {
			cards = newCards;
			populateValidHands();
		}
		
		if (last == null) {
			return validHands.get(1).first();
		} else {
			return validHands.get(last.getSize()).first();
		}
	}
	
	/**
	 * Used for testing
	 */
	public HashMap<Integer, TreeSet<Hand>> getHands() {
		return validHands;
	}
	
	/**
	 * Find the best valid hand for each type from available cards
	 * @param cards Cards held by Player
	 * @return Map from HandType -> Available valid hands
	 */
	@SuppressWarnings("unchecked")
	private void populateValidHands() {
		Rank lastRank = null;
		ArrayList<Card> ofAKind = new ArrayList<Card>();
		ArrayList<Card> straight = new ArrayList<Card>();
		
		// clear existing hands
		for (int t : validHands.keySet()) {
			TreeSet<Hand> hands = validHands.get(t);
			hands.clear();
		}
				
		Iterator<Card> iter = cards.iterator();
		while (iter.hasNext()) {
			Card c = iter.next();
			
			
			// If rank changing, update of-a-kind hands and check for straights
			if (lastRank == null || c.getRank() != lastRank) {
				if (lastRank != null && ((lastRank.getScore() >> 1) != c.getRank().getScore())) {
					straight.clear();
				} 
				
				straight.add(c);
				lastRank = c.getRank();
				
				// high card
				validHands.get(1).add(new Hand(Arrays.asList(c)));
				
				// X of a kind
				if (ofAKind.size() > 1) {
					validHands.get(ofAKind.size()).add(new Hand(ofAKind));
				}
				
				ofAKind.clear();
				
				// straight or straight flush
				if (straight.size() == 5) {
					validHands.get(5).add(new Hand(straight));
					
					// keep rolling list of last 5 consecutive ranks...
					straight.remove(0);
				}
			} 
			
			// Rolling X-of-a-kind
			ofAKind.add(c);
			if (ofAKind.size() > 1) {
				validHands.get(ofAKind.size()).add(new Hand(ofAKind));
			}
			
			// Remember suits
			TreeSet<Card> ofSuit = suits.get(c.getSuit());
			ofSuit.add(c);
		}
		
		// Flushes
		for (Suit s : suits.keySet()) {
			TreeSet<Card> suitCards = suits.get(s);
			
			// make a flush with the highest top card and then the lowest kickers
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
		
		// Full House
		TreeSet<Hand> trips = validHands.get(3);
		TreeSet<Hand> pairs = validHands.get(2);
		
		if (trips.size() > 0 && pairs.size() > trips.size()) {
			TreeSet<Card> fh = (TreeSet<Card>) trips.first().getCards().clone();
			
			// all trips will also be in pairs
			// skip over them...
			Iterator<Hand> pairIter = pairs.iterator();
			for (int i = 0; i < trips.size(); i++) pairIter.next();
			fh.addAll(pairIter.next().getCards());
			validHands.get(5).add(new Hand(fh));
		}
		
		// Quads with Kicker
		TreeSet<Hand> quads = validHands.get(4);
		if (quads.size() > 0) {
			TreeSet<Card> qk = (TreeSet<Card>) quads.first().getCards().clone();
			
			// quad aces would also be the high card...
			Iterator<Hand> highCardIter = validHands.get(1).iterator();
			Hand h = highCardIter.next();
			
			if (h.getHighCard().getRank() == qk.first().getRank()) {
				h = highCardIter.next();
			}
			
			qk.addAll(h.getCards());
			validHands.get(5).add(new Hand(qk));
		}
	}
}
