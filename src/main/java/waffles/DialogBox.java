package waffles;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents one chat message and its speaker avatar.
 */
public class DialogBox extends HBox {
    /** The message text injected from the FXML view. */
    @FXML
    private Label dialog;

    /** The speaker avatar injected from the FXML view. */
    @FXML
    private ImageView displayPicture;

    /**
     * Creates a dialog box from the reusable FXML view.
     *
     * @param text the message to display
     * @param image the speaker avatar
     */
    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/DialogBox.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load the dialog box view.", exception);
        }
        assert dialog != null : "DialogBox.fxml must inject the dialog label";
        assert displayPicture != null : "DialogBox.fxml must inject the speaker image";
        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /** Moves the avatar to the left for a chatbot response. */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
    }

    /** Creates a dialog box aligned as a user message. */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /** Creates a dialog box aligned as a Waffles response. */
    public static DialogBox getWafflesDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        return dialogBox;
    }
}
