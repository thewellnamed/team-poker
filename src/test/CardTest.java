package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import poker.Card;
import poker.enums.Rank;
import poker.enums.Suit;

public class CardTest {

	@Test
	public void testFromString() {
		Card c = new Card("5s");
		assertThat(c.toString(), is("5s"));
		assertThat(c.getScore(), equalTo(Rank.FIVE.getScore() | Suit.SPADES.getScore()));
		
		// case insensitive
		c = new Card("AH");
		assertThat(c.toString(), is("Ah"));
		
		c = new Card("Td");
		assertThat(c.getRank(), is(Rank.TEN));
		assertThat(c.getSuit(), is(Suit.DIAMONDS));
	}

}
