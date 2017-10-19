package poker.player.bots;

import java.util.TreeSet;
import poker.Card;
import poker.Hand;

public class PlayerAIBase implements IPokerBot {
	
	// Methods used by local engine
	public Hand getNextHand(TreeSet<Card> cards, Hand lastHand) {
		return null;
	}
	
	// IPokerBot implementation allows this class to be dropped into
	// other engines which use the same interface...
}
