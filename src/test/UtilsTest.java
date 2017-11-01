package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import java.util.TreeSet;

import poker.Card;
import poker.Utils;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 */

public class UtilsTest {

	@Test
	public void testGetCardsFromString() {
		TreeSet<Card> cards = Utils.getCardsFromString("5d 5c 5s 5h");
		assertThat(cards.size(), is(4));
		assertThat(cards.contains(Card.ofValue("5c")), is(true));
		assertThat(cards.contains(Card.ofValue("5d")), is(true));
		assertThat(cards.contains(Card.ofValue("5h")), is(true));
		assertThat(cards.contains(Card.ofValue("5s")), is(true));
	}
	
	@Test
	public void testNoSpaces() {
		TreeSet<Card> cards = Utils.getCardsFromString("AdKdQdJdTd");
		assertThat(cards.size(), is(5));
		assertThat(cards.contains(Card.ofValue("Ad")), is(true));
		assertThat(cards.contains(Card.ofValue("Kd")), is(true));
		assertThat(cards.contains(Card.ofValue("Qd")), is(true));
		assertThat(cards.contains(Card.ofValue("Jd")), is(true));
		assertThat(cards.contains(Card.ofValue("Td")), is(true));
	}
	
	@Test 
	public void testGetCardString() {
		TreeSet<Card> cards = new TreeSet<Card>();
		cards.add(Card.ofValue("As"));
		cards.add(Card.ofValue("Ad"));
		cards.add(Card.ofValue("5c"));
		
		// TreeSet sorts...
		assertThat(Utils.getCardString(cards), is("As Ad 5c"));
	}

}
