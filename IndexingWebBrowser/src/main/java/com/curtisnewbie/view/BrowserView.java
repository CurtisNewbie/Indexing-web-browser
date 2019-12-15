package com.curtisnewbie.view;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
     * Universal Menu for both views
     * 
     * @see MenuButton
     */
    private Menu menu;

    /**
     * A Pane consits of a group of nodes/componenets for the layout displaying the
     * HTML viewing UI
     */
    private DisplayPane displayPane;

    /**
     * A Pane consits of group of nodes/componenets for the layout displaying the
     * query searching UI
     */
    private QueryPane queryPane;

    public BrowserView() {
        init();
    }

    /** Initialise The View including its' internal components */
    private void init() {
        this.menu = new MenuBtn();
        displayPane = new DisplayPane(menu);
        queryPane = new QueryPane(menu);

        // by default, displayPane is shown first
        this.setCenter(displayPane);
    }

    /**
     * Add Multiple EventHandlers for the Menu, each handler for one of MenuItem in
     * the menu. These EventHandlers are registered in order.
     * 
     * @param handlers EventHandlers
     * @throws IllegalArgumentException when the number of given handlers is not
     *                                  equal to the number of MenuItems under this
     *                                  Menu
     * @see MenuButton
     */
    public void addMenuEventHandlers(List<EventHandler<ActionEvent>> handlers) throws IllegalArgumentException {
        var menuItems = menu.getItems();

        if (handlers.size() != menuItems.size())
            throw new IllegalArgumentException(
                    "Number of EventHandlers should be equal to the number of MenuItems under this Menu");
        else {
            for (int i = 0; i < handlers.size(); i++) {
                menuItems.get(i).setOnAction(handlers.get(i));
            }
        }
    }

}
