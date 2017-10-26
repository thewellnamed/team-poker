package poker;

import java.util.TreeSet;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.regex.Matcher;

/**
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 */

public class Utils {
	// Match card format, case insensitive. 
	// Also ignores whitespace.
	private static final Pattern cardPattern = Pattern.compile("[2-9tjqkaTJQKA]{1}[cdshCDSH]{1}");
	
	public static TreeSet<Card> getCardsFromString(String cardStr) {
		TreeSet<Card> ret = new TreeSet<Card>();
		Matcher m = cardPattern.matcher(cardStr);
		
		while (m.find()) {
			ret.add(Card.ofValue(m.group()));
		}
		
		return ret;
	}
	
	public static String getCardString(TreeSet<Card> cards) {
		return cards.stream().map(c -> c.toString()).collect(Collectors.joining(""));
	}
}
