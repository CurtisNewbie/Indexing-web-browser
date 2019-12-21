package com.curtisnewbie.controller;

import com.curtisnewbie.view.*;
import com.curtisnewbie.webBrowserModel.WebDoc;
import com.curtisnewbie.webBrowserModel.WebIndexForBody;
import com.curtisnewbie.webBrowserModel.WebIndexForHead;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Controller in MVC, this controller controls how the view and the model
 * interact with eachother.
 */
public class BrowserController {

    private BrowserView view;
    private HashSet<String> urlSet;
    private String default_url;
    private WebIndexForHead headIndex;
    private WebIndexForBody bodyIndex;

    public BrowserController(BrowserView view) {
        this.view = view;
        this.urlSet = new HashSet<>();
        this.default_url = "https://www.google.com";
        this.headIndex = new WebIndexForHead();
        this.bodyIndex = new WebIndexForBody();

        // register EventHandlers
        regMenuEventHandlers();
        regUrlLoadingEventHandler();
        regNewTabEventHandler();
        regBackwordBtnHandler();
        regForwardBtnHandler();

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
                    // save unique url in history
                    if (!urlSet.contains(url)) {
                        urlSet.add(url);
                        // update browsing history
                        updateHistoryPanel(url);
                        // update web index
                        updateWebIndices(url);
                    }
                }
            }
        });
    }

    /**
     * Add {@code EventHandler} to backTrack Btn for going back in history in
     * current selected {@code Tab} or {@code WebView}
     * 
     * @see UrlInputBox
     * @see BrowserView
     */
    private void regBackwordBtnHandler() {
        this.view.AddBackTrackBtnHandler(e -> {
            var currTab = this.view.getDisplayPane().getCurrentTab();
            if (currTab != null) {
                var webHistory = ((WebView) currTab.getContent()).getEngine().getHistory();
                int index = webHistory.getCurrentIndex();
                if (index >= 0 && index < webHistory.getEntries().size())
                    webHistory.go(-1);
            }
        });
    }

    /**
     * Add {@code EventHandler} to forward Btn for going forward in history in
     * current selected {@code Tab} or {@code WebView}
     * 
     * @see UrlInputBox
     * @see BrowserView
     */
    private void regForwardBtnHandler() {
        this.view.AddForwardBtnHandler(e -> {
            var currTab = this.view.getDisplayPane().getCurrentTab();
            if (currTab != null) {
                var webHistory = ((WebView) currTab.getContent()).getEngine().getHistory();
                int index = webHistory.getCurrentIndex();
                if (index >= 0 && index < webHistory.getEntries().size())
                    webHistory.go(1);
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
     * <p>
     * Update historyPanel. This involves creating a new {@code Button} (containing
     * this url string) in the historyPanel, and setup a
     * {@code EventHandler<ActionEvent>} for this button to navigate back to the
     * DisplayPane to display the webpage of this url.
     * </p>
     * <p>
     * This method does not know whether this url has been visited before, so this
     * method can generate duplicates buttons in the historyPanel.
     * </p>
     * 
     * 
     * @param url URL string
     */
    private void updateHistoryPanel(String url) {
        // update history view
        var btn = new Button(url);
        view.getQueryPane().getHistoryPanel().add(btn);

        // assign eventhandler for this btn
        btn.setOnAction(e1 -> {
            view.getDisplayPane().addTab(url);
            view.switchView(view.getDisplayPane());
        });
    }

    /**
     * <p>
     * Update the WebIndex for body and head sections of the webpage, only when this
     * url has neven been accessed to. This method should only be called when the
     * url is correct and has been successfully accessed.
     * </p>
     * <p>
     * This method does not know whether this url has been visited before, so other
     * means should be used to ensure this url is unique and has neven been passed
     * to this method before.
     * </p>
     * 
     * @param url URL String
     */
    private void updateWebIndices(String url) {
        try {
            WebDoc doc = new WebDoc(url);
            bodyIndex.add(doc);
            headIndex.add(doc);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
