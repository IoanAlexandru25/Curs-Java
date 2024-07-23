package com.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label failMessage;

    @FXML
    private Button loginButton;

    public void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User loggedInUser = MongoDBUtil.authenticate(username, password);

        if (loggedInUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("library-view.fxml"));
                Scene libraryScene = new Scene(fxmlLoader.load());

                LibraryController libraryController = fxmlLoader.getController();
                libraryController.setUser(loggedInUser);

                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(libraryScene);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            failMessage.setVisible(true);
        }
    }
}
