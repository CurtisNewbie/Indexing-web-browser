package com.curtisnewbie.view;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class QueryPane extends GridPane {

    /** Control Panel for entering queries and starting query searching */
    private QueryControlPanel queryControlPanel;

    /** Panel for displaying browsing history */
    private HistoryPanel historyPanel;

    /**
     * Panel for displaying results of queries (incl. content words and keywords)
     */
    private QueryResultPanel queryResultPanel;

    /**
     * Instantiate QueryPane
     * 
     * @param menuBtn the menu for this view (it can be universal for the whole
     *                program if necessary)
     */
    public QueryPane() {

        // create panels that display histories, query controls, queries results
        queryControlPanel = new QueryControlPanel();
        historyPanel = new HistoryPanel();
        queryResultPanel = new QueryResultPanel();

        this.add(queryResultPanel, 0, 0);
        /*
         * -----------------------------------------------------------------------------
         * 
         * 
         * Second columns will be used for additional functionality
         * 
         * -----------------------------------------------------------------------------
         * this.add(htmlAnalysis?????, 1, 0);
         */
        this.add(new VBox(queryControlPanel, historyPanel), 2, 0);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100 / 3);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(100 / 3);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(100 / 3);
        this.getColumnConstraints().addAll(col1, col2, col3);
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
