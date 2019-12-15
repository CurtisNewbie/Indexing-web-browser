package com.curtisnewbie.controller;

import com.curtisnewbie.view.*;
import javafx.event.*;
import javafx.scene.control.Button;

import java.util.*;

/**
 * Controller in MVC, this controller controls how the view and the model
 * interact with eachother.
 */
public class BrowserController {

    private BrowserView view;
    private HashSet<String> urlSet;

    public BrowserController(BrowserView view) {
        this.view = view;
        this.urlSet = new HashSet<>();

        // register EventHandlers
        regMenuEventHandlers();
        regUrlEventHandlers();
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

    /** Register EventHandler for loading url and creating new tab */
    private void regUrlEventHandlers() {
        this.view.addUrlEventHandler(e -> {
            // update view
            var displayPane = this.view.getDisplayPane();
            String url = displayPane.getUrlInputBox().getUrlTextField().getText();
            displayPane.addTab(url);

            // save unique url
            if (url != null && !url.isEmpty() && !urlSet.contains(url)) {
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
        });
    }
}
