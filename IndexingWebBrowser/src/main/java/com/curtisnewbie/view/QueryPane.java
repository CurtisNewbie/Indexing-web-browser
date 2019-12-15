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

    /**
     * Panel for displaying results of queries (incl. content words and keywords)
     */
    private QueryResultPanel queryResultPanel;

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

        // create panels that display histories, query controls, queries results
        queryControlPanel = new QueryControlPanel();
        historyPanel = new HistoryPanel();
        queryResultPanel = new QueryResultPanel();

        // this grid pane is used to organise the queryControlPanel, historyPanel and
        // queryResultPanel
        GridPane panelGridOrganiser = new GridPane();
        panelGridOrganiser.add(queryResultPanel, 0, 0);
        /*
         * -----------------------------------------------------------------------------
         * 
         * 
         * Second columns will be used for additional functionality
         * 
         * -----------------------------------------------------------------------------
         * panelGridOrganiser.add(htmlAnalysis?????, 1, 0);
         */
        panelGridOrganiser.add(new VBox(queryControlPanel, historyPanel), 2, 0);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100 / 3);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(100 / 3);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(100 / 3);
        panelGridOrganiser.getColumnConstraints().addAll(col1, col2, col3);
        this.setCenter(panelGridOrganiser);
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

/**
 * A GridPane used to display the results of query (content words and keywords).
 * The results consists a list of url Strings, and each url string is in a
 * clickable Button.
 */
class QueryResultPanel extends GridPane {

    private final String title = "Query Results: ";
    private final String cwTitle = "Content Words: ";
    private final String kwTitle = "Keywords: ";

    /** Display content word results */
    private VBox cwRes;

    /** Display keywords results */
    private VBox kwRes;

    /** {@code ObservableList} for {@code VBox cwRes} */
    private ObservableList<Node> cwResList;

    /** {@code ObservableList} for {@code VBox kwRes} */
    private ObservableList<Node> kwResList;

    public QueryResultPanel() {
        cwRes = new VBox();
        kwRes = new VBox();
        cwResList = cwRes.getChildren();
        kwResList = kwRes.getChildren();

        // create and add two VBox to seperate the results for content words and kewords
        var topBox = new VBox();
        topBox.getChildren().addAll(new Text(title), new Text(cwTitle), new ScrollPane(cwRes));
        var bottomBox = new VBox();
        bottomBox.getChildren().addAll(new Text(kwTitle), new ScrollPane(kwRes));
        this.add(topBox, 0, 0);
        this.add(bottomBox, 0, 1);

        // set constraints for rows and columns
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);
        this.getRowConstraints().addAll(row1, row2);
        this.getColumnConstraints().add(col1);

        /*
         * --------------------------------------------------------------------------
         * 
         * for testing
         * 
         * --------------------------------------------------------------------------
         */
        var testList = new ArrayList<String>();
        for (int i = 0; i < 40; i++)
            testList.add("https://www.google.com");
        refreshCwRes(testList);
        refreshKwRes(testList);
    }

    /**
     * Refresh content word query results by clearing the original
     * {@code ObservableList<Node> cwResList} and reloading the strings (each in a
     * {@code Button}) into this {@code ObservableList}
     * 
     * @param urls A List of url strings
     * 
     */
    public void refreshCwRes(List<String> urls) {
        if (urls != null) {
            cwResList.clear();
            for (String url : urls) {
                var btn = new Button(url);
                cwResList.add(btn);
            }
        }
    }

    /**
     * Refresh keyword query results by clearing the original
     * {@code ObservableList<Node> kwResList} and reloading the strings (each in a
     * {@code Button}) into this {@code ObservableList}
     * 
     * @param urls A List of url strings
     * 
     */
    public void refreshKwRes(List<String> urls) {
        if (urls != null) {
            kwResList.clear();
            for (String url : urls) {
                var btn = new Button(url);
                kwResList.add(btn);
            }
        }
    }

}
