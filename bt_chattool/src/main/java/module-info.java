module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bt_chattool to javafx.fxml;
    exports com.example.bt_chattool;
}