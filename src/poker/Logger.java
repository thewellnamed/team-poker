package poker;

/** 
 * @author Charles Williams, Matthew Kauffman, Lorenzo Colmenero
 * Logging helper. Set enabled to true to debug
 */

public class Logger {
	private static final boolean ENABLED = false;
	
	/**
	 * Basic logging helper
	 * @param format Format string
	 * @param args 
	 */
	public static void info(String format, Object ... args) {
		info(false, format, args);
	}
	
	/**
	 * 
	 * @param overrideEnabled
	 * @param format
	 * @param args
	 */
	public static void info(boolean overrideEnabled, String format, Object ... args) {
		if (ENABLED || overrideEnabled) {
			System.out.println(String.format(format, args));
		}
	}
}
