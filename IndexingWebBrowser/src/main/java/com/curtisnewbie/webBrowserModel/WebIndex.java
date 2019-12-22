package com.curtisnewbie.webBrowserModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * It is a web index that contains a number of WebDoc. It can check the web
 * documents stored and see which one contains a particular word.
 * 
 * @see WebDoc
 * @see WebIndexForBody
 * @see WebIndexForHead
 * @author Yongjie Zhuang
 */
public abstract class WebIndex {

	/**
	 * The 'keys' are the words, the 'values' are the Set of WebDoc.
	 */
	protected Map<String, Set<WebDoc>> webDocsMap;

	/**
	 * Number of WebDoc in this web index.
	 */
	protected int numOfDocs;

	/**
	 * Number of words read from these WebDoc.
	 */
	protected int numOfWords;

	/**
	 * Initialise the webDocs. Assign 0 to numOfDocs and numOfWord.
	 */
	public WebIndex() {
		webDocsMap = new HashMap<>();
		this.numOfDocs = 0;
		this.numOfWords = 0;
	}

	/**
	 * Add an object of WebDoc into the index and increment numOfDocs by one.
	 * WebIndex internally uses a map for searching WebDoc based on the given words,
	 * and this WebDoc is added in a way such that the words in this WebDoc becomes
	 * the keys, and this WebDoc becomes the value.
	 * 
	 * @param doc An object of WebDoc
	 */
	public abstract void add(WebDoc doc);

	/**
	 * This method searches through the web index and finds the results that match
	 * the given String. It returns a deep copy of the result as a Set<WebDoc>.
	 * 
	 * @param wd the String that is searched
	 * @return a Set<WebDoc> that matches the String.
	 */
	public Set<WebDoc> getMatches(String wd) {
		Set<WebDoc> deepCopySet = new TreeSet<>();
		Set<WebDoc> resultSet = webDocsMap.get(wd);
		if (resultSet != null) {
			deepCopySet.addAll(resultSet);
			return deepCopySet;
		} else {
			return null;
		}
	}

	/**
	 * This method returns a deep copy of webDocsMap(A HashMap) that contains all
	 * the keys and values stored in this webIndex.
	 * 
	 * @return a Hashmap contains all the keys and values stored in this webIndex.
	 */
	public Map<String, Set<WebDoc>> getWebDocsMap() {
		Map<String, Set<WebDoc>> deepCopySet = new HashMap<>();
		deepCopySet.putAll(webDocsMap);
		return deepCopySet;
	}

	/**
	 * Get a string that provide the overall summary of this WebIndex
	 * 
	 * @return a string of the overall summary of this WebIndex.
	 */
	@Override
	public String toString() {
		return "WebIndex contains " + numOfWords + " from " + numOfDocs + " documents";
	}
}
