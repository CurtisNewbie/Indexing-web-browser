package com.curtisnewbie.view;

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
     * Panel in the middle of the queryPane to show the unique words (in head and
     * body) of the selected url.
     */
    private SummaryPanel urlSummaryPanel;

    /**
     * Instantiate QueryPane
     * 
     * @param menuBtn the menu for this view (it can be universal for the whole
     *                program if necessary)
     */
    public QueryPane() {
        // create panels that display histories, query controls, queries results, and
        // displays the summary of the selected url in the queryResultPanel
        queryControlPanel = new QueryControlPanel();
        historyPanel = new HistoryPanel();
        queryResultPanel = new QueryResultPanel();
        urlSummaryPanel = new SummaryPanel();

        this.add(queryResultPanel, 0, 0);
        this.add(urlSummaryPanel, 1, 0);
        this.add(new VBox(queryControlPanel, historyPanel), 2, 0);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100 / 3);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(100 / 3);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(100 / 3);
        this.getColumnConstraints().addAll(col1, col2, col3);
    }

    public HistoryPanel getHistoryPanel() {
        return historyPanel;
    }

    public QueryControlPanel getQueryControlPanel() {
        return queryControlPanel;
    }

    public QueryResultPanel getQueryResultPanel() {
        return queryResultPanel;
    }

    /**
     * Get the panel located in the middle of the Query Pane
     * 
     * @return a panel located in the middle of the Query Pane
     */
    public SummaryPanel getUrlSummaryPanel() {
        return urlSummaryPanel;
    }

}
