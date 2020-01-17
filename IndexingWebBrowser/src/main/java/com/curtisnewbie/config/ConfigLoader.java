package com.curtisnewbie.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class ConfigLoader {

    private final String CONFIG_PATH = "config.json";
    private ClassLoader classLoader;

    public ConfigLoader() {
        this.classLoader = this.getClass().getClassLoader();
    }

    public Map<String, String> loadConfigFile() {

        var in = classLoader.getResourceAsStream(CONFIG_PATH);
        try (JsonParser parser = new JsonFactory().createParser(in);) {
            Map<String, String> map = new HashMap<>(); // may be modified in the future, it is now used for simple json
                                                       // file.
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                map.put(parser.getCurrentName(), parser.getText());
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}