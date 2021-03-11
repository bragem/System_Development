package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Login {
    public JFXTextField usernameField;
    public JFXPasswordField passwordField;

    /**
     * Event handler for clicks on the "login" button.
     *
     * @author trthingnes
     */
    public void buttonClickLogin() {
        System.out.println("Login clicked.");
        System.out.printf("Got username '%s' and password '%s'.%n", usernameField.getText(), passwordField.getText());
    }

    /**
     * Event handler for key presses on the "login" button and frame.
     *
     * @param event
     *            Event information
     * 
     * @author trthingnes
     */
    public void keyPressLogin(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            System.out.println("Enter pressed.");
            System.out.printf("Got username '%s' and password '%s'.%n", usernameField.getText(),
                    passwordField.getText());
        }
    }
}
