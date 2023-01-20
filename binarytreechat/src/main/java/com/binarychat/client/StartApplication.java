package com.binarychat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("StartScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("BinaryChat");
        stage.getIcons().add(new Image("file:icon.png"));
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
         closeWindow(stage);
        });
    }

    /**
     * if an attempt is made to close the application window, a pop up window appears to prevent accidental closure
     */
    public void closeWindow(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You are about to exit!");
        alert.setContentText("Click OK if you really want to exit.");

        if(alert.showAndWait().get() == ButtonType.OK){
            Runtime.getRuntime().exit(0);
        }
    }

    /**
     * main starts the client application
     */
    public static void main(String[] args) {
        launch();
    }
}
