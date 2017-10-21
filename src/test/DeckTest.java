package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import java.util.HashSet;

import poker.Card;
import poker.Deck;

public class DeckTest {

	@Test
	public void testShuffle() {
		Deck deck = new Deck();
		assertThat(deck.size(), is(52));
		
		HashSet<Card> cards = new HashSet<Card>();
		HashSet<String> labels = new HashSet<String>();
		
		int[] suits = { 0, 0, 0, 0, 0 }; // index 0 unused
		int[] ranks = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		deck.shuffle();
		while (!deck.empty()) {
			Card c = deck.getNextCard();
			
			suits[c.getSuit().getId()]++;
			ranks[c.getRank().getId()]++;
			
			cards.add(c);
			labels.add(c.toString());
		}
		
		// 52 unique cards
		assertThat(cards.size(), is(52));
		assertThat(labels.size(), is(52));
		
		// 4 of each rank
		for (int i = 1; i < 14; i++) {
			assertThat(ranks[i], is(4));
		}
		
		// 13 of each suit
		for (int i = 1; i < 5; i++) {
			assertThat(suits[i], is(13));
		}
	}

}
