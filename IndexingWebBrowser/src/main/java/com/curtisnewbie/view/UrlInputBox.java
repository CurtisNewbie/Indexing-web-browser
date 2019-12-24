package com.curtisnewbie.view;

import java.io.InputStream;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * HBox that contains the TextField for entering URL, and the Buttons for going
 * back and forth between the previously viewed web pages.
 */
public class UrlInputBox extends HBox {

    /** Path to the icon for backTrackBtn buttn */
    private final String PATH_TO_BACKICON = "img/arrow_back.png";

    /** Path to the icon for forwardBtn buttn */
    private final String PATH_TO_FORWICON = "img/arrow_forward.png";

    /** Path to the icon for addTabBtn button */
    private final String PATH_TO_ADDICON = "img/plus_icon.png";

    /** Maximum height of icon images in the buttons */
    private final double MAX_IMG_HEIGHT = 20.0;

    /** TextField for entering URL */
    private TextField urlTextField;

    /** Button for going back to the previous webpage */
    private Button backTrackBtn;

    /** Button for going forward to the recent viewed webpage */
    private Button forwardBtn;

    /** Button for adding a new tab */
    private Button addTabBtn;

    public UrlInputBox() {
        // load icon images for buttons
        ClassLoader loader = getClass().getClassLoader();
        InputStream backIn = loader.getResourceAsStream(PATH_TO_BACKICON);
        if (backIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_BACKICON + "\"");

        InputStream forwIn = loader.getResourceAsStream(PATH_TO_FORWICON);
        if (forwIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_FORWICON + "\"");
        InputStream addIn = loader.getResourceAsStream(PATH_TO_ADDICON);
        if (addIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_ADDICON + "\"");

        // initialise buttons and textfield
        ImageView backIcon = new ImageView(new Image(backIn));
        backIcon.setFitHeight(MAX_IMG_HEIGHT);
        backIcon.setFitWidth(MAX_IMG_HEIGHT);
        ImageView forwIcon = new ImageView(new Image(forwIn));
        forwIcon.setFitWidth(MAX_IMG_HEIGHT);
        forwIcon.setFitHeight(MAX_IMG_HEIGHT);
        ImageView addIcon = new ImageView(new Image(addIn));
        addIcon.setFitWidth(MAX_IMG_HEIGHT);
        addIcon.setFitHeight(MAX_IMG_HEIGHT);

        backTrackBtn = new Button(null, backIcon);
        forwardBtn = new Button(null, forwIcon);
        addTabBtn = new Button(null, addIcon);
        urlTextField = new TextField();
        // the actual height for the TextField should be MAX_IMG_HEIGHT + 10, since in
        // the buttons, additional padding within buttons are added
        urlTextField.setMinHeight(MAX_IMG_HEIGHT + 10);
        this.getChildren().addAll(backTrackBtn, forwardBtn, urlTextField, addTabBtn);
        HBox.setHgrow(urlTextField, Priority.ALWAYS);
    }

    public TextField getUrlTextField() {
        return this.urlTextField;
    }

    public Button getBackTrackBtn() {
        return this.backTrackBtn;
    }

    public Button getForwardBtn() {
        return this.forwardBtn;
    }

    public Button getAddTabBtn() {
        return this.addTabBtn;
    }
}