package com.curtisnewbie.view;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * VBox that displays the history of the browsed webpages. Each webpage (URL
 * links) shown in this VBox is a clickable button.
 */
public class HistoryPanel extends VBox {

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