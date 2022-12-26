package com.binarychat.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

public class MessageScreenController {
    @FXML
    private TextArea message;

    @FXML
    private Button send;

    @FXML
    public void onSend(KeyEvent event) {
        send.setDisable(message.getText().isEmpty());
    }
}
