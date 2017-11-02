package poker.io;

import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import poker.Card;
import poker.GameResults;
import poker.Hand;
import poker.Rules;
import poker.Utils;
import poker.player.Player;
import poker.player.bots.*;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * Helper methods to collect user input
 */
public class UserInput {
	private static Scanner in = new Scanner(System.in);

	/**
	 * Prompt user for number of players in game
	 * @param max Max players
	 * @return player count
	 */
	public static int getPlayerCount(int max) {
		int players;
		do {
			System.out.print(String.format("Enter number of players (Min: 2, Max: %d): ", max));
			
			try {
				players = Integer.parseInt(in.nextLine());
			} 
			catch (NumberFormatException e) {
				players = -1;
			}
				
			if (players < 2 || players > max) {
				System.out.println("Invalid player count!");
			}
			
		} while (players < 2 || players > max);
		
		return players;
	}
	
	/**
	 * Prompt user for type of next player
	 * @param next Player number
	 * @return Player.BOT or Player.HUMAN
	 */
	public static int getNextPlayerType(int next) {
		int playerType = -1;
		
		do {
			System.out.print(String.format("Player %d Type [bot, human]: ", next));
			String type = in.nextLine();
			
			switch (type.trim().toUpperCase()) {
				case "HUMAN":
					playerType = Player.HUMAN;
					break;
					
				case "BOT":
					playerType = Player.BOT;
					break;
					
				default:
					System.out.println("Invalid player type!");
					break;
			}
		} while (playerType < 0);
		
		return playerType;
	}
	
	/**
	 * Get next player name
	 * @param next Player number
	 * @return Name
	 */
	public static String getNextPlayerName(int next) {
		System.out.print(String.format("Player %d name: ", next));
		return in.nextLine().trim();
	}
	
	public static PlayerAIBase getNextPlayerAI(int next) {
		PlayerAIBase ai = null;
		
		do {
			System.out.print(String.format("Player %d Bot AI [Matthew, Random]: ", next));
			String type = in.nextLine();
			
			switch (type.trim().toUpperCase()) {
				case "MATTHEW":
					ai = new MatthewBot();
					break;
					
				case "RANDOM":
					ai = new RandomBot();
					break;
					
				default:
					System.out.println("Invalid bot name!");
					break;
			}
		} while (ai == null);
		
		return ai;
	}
	
	public static int getRoundCount() {
		int count;
		System.out.print("Enter number of games: ");
		try {
			count = Integer.parseInt(in.nextLine());
		}
		catch (NumberFormatException e) {
			count = 1;
		}
		
		return count;
	}
	
	/**
	 * Prompt human player for their next hand
	 * @return Hand
	 */
	public static Hand getPlayerHand(Player p, Hand last) {
		String lastPlay = (last == null) ? "" : 
				String.format(" Must beat: %s", Utils.getCardString(last.getCards()));
		
		System.out.println(String.format("%s's turn.%s", p.getName(), lastPlay));
		System.out.println(String.format("Current Cards: %s", Utils.getCardString(p.getCards())));
		Hand h = null;
		do {
			System.out.print("Enter Hand to play (or 'pass'): ");
			String cardStr = in.nextLine();
			
			// if passing, return immediately
			if (cardStr.equalsIgnoreCase("pass")) {
				return null;
			}
			
			TreeSet<Card> parsedCards = Utils.getCardsFromString(cardStr);
			if (parsedCards == null) {
				System.out.println("Invalid hand.");
			} else {
				h = new Hand(parsedCards);
			}
		} while (h == null);
		
		System.out.print("\n");
		return h;
	}
	
	/**
	 * Invalid hand 
	 */
	public static void raiseInvalidHand(Player p, int errorType) {
		switch (errorType) {
			case Rules.HAND_INVALID:
				System.out.println("That's not a valid hand!");
				break;
				
			case Rules.HAND_INVALID_PLAYER_CARDS:
				System.out.println("You must play cards that you hold!");
				break;
				
			case Rules.HAND_INVALID_LOW_SCORE:
				System.out.println("You must play a hand that beats the last hand!");
				break;
				
			case Rules.HAND_INVALID_STARTING_CARD:
				System.out.println("You must open with a hand that uses your lowest card!");
				break;
				
			default:
				// wat?
				break;
		}
	}
	
	/**
	 * Post message to user
	 * @param format Format string
	 * @param args Args
	 */
	public static void postMessage(String format, Object ... args) {
		System.out.println(String.format(format, args));
	}
	
	/**
	 * Generate output table of results
	 */
	public static void postResults(List<GameResults> results) {
		String placeLabels[] = { "1st", "2nd", "3rd", "4th" };
		StringBuffer placesColHeader = new StringBuffer();
		StringBuffer placesCol = new StringBuffer();
		for (int i = 0; i < results.size(); i++) {
			placesColHeader.append("-------+");
			placesCol.append("   " + placeLabels[i] + " |"); 
		}
		
		System.out.println("+----------------------+-------+" + placesColHeader);
		System.out.println("| Player               | Score |" + placesCol);
		System.out.println("+----------------------+-------+" + placesColHeader);
		
		for (GameResults r : results) {
			int[] places = r.getResults();
			
			System.out.print(String.format("| %1$-20s |", r.getPlayer().getName()));
			System.out.print(String.format(" %1$5s |", Integer.toString(r.getScore())));
			
			for (int i = 0; i < results.size(); i++) {
				System.out.print(String.format(" %1$5s |", places[i]));
			}
			System.out.print("\n");
		}
		
		System.out.println("+----------------------+-------+" + placesColHeader);		
	}
}
