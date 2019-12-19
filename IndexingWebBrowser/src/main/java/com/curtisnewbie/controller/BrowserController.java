package com.curtisnewbie.controller;

import com.curtisnewbie.view.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.*;

/**
 * Controller in MVC, this controller controls how the view and the model
 * interact with eachother.
 */
public class BrowserController {

    private BrowserView view;
    private HashSet<String> urlSet;
    // private ArrayList<LinkedList<String>> historyForTabs;
    private String default_url;

    public BrowserController(BrowserView view) {
        this.view = view;
        this.urlSet = new HashSet<>();
        // this.historyForTabs = new ArrayList<LinkedList<String>>();
        this.default_url = "https://www.google.com";

        // register EventHandlers
        regMenuEventHandlers();
        regUrlLoadingEventHandler();
        regNewTabEventHandler();

        // by default, display a new tab displaying the default_url
        var firstTab = this.view.getDisplayPane().addTab(default_url);
        regStateChangeHandler(firstTab);
    }

    /** register EventHandlers for Menu */
    private void regMenuEventHandlers() {
        ArrayList<EventHandler<ActionEvent>> handlers = new ArrayList<>();
        handlers.add(e -> {
            // handler for toDisplayPane menuItem
            this.view.switchView(view.getDisplayPane());
        });
        handlers.add(e -> {
            // handler for toQueryPane menuItem
            this.view.switchView(view.getQueryPane());
        });
        view.addMenuEventHandlers(handlers);
    }

    /**
     * Register EventHandler for loading url in textfield. When no tab exists,
     * entering url in textfield will result in creating a new tab to load such url.
     */
    private void regUrlLoadingEventHandler() {
        this.view.addUrlLoadingEventHandler(e -> {
            // update view
            var displayPane = this.view.getDisplayPane();
            String url = displayPane.getUrlInputBox().getUrlTextField().getText();

            if (url != null && !url.isEmpty()) {
                Tab currTab = this.view.getDisplayPane().getCurrentTab();
                if (currTab == null) {
                    // if there is no tab being selected, add new tab
                    Tab createdTab = displayPane.addTab(completeURL(url));
                    // keep tracks of change of state
                    regStateChangeHandler(createdTab);
                } else {
                    // update current tab to this url
                    WebView currWebView = (WebView) currTab.getContent();
                    currWebView.getEngine().load(completeURL(url));
                }
            }
        });
    }

    /**
     * <p>
     * Register EventHandler to handle event for creating new {@code Tab}. The new
     * {@code Tab} created is registered with a {@code
     * ChangeListener<State>}, through which it detects the change of the state of
     * the {@code WebEngine} in the {@code Tab}.
     * </p>
     * <p>
     * When the {@code WebEngine} successfully load the webpage (of the URL entered
     * in textfield or hyperlink clicked inside the {@code WebView}), it records the
     * URL in the {@code HistoryPanel}. Note that history is only updated, when the
     * webpage is successfully loaded.
     * </p>
     */
    private void regNewTabEventHandler() {
        this.view.addNewTabHandler(e -> {
            Tab createdTab = this.view.getDisplayPane().addTab(default_url);
            // keep tracks of change of state
            regStateChangeHandler(createdTab);
        });
    }

    /**
     * <p>
     * Register a {@code ChangeListener<State>} with a {@code Tab createdTab} so
     * that whenever the {@code WebView} in this tab successfully loads a webpage,
     * it updates the historyPanel.
     * </P>
     * <p>
     * The createdTab must already has a WebView insider, else it can throw
     * exceptions.
     * </p>
     * 
     * @param createdTab Tab with WebView in it as its content.
     */
    private void regStateChangeHandler(Tab createdTab) {
        WebEngine engine = ((WebView) createdTab.getContent()).getEngine();
        engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
                // if loading url is successful
                if (newValue == State.SUCCEEDED) {
                    String url = engine.getLocation();
                    // update browsing history
                    updateHistoryPanel(url);
                }
            }
        });
    }

    /**
     * Add "http://" protocol to the given url string if this url is not starting
     * with "http://" or "https://".
     * 
     * @param oriUrl
     * @return url string
     */
    private String completeURL(String oriUrl) {
        return oriUrl.startsWith("http://") || oriUrl.startsWith("https://") ? oriUrl : "http://" + oriUrl;
    }

    /**
     * Update historyPanel only when this url is unique, i.e., it has never been
     * accessed. This involves creating a new {@code Button} (containing this url
     * string) in the historyPanel, and setup a {@code EventHandler<ActionEvent>}
     * for this button to navigate back to the DisplayPane to display the webpage of
     * this url.
     * 
     * @param url URL string
     */
    private void updateHistoryPanel(String url) {
        // save unique url in history
        if (!urlSet.contains(url)) {
            urlSet.add(url);

            // update history view
            var btn = new Button(url);
            view.getQueryPane().getHistoryPanel().add(btn);

            // assign eventhandler for this btn
            btn.setOnAction(e1 -> {
                view.getDisplayPane().addTab(url);
                view.switchView(view.getDisplayPane());
            });
        }
    }

}
