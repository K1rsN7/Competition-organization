module com.example.demdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.cursach to javafx.fxml;
    exports com.example.cursach;
}