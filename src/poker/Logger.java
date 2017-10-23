package poker;

/**
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
