package com.curtisnewbie.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.web.WebView;
import javafx.scene.control.TabPane;

/**
 * <p>
 * This program has two views (or cards of CardLayout) in Javafx, one is for
 * viewing the content of the webpages, and another one is for using query to
 * seaarch contents.
 * </p>
 * <p>
 * This class itself is a subclass of BorderPane, and it represents one of the
 * view in this program for displaying content of webpages.
 * </p>
 * 
 * @author Yongjie Zhuang
 * 
 * @see BrowserView
 */
public class DisplayPane extends BorderPane {

    /**
     * It's a HBox that contains some of the control components/nodes in this pane
     * 
     * @see UrlInputBox
     */
    private UrlInputBox urlInputbox;

    /**
     * TabPane where each tab shows the content of a web page
     */
    private TabPane tabPane;

    /**
     * Instantiate DisplayPane
     * 
     * @param menuBtn the menu for this view (it can be universal for the whole
     *                program if necessary)
     * @see DisplayPane
     * @see MenuButton
     */
    public DisplayPane() {
        this.urlInputbox = new UrlInputBox();
        this.setTop(urlInputbox);
        this.tabPane = new TabPane();
        this.setCenter(tabPane);
    }

    /**
     * Create a new {@code Tab} that loads the webpage of the given url. It doesn
     * not create new tab if the given url is {@code NULL} or of a length of 0.
     * However, if the given url is invalid (i.e., doesn't exist), it will simply
     * create a empty tab with no content in it.
     * 
     * @param url a URL String
     * @return return {@code Tab} created if successful, else return {@code null}
     * 
     */
    public Tab addTab(String url) {
        if (url != null && !url.isEmpty()) {
            Tab tab = new Tab();
            var view = new WebView();
            view.getEngine().load(url);
            tab.setContent(view);
            tabPane.getTabs().add(tab);
            return tab;
        } else {
            return null;
        }
    }

    /**
     * Create a new {@code Tab} that loads the webpage of the given html text. It
     * doesn not create new tab if the given htmlText is {@code NULL}.
     * 
     * 
     * @param htmlText html text
     * @return return {@code Tab} created if successful, else return {@code null}
     * 
     */
    public Tab loadIntoNewTab(String htmlText) {
        if (htmlText != null) {
            Tab tab = new Tab();
            var view = new WebView();
            view.getEngine().loadContent(htmlText);
            tab.setContent(view);
            tabPane.getTabs().add(tab);
            return tab;
        } else {
            return null;
        }
    }

    /**
     * Get the UrlInputBox which contains the TextField for entering URL, and the
     * Buttons for going back and forth between the previously viewed web pages.
     * 
     * @return an object of UrlInputBox
     * @see UrlInputBox
     */
    public UrlInputBox getUrlInputBox() {
        return this.urlInputbox;
    }

    /**
     * Get current selected tab.
     * 
     * @return return {@code Tab} if there is tab selected, else return {@code Null}
     *         if there is no tab availabel at all.
     */
    public Tab getCurrentTab() {
        SingleSelectionModel<Tab> model = this.tabPane.getSelectionModel();
        return model.isEmpty() ? null : model.getSelectedItem();
    }

}
