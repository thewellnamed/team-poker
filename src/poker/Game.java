package poker;

import java.security.InvalidParameterException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import poker.enums.Rank;
import poker.enums.Suit;
import poker.io.UserInput;
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
	private Card startingCard;
	private boolean haveHumanPlayers;
	
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
		
		// assume all bots. Updated below...
		haveHumanPlayers = false;
		
		// Deal the cards and the player/bot with the lowest card will go first. 
		long lowestCardScore = Rank.ACE.getScore() | Suit.SPADES.getScore();		
		for (int p = 0; p < players.size(); p++) {
			TreeSet<Card> cards = new TreeSet<Card>();
			for (int j = 0; j < Rules.CARDS_PER_PLAYER; j++) {
				cards.add(deck.getNextCard());
			}
			
			if (cards.last().getScore() < lowestCardScore) {
				startingCard = cards.last();
				lowestCardScore = startingCard.getScore();
				next = p;
			}
			
			Player player = players.get(p);
			if (!player.isBot()) {
				haveHumanPlayers = true;
			}
			player.setCards(cards);
		}
		
		postMessage("First player: %s", players.get(next).getName());
	}

	/**
	 * Run until complete
	 */
	public ArrayList<Player> run() {
		return run(-1);
	}
	
	/**
	 * Run for maxRounds rounds
	 * @param maxRounds Stop execution after a set number of rounds
	 * @throws Exception 
	 * @returns Array of players, winner first, then second place, etc.
	 */
	public ArrayList<Player> run(int maxRounds) {		
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
				postMessage("Winner %d: %s", results.size() + 1, winningPlayer.getName());
				results.add(winningPlayer);
				players.remove(winningPlayer);
				
				if (players.size() == 1) {
					// Game Over!
					results.add(players.get(0));
				} else if (next >= players.size()) {
					next = 0;
				}
			}
		} while (results.size() < numPlayers && (maxRounds < 0 || roundCount < maxRounds));
		
		postMessage("ran for %d rounds", roundCount);
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
	 */
	private Player round() {
		int lastSuccess = next;
		Hand last = null;
		
		// each player plays until the round ends
		// when all but one player passes
		do {
			Player nextPlayer = players.get(next);
			Hand play;
			boolean doneWithPlayer;
			
			do {
				doneWithPlayer = true;
				play = nextPlayer.getNextHand(last, playedHands);
				postMessage("%s played %s", nextPlayer.getName(), play == null ? "pass" : play);
				
				if (play != null) {	
					int valid = Rules.checkHand(nextPlayer,  play, last, startingCard);
					if (valid < 0) {
						play = null;
						
						if (nextPlayer.isBot()) {
							throw new IllegalStateException("Bot played invalid hand: err = " + valid);
						} else {
							doneWithPlayer = false;
							UserInput.raiseInvalidHand(nextPlayer, valid);
						}
					}
				}
			} while (!doneWithPlayer);
					
			if (play != null) {
				lastSuccess = next;
				last = play;
				playedHands.add(play);
				nextPlayer.getCards().removeAll(play.getCards());
				
				// ensure lowest card rule is only enforced once...
				startingCard = null;
				
				// The player has won, short circuit....
				if (nextPlayer.getCards().size() == 0) {
					return nextPlayer;
				}
			}
			
			next = (next + 1) % players.size();
		} while (next != lastSuccess);
				
		postMessage("Round winner = %s\n", players.get(lastSuccess).getName());
		return null;
	}
	
	/**
	 * Post user message
	 */
	private void postMessage(String format, Object ... args) {
		Logger.info(format, args);
		
		if (haveHumanPlayers) {
			UserInput.postMessage(format, args);
		}
	}
}
