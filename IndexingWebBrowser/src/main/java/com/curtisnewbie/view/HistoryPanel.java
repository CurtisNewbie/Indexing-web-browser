package com.curtisnewbie.view;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
        ScrollPane sp = new ScrollPane(historiesVBox);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.getChildren().addAll(new Text(title), sp);
    }

    /**
     * Add one url string (in a {@code Button}) to the {@code ObservableList}
     * 
     * @param urlBtn a Button contains the url string
     */
    public void add(Button urlBtn) {
        if (urlBtn != null)
            observableList.add(urlBtn);
    }

    // /**
    // * Refresh this history panel by clearing the original {@code ObservableList}
    // * and reloading the strings (each in a {@code Button}) into this {@code
    // * ObservableList}
    // *
    // * @param urls A List of url strings
    // *
    // */
    // public void refresh(List<String> urls) {
    // if (urls != null && urls.size() > 0) {
    // observableList.clear();
    // for (String url : urls) {
    // var btn = new Button(url);
    // observableList.add(btn);
    // }
    // }
    // }
}