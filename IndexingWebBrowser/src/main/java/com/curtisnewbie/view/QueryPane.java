package com.curtisnewbie.view;

import javafx.scene.control.ScrollPane;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.text.Text;
import java.io.*;

public class QueryPane extends BorderPane {

    /** Maximum Height of Image */
    private double MAX_IMG_HEIGHT = 20;

    /** Path to the icon for menuBtn buttn */
    private final String PATH_TO_MENUICON = "img/menu_icon.png";

    /**
     * menu button for switching to another "view" (i.e., the view for displaying
     * content of webpages).
     * 
     * @see BrowserView
     * @see QueryPane
     */
    private Button menuBtn;

    /** Control Panel for entering queries and starting query searching */
    private QueryControlPanel queryControlPanel;

    /** Panel for displaying browsing history */
    private HistoryPanel historyPanel;

    public QueryPane() {
        // load image icon for the menuBtn button
        ClassLoader loader = this.getClass().getClassLoader();
        InputStream menuIn = loader.getResourceAsStream(PATH_TO_MENUICON);
        if (menuIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_MENUICON + "\"");

        ImageView menuIcon = new ImageView(new Image(menuIn));
        menuIcon.setFitWidth(MAX_IMG_HEIGHT);
        menuIcon.setFitHeight(MAX_IMG_HEIGHT);
        menuBtn = new Button(null, menuIcon);
        this.setTop(menuBtn);
        BorderPane.setAlignment(menuBtn, Pos.CENTER_RIGHT);

        // for testing only
        this.setCenter(new HistoryPanel());
    }
}

/**
 * VBox that displays the history of the browsed webpages. Each webpage (URL
 * links) shown in this VBox is a clickable button.
 */
class HistoryPanel extends VBox {

    private final String title = "History:  ";
    private VBox historiesVBox;
    private ObservableList<Node> observableList;

    public HistoryPanel() {
        historiesVBox = new VBox();
        observableList = historiesVBox.getChildren();
        this.getChildren().addAll(new Text(title), new ScrollPane(historiesVBox));
        /*
         * --------------------------------------------------------------------------
         * 
         * for testing
         * 
         * --------------------------------------------------------------------------
         */
        var testList = new ArrayList<String>();
        for (int i = 0; i < 30; i++)
            testList.add("https://www.google.com");
        refresh(testList);
    }

    /**
     * Refresh this history panel by clearing the original {@code ObservableList}
     * and reloading the strings (each in a {@code Button}) into this {@code
     * ObservableList}
     * 
     * @param urls A List of url strings
     * 
     */
    public void refresh(List<String> urls) {
        if (urls != null && urls.size() > 0) {
            observableList.clear();
            for (String url : urls) {
                var btn = new Button(url);
                observableList.add(btn);
            }
        }
    }
}

/**
 * VBox that consits of controls used to control searching web content (content
 * words and keywords). E.g., TextField for entering infix queries and prefix
 * queries.
 */
class QueryControlPanel extends VBox {

    private final int HBOX_SPACING = 5;
    private final int MARGIN_TOP = 5;
    private final int MARGIN_RIGHT = 5;
    private final int MARGIN_BOTTOM = 5;
    private final int MARGIN_LEFT = 5;

    private Label infixLabel;
    private Label prefixLabel;
    private TextField infixTf;
    private TextField prefixTf;

    public QueryControlPanel() {
        infixLabel = new Label("Infix Query: ");
        prefixLabel = new Label("Prefix Query: ");
        infixTf = new TextField();
        prefixTf = new TextField();

        HBox boxTop = new HBox(infixLabel, infixTf);
        boxTop.setSpacing(HBOX_SPACING);
        HBox.setHgrow(infixTf, Priority.ALWAYS);
        VBox.setMargin(boxTop, new Insets(MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM, MARGIN_LEFT));

        HBox boxBottom = new HBox(prefixLabel, prefixTf);
        boxBottom.setSpacing(HBOX_SPACING);
        HBox.setHgrow(prefixTf, Priority.ALWAYS);
        VBox.setMargin(boxBottom, new Insets(MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM, MARGIN_LEFT));

        this.getChildren().addAll(boxTop, boxBottom);
    }

}
