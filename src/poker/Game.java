package poker;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import poker.enums.Rank;
import poker.enums.Suit;
import poker.player.Player;

public class Game {
	private Deck deck;
	private List<Player> players;
	private ArrayList<Hand> playedHands;
	private ArrayList<Player> results;
	private int next;
	
	public static final int MAX_PLAYERS = 4;
	public static final int CARDS_PER_PLAYER = 13;
	
	public Game(List<Player> newPlayers) {
		players = newPlayers;
		deck = new Deck();
		deck.shuffle();
		
		if (players.size() > MAX_PLAYERS) {
			throw new InvalidParameterException("Max 4 players");
		}
		
		// sort based on position
		players.sort(null);
		long lowestCard = Rank.ACE.getScore() | Suit.SPADES.getScore();
		
		// deal cards and find first to play based on lowest card
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
	 * Run the game until results determined
	 * @returns Array of players, winner first, then second place, etc.
	 */
	public ArrayList<Player> run() {
		results = new ArrayList<Player>();
		playedHands = new ArrayList<Hand>();
		
		int numPlayers = players.size();
		int winningPlayer;
		
		do {
			winningPlayer = round();
			if (winningPlayer > 0) {
				results.add(players.get(winningPlayer));
				players.remove(winningPlayer);
			}
		} while (results.size() < numPlayers);
		
		return results;
	}
	
	/**
	 * Executes one round of the game
	 * @return player index of player if player runs out of cards
	 *         else return -1
	 */
	private int round() {
		int passed = 0;
		int lastSuccess = 0;
		Hand last = null;
		
		do {
			Player nextPlayer = players.get(next);
			Hand play = nextPlayer.getNextHand(last, playedHands);
			
			Logger.info("Player %s played hand %s", nextPlayer, play == null ? "pass" : play);
			
			if (play == null) {
				passed++;
			} else {
				lastSuccess = next;
				last = play;
				playedHands.add(play);
				nextPlayer.getCards().removeAll(play.getCards());
				
				// player has won, short circuit...
				if (nextPlayer.getCards().size() == 0) {
					Logger.info("Found game winner: %s", nextPlayer);
					return next;
				}
			}
			
			next = (next + 1) % players.size();
		} while (passed < players.size() - 1);
		
		Logger.info("Round winner = %s", players.get(lastSuccess));
		
		// next round begins with winner of this round
		next = lastSuccess;
		return -1;
	}
}
