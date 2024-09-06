module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jjwt.api;
    requires jbcrypt;
    requires mongo.java.driver;
    requires java.management;


    opens controllers to javafx.fxml;
    exports controllers;
    exports models;
    opens models to javafx.fxml;
    exports main;
    opens main to javafx.fxml;
    exports utils;
    opens utils to javafx.fxml;
}


