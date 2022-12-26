package com.binarychat.client;

import javafx.event.ActionEvent;
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
    public void onMessage(KeyEvent event) {
        if(!message.getText().isEmpty())
            send.setDisable(false);
    }

    protected void onSend(ActionEvent event) {
        // TODO: 26.12.2022// constructor MessageScreen aufrufen und ip,name, port übergeben
    }

}
