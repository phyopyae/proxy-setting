package application;

import java.io.IOException;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProxySettingMain extends Application {

	private static String OS = System.getProperty("os.name").toLowerCase();

	@Override
	public void start(Stage primaryStage) {
		try {

			Button button = new Button("Connect");

			StackPane pane = new StackPane();

			String currentOS = "";
			Label osLabel = new Label();
			if (isWindows()) {
				currentOS = "Windows";
				osLabel.setText("Current OS : " + currentOS);
				button.setOnAction(e -> updateProxySettingForWindows(button));
			} else if (isMac()) {
				currentOS = "Mac";
				osLabel.setText("Current OS : " + currentOS);
				button.setOnAction(e -> updateProxySettingForMac(button));
			} else if (isUnix()) {
				currentOS = "Unix or Linux";
			} else if (isSolaris()) {
				currentOS = "Solaris";
			} else {
				currentOS = "Not Supported!";
			}

			VBox vbox = new VBox(button, osLabel);
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

	public static void updateProxySettingForWindows(Button button) {
		Map<String, Object> windowsCmd = CommandUtility.COMMANDS_FOR_WINDOWS;
		if (button.getText().equalsIgnoreCase("Connect")) {
			button.setText("Disconnect");
			String proxyAddress = "localhost";
			String proxyPort = "80";

			String enableCmd = windowsCmd.get("EnableProxy").toString();
			changeSetting(enableCmd);

			String changeProxyCmd = windowsCmd.get("UpdateProxy").toString();
			changeProxyCmd.replaceAll("<proxyAddress>", proxyAddress);
			changeProxyCmd.replaceAll("<proxyPort>", proxyPort);
			changeSetting(changeProxyCmd);
		} else {
			button.setText("Connect");
			String disableCmd = windowsCmd.get("DisableProxy").toString();
			changeSetting(disableCmd);
		}
	}

	public static void updateProxySettingForMac(Button button) {
		Map<String, Object> windowsCmd = CommandUtility.COMMANDS_FOR_MAC;
		if (button.getText().equalsIgnoreCase("Connect")) {
			button.setText("Disconnect");
//			String proxyAddress = "localhost";
//			String proxyPort = "80";
			
			String enableCmd = "networksetup -setsocksfirewallproxystate \"Wi-Fi\" on ";
			changeSetting(enableCmd);

			String changeProxyCmd = "networksetup -setsocksfirewallproxy \"Wi-Fi\" 127.0.0.1 8080 ";
			changeSetting(changeProxyCmd);
		} else {
			button.setText("Connect");
			String disableCmd = "networksetup -setsocksfirewallproxystate \"Wi-Fi\" off ";
			changeSetting(disableCmd);
		}
	}
	
	public static void updateProxySettingForLinux(Button button) {
		Map<String, Object> linuxCmd = CommandUtility.COMMANDS_FOR_LINUX;
		if (button.getText().equalsIgnoreCase("Connect")) {
			button.setText("Disconnect");
			String proxyAddress = "127.0.0.1";
			String proxyPort = "80";
			
//			String enableCmd = "networksetup -setsocksfirewallproxystate \"Wi-Fi\" on ";
//			changeSetting(enableCmd);

			String changeProxyCmd = linuxCmd.get("UpdateHttpProxy").toString();
			changeProxyCmd.replaceAll("<proxyAddress>", proxyAddress);
			changeProxyCmd.replaceAll("<proxyPort>", proxyPort);
			changeSetting(changeProxyCmd);
		} else {
			button.setText("Connect");
			String disableCmd = linuxCmd.get("DisableHttpProxy").toString();
			changeSetting(disableCmd);
		}
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

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
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
