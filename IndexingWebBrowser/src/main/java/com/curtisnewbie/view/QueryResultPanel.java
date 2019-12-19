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
 * A GridPane used to display the results of query (Head and Body sections in
 * the HTML). The results consists a list of url Strings, and each url string is
 * in a clickable Button.
 */
public class QueryResultPanel extends GridPane {

    private final String title = "Query Results: ";
    private final String hdTitle = "<Head>: ";
    private final String bdTitle = "<Body>: ";

    /** Display Head results */
    private VBox hdRes;

    /** Display Body results */
    private VBox bdRes;

    /** {@code ObservableList} for {@code VBox hdRes} */
    private ObservableList<Node> hdResList;

    /** {@code ObservableList} for {@code VBox bdRes} */
    private ObservableList<Node> bdResList;

    public QueryResultPanel() {
        hdRes = new VBox();
        bdRes = new VBox();
        hdResList = hdRes.getChildren();
        bdResList = bdRes.getChildren();

        // create and add two VBox to seperate the results for Head and Body
        var topBox = new VBox();
        ScrollPane topBoxSP = new ScrollPane(hdRes);
        topBoxSP.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        topBoxSP.setFitToHeight(true);
        topBoxSP.setFitToWidth(true);
        topBox.getChildren().addAll(new Text(title), new Text(hdTitle), topBoxSP);

        var bottomBox = new VBox();
        ScrollPane bottomBoxSP = new ScrollPane(bdRes);
        bottomBoxSP.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        bottomBoxSP.setFitToHeight(true);
        bottomBoxSP.setFitToWidth(true);
        bottomBox.getChildren().addAll(new Text(bdTitle), bottomBoxSP);
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
        refreshHdRes(testList);
        refreshBdRes(testList);
    }

    /**
     * Refresh Head query results by clearing the original
     * {@code ObservableList<Node> hdResList} and reloading the strings (each in a
     * {@code Button}) into this {@code ObservableList}
     * 
     * @param urls A List of url strings
     * 
     */
    public void refreshHdRes(List<String> urls) {
        if (urls != null) {
            hdResList.clear();
            for (String url : urls) {
                var btn = new Button(url);
                hdResList.add(btn);
            }
        }
    }

    /**
     * Refresh Body query results by clearing the original
     * {@code ObservableList<Node> bdResList} and reloading the strings (each in a
     * {@code Button}) into this {@code ObservableList}
     * 
     * @param urls A List of url strings
     * 
     */
    public void refreshBdRes(List<String> urls) {
        if (urls != null) {
            bdResList.clear();
            for (String url : urls) {
                var btn = new Button(url);
                bdResList.add(btn);
            }
        }
    }

}