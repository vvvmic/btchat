package com.binarychat.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class StartScreenController {

    @FXML
    private Button chatRoomButton;

    @FXML
    private Button singleChatButton;

    /**
     * @param event pressing the chatroom button starts the event
     * for future messages it is stored that these should be broadcast
     */
    @FXML
    protected void onConnectChatRoom(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("LoginScreen.fxml"));
        LoginScreenController.setIsBroadcast(true);
        Stage stage = (Stage) chatRoomButton.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 720, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("BinaryChat");
        stage.setScene(scene);
    }

    /**
     * @param event pressing the singe chat button starts the event
     * for future messages it is stored that these should be not broadcast
     */
    @FXML
    protected void onConnectSingleChat(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("LoginScreen.fxml"));
        LoginScreenController.setIsBroadcast(false);
        Stage stage = (Stage) singleChatButton.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 720, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("BinaryChat");
        stage.setScene(scene);
    }
}
