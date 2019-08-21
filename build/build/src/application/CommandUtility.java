package application;

import java.util.HashMap;
import java.util.Map;

public class CommandUtility {

	public static final Map<String, Object> COMMANDS_FOR_WINDOWS = initWindowsCommandsMap();
	
	public static final Map<String, Object> COMMANDS_FOR_MAC = initMacCommandsMap();
	
	public static final Map<String, Object> COMMANDS_FOR_LINUX = initLinuxCommandsMap();
	
	private static Map<String, Object> initWindowsCommandsMap() {
		Map<String, Object> windowsCmdMap = new HashMap<String, Object>();
		windowsCmdMap.put("EnableProxy", "reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 1 /f");
		windowsCmdMap.put("DisableProxy", "reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 0 /f");
		windowsCmdMap.put("UpdateProxy", "reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyServer /t REG_SZ /d <proxyAddress>:<proxyPort> /f");
		windowsCmdMap.put("UpdateProxyList", "reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyServer /t REG_SZ /d <proxyList> /f");
		return windowsCmdMap;
	}

	private static Map<String, Object> initMacCommandsMap() {
		Map<String, Object> macCmdMap = new HashMap<String, Object>();
		macCmdMap.put("EnableSocksProxy", "networksetup -setsocksfirewallproxystate \"Wi-Fi\" on ");
		macCmdMap.put("DisableSocksProxy", "networksetup -setsocksfirewallproxystate \"Wi-Fi\" off ");
		macCmdMap.put("UpdateSocksProxy", "networksetup -setsocksfirewallproxy \"Wi-Fi\" <proxyAddress> <proxyPort> ");
		return macCmdMap;
	}

	private static Map<String, Object> initLinuxCommandsMap() {
		Map<String, Object> linuxCmdMap = new HashMap<String, Object>();
		linuxCmdMap.put("UpdateHttpProxy", "export http_proxy=\"http://<proxyAddress>:<proxyPort>\"");
		linuxCmdMap.put("UpdateHttpsProxy", "export https_proxy=\"https://<proxyAddress>:<proxyPort>\"");
		linuxCmdMap.put("UpdateFtpProxy", "export ftp_proxy=\"http://<proxyAddress>:<proxyPort>\"");
		linuxCmdMap.put("UpdateSocketProxy", "export socks_proxy=\"http://<proxyAddress>:<proxyPort>\"");
		
		linuxCmdMap.put("DisableHttpProxy", "unset http_proxy");
		linuxCmdMap.put("DisableHttpsProxy", "unset https_proxy");
		linuxCmdMap.put("DisableFtpProxy", "unset ftp_proxy");
		linuxCmdMap.put("DisableSocketProxy", "unset socks_proxy");
				
		return linuxCmdMap;
	}
	
	public static String convertProxyFormat(String proxyAddress, String proxyPort, String command) {
		command.replaceAll("<proxyAddress>", proxyAddress);
		command.replaceAll("<proxyPort>", proxyPort);
		return command;
	}
}
