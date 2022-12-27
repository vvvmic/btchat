package com.binarychat.client;

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

    private String username;
    private String ipAddress;
    private int port;

    @FXML
    protected void onConnect(ActionEvent event) throws IOException {
        Stage stage = (Stage) connectButton.getScene().getWindow();

        username = usernameField.getText();
        ipAddress = ipAddressField.getText();
        port = Integer.parseInt(portField.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MessageScreen.fxml"));
        Parent root = fxmlLoader.load();
        MessageScreenController messageScreenController = fxmlLoader.getController();
        messageScreenController.setUsername(this.getUsername());
        Scene scene = new Scene(root, 720, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("BinaryChat");
        LoginScreen loginScreen = new LoginScreen(getUsername(),getIpAddress(),getPort());
        stage.setScene(scene);
    }

    public String getUsername(){return username;}

    public String getIpAddress(){return ipAddress;}

    public int getPort(){return port;}

    @FXML
    protected void onUsernameChanged(KeyEvent event) {
        if(!usernameField.getText().isEmpty() && !ipAddressField.getText().isEmpty() && !portField.getText().isEmpty())
            connectButton.setDisable(false);
    }

    @FXML
    protected void onIpAddressChanged(KeyEvent event) {
        if(!usernameField.getText().isEmpty() && !ipAddressField.getText().isEmpty() && !portField.getText().isEmpty())
            connectButton.setDisable(false);
    }

    @FXML
    protected void onPortChanged(KeyEvent event) {
        if(!usernameField.getText().isEmpty() && !ipAddressField.getText().isEmpty() && !portField.getText().isEmpty())
            connectButton.setDisable(false);
    }
}
