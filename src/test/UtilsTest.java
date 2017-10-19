package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import java.util.TreeSet;

import poker.Card;
import poker.Utils;

public class UtilsTest {

	@Test
	public void testGetCardsFromString() {
		TreeSet<Card> cards = Utils.getCardsFromString("5d 5c 5s 5h");
		assertThat(cards.size(), is(4));
		assertThat(cards.contains(new Card("5c")), is(true));
		assertThat(cards.contains(new Card("5d")), is(true));
		assertThat(cards.contains(new Card("5h")), is(true));
		assertThat(cards.contains(new Card("5s")), is(true));
	}
	
	@Test
	public void testNoSpaces() {
		TreeSet<Card> cards = Utils.getCardsFromString("AdKdQdJdTd");
		assertThat(cards.size(), is(5));
		assertThat(cards.contains(new Card("Ad")), is(true));
		assertThat(cards.contains(new Card("Kd")), is(true));
		assertThat(cards.contains(new Card("Qd")), is(true));
		assertThat(cards.contains(new Card("Jd")), is(true));
		assertThat(cards.contains(new Card("Td")), is(true));
	}
	
	@Test 
	public void testGetCardString() {
		TreeSet<Card> cards = new TreeSet<Card>();
		cards.add(new Card("As"));
		cards.add(new Card("Ad"));
		cards.add(new Card("5c"));
		
		// TreeSet sorts...
		assertThat(Utils.getCardString(cards), is("AsAd5c"));
	}

}
