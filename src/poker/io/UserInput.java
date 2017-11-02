package poker.io;

import java.util.Scanner;
import java.util.TreeSet;

import poker.Card;
import poker.Hand;
import poker.Rules;
import poker.Utils;
import poker.player.Player;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * Helper methods to collect user input
 */
public class UserInput {
	private static Scanner in = new Scanner(System.in);

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
}
