module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jjwt.api;
    requires jbcrypt;
    requires mongo.java.driver;
    requires java.management;


    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
    exports models;
    opens models to javafx.fxml;
}


