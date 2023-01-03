module com.example.binarytreechat {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.binarychat.client;
    opens com.binarychat.client to javafx.fxml;
    exports com.binarychat.client.controller;
    opens com.binarychat.client.controller to javafx.fxml;
}