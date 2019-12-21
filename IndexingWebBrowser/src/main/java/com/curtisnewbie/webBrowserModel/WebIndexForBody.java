package com.curtisnewbie.webBrowserModel;

import java.util.Set;
import java.util.TreeSet;

/**
 * Subclass of WebIndex that implements the add(WebDoc doc) method exclusively
 * for the body words in a WebDoc
 * 
 * @see WebIndex
 * @see WebDoc
 */
public class WebIndexForBody extends WebIndex {
    @Override
    public void add(WebDoc doc) {
        Set<String> tempWordSet;
        tempWordSet = doc.getBodyWords();
        if (!tempWordSet.isEmpty()) {
            for (String word : tempWordSet) {
                Set<WebDoc> newSet = new TreeSet<WebDoc>();
                newSet.add(doc);
                Set<WebDoc> temp = webDocsMap.putIfAbsent(word, newSet);
                if (temp != null) {
                    // If the key is already associated with a set, add the doc to this set
                    temp.add(doc);
                }
            }
        }
        numOfDocs++;
        numOfWords = webDocsMap.size();
    }
}