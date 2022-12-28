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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
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
    private Button logoutButton;

    @FXML
    private Text usernameField;

    private String ipAddress;
    private String username;
    private int port;

    private Client client;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            LoginScreenController loginScreenController = new LoginScreenController();
            //Todo: client eingebenene IP und Port übergeben
            client = new Client(new Socket("192.168.2.81", 5000));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNumber, Number newNumber) {
                scroll.setVvalue((Double) newNumber);
            }
        });

        client.receiveMessageFromServer(vBox);

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = (username + message.getText());
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_RIGHT); //TODO: Ausrichtung, wie machen wir das
                hbox.setPadding(new Insets(5,5,5,10));

                Text text = new Text(messageToSend);
                TextFlow textFlow = new TextFlow(text);

                textFlow.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-background-color: #27AE60;" + "-fx-background-radius: 25;");

                textFlow.setPadding(new Insets(5,10,5,10));

                hbox.getChildren().add(textFlow);
                vBox.getChildren().add(hbox);

                client.sendMessageToServer(messageToSend);
                message.clear();

            }
        });

    }

    public static void addLabel(String messageFromServer, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235);" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    public void setUsernameField(String usernameField) {
        this.usernameField.setText(usernameField);
    }

    public void setUsername(String usernameField) { this.username = usernameField; }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
