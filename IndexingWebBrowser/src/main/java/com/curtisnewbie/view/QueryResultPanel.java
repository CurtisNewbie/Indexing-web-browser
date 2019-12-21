package com.curtisnewbie.view;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * A GridPane used to display the results of query (Head and Body sections in
 * the HTML). The results consists a list of url Strings, and each url string is
 * in a clickable Button.
 */
public class QueryResultPanel extends GridPane {

    private final int PANEL_TITLE_FONT = 15;
    private final int VBOX_SPACING = 5;

    private final String PANEL_TITLE = "Query Results: ";
    private final String HEAD_LABEL_STR = "<Head>: ";
    private final String BODY_LABEL_STR = "<Body>: ";

    private final int PANEL_PADDING_TOP = 15;
    private final int PANEL_PADDING_BOTTOM = 15;
    private final int PANEL_PADDING_LEFT = 5;
    private final int PANEL_PADDING_RIGHT = 5;

    private Label panelLabel;
    private Label headLabel;
    private Label bodyLabel;

    /** Display Head results */
    private VBox hdResVBox;

    /** Display Body results */
    private VBox bdResVBox;

    public QueryResultPanel() {
        panelLabel = new Label(PANEL_TITLE);
        panelLabel.setFont(new Font(PANEL_TITLE_FONT));
        headLabel = new Label(HEAD_LABEL_STR);
        bodyLabel = new Label(BODY_LABEL_STR);
        hdResVBox = new VBox();
        bdResVBox = new VBox();

        // create and add two VBox to seperate the results for Head and Body
        var topBox = new VBox();
        topBox.setSpacing(VBOX_SPACING);
        ScrollPane topBoxSP = new ScrollPane(hdResVBox);
        topBoxSP.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        topBoxSP.setFitToHeight(true);
        topBoxSP.setFitToWidth(true);
        topBoxSP.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        topBox.getChildren().addAll(panelLabel, headLabel, topBoxSP);
        this.add(topBox, 0, 0);

        var bottomBox = new VBox();
        bottomBox.setSpacing(VBOX_SPACING);
        ScrollPane bottomBoxSP = new ScrollPane(bdResVBox);
        bottomBoxSP.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        bottomBoxSP.setFitToHeight(true);
        bottomBoxSP.setFitToWidth(true);
        bottomBoxSP.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        bottomBox.getChildren().addAll(bodyLabel, bottomBoxSP);
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
        this.setPadding(new Insets(PANEL_PADDING_TOP, PANEL_PADDING_RIGHT, PANEL_PADDING_BOTTOM, PANEL_PADDING_LEFT));
    }

    /**
     * Get Vbox for displaying head result
     * 
     * @return Vbox for displaying head result
     */
    public VBox getHdResVBox() {
        return hdResVBox;
    }

    /**
     * Get Vbox for displaying body result
     * 
     * @return Vbox for displaying body result
     */

    public VBox getBdResVBox() {
        return bdResVBox;
    }

}