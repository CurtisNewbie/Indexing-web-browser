package com.curtisnewbie.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * VBox that consits of controls used to control searching web content (content
 * words and keywords). E.g., TextField for entering infix queries and prefix
 * queries.
 */
public class QueryControlPanel extends VBox {

    private final int TITLE_FONT = 15;

    private final int HBOX_SPACING = 5;
    private final int MARGIN_TOP = 5;
    private final int MARGIN_RIGHT = 0;
    private final int MARGIN_BOTTOM = 5;
    private final int MARGIN_LEFT = 0;
    private final int PANEL_PADDING_TOP = 15;
    private final int PANEL_PADDING_BOTTOM = 15;
    private final int PANEL_PADDING_LEFT = 0;
    private final int PANEL_PADDING_RIGHT = 0;

    private Label panelLabel;
    private Label infixLabel;
    private Label prefixLabel;
    private TextField infixTf;
    private TextField prefixTf;

    private final String INFIX_LABEL_STR = "Infix Query: ";
    private final String PREFIX_LABEL_STR = "Prefix Query: ";
    private final String PANEL_TITLE = "Query Control Panel: ";

    public QueryControlPanel() {
        panelLabel = new Label(PANEL_TITLE);
        panelLabel.setFont(new Font(TITLE_FONT));
        panelLabel.setPadding(new Insets(MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM, MARGIN_LEFT));
        infixLabel = new Label(INFIX_LABEL_STR);
        prefixLabel = new Label(PREFIX_LABEL_STR);
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

        this.getChildren().addAll(panelLabel, boxTop, boxBottom);
        this.setPadding(new Insets(PANEL_PADDING_TOP, PANEL_PADDING_RIGHT, PANEL_PADDING_BOTTOM, PANEL_PADDING_LEFT));
    }
}