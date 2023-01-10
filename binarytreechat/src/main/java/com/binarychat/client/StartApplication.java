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

    public void closeWindow(Stage stage){ //pop up window appears when the application is to be closed
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You are about to exit!");
        alert.setContentText("Do you really want to exit?");

        if(alert.showAndWait().get() == ButtonType.OK){
            Runtime.getRuntime().exit(0);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
