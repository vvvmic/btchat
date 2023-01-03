module com.example.binarytreechat {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.binarychat.client;
    opens com.binarychat.client to javafx.fxml;
}