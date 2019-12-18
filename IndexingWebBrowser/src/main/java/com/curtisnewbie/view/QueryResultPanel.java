package com.curtisnewbie.view;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A GridPane used to display the results of query (content words and keywords).
 * The results consists a list of url Strings, and each url string is in a
 * clickable Button.
 */
public class QueryResultPanel extends GridPane {

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
        ScrollPane topBoxSP = new ScrollPane(cwRes);
        topBoxSP.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        topBoxSP.setFitToHeight(true);
        topBoxSP.setFitToWidth(true);
        topBox.getChildren().addAll(new Text(title), new Text(cwTitle), topBoxSP);

        var bottomBox = new VBox();
        ScrollPane bottomBoxSP = new ScrollPane(kwRes);
        bottomBoxSP.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        bottomBoxSP.setFitToHeight(true);
        bottomBoxSP.setFitToWidth(true);
        bottomBox.getChildren().addAll(new Text(kwTitle), bottomBoxSP);
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