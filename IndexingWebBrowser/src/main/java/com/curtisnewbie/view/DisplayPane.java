package com.curtisnewbie.view;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.image.*;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;

import java.io.InputStream;

/**
 * <p>
 * This program has two views (or cards of CardLayout) in Javafx, one is for
 * viewing the content of the webpages, and another one is for using query to
 * seaarch contents.
 * </p>
 * <p>
 * This class itself is a subclass of BorderPane, and it represents one of the
 * view in this program for displaying content of webpages.
 * </p>
 * 
 * @see BrowserView
 */
public class DisplayPane extends BorderPane {

    private UrlInputBox urlInputbox;
    private WebView webView;

    public DisplayPane() {
        this.urlInputbox = new UrlInputBox();
        this.webView = new WebView();
        this.setTop(urlInputbox);
        this.setCenter(webView);
    }

    public UrlInputBox getUrlInputBox() {
        return this.urlInputbox;
    }

    public WebView getWebView() {
        return this.webView;
    }
}

/**
 * HBox that contains the TextField for entering URL, and the Buttons for going
 * back and forth between the previously viewed web pages.
 */
class UrlInputBox extends HBox {

    private final String PATH_TO_BACKICON = "img/arrow_back.png";
    private final String PATH_TO_FORWICON = "img/arrow_forward.png";
    private final String PATH_TO_MENUICON = "img/menu_icon.png";

    private final double MAX_IMG_HEIGHT = 20.0;

    private TextField urlTextField;
    private Button backTrackBtn;
    private Button forwardBtn;
    private Button menuBtn;

    public UrlInputBox() {
        // load icon images for buttons
        ClassLoader loader = getClass().getClassLoader();
        InputStream backIn = loader.getResourceAsStream(PATH_TO_BACKICON);
        if (backIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_BACKICON + "\"");

        InputStream forwIn = loader.getResourceAsStream(PATH_TO_FORWICON);
        if (forwIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_FORWICON + "\"");

        InputStream menuIn = loader.getResourceAsStream(PATH_TO_MENUICON);
        if (menuIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_MENUICON + "\"");

        // initialise buttons and textfield
        ImageView backIcon = new ImageView(new Image(backIn));
        backIcon.setFitHeight(MAX_IMG_HEIGHT);
        backIcon.setFitWidth(MAX_IMG_HEIGHT);
        ImageView forwIcon = new ImageView(new Image(forwIn));
        forwIcon.setFitWidth(MAX_IMG_HEIGHT);
        forwIcon.setFitHeight(MAX_IMG_HEIGHT);
        ImageView menuIcon = new ImageView(new Image(menuIn));
        menuIcon.setFitWidth(MAX_IMG_HEIGHT);
        menuIcon.setFitHeight(MAX_IMG_HEIGHT);

        backTrackBtn = new Button(null, backIcon);
        forwardBtn = new Button(null, forwIcon);
        menuBtn = new Button(null, menuIcon);
        urlTextField = new TextField();
        // the actual height for the TextField should be MAX_IMG_HEIGHT + 10, since in
        // the buttons, additional padding within buttons are added
        urlTextField.setMinHeight(MAX_IMG_HEIGHT + 10);

        this.getChildren().addAll(backTrackBtn, forwardBtn, urlTextField, menuBtn);
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

    public Button getMenuBtn() {
        return this.menuBtn;
    }

}
