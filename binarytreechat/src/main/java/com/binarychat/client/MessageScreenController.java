package com.binarychat.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MessageScreenController implements Initializable{


    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox vBox;

    @FXML
    private TextArea message;

    @FXML
    private Button sendButton;

    @FXML
    private Text usernameField;

    private Client client;

    @FXML
    private Parent root;

    @FXML
    public void onMessage(KeyEvent event) {
        sendButton.setDisable(message.getText().isEmpty());
    }

    @FXML
    protected void onLogout(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);//pop up window appears when the logout button is selected
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to log out!");
        alert.setContentText("Do you really want to log out?");

        if(alert.showAndWait().get() == ButtonType.OK){
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("LoginScreen.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 720, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("BinaryChat");
        stage.getIcons().add(new Image("file:icon.png"));
        client.logoutfromServer(this.client);
        stage.setScene(scene);}
    }

    @FXML
    protected void saveListToTextFile(ActionEvent event) {
        printMessageList.printMessageList(client.getMessageList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client = new Client(LoginScreenController.getIpAddress());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNumber, Number newNumber) {
                scroll.setVvalue((Double) newNumber);
            }
        }); //a new value for the scroll pane, in order to automatically scroll to the bottom

        client.receiveMessageFromServer(vBox);

        sendButton.setOnAction(new EventHandler<ActionEvent>() { //adding functionality to the bottom
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = (message.getText());
                String usernameToSend =(usernameField.getText());// NICKNAMES will be attached (▰˘◡˘▰)

                ///////////////////////////TIMESTAMP//////////////////////////////////////
                String timeStamp = String.valueOf(LocalDateTime.now());

                HBox hboxTimestamp = new HBox();
                hboxTimestamp.setAlignment(Pos.CENTER_RIGHT);
                hboxTimestamp.setPadding((new Insets(7,15,0,5)));

                Text timestampForHbox = new Text(timeStamp);
                TextFlow textFlowTimestamp = new TextFlow(timestampForHbox);
                textFlowTimestamp.setId("textFlowTimestamp");
                textFlowTimestamp.setOpacity(0.5);
                textFlowTimestamp.setStyle("-fx-font-size: 10px;");
                textFlowTimestamp.setPadding(new Insets(7,5,0,5));
                ///////////////////////////TIMESTAMP/////////////////////////////////////

                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_RIGHT); //Ausrichtung von Hbox
                hbox.setPadding(new Insets(10,15,10,5));

                Text text = new Text(messageToSend);
                TextFlow textFlow = new TextFlow(text); //wrapping the text, reach TextField
                textFlow.setId("textflow");

                textFlow.setStyle("-fx-background-color: #27AE60;" + "-fx-background-radius: 25;");

                textFlow.setPadding(new Insets(10,15,10,15));

                ///////////////////////NICKNAME/////////////////////////////////////
                HBox hboxUsername = new HBox();
                hboxUsername.setAlignment(Pos.CENTER_RIGHT);
                hboxUsername.setPadding((new Insets(7,15,0,5)));

                Text username = new Text(usernameToSend);
                TextFlow textFlowUsername = new TextFlow(username);
                textFlowUsername.setId("textFlowUsername");
                textFlowUsername.setOpacity(0.5);
                textFlowUsername.setStyle("-fx-font-size: 11px;");
                textFlowUsername.setPadding(new Insets(7,5,0,5));
                /////////////////////////NICKNAME////////////////////////////////////

                hboxTimestamp.getChildren().add(textFlowTimestamp);
                hboxUsername.getChildren().add(textFlowUsername);

                vBox.getChildren().add(hboxUsername);
                vBox.getChildren().add(hboxTimestamp);
                hbox.getChildren().add(textFlow); //adding Textflow to horizontal box
                vBox.getChildren().add(hbox); //dding horizontal box to vertical box


                client.sendMessageToServer(messageToSend);
                message.clear();
            }
        });
    }

    public static void addLabel(String messageFromServer, VBox vBox){ //GUI for receiving of userMessageTypes
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(10,5,10,15));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setId("textflow2");


        textFlow.setStyle(
                "-fx-text-fill: #000000;" + "-fx-background-color: #DDDDDD;" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(10,15,10,15));
        hBox.getChildren().add(textFlow); //adding to Hbox


        Platform.runLater(new Runnable() { //we could not be using another Thread, but this helps us with this problem
            @Override
            public void run() {
                vBox.getChildren().add(hBox);

            }
        });
    }

    public void setUsernameField(String usernameField) {
        this.usernameField.setText(usernameField);
    }
}
