module com.example.rsa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.rsa to javafx.fxml;
    exports com.example.rsa.he;
    opens com.example.rsa.he to javafx.fxml;
    exports com.example.rsa.lab6;
    opens com.example.rsa.lab6 to javafx.fxml;
    exports com.example.rsa.lab7;
    opens com.example.rsa.lab7 to javafx.fxml;
}