package com.curtisnewbie.controller;

import com.curtisnewbie.view.*;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
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

    /** Register EventHandler for loading url */
    private void regUrlLoadingEventHandler() {
        this.view.addUrlLoadingEventHandler(e -> {
            // update view
            var displayPane = this.view.getDisplayPane();
            String url = displayPane.getUrlInputBox().getUrlTextField().getText();

            if (url != null && !url.isEmpty()) {
                Tab currTab = this.view.getDisplayPane().getCurrentTab();
                if (currTab == null) {
                    // if there is no tab being selected, add new tab
                    displayPane.addTab(completeURL(url));
                } else {
                    // update current tab to this url
                    WebView currWebView = (WebView) currTab.getContent();
                    currWebView.getEngine().load(completeURL(url));

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
        });
    }

    /** Register EventHandler to handle event for creating new tab */
    private void regNewTabEventHandler() {
        this.view.addNewTabHandler(e -> {
            this.view.getDisplayPane().addTab(default_url);
        });
    }

    private String completeURL(String oriUrl) {
        return oriUrl.startsWith("http://") || oriUrl.startsWith("https://") ? oriUrl : "http://" + oriUrl;
    }

}
