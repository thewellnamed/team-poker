package poker.player.bots;

import java.util.ArrayList;
import java.util.TreeSet;

import poker.Card;
import poker.Hand;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * The interface for the bots.
 */

public interface IPokerBot {
	public Hand getNextHand(TreeSet<Card> cards, Hand last, ArrayList<Hand> previous);
}
