package com.curtisnewbie.controller;

import com.curtisnewbie.view.*;
import com.curtisnewbie.webBrowserModel.WebDoc;
import com.curtisnewbie.webBrowserModel.WebIndexForBody;
import com.curtisnewbie.webBrowserModel.WebIndexForHead;
import com.curtisnewbie.webBrowserModel.Query;
import com.curtisnewbie.webBrowserModel.QueryBuilder;

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
        this.view.addMenuEventHandlers(CreateMenuEventHandlers());
        this.view.addUrlLoadingEventHandler(CreateUrlLoadingEventHandler());
        this.view.addNewTabHandler(CreateNewTabEventHandler());
        this.view.addBackTrackBtnHandler(CreateBackwordBtnHandler());
        this.view.addForwardBtnHandler(CreateForwardBtnHandler());
        this.view.addInfixQueryHandler(CreateInfixQueryHandler());
        this.view.addPrefixQueryHandler(CreatePrefixQueryHandler());

        // by default, display a new tab displaying the default_url
        var firstTab = this.view.getDisplayPane().addTab(default_url);
        regStateChangeHandler(firstTab);
    }

    /**
     * Create a list of EventHandlers for Menu
     * 
     * @return List of {@code EventHandler} for Menu
     */
    private ArrayList<EventHandler<ActionEvent>> CreateMenuEventHandlers() {
        ArrayList<EventHandler<ActionEvent>> handlers = new ArrayList<>();
        handlers.add(e -> {
            // handler for toDisplayPane menuItem
            this.view.switchView(view.getDisplayPane());
        });
        handlers.add(e -> {
            // handler for toQueryPane menuItem
            this.view.switchView(view.getQueryPane());
        });
        return handlers;
    }

    /**
     * Create EventHandler for loading url in textfield. When no tab exists,
     * entering url in textfield will result in creating a new tab to load such url.
     * 
     * @return {@code EventHandler} for loading url in textfield
     */
    private EventHandler<ActionEvent> CreateUrlLoadingEventHandler() {
        return e -> {
            // update view
            var displayPane = this.view.getDisplayPane();
            String url = displayPane.getUrlInputBox().getUrlTextField().getText();

            if (url != null && !url.isEmpty()) {

                // always create a new tab for local file
                if (url.toLowerCase().startsWith("file:")) {
                    try {
                        String content = WebDoc.readLocalFile(url);
                        Tab createdTab = displayPane.loadIntoNewTab(content);
                        // register ChangeListener with this cratedTab
                        regStateChangeHandler(createdTab);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    // for online webpage
                    Tab currTab = this.view.getDisplayPane().getCurrentTab();
                    if (currTab == null) {
                        // if there is no tab being selected, add new tab
                        Tab createdTab = displayPane.addTab(completeURL(url));
                        // register ChangeListener with this cratedTab
                        regStateChangeHandler(createdTab);
                    } else {
                        // update current tab to this url
                        WebView currWebView = (WebView) currTab.getContent();
                        currWebView.getEngine().load(completeURL(url));
                    }
                }
            }
        };

    }

    /**
     * <p>
     * Create EventHandler to handle event for creating new {@code Tab}. The new
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
     * 
     * @return {@code EventHandler} to handle event for creating new {@code Tab}
     */
    private EventHandler<ActionEvent> CreateNewTabEventHandler() {
        return e -> {
            Tab createdTab = this.view.getDisplayPane().addTab(default_url);
            // register ChangeListener with this cratedTab
            regStateChangeHandler(createdTab);
        };
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

                    // if this is a local file, the .getLocation() method cannot return the actual
                    // location of this file.
                    var textField = view.getDisplayPane().getUrlInputBox().getUrlTextField();
                    if (url == null || url.isEmpty())
                        url = textField.getText();

                    // save unique url in history
                    if (url != null && !urlSet.contains(url)) {
                        urlSet.add(url);
                        // update browsing history
                        updateHistoryPanel(url);
                        // update web index
                        updateWebIndices(url);
                    }

                    // update textField to tell the user the location of current webpage (just like
                    // what normal browser does)
                    if (url != null && !textField.getText().equalsIgnoreCase(url)) {
                        textField.setText(url);
                    }
                }
            }
        });
    }

    /**
     * Create {@code EventHandler} for Button to go back in history in current
     * selected {@code Tab} or {@code WebView}
     * 
     * @return {@code EventHandler} for Button to go back in history in current
     *         selected {@code Tab} or {@code WebView}
     * 
     * @see UrlInputBox
     * @see BrowserView
     */
    private EventHandler<ActionEvent> CreateBackwordBtnHandler() {
        return e -> {
            var currTab = this.view.getDisplayPane().getCurrentTab();
            if (currTab != null) {
                var webHistory = ((WebView) currTab.getContent()).getEngine().getHistory();
                int index = webHistory.getCurrentIndex();
                if (index >= 0 && index < webHistory.getEntries().size())
                    webHistory.go(-1);
            }
        };
    }

    /**
     * create {@code EventHandler} for Button to go forward in history in current
     * selected {@code Tab} or {@code WebView}
     * 
     * @return {@code EventHandler} for Button to go forward in history in current
     *         selected {@code Tab} or {@code WebView}
     * @see UrlInputBox
     * @see BrowserView
     */
    private EventHandler<ActionEvent> CreateForwardBtnHandler() {
        return e -> {
            var currTab = this.view.getDisplayPane().getCurrentTab();
            if (currTab != null) {
                var webHistory = ((WebView) currTab.getContent()).getEngine().getHistory();
                int index = webHistory.getCurrentIndex();
                if (index >= 0 && index < webHistory.getEntries().size())
                    webHistory.go(1);
            }
        };
    }

    /**
     * Create {@code EventHandler} to process the infix query and update the result
     * panel to display the results
     * 
     * @return {@code EventHandler} to process the infix query and update the result
     *         panel to display the results
     * 
     * @see BrowserView
     * @see QueryControlPanel
     */
    private EventHandler<ActionEvent> CreateInfixQueryHandler() {
        return e -> {
            var textField = this.view.getQueryPane().getQueryControlPanel().getInfixTf();
            String infixQuery = textField.getText();
            if (infixQuery != null && !infixQuery.isEmpty()) {
                // parse query
                Query parsedQuery = QueryBuilder.parseInfixForm(infixQuery);
                // update view
                updateQueryResultpanel(parsedQuery);
            }
            textField.clear();
        };
    }

    /**
     * Create {@code EventHandler} to process the prefix query and update the result
     * panel to display the results
     * 
     * @return {@code EventHandler} to process the prefix query and update the
     *         result panel to display the results
     * 
     * @see BrowserView
     * @see QueryControlPanel
     */
    private EventHandler<ActionEvent> CreatePrefixQueryHandler() {
        return e -> {
            var textField = this.view.getQueryPane().getQueryControlPanel().getPrefixTf();
            String prefixQuery = textField.getText();
            if (prefixQuery != null && !prefixQuery.isEmpty()) {
                // parse query
                Query parsedQuery = QueryBuilder.parse(prefixQuery);
                // update view
                updateQueryResultpanel(parsedQuery);
            }
            textField.clear();
        };
    }

    /**
     * Update the QueryResultPanel by clearing the {@code ObservableList<Node>} in
     * the two VBoxs, and refill them with the Buttons that contain the results of
     * the query (the URL String). Each button is registered with an EventHandler
     * that when it is clicked, the displayPane will be shown and a new tab will be
     * created displaying the webpage of this url.
     * 
     * @param parsedQuery
     * @see QueryResultPanel
     */
    private void updateQueryResultpanel(Query parsedQuery) {
        // get results of head and body
        Set<WebDoc> hdResSet = parsedQuery.matches(headIndex);
        Set<WebDoc> bdResSet = parsedQuery.matches(bodyIndex);

        List<String> headList = new ArrayList<>();
        if (hdResSet != null)
            for (WebDoc doc : hdResSet) {
                headList.add(doc.getUrlString());
            }
        List<String> bodyList = new ArrayList<>();
        if (bdResSet != null)
            for (WebDoc doc : bdResSet) {
                bodyList.add(doc.getUrlString());
            }

        // update view
        var queryResult = this.view.getQueryPane().getQueryResultPanel();
        var headVBox = queryResult.getHdResVBox();
        headVBox.getChildren().clear();
        for (String url : headList) {
            headVBox.getChildren().add(queryResultBtn(url));
        }
        var bodyVBox = queryResult.getBdResVBox();
        bodyVBox.getChildren().clear();
        for (String url : bodyList) {
            bodyVBox.getChildren().add(queryResultBtn(url));
        }
    }

    /**
     * Return a new Button of the given url, which is registered with an
     * EventHandler that when it is clicked, the displayPane will be shown and a new
     * tab created for this url.
     * 
     * @param url URL string
     * @return a new Button of the url that is registered with an EventHandler that
     *         navigates to the displayPane and create a new tab for this url.
     */
    private Button queryResultBtn(String url) {
        var btn = new Button(url);
        btn.setOnAction(e -> {
            view.getDisplayPane().addTab(url);
            view.switchView(view.getDisplayPane());
        });
        return btn;
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
