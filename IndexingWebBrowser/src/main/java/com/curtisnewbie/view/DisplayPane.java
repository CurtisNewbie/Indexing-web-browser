package com.curtisnewbie.view;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.image.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.web.WebView;
import javafx.scene.control.TabPane;
import java.io.InputStream;

/**
 * <p>
 * This program has two views (or cards of CardLayout) in Javafx, one is for
 * viewing the content of the webpages, and another one is for using query to
 * seaarch contents.
 * </p>
 * <p>
 * This class itself is a subclass of VBox, and it represents one of the view in
 * this program for displaying content of webpages. It internally uses
 * BorderPane to organise the subnodes.
 * </p>
 * 
 * @see BrowserView
 */
public class DisplayPane extends VBox {

    /**
     * It's a HBox that contains some of the control components/nodes in this pane
     * 
     * @see UrlInputBox
     */
    private UrlInputBox urlInputbox;

    /**
     * TabPane where each tab shows the content of a web page
     */
    private TabPane tabPane;

    /**
     * menu button for switching to another "view" (i.e., the view for query
     * searching).
     * 
     * @see BrowserView
     * @see UrlInputBox
     */
    private Menu menuBtn;

    /**
     * Instantiate DisplayPane
     * 
     * @param menuBtn the menu for this view (it can be universal for the whole
     *                program if necessary)
     * @see DisplayPane
     * @see MenuButton
     */
    public DisplayPane(Menu menuBtn) {
        BorderPane borderPane = new BorderPane();
        this.urlInputbox = new UrlInputBox();
        borderPane.setTop(urlInputbox);
        this.tabPane = new TabPane();
        borderPane.setCenter(tabPane);
        this.menuBtn = menuBtn;
        this.getChildren().addAll(new MenuBar(menuBtn), borderPane);

        // for testing
        var v = new WebView();
        v.getEngine().load("https://www.google.com");
        var tb = new Tab();
        tb.setContent(v);
        tabPane.getTabs().add(tb);

        var tf = urlInputbox.getUrlTextField();
        tf.setOnAction(e -> {
            String url = tf.getText();
            if (url != null && !url.isEmpty())
                addTab(url);
        });
    }

    /**
     * Create a new {@code Tab} that loads the webpage of the given url. It doesn
     * not create new tab if the given url is {@code NULL} or of a length of 0.
     * However, if the given url is invalid (i.e., doesn't exist), it will simply
     * create a empty tab with no content in it.
     * 
     * @param url a URL String
     * 
     */
    public void addTab(String url) {
        if (url != null && !url.isEmpty()) {
            Tab tab = new Tab();
            var view = new WebView();
            view.getEngine().load(url);
            tab.setContent(view);
            tabPane.getTabs().add(tab);
        }
    }

    /**
     * Get the UrlInputBox which contains the TextField for entering URL, and the
     * Buttons for going back and forth between the previously viewed web pages.
     * 
     * @return an object of UrlInputBox
     * @see UrlInputBox
     */
    public UrlInputBox getUrlInputBox() {
        return this.urlInputbox;
    }

    /**
     * Get Menu
     * 
     * @return Menu obj
     */
    public Menu getMenuBtn() {
        return this.menuBtn;
    }

}

/**
 * HBox that contains the TextField for entering URL, and the Buttons for going
 * back and forth between the previously viewed web pages.
 */
class UrlInputBox extends HBox {

    /** Path to the icon for backTrackBtn buttn */
    private final String PATH_TO_BACKICON = "img/arrow_back.png";

    /** Path to the icon for forwardBtn buttn */
    private final String PATH_TO_FORWICON = "img/arrow_forward.png";

    /** Maximum height of icon images in the buttons */
    private final double MAX_IMG_HEIGHT = 20.0;

    /** TextField for entering URL */
    private TextField urlTextField;

    /** Button for going back to the previous webpage */
    private Button backTrackBtn;

    /** Button for going forward to the recent viewed webpage */
    private Button forwardBtn;

    public UrlInputBox() {
        // load icon images for buttons
        ClassLoader loader = getClass().getClassLoader();
        InputStream backIn = loader.getResourceAsStream(PATH_TO_BACKICON);
        if (backIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_BACKICON + "\"");

        InputStream forwIn = loader.getResourceAsStream(PATH_TO_FORWICON);
        if (forwIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_FORWICON + "\"");

        // initialise buttons and textfield
        ImageView backIcon = new ImageView(new Image(backIn));
        backIcon.setFitHeight(MAX_IMG_HEIGHT);
        backIcon.setFitWidth(MAX_IMG_HEIGHT);
        ImageView forwIcon = new ImageView(new Image(forwIn));
        forwIcon.setFitWidth(MAX_IMG_HEIGHT);
        forwIcon.setFitHeight(MAX_IMG_HEIGHT);

        backTrackBtn = new Button(null, backIcon);
        forwardBtn = new Button(null, forwIcon);
        urlTextField = new TextField();
        // the actual height for the TextField should be MAX_IMG_HEIGHT + 10, since in
        // the buttons, additional padding within buttons are added
        urlTextField.setMinHeight(MAX_IMG_HEIGHT + 10);
        this.getChildren().addAll(backTrackBtn, forwardBtn, urlTextField);
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
}
