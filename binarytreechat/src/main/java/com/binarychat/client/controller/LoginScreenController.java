package com.binarychat.client.controller;

import com.binarychat.IPAddress.checkIPAddress;
import com.binarychat.IPAddress.checkPort;
import com.binarychat.client.StartApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginScreenController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField ipAddressField;

    @FXML
    private TextField portField;

    @FXML
    private Button connectButton;

    @FXML
    private Button homeButton;

    private static String username;
    private static String ipAddress;
    private static int port;

    @FXML
    protected void onConnect(ActionEvent event) throws IOException {
        Stage stage = (Stage) connectButton.getScene().getWindow();

        username = usernameField.getText();
        ipAddress = ipAddressField.getText();
        port = Integer.parseInt(portField.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MessageScreen.fxml"));
        Parent root = fxmlLoader.load();
        MessageScreenController messageScreenController = fxmlLoader.getController();
        messageScreenController.setUsernameField(this.username);
        Scene scene = new Scene(root, 720, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("BinaryChat");
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

    public static int getPort(){return port;}

    @FXML
    protected void setConnectButtonEnable(KeyEvent event) {
        if(!usernameField.getText().isEmpty() && !portField.getText().isEmpty() && checkIPAddress.validateIP(ipAddressField.getText()) && checkPort.validatePort(Integer.parseInt(portField.getText()))) {
            connectButton.setDisable(false);
        }else {
            connectButton.setDisable(true);
        }

    }
/*
    @FXML
    protected void onUsernameChanged(KeyEvent event) {
        if(!usernameField.getText().isEmpty() && !ipAddressField.getText().isEmpty() && !portField.getText().isEmpty())
            connectButton.setDisable(false);
    }

    @FXML
    protected void onIpAddressChanged(KeyEvent event) {
        if(!usernameField.getText().isEmpty() && !portField.getText().isEmpty() && checkIPAddress.validateIP(ipAddressField.getText()))
            connectButton.setDisable(false);
    }

    @FXML
    protected void onPortChanged(KeyEvent event) {
        if(!usernameField.getText().isEmpty() && !ipAddressField.getText().isEmpty() && checkPort.validatePort(Integer.parseInt(portField.getText())))
            connectButton.setDisable(false);
    }

 */

}