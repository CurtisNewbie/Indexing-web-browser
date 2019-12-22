package com.curtisnewbie.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/** Panel that has a TextArea. */
public class SummaryPanel extends VBox {

    private final int PANEL_TITLE_FONT = 15;
    private final int VBOX_SPACING = 5;
    private final int PREF_ROW_COUNT = 100;

    /** Padding for the panel */
    private final int PANEL_PADDING_TOP = 15;
    private final int PANEL_PADDING_BOTTOM = 15;
    private final int PANEL_PADDING_LEFT = 5;
    private final int PANEL_PADDING_RIGHT = 10;

    private final String TITLE = "SUMMARY PANEL: ";
    private TextArea textArea;

    public SummaryPanel() {
        Label titleLabel = new Label(TITLE);
        titleLabel.setFont(new Font(PANEL_TITLE_FONT));
        textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefRowCount(PREF_ROW_COUNT);
        this.setPadding(new Insets(PANEL_PADDING_TOP, PANEL_PADDING_RIGHT, PANEL_PADDING_BOTTOM, PANEL_PADDING_LEFT));
        this.setSpacing(VBOX_SPACING);
        this.getChildren().addAll(titleLabel, textArea);
        VBox.setVgrow(textArea, Priority.ALWAYS);
    }

    public TextArea getTextArea() {
        return textArea;
    }

}