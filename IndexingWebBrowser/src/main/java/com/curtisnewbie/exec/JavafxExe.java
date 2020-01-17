package com.curtisnewbie.exec;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.stage.Screen;
import javafx.scene.Scene;

import com.curtisnewbie.view.*;

import java.util.Map;

import com.curtisnewbie.config.Config;
import com.curtisnewbie.config.ConfigLoader;
import com.curtisnewbie.controller.*;

/**
 * This where the javafx implementation of this program is started.
 * 
 * @author Yongjie Zhuang
 */
public class JavafxExe extends Application {

    private final String DEF_TITLE = "Simple Indexing Web Browser by Yongjie";
    private Map<String, String> configMap;
    private String title;

    @Override
    public void init() throws Exception {
        super.init();
        // load json config
        ConfigLoader configLoader = new ConfigLoader();
        Map<String, String> configMap = configLoader.loadConfigFile();
        if (configMap != null) {
            this.configMap = configMap;
            this.title = configMap.get(Config.CONFIG_TITLE);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BrowserView view = new BrowserView();

        // controller that controls the view
        BrowserController controller = new BrowserController(view, configMap);

        // get screen size
        Rectangle2D screen = Screen.getPrimary().getBounds();
        double width = screen.getWidth();
        double height = screen.getHeight();

        Scene scene = new Scene(view, width * 0.7, height * 0.7);
        primaryStage.setScene(scene);
        primaryStage.setTitle(title == null ? DEF_TITLE : title);
        primaryStage.show();
    }

}