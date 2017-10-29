package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import poker.Card;
import poker.Game;
import poker.Hand;
import poker.Utils;
import poker.player.Player;
import poker.player.bots.PlayerAIBase;

/**
 * Test game logic
 */
public class GameTest {
	@Test
	public void testBasicRoundFunctionality() {
		SimpleBot aliceBot = new SimpleBot();
		SimpleBot bobBot = new SimpleBot();
		
		Player alice = new Player(Player.BOT, Player.NORTH, "Alice", aliceBot);
		Player bob = new Player(Player.BOT, Player.EAST, "Bob", bobBot);
		
		Game game = new Game(Arrays.asList(alice, bob));
		game.setNextPlayer(0);
		alice.setCards(Utils.getCardsFromString("KsKc2c"));
		bob.setCards(Utils.getCardsFromString("AhAdTh"));
		
		game.run(1); // execute single round
		assertThat(aliceBot.plays, is(2)); // 2c -> Ks
		assertThat(aliceBot.passes, is(1));
		assertThat(alice.getCards().toString(), equalTo("[Kc]"));
		assertThat(bobBot.plays, is(2));
		assertThat(bobBot.passes, is(0));
		assertThat(bob.getCards().toString(), equalTo("[Ad]"));		
		assertThat(game.getNextPlayer(), equalTo(bob));
		
		game.run(1); // next round
		assertThat(aliceBot.plays, is(2));
		assertThat(aliceBot.passes, is(1));
		assertThat(alice.getCards().toString(), equalTo("[Kc]"));
		assertThat(bobBot.plays, is(3));
		assertThat(bobBot.passes, is(0));
		assertThat(bob.getCards().size(), is(0));
		
		assertThat(game.getCurrentResults().size(), is(2));
		assertThat(game.getCurrentResults().get(0), equalTo(bob));
	}
	
	
	/**
	 * Simple bot for testing
	 * Allows plays lowest possible valid hand
	 */
	private class SimpleBot extends PlayerAIBase {
		public int plays;
		public int passes;
		
		public SimpleBot() {
			plays = 0;
			passes = 0;
		}
		
		@Override
		public Hand getNextHand(TreeSet<Card> cards, Hand last, ArrayList<Hand> previous) {
			Hand ret = null;
			populateValidHands(cards);
			
			if (last == null) {
				for (int i = 1; i <= 5; i++) {
					TreeSet<Hand> hands = validHands.get(i);
					if (hands.size() > 0) {
						ret = hands.last();
						break;
					}
				}
			} else {
				TreeSet<Hand> hands = validHands.get(last.getSize());
				
				if (!hands.isEmpty()) {
					TreeSet<Hand> valid = new TreeSet<Hand>(hands.stream().filter(h -> h.getScore() > last.getScore()).collect(Collectors.toList()));
					if (valid.size() > 0) {
						ret = valid.last();
					}
				}	
			}
			
			if (ret == null) {
				passes++;
			} else {
				plays++;
			}
					
			return ret;			
		}
	}
}
