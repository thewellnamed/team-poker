package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import poker.Card;
import poker.enums.Rank;
import poker.enums.Suit;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 */

public class CardTest {

	@Test
	public void testFromString() {
		Card c = Card.ofValue("5s");
		assertThat(c.toString(), is("5s"));
		assertThat(c.getScore(), equalTo(Rank.FIVE.getScore() | Suit.SPADES.getScore()));
		
		// Case insensitive when playing cards.
		c = Card.ofValue("Ah");
		assertThat(c.toString(), is("Ah"));
		
		c = Card.ofValue("Td");
		assertThat(c.getRank(), is(Rank.TEN));
		assertThat(c.getSuit(), is(Suit.DIAMONDS));
	}
	
	@Test
	public void testInvalidCard() {
		Card c = Card.ofValue("13f");
		assertThat(c, equalTo(null));
	}

}
