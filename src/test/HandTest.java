package test;

import static org.junit.Assert.*;

import java.util.TreeSet;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import poker.Card;
import poker.Hand;
import poker.enums.HandType;
import poker.enums.Rank;
import poker.enums.Suit;

public class HandTest {

	@Test
	public void testHighCard() {
		Hand h = new Hand("Ac");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.HIGH_CARD));
		assertThat(h.getScore(), is(Hand.HIGH_CARD_BASE_SCORE + h.getHighCard().getValue()));
		
		h = new Hand("2d");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.HIGH_CARD));
		assertThat(h.getScore(), is(Hand.HIGH_CARD_BASE_SCORE + h.getHighCard().getValue()));
	}
	
	@Test
	public void testPair() {
		Hand h = new Hand("AcAd");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.PAIR));
		assertThat(h.getScore(), is(Hand.PAIR_BASE_SCORE + h.getHighCard().getValue()));
		
		h = new Hand("AcKs");
		assertThat(h.isValid(), is(false));
		assertThat(h.getType(), is(HandType.INVALID));
		assertThat(h.getScore(), is(0));
	}
	
	@Test
	public void testTrips() {
		Hand h = new Hand("2s2d2c");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.TRIPS));
		assertThat(h.getScore(), is(Hand.TRIPS_BASE_SCORE + h.getHighCard().getRank().getValue()));
	}
	
	@Test
	public void testQuads() {
		Hand h = new Hand("Jd Js Jc Jh");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.QUADS));
		assertThat(h.getScore(), is(Hand.QUADS_BASE_SCORE + (h.getHighCard().getRank().getValue() * Hand.QUADS_RANK_MULTIPLIER)));
		
		h = new Hand("Td Ts Tc Ac");
		assertThat(h.isValid(), is(false));
		assertThat(h.getType(), is(HandType.INVALID));
		assertThat(h.getScore(), is(0));
	}
	
	@Test
	public void testStraight() {
		Hand h =  new Hand("5c 8d 6s 7s 9h");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.STRAIGHT));
		assertThat(h.getScore(), is(Hand.STRAIGHT_BASE_SCORE + h.getHighCard().getValue()));
		
		h = new Hand("10c 6d 8c 4h 9s");
		assertThat(h.isValid(), is(false));
		assertThat(h.getType(), is(HandType.INVALID));
		assertThat(h.getScore(), is(0));
	}
	
	@Test
	public void testFlush() {
		Hand h =  new Hand("Td 6d 4d 2d Jd");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.FLUSH));
		assertThat(h.getScore(), is(Hand.FLUSH_BASE_SCORE + h.getHighCard().getValue()));
	}
	
	@Test
	public void testFullHouse() {
		Hand h = new Hand("Ad As Ac Kd Kh");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.FULL_HOUSE));
		assertThat(h.getScore(), is(Hand.FULL_HOUSE_BASE_SCORE + (Rank.ACE.getValue() * 13) + Rank.KING.getValue()));
	}
	
	@Test
	public void testQuadsWithKicker() {
		Hand h = new Hand("Jd Js Jc Jh Td");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.QUADS_WITH_KICKER));
		assertThat(h.getScore(), is(Hand.QUADS_BASE_SCORE + (Rank.JACK.getValue() * Hand.QUADS_RANK_MULTIPLIER) + (new Card("Td").getValue())));
	}
	
	@Test
	public void testStraightFlush() {
		Hand h =  new Hand("7s 8s 4s 6s 5s");
		assertThat(h.isValid(), is(true));
		assertThat(h.getType(), is(HandType.STRAIGHT_FLUSH));
		assertThat(h.getScore(), is(Hand.STRAIGHT_FLUSH_BASE_SCORE + h.getHighCard().getValue()));
	}
	
	@Test
	public void testRelativeHandStrength() {
		Hand king = new Hand("Kd");
		Hand jack = new Hand("Js");
		
		assertThat(king.getScore() > jack.getScore(), is(true));
		
		Hand acesDiamond = new Hand("AcAd");
		Hand acesSpade = new Hand("AsAh");
		
		assertThat(acesSpade.getScore() > acesDiamond.getScore(), is(true));
		assertThat(acesDiamond.getScore() > king.getScore(), is(true));
		
		Hand tripDeuces = new Hand("2s2c2d");
		assertThat(tripDeuces.getScore() > acesSpade.getScore(), is(true));
		
		Hand tripQueens = new Hand("QsQdQh");
		assertThat(tripQueens.getScore() > tripDeuces.getScore(), is(true));
				
		Hand lowStraight = new Hand("2d 3s 4c 5h 6h");
		assertThat(lowStraight.getScore() > tripQueens.getScore(), is(true));
		
		Hand highStraight = new Hand("Tc Jd Qs Ks As");
		assertThat(highStraight.getScore() > lowStraight.getScore(), is(true));
		
		Hand lowFlush = new Hand("2d 5d 4d 8d 6d");
		Hand highFlushClubs = new Hand("Ac 3c Tc Jc 6c");
		Hand highFlushSpades  = new Hand("As 3s Ts Js 6s");
		
		assertThat(lowFlush.getScore() > highStraight.getScore(), is(true));
		assertThat(highFlushClubs.getScore() > lowFlush.getScore(), is(true));
		assertThat(highFlushSpades.getScore() > highFlushClubs.getScore(), is(true));
		
		Hand acesFull = new Hand("Ad Ac Ah Th Tc");
		Hand kingsFull = new Hand("Ks Kc Kd 3s 3h");
		
		assertThat(kingsFull.getScore() > highFlushSpades.getScore(), is(true));
		assertThat(acesFull.getScore() > kingsFull.getScore(), is(true));
		
		Hand quadTwos = new Hand("2s 2c 2d 2h");
		Hand quadAces = new Hand("Ac Ad As Ah");
		assertThat(quadTwos.getScore() > acesFull.getScore(), is(true));
		assertThat(quadAces.getScore() > quadTwos.getScore(), is(true));
		
		Hand quadThreesKicker = new Hand("3c 3d 3s 3h 4c");
		assertThat(quadThreesKicker.getScore() > acesFull.getScore(), is(true));
		assertThat(quadThreesKicker.getScore() < quadAces.getScore(), is(true));
		
		Hand lowStraightFlush = new Hand("2s 3s 4s 5s 6s");
		Hand highStraightFlush = new Hand("8c 9c Tc Jc Qc");
		
		assertThat(lowStraightFlush.getScore() > quadThreesKicker.getScore(), is(true));
		assertThat(highStraightFlush.getScore() > lowStraightFlush.getScore(), is(true));
	}
	
	@Test
	public void testHandComparator() {
		TreeSet<Hand> hands = new TreeSet<Hand>();
		hands.add(new Hand("2c"));
		hands.add(new Hand("2s"));
		hands.add(new Hand("3c3h"));
		hands.add(new Hand("6c6s6d"));
		hands.add(new Hand("4c5s6d7h8h"));
		hands.add(new Hand("4sKs7s9s2s"));
		hands.add(new Hand("2s2d2c4c4s"));
		hands.add(new Hand("5c5s5h5dAs"));
		hands.add(new Hand("JcJdJsJh"));
		hands.add(new Hand("3s4s5s6s7s"));
		
		assertThat(hands.toString(), equalTo("[2c, 2s, 3h3c, 6s6d6c, 8h7h6d5s4c, Ks9s7s4s2s, 4s4c2s2d2c, As5s5h5d5c, JsJhJdJc, 7s6s5s4s3s]"));
	}
	
    // 3.843s
	@Test
	public void testSpeed() {
		TreeSet<Card> test = new TreeSet<Card>();
		test.add(new Card(Rank.TWO, Suit.SPADES));
		test.add(new Card(Rank.ACE, Suit.SPADES));
		test.add(new Card(Rank.FOUR, Suit.SPADES));
		test.add(new Card(Rank.NINE, Suit.SPADES));
		test.add(new Card(Rank.TEN, Suit.CLUBS));
		
		Hand h;
		for (int i = 0; i < 10000000; i++) {
			h = new Hand(test);
		}
	}	
}

