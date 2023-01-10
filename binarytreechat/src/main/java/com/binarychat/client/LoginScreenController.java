package com.binarychat.client;

import com.binarychat.IPAddress.checkIPAddress;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginScreenController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField ipAddressField;

    @FXML
    private TextField chatWithField;

    @FXML
    private Button connectButton;

    @FXML
    private Button homeButton;

    private static String username;
    private static String ipAddress;

    private static String chatWith ="default";

    private static boolean isBroadcast;

    @FXML
    protected void onConnect(ActionEvent event) throws IOException {
        Stage stage = (Stage) connectButton.getScene().getWindow();

        username = usernameField.getText();
        ipAddress = ipAddressField.getText();
        if(!chatWithField.getText().isEmpty()){
            chatWith = chatWithField.getText();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MessageScreen.fxml"));
        Parent root = fxmlLoader.load();
        MessageScreenController messageScreenController = fxmlLoader.getController();
        messageScreenController.setUsernameField(this.username);
        Scene scene = new Scene(root, 720, 720);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("BinaryChat");
        stage.getIcons().add(new Image("file:icon.png"));
        stage.setScene(scene);
    }

    @FXML
    protected void backToHomeScreen(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("StartScreen.fxml"));

        Stage stage = (Stage) homeButton.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 720, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("BinaryChat");
        stage.setScene(scene);
    }

    public static String getUsername(){return username;}

    public static String getIpAddress(){return ipAddress;}

    public static String getChatWith(){return chatWith;}

    @FXML
    protected void setConnectButtonEnable(KeyEvent event) {
        if(!usernameField.getText().isEmpty()  && checkIPAddress.validateIP(ipAddressField.getText())) {
            connectButton.setDisable(false);
        }else {
            connectButton.setDisable(true);
        }

    }

    public static void setIsBroadcast(boolean broadcast){isBroadcast = broadcast;}

    public static boolean getIsBroadcast(){return isBroadcast;}
}
