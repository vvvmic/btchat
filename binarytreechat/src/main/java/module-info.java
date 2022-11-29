module com.example.binarytreechat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.binarytreechat to javafx.fxml;
    exports com.example.binarytreechat;
}