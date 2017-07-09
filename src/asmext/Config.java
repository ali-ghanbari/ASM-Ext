package asmext;

/**
 * Contains configurations read from command line
 * 
 * @author Ali Ghanbari
 *
 */
public class Config {
	public static final String classPath = System.getProperty("asmext.cp", ".");
	
	public static final boolean verbose =
			Boolean.parseBoolean(System.getProperty("asmext.verbose", "true"));
}
