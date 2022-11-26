package com.example.bt_chattool;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientController {
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
