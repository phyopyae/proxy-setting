package javafx.proxysetting;

import java.util.HashMap;
import java.util.Map;

/**
 * Command Utility Class
 * 
 * @author PhyoPyae
 *
 */
public class CommandUtility {

	/**
	 * Initial/Fixed command Map for Windows
	 */
	public static final Map<String, Object> COMMANDS_FOR_WINDOWS = initWindowsCommandsMap();

	/**
	 * Initial/Fixed command Map for Mac Map
	 */
	public static final Map<String, Object> COMMANDS_FOR_MAC = initMacCommandsMap();

	/**
	 * Initial/Fixed command Map for Linux Map
	 */
	public static final Map<String, Object> COMMANDS_FOR_LINUX = initLinuxCommandsMap();

	/**
	 * Added fixed proxy setting command of Windows into Map
	 * 
	 * @return Windows Command Map
	 */
	private static Map<String, Object> initWindowsCommandsMap() {
		Map<String, Object> windowsCmdMap = new HashMap<String, Object>();
		windowsCmdMap.put("EnableProxy",
				"reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 1 /f");
		windowsCmdMap.put("DisableProxy",
				"reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 0 /f");
		windowsCmdMap.put("UpdateProxy",
				"reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyServer /t REG_SZ /d <proxyAddress>:<proxyPort> /f");
		windowsCmdMap.put("UpdateProxyList",
				"reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyServer /t REG_SZ /d <proxyList> /f");
		
		return windowsCmdMap;
	}

	/**
	 * Added fixed proxy setting command of Mac into Map
	 * 
	 * @return Mac Command Map
	 */
	private static Map<String, Object> initMacCommandsMap() {
		Map<String, Object> macCmdMap = new HashMap<String, Object>();
		macCmdMap.put("EnableSocksProxy", "/usr/sbin/networksetup -setsocksfirewallproxystate 'Wi-Fi' on ");
		macCmdMap.put("EnableWebProxy", "/usr/sbin/networksetup -setwebproxystate 'Wi-Fi' on ");

		macCmdMap.put("DisableSocksProxy", "/usr/sbin/networksetup -setsocksfirewallproxystate 'Wi-Fi' off ");
		macCmdMap.put("DisableWebProxy", "/usr/sbin/networksetup -setwebproxystate 'Wi-Fi' off ");

		macCmdMap.put("UpdateSocksProxy",
				"/usr/sbin/networksetup -setsocksfirewallproxy 'Wi-Fi' <proxyAddress> <proxyPort> ");
		macCmdMap.put("UpdateWebProxy", "/usr/sbin/networksetup -setwebproxy 'Wi-Fi' <proxyAddress> <proxyPort> ");
		return macCmdMap;
	}

	/**
	 * Added fixed proxy setting command of Linux into Map
	 * 
	 * @return Linux Command Map
	 */
	private static Map<String, Object> initLinuxCommandsMap() {
		Map<String, Object> linuxCmdMap = new HashMap<String, Object>();

		linuxCmdMap.put("UpdateProxyModeManual", "gsettings set org.gnome.system.proxy mode 'manual'");
		linuxCmdMap.put("UpdateProxyModeOff", "gsettings set org.gnome.system.proxy mode 'none'");
		linuxCmdMap.put("UpdateProxyHttpEnable", "gsettings set org.gnome.system.proxy.http enabled true");
		linuxCmdMap.put("UpdateProxyAddress", "gsettings set org.gnome.system.proxy.http host '<proxyAddress>'");
		linuxCmdMap.put("UpdateProxyPort", "gsettings set org.gnome.system.proxy.http port <proxyPort>");

		return linuxCmdMap;
	}

	/**
	 * To convert proxy address and proxy port to given value
	 * 
	 * @param proxyAddress
	 *            address of proxy
	 * @param proxyPort
	 *            port of proxy
	 * @param command
	 *            command
	 * @return converted command
	 */
	public static String convertProxyFormat(String proxyAddress, String proxyPort, String command) {
		command.replaceAll("<proxyAddress>", proxyAddress);
		command.replaceAll("<proxyPort>", proxyPort);
		return command;
	}
}
