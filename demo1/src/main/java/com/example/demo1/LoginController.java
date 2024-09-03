package com.example.demo1;

import exceptions.UserAlreadyExistsException;
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
    private Label failLoginMessage;

    @FXML
    private Label failRegisterMessage;

    @FXML
    private Label userAlreadyExists;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

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
        }
        else {
            failRegisterMessage.setVisible(false);
            userAlreadyExists.setVisible(false);
            failLoginMessage.setVisible(true);
        }
    }

    public void handleRegisterButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User registeredUser = null;
        try {
            registeredUser = MongoDBUtil.registration(username, password);
        } catch (UserAlreadyExistsException e) {
            failRegisterMessage.setVisible(false);
            failLoginMessage.setVisible(false);
            userAlreadyExists.setVisible(true);
            e.printStackTrace();
            return;
        }

        if (registeredUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("library-view.fxml"));
                Scene libraryScene = new Scene(fxmlLoader.load());

                LibraryController libraryController = fxmlLoader.getController();
                libraryController.setUser(registeredUser);

                Stage stage = (Stage) registerButton.getScene().getWindow();
                stage.setScene(libraryScene);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            failLoginMessage.setVisible(false);
            userAlreadyExists.setVisible(false);
            failRegisterMessage.setVisible(true);
        }

    }
}
