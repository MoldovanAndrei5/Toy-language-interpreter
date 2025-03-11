module com.example.test2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jdk.jdi;


    opens com.example.test2 to javafx.fxml;
    exports com.example.test2;
}