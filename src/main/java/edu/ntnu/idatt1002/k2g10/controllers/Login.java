package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.ntnu.idatt1002.k2g10.App;
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
        App.getLogger().info("Login button clicked");
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
            App.getLogger().info("Enter pressed.");
        }
    }
}
