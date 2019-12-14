package com.curtisnewbie.view;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class QueryPane extends BorderPane {

    /**
     * menu button for switching to another "view" (i.e., the view for displaying
     * content of webpages).
     * 
     * @see BrowserView
     * @see QueryPane
     */
    private Button menuBtn;

    public QueryPane() {
        this.setCenter(new QueryControlPanel());
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
