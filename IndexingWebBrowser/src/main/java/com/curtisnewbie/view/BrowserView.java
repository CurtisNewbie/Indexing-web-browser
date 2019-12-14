package com.curtisnewbie.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * <p>
 * The Highest level representation of the view in MVC. This class provides
 * methods to get information from and update status of the various components
 * stored.
 * </p>
 * <p>
 * This program has two views (or cards of CardLayout) in Javafx, one is for
 * viewing the content of the webpages, and another one is for using query to
 * seaarch contents.
 * </p>
 * <p>
 * This class itself is a subclass of Pane, and it controls switching between
 * the two views by have two BorderPane as its instance variables.
 * </p>
 */
public class BrowserView extends BorderPane {

    /**
     * A Pane consits of a group of nodes/componenets for the layout displaying the
     * HTML viewing UI
     */
    private BorderPane displayPane;

    /**
     * A Pane consits of group of nodes/componenets for the layout displaying the
     * query searching UI
     */
    private BorderPane queryPane;

    public BrowserView() {
        init();
    }

    /** Initialise The View including its' internal components */
    private void init() {

        displayPane = new DisplayPane();
        this.setCenter(displayPane);
    }

}