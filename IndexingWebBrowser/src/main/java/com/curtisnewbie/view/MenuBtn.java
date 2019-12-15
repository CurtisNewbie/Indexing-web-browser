package com.curtisnewbie.view;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MenuBtn extends Menu {

    /** Maximum height of icon images in the buttons */
    private double MAX_IMG_HEIGHT = 20;

    /** ObservableList of MenuItem under this menu */
    private ObservableList<MenuItem> observableItems;

    /** Path to the icon for menuBtn buttn */
    private final String PATH_TO_MENUICON = "img/menu_icon.png";

    public MenuBtn() {
        // load icon for this menu
        ClassLoader loader = getClass().getClassLoader();
        InputStream menuIn = loader.getResourceAsStream(PATH_TO_MENUICON);
        if (menuIn == null)
            throw new IllegalArgumentException("Cannot find Icon Image at \"" + PATH_TO_MENUICON + "\"");

        ImageView menuIcon = new ImageView(new Image(menuIn));
        menuIcon.setFitWidth(MAX_IMG_HEIGHT);
        menuIcon.setFitHeight(MAX_IMG_HEIGHT);
        this.setGraphic(menuIcon);

        // add to MenuItems in this menu
        this.observableItems = this.getItems();
        MenuItem toDisplayPane = new MenuItem("DisplayPane");
        MenuItem toQueryPane = new MenuItem("QueryPane");
        this.observableItems.addAll(toDisplayPane, toQueryPane);
    }

}