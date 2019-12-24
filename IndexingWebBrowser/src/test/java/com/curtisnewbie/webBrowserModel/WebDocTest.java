package com.curtisnewbie.webBrowserModel;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.Test;

/** Test cases for WebDoc */
public class WebDocTest {

    @Test
    public void fileTypeShouldBeDetected() throws IllegalArgumentException, IOException, URISyntaxException {
        assertSame(WebDoc.FileType.WEB_URL, new WebDoc("https://www.google.com").getFileType());
        URL url = getClass().getClassLoader().getResource("testFile/Test.html");
        String absPath = Paths.get(url.toURI()).toFile().getAbsolutePath();
        assertSame(WebDoc.FileType.LOCAL_WEB_DOC, new WebDoc("file:" + absPath).getFileType());
    }

    @Test
    public void localHtmlShouldNotBeNullOrEmpty() throws IllegalArgumentException, IOException, URISyntaxException {
        URL url = getClass().getClassLoader().getResource("testFile/Test.html");
        String absPath = Paths.get(url.toURI()).toFile().getAbsolutePath();
        String content = new WebDoc("file:" + absPath).getContent();
        assertTrue(content != null && content.length() > 0);
    }

}