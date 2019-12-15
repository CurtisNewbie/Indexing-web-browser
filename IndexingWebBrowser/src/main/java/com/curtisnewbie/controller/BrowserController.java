package com.curtisnewbie.controller;

import com.curtisnewbie.view.*;
import javafx.event.*;
import java.util.*;

public class BrowserController {

    private BrowserView view;

    public BrowserController(BrowserView view) {
        this.view = view;

        // register EventHandlers
        regMenuEventHandlers();
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
}
