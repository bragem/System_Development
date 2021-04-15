package edu.ntnu.idatt1002.k2g10.factory;

import edu.ntnu.idatt1002.k2g10.App;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Generates {@link Dialog} objects.
 * @author trthingnes
 */
public class DialogFactory {
    /**
     * Get a dialog with the given title, content and buttons.
     *
     * @param title Window title to display.
     * @param content Content to display in window.
     * @param buttonTypes Buttons to display in window.
     * @return Dialog.
     */
    public static Dialog<ButtonType> getDialog(String title, String content, ButtonType[] buttonTypes) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setContentText(content);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypes);

        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(App.class.getResource("/img/icon.png").toString()));

        return dialog;
    }

    /**
     * Gets a dialog with the given title, content and OK button.
     * @param title Window title to display.
     * @param content Content to display in window.
     * @return Dialog.
     * @see #getDialog(String, String, ButtonType[])
     */
    public static Dialog<ButtonType> getOKDialog(String title, String content) {
        return getDialog(title, content, new ButtonType[] { ButtonType.OK });
    }

    /**
     * Gets a dialog with the given title, content and YES and NO buttons.
     * @param title Window title to display.
     * @param content Content to display in window.
     * @return Dialog.
     * @see #getDialog(String, String, ButtonType[])
     */
    public static Dialog<ButtonType> getYesNoDialog(String title, String content) {
        return getDialog(title, content, new ButtonType[] { ButtonType.YES, ButtonType.NO });
    }
}
