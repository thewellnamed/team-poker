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

public abstract class PlayerAIBase implements IPokerBot {
	
	protected String playerName;
	protected HashMap<Integer, TreeSet<Hand>> validHands;
	
	public PlayerAIBase() {
		// initialize hand map
		validHands = new HashMap<Integer, TreeSet<Hand>>();
		validHands.put(1, new TreeSet<Hand>());
		validHands.put(2, new TreeSet<Hand>());
		validHands.put(3, new TreeSet<Hand>());
		validHands.put(4, new TreeSet<Hand>());
		validHands.put(5, new TreeSet<Hand>()); // grouping all 5 card hands
	}
	
	/**
	 * Set bot name
	 * @param name
	 */
	public void setPlayerName(String name) {
		playerName = name;
	}
	
	/**
	 * Main API interface
	 * @param cards Cards held by player
	 * @param previous Hands previously played in this game
	 * @return Hand to play
	 */
	public abstract Hand getNextHand(TreeSet<Card> cards, Hand last, ArrayList<Hand> previous);
	
	/**
	 * Used for testing
	 */
	public HashMap<Integer, TreeSet<Hand>> getHands() {
		return validHands;
	}
	
	/**
	 * To string
	 */
	@Override 
	public String toString() {
		return String.format("Bot(%s)", playerName);
	}
	
	/**
	 * Find the best valid hand for each type from available cards
	 * @param cards Cards held by Player
	 * @return Map from HandType -> Available valid hands
	 */
	@SuppressWarnings("unchecked")
	protected void populateValidHands(TreeSet<Card> cards) {
		Rank lastRank = null;
		ArrayList<Card> ofAKind = new ArrayList<Card>();
		ArrayList<Card> straight = new ArrayList<Card>();
		HashMap<Suit, TreeSet<Card>> suits;
		
		// initialize suit map
		suits = new HashMap<Suit, TreeSet<Card>>();
		suits.put(Suit.CLUBS, new TreeSet<Card>());
		suits.put(Suit.DIAMONDS, new TreeSet<Card>());
		suits.put(Suit.HEARTS, new TreeSet<Card>());
		suits.put(Suit.SPADES, new TreeSet<Card>());
		
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
				if (highCardIter.hasNext()) {
					h = highCardIter.next();
				} else {
					h = null;
				}
			}
			
			if (h != null) {
				qk.addAll(h.getCards());
				validHands.get(5).add(new Hand(qk));
			}
		}
	}
}
