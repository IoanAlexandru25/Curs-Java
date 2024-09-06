package controllers;

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
import proxy.AbstractAuthenticationService;
import proxy.AuthenticationServiceProxy;
import utils.DeviceUtil;
import utils.MongoDBUtil;

import java.io.IOException;

public class LoginController {
    private MongoDBUtil mongoDBUtil = new MongoDBUtil();

    private AbstractAuthenticationService abstractAuthenticationService;

    private String deviceId;

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

    public LoginController() {
        this.abstractAuthenticationService = new AuthenticationServiceProxy(this, mongoDBUtil);
        this.deviceId = DeviceUtil.getDeviceId();
    }

    public void setFailLoginMessage(String failMessage) {
        failLoginMessage.setText(failMessage);
    }

    public void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User loggedInUser = abstractAuthenticationService.authentication(username, password, deviceId);

        if (loggedInUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/library-view.fxml"));
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
            registeredUser = mongoDBUtil.registration(username, password);
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
