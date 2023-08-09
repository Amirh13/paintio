module com.example.paintio {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.paintio to javafx.fxml;
    exports com.example.paintio;
}