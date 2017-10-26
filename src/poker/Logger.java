package poker;

/** 
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * Logging helper. Set enabled to true to debug
 */

public class Logger {
	private static final boolean ENABLED = false;
	
	public static void info(String format, Object ... args) {
		if (ENABLED) {
			System.out.println(String.format(format, args));
		}
	}
}
