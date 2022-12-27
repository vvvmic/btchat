package com.binarychat.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MessageScreenController {
    @FXML
    private TextArea message;

    @FXML
    private Button sendButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Text username;

    @FXML
    public void onMessage(KeyEvent event) {
        if(!message.getText().isEmpty())
            sendButton.setDisable(false);
    }

    @FXML
    protected void onLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("LoginScreen.fxml"));

        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 720, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("BinaryChat");
        stage.setScene(scene);
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    protected void onSend(ActionEvent event) {
        // TODO: 26.12.2022// constructor MessageScreen aufrufen und ip,name, port übergeben
    }

}
