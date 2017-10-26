package poker;

import java.security.InvalidParameterException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import poker.enums.Rank;
import poker.enums.Suit;
import poker.player.Player;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * The gameplay.
 */

public class Game {
	private Deck deck;
	private ArrayList<Player> players;
	private ArrayList<Hand> playedHands;
	private ArrayList<Player> results;
	private int next;
	
	public static final int MAX_PLAYERS = 4;
	public static final int CARDS_PER_PLAYER = 13;
	
	public Game(List<Player> newPlayers) {		
		players = new ArrayList<Player>(newPlayers);
		results = new ArrayList<Player>();
		playedHands = new ArrayList<Hand>();		
		deck = new Deck();

		// The sort is based on the position of the players/bots.
		players.sort(null);
		deck.shuffle();
		
		// Deal the cards and the player/bot with the lowest card will go first. 
		long lowestCard = Rank.ACE.getScore() | Suit.SPADES.getScore();
				
		for (int p = 0; p < players.size(); p++) {
			TreeSet<Card> cards = new TreeSet<Card>();
			for (int j = 0; j < CARDS_PER_PLAYER; j++) {
				cards.add(deck.getNextCard());
			}
			
			if (cards.last().getScore() < lowestCard) {
				lowestCard = cards.last().getScore();
				next = p;
			}
			
			players.get(p).setCards(cards);
		}
	}
	
	/**
	 * Run the game until the results determined.
	 * @throws Exception 
	 * @returns Array of players, winner first, then second place, etc.
	 */
	public ArrayList<Player> run() throws IllegalStateException {		
		int numPlayers = players.size();
		int winningPlayer;
		int roundCount = 1;
			
		if (numPlayers < 2 || numPlayers > MAX_PLAYERS) {
			throw new InvalidParameterException("Max 4 players");
		}
		
		do {
			winningPlayer = round();
			if (winningPlayer >= 0) {
				Player wp = players.get(winningPlayer);
				Logger.info("found winner %d: %s", results.size() + 1, wp);
				results.add(players.get(winningPlayer));
				players.remove(winningPlayer);
				
				if (players.size() == 1) {
					// Game Over!
					results.add(players.get(0));
				}
				
				setStartingPlayer();
			}
			roundCount++;
		} while (results.size() < numPlayers);
		
		Logger.info("ran for %d rounds", roundCount);
		return results;
	}
	
	/**
	 * Executes one round of the game.
	 * @return player index of player if player runs out of cards
	 *         else return -1
	 * @throws Exception 
	 */
	private int round() throws IllegalStateException {
		int passed = 0;
		int lastSuccess = next;
		Hand last = null;
		
		do {
			Player nextPlayer = players.get(next);
			Hand play = nextPlayer.getNextHand(last, playedHands);
			
			Logger.info("%s played hand %s", nextPlayer, play == null ? "pass" : play);
			
			if (play != null) {	
				// You have to play the correct cards in your hand. 
				// This will check to see if you are playing the correct cards that are in your hand.
				if (!nextPlayer.getCards().containsAll(play.getCards())) {
					Logger.info("--- invalid hand.");
					if (nextPlayer.isBot()) {
						throw new IllegalStateException("bot played invalid hand");
					}
					play = null;
				}
				
				// You must play a valid collection of cards.
				// This will check if the play is valid.
				if (!play.isValid()) {
					Logger.info("--- invalid hand.");
					if (nextPlayer.isBot()) {
						throw new IllegalStateException("bot played invalid hand");
					}
					play = null;
				}
				
				// The next hand must be the previous hand.
				// This will check if the score of the new hand is greater than the previous hand. If not, return invalid.
				else if (last != null) {
					if (play.getSize() != last.getSize() || 
					    play.getScore() < last.getScore()) {
						
						Logger.info("--- Does not beat last hand");
						if (nextPlayer.isBot()) {
							throw new IllegalStateException("bot played invalid hand");
						}
						play = null;
					}
				}
			}
					
			if (play == null) {
				passed++;
			} else {
				lastSuccess = next;
				last = play;
				passed = 0;
				playedHands.add(play);
				nextPlayer.getCards().removeAll(play.getCards());
				
				// The player has won, short circuit....
				if (nextPlayer.getCards().size() == 0) {
					return next;
				}
			}
			
			next = (next + 1) % players.size();
		} while (passed < players.size() - 1);
				
		Logger.info("Round winner = %s", players.get(lastSuccess));
		
		// The winner of the current round will go first the next game.
		next = lastSuccess;
		return -1;
	}
	
	/**
	 * Re-initialize next turn to the player with lowest card.
	 */
	private void setStartingPlayer() {
		long lowestCard = Rank.ACE.getScore() | Suit.SPADES.getScore();
		
		// Deal the cards and find the player who would be first to play based on the lowest card.
		for (int p = 0; p < players.size(); p++) {
			
			Card lowest = players.get(p).getCards().last();
			if (lowest.getScore() < lowestCard) {
				next = p;
				lowestCard = lowest.getScore();
			}
		}
	}
}
