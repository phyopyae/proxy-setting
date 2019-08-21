package javafx.proxysetting;

/**
 * Common Utility Class
 * 
 * @author PhyoPyae
 *
 */
public class CommonUtility {

	/**
	 * OS of the machine
	 */
	private static String OS = System.getProperty("os.name").toLowerCase();

	/**
	 * Getter of OS
	 * 
	 * @return OS
	 */
	public static String getOS() {
		return OS;
	}

	/**
	 * Setter of OS
	 * 
	 * @param oS
	 *            OS
	 */
	public static void setOS(String oS) {
		OS = oS;
	}

	/**
	 * Confirm application is run on Windows or not
	 * 
	 * @return true if Windows
	 */
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	/**
	 * Confirm application is run on Mac or not
	 * 
	 * @return true if Mac
	 */
	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	/**
	 * Confirm application is run on Linux or not
	 * 
	 * @return true if Linux
	 */
	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	/**
	 * Confirm application is run on Solaris or not
	 * 
	 * @return true if Solaris
	 */
	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}
}
