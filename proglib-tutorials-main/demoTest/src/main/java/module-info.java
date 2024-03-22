module com.example.demotest {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demotest to javafx.fxml;
    exports com.example.demotest;
}