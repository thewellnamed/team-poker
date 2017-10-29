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
	
	/**
	 * Initialize game
	 * @param newPlayers Players
	 */
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
			for (int j = 0; j < Rules.CARDS_PER_PLAYER; j++) {
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
	 * Run until complete
	 */
	public ArrayList<Player> run() throws IllegalStateException {
		return run(-1);
	}
	
	/**
	 * Run for maxRounds rounds
	 * @param maxRounds Stop execution after a set number of rounds
	 * @throws Exception 
	 * @returns Array of players, winner first, then second place, etc.
	 */
	public ArrayList<Player> run(int maxRounds) throws IllegalStateException {		
		int numPlayers = players.size();
		int roundCount = 0;
		Player winningPlayer;
			
		if (numPlayers < 2 || numPlayers > Rules.MAX_PLAYERS) {
			throw new InvalidParameterException(String.format("Max %d players", Rules.MAX_PLAYERS));
		}
		
		do {
			roundCount++;
			winningPlayer = round();
			if (winningPlayer != null) {
				Logger.info("found winner %d: %s", results.size() + 1, winningPlayer);
				results.add(winningPlayer);
				players.remove(winningPlayer);
				
				if (players.size() == 1) {
					// Game Over!
					results.add(players.get(0));
				} else {
					setStartingPlayer();
				}
			}
		} while (results.size() < numPlayers && (maxRounds < 0 || roundCount < maxRounds));
		
		Logger.info("ran for %d rounds", roundCount);
		return results;
	}
	
	/**
	 * Returns next player to play
	 */
	public Player getNextPlayer() {
		return players.get(next);
	}
	
	/**
	 * Override next player to play (testing)
	 */
	public void setNextPlayer(int player) {
		next = player;
	}
	
	/**
	 * Get current results array
	 */
	public ArrayList<Player> getCurrentResults() {
		return results;
	}
	
	/**
	 * Executes one round of the game.
	 * @return player if round winner runs out of cards
	 *         else return null
	 * @throws Exception 
	 */
	private Player round() throws IllegalStateException {
		int lastSuccess = next;
		Hand last = null;
		
		do {
			Player nextPlayer = players.get(next);
			Hand play = nextPlayer.getNextHand(last, playedHands);
			
			Logger.info("%s played hand %s", nextPlayer, play == null ? "pass" : play);
			
			if (play != null) {	
				int valid = Rules.checkHand(nextPlayer,  play, last);
				if (valid < 0) {
					play = null;
					
					if (nextPlayer.isBot()) {
						throw new IllegalStateException("Bot played invalid hand: err = " + valid);
					} else {
						// todo: prompt human player to try again...
					}
				}
			}
					
			if (play != null) {
				lastSuccess = next;
				last = play;
				playedHands.add(play);
				nextPlayer.getCards().removeAll(play.getCards());
				
				// The player has won, short circuit....
				if (nextPlayer.getCards().size() == 0) {
					return nextPlayer;
				}
			}
			
			next = (next + 1) % players.size();
		} while (next != lastSuccess);
				
		Logger.info("Round winner = %s\n", players.get(lastSuccess));
		return null;
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
