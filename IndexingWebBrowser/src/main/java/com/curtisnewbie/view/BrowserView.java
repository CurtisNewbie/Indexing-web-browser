package com.curtisnewbie.view;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * <p>
 * The Highest level representation of the view in MVC. This class provides
 * methods to get information from and update status of the various components
 * stored.
 * </p>
 * <p>
 * This program has two views (or cards in CardLayout in Swing) in Javafx, one
 * is for viewing the content of the webpages, and another one is for using
 * query to seaarch contents.
 * </p>
 * <p>
 * This class itself is a subclass of Pane, and it controls switching between
 * the two views by have two BorderPane as its instance variables.
 * </p>
 */
public class BrowserView extends BorderPane {

    /**
     * Universal Menu for this program
     * 
     * @see MenuBtn
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
        this.menu = new MenuBtn();
        this.setTop(new MenuBar(menu));
        displayPane = new DisplayPane();
        queryPane = new QueryPane();

        // by default, displayPane is shown first
        this.setCenter(displayPane);
    }

    /**
     * Switch between "view" (e.g., QueryPane and DisplayPane). If the one that is
     * switched to is the same as the current view, it does nothing.
     * 
     * @param view A Pane that is displayed at the center of this program.
     */
    public void switchView(Pane view) {
        if (view != getCenter()) {
            this.setCenter(view);
        }
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

    /**
     * Add EventHandler for loading url. This handler is for the ActionEvent that is
     * fired when a user presses Enter key on the URL TextField.
     * 
     * @param handler EventHandler<ActionEvent> for loading Url
     */
    public void addUrlLoadingEventHandler(EventHandler<ActionEvent> handler) {
        displayPane.getUrlInputBox().getUrlTextField().setOnAction(handler);
    }

    /**
     * Add EventHandler for infix query textfield. This handler should process the
     * infix query and update the result panel to display the results.
     * 
     * @param handler
     */
    public void addInfixQueryHandler(EventHandler<ActionEvent> handler) {
        queryPane.getQueryControlPanel().getInfixTf().setOnAction(handler);
    }

    /**
     * Add EventHandler for prefix query textfield. This handler should process the
     * prefix query and update the result panel to display the results.
     * 
     * @param handler
     */
    public void addPrefixQueryHandler(EventHandler<ActionEvent> handler) {
        queryPane.getQueryControlPanel().getPrefixTf().setOnAction(handler);
    }

    /**
     * Add EventHandler for creating new tab.
     * 
     * @param handler EventHandler for creating new tab
     */
    public void addNewTabHandler(EventHandler<ActionEvent> handler) {
        displayPane.getUrlInputBox().getAddTabBtn().setOnAction(handler);
    }

    /**
     * Add EventHandler for the backTrack button in UrlInputBox in DisplayPane. It
     * should force the current selected WebView to go backword in history.
     * 
     * @param handler EventHandler for the button to go backword in history
     * 
     * @see DisplayPane
     * @see UrlInputBox
     */
    public void AddBackTrackBtnHandler(EventHandler<ActionEvent> handler) {
        displayPane.getUrlInputBox().getBackTrackBtn().setOnAction(handler);
    }

    /**
     * Add EventHandler for the forward button in UrlInputBox in DisplayPane. It
     * should force the current selected WebView to go forward in history.
     * 
     * @param handler EventHandler for the button to go forward in history
     * 
     * @see DisplayPane
     * @see UrlInputBox
     */
    public void AddForwardBtnHandler(EventHandler<ActionEvent> handler) {
        displayPane.getUrlInputBox().getForwardBtn().setOnAction(handler);
    }

    public Menu getMenu() {
        return menu;
    }

    public QueryPane getQueryPane() {
        return queryPane;
    }

    public DisplayPane getDisplayPane() {
        return displayPane;
    }

}
