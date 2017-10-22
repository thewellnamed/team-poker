package poker.player.bots;

import java.util.ArrayList;
import java.util.TreeSet;

import poker.Card;
import poker.Hand;

public interface IPokerBot {
	public Hand getNextHand(TreeSet<Card> cards, Hand last, ArrayList<Hand> previous);
}
