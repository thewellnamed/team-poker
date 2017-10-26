package test.bot;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import poker.Card;
import poker.Hand;
import poker.player.bots.MatthewBot;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * This particular bot is used to test the different possible hand plays a player could do...
 * Depending on what cards are in the hand.
 */

public class MatthewBotTest {
	@Test
	public void testValidHands() {
		MatthewBot bot = new MatthewBot();
		
		TreeSet<Card> cards = new TreeSet<Card>(Arrays.asList(
			Card.ofValue("As"),
			Card.ofValue("Ah"),
			Card.ofValue("Ad"),
			Card.ofValue("Ac"),
			Card.ofValue("Qs"),
			Card.ofValue("Qc"),
			Card.ofValue("Js"),
			Card.ofValue("Ts"),
			Card.ofValue("9s"),
			Card.ofValue("8s"),
			Card.ofValue("7d"),
			Card.ofValue("7c"),
			Card.ofValue("5s")
		));
		
		bot.getNextHand(cards, null, new ArrayList<Hand>());
		
		HashMap<Integer, TreeSet<Hand>> hands = bot.getHands();
		for (int i = 1; i <= 5; i++) {
			TreeSet<Hand> next = hands.get(i);
			
			if (next.size() > 0) {
				System.out.print("Hands of size " + i + ":\n");
				for (Hand h : next) {
					System.out.println(String.format("   %s - type=%s", h, h.getType()));
				}
				System.out.print("\n");
			}
		}
	}
}
