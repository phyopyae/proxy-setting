package javafx.proxysetting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Changing Proxy Setting for Windows, Mac and Linux by JavaFX application.
 * 
 * @author PhyoPyae
 *
 */
public class ProxySettingMain extends Application {

	/**
	 * Default Date Format
	 */
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	
	/**
	 * Fixed Proxy Address
	 */
	private static final String PROXY_ADDRESS = "192.168.49.1";
	
	/**
	 * Fixed Proxy Port
	 */
	private static final String PROXY_PORT = "1111";

	static Label loading = new Label();

	/**
	 * Start the JavaFX application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {

			Button button = new Button("Connect");

			StackPane pane = new StackPane();

			String currentOS = "";
			System.out.println(CommonUtility.getOS());
			Label osLabel = new Label();
			if (CommonUtility.isWindows()) {
				currentOS = "Windows";
				loading.setText("");
				osLabel.setText("Current OS : " + currentOS);
				button.setOnAction(e -> updateProxySettingForWindows(button));
			} else if (CommonUtility.isMac()) {
				currentOS = "Mac";
				osLabel.setText("Current OS : " + currentOS);
				button.setOnAction(e -> updateProxySettingForMac(button));
			} else if (CommonUtility.isUnix()) {
				currentOS = "Unix or Linux";
				osLabel.setText("Current OS : " + currentOS);
				button.setOnAction(e -> updateProxySettingForLinux(button));
			} else if (CommonUtility.isSolaris()) {
				currentOS = "Solaris";
			} else {
				currentOS = "Not Supported!";
			}

			VBox vbox = new VBox(button, osLabel, loading);
			vbox.setAlignment(Pos.CENTER);
			vbox.prefWidthProperty().bind(pane.widthProperty());
			vbox.prefHeightProperty().bind(pane.heightProperty());
			pane.getChildren().add(vbox);

			Scene scene = new Scene(pane, 300, 100);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Proxy Setting");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * To update proxy setting for Windows
	 * 
	 * @param button
	 *            button
	 */
	public static void updateProxySettingForWindows(Button button) {
		Map<String, Object> windowsCmd = CommandUtility.COMMANDS_FOR_WINDOWS;
		if (button.getText().equalsIgnoreCase("Connect")) {
			String proxyAddress = PROXY_ADDRESS;
			String proxyPort = PROXY_PORT;

			String enableCmd = windowsCmd.get("EnableProxy").toString();
			int enableResult = changeSetting(enableCmd);

			String changeProxyCmd = windowsCmd.get("UpdateProxy").toString();
			changeProxyCmd = changeProxyCmd.replace("<proxyAddress>", proxyAddress);
			changeProxyCmd = changeProxyCmd.replace("<proxyPort>", proxyPort);
			int changeResult = changeSetting(changeProxyCmd);
			if (enableResult == 0 || changeResult == 0) {
				new Thread(() -> {
					Platform.runLater(() -> loading.setText("Loading ...."));

					executePowerShellFile();

					Platform.runLater(() -> loading.setText(""));
				}).start();

				button.setText("Disconnect");
			}
		} else {
			String disableCmd = windowsCmd.get("DisableProxy").toString();
			int disableResult = changeSetting(disableCmd);

			if (disableResult == 0) {
				new Thread(() -> {
					Platform.runLater(() -> loading.setText("Loading ...."));

					executePowerShellFile();

					Platform.runLater(() -> loading.setText(""));
				}).start();

				button.setText("Connect");
			}
		}
	}

	/**
	 * To update proxy setting for Mac by using following command networksetup
	 * [-setftpproxy networkservice domain portnumber authenticated username
	 * password] [-setftpproxystate networkservice on | off] [-getwebproxy
	 * networkservice] [-setwebproxy networkservice domain portnumber authenticated
	 * username password] [-setwebproxystate networkservice on | off]
	 * [-getsecurewebproxy networkservice] [-setsecurewebproxy networkservice domain
	 * portnumber authenticated username password] [-setsecurewebproxystate
	 * networkservice on | off] [-getstreamingproxy networkservice]
	 * [-setstreamingproxy networkservice domain portnumber authenticated username
	 * password] [-setstreamingproxystate networkservice on | off] [-getgopherproxy
	 * networkservice] [-setgopherproxy networkservice domain portnumber
	 * authenticated username password] [-setgopherproxystate networkservice on |
	 * off] [-getsocksfirewallproxy networkservice] [-setsocksfirewallproxy
	 * networkservice domain portnumber authenticated username password]
	 * [-setsocksfirewallproxystate networkservice on | off] [-getproxybypassdomains
	 * networkservice] [-setproxybypassdomains networkservice domain1 [domain2]
	 * [...]] [-getpassiveftp networkservice]
	 * 
	 * @param button
	 *            button
	 */
	public static void updateProxySettingForMac(Button button) {
		Map<String, Object> macCmd = CommandUtility.COMMANDS_FOR_MAC;
		if (button.getText().equalsIgnoreCase("Connect")) {
			button.setText("Disconnect");
			String proxyAddress = PROXY_ADDRESS;
			String proxyPort = PROXY_PORT;

			String socksEnableCmd = macCmd.get("EnableSocksProxy").toString();
			changeSettingMac(socksEnableCmd);

			String changeProxyCmd = macCmd.get("UpdateSocksProxy").toString();
			changeProxyCmd = changeProxyCmd.replace("<proxyAddress>", proxyAddress);
			changeProxyCmd = changeProxyCmd.replace("<proxyPort>", proxyPort);
			changeSettingMac(changeProxyCmd);
		} else {
			button.setText("Connect");
			String changeProxyCmd = macCmd.get("UpdateSocksProxy").toString();
			changeProxyCmd = changeProxyCmd.replace("<proxyAddress>", "''");
			changeProxyCmd = changeProxyCmd.replace("<proxyPort>", "0");
			changeSettingMac(changeProxyCmd);

			String socksDisableCmd = macCmd.get("DisableSocksProxy").toString();
			changeSettingMac(socksDisableCmd);
		}
	}

	/**
	 * To update proxy setting for linux Using gsettings command to change system
	 * proxy
	 * 
	 * @param button
	 *            button
	 */
	public static void updateProxySettingForLinux(Button button) {
		Map<String, Object> linuxCmd = CommandUtility.COMMANDS_FOR_LINUX;
		Date date = new Date();
		String dateStr = DateUtility.dateToString(date, DATE_FORMAT);

		if (button.getText().equalsIgnoreCase("Connect")) {
			String proxyAddress = PROXY_ADDRESS;
			String proxyPort = PROXY_PORT;

			String updateProxyModeCmd = linuxCmd.get("UpdateProxyModeManual").toString();
			changeSettingLinux(updateProxyModeCmd, dateStr);

			String enableHttpProxyCmd = linuxCmd.get("UpdateProxyHttpEnable").toString();
			changeSettingLinux(enableHttpProxyCmd, dateStr);

			String updateProxyAddressCmd = linuxCmd.get("UpdateProxyAddress").toString();
			updateProxyAddressCmd = updateProxyAddressCmd.replace("<proxyAddress>", proxyAddress);
			changeSettingLinux(updateProxyAddressCmd, dateStr);

			String updateProxyPortCmd = linuxCmd.get("UpdateProxyPort").toString();
			updateProxyPortCmd = updateProxyPortCmd.replace("<proxyPort>", proxyPort);
			changeSettingLinux(updateProxyPortCmd, dateStr);
		} else {
			button.setText("Connect");
			String updateProxyModeCmd = linuxCmd.get("UpdateProxyModeOff").toString();
			changeSettingLinux(updateProxyModeCmd, dateStr);
		}
	}

	/**
	 * To change setting for Windows
	 * 
	 * @param command
	 *            windows command
	 */
	public static int changeSetting(String command) {
		Process process = null;

		System.out.println(command);
		try {
			process = Runtime.getRuntime().exec(command);
			if (process.waitFor() == 0) {
				// Everything went fine
			} else {
				// Something went wrong
			}
			return process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * To change setting for Mac
	 * 
	 * @param command
	 *            mac command
	 */
	public static void changeSettingMac(String command) {
		System.out.println(command);
		ProcessBuilder processBuilder = new ProcessBuilder();
		try {
			processBuilder.command("bash", "-c", command);
			Process process = processBuilder.start();
			if (process.waitFor() == 0) {
				System.out.println("Success!");
				// Everything went fine
			} else {
				System.out.println("Something went wrong!");
				// Something went wrong
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * To change setting of Linux
	 * 
	 * @param command
	 *            linux command
	 * @param date
	 *            changing date
	 */
	public static void changeSettingLinux(String command, String date) {

		ProcessBuilder processBuilder = new ProcessBuilder();
		try {
			processBuilder.command("bash", "-c", command);
			System.out.println("Start : " + command + " - " + date);
			Process process = processBuilder.start();
			if (process.waitFor() == 0) {
				System.out.println("Success!");
				// Everything went fine
			} else {
				System.out.println("Something went wrong!");
				// Something went wrong
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * To execute the powershell script 
	 * Library : https://github.com/profesorfalken/jPowerShell
	 * @return
	 */
	public static Integer executePowerShellFile() {
		try (PowerShell powerShell = PowerShell.openSession()) {
			// Increase timeout to give enough time to the script to finish
			Map<String, String> config = new HashMap<String, String>();
			config.put("maxWait", "80000");

			String script = "/resources/RefreshExplorer.ps1";

			BufferedReader srcReader = new BufferedReader(
					new InputStreamReader(ProxySettingMain.class.getResourceAsStream(script)));

			// Execute script
			PowerShellResponse response = powerShell.executeScript(srcReader);

			// Print results if the script
			System.out.println("Script output:" + response.getCommandOutput());
			return 0;
		} catch (PowerShellNotAvailableException ex) {
			ex.printStackTrace();
			return 1;
		}
	}

	/**
	 * Main method to launch JavaFX application
	 * 
	 * @param args
	 *            argument
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
