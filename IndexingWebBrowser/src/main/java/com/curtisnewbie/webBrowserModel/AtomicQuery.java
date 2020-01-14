package com.curtisnewbie.webBrowserModel;

import java.util.Set;

/**
 * This class is used to handle the prefix AtomicQuery, e.g., banana.
 * Theoretically, AtomicQuery is the 'end' of the recursion.
 * 
 * @author Yongjie Zhuang
 *
 */
public class AtomicQuery implements Query {
	/**
	 * It's the AtomicQuery itself or the word that is searched.
	 */
	private String query;

	/**
	 * Instantiate AtomicQuery
	 * 
	 * @param s a simple word as the query
	 */
	public AtomicQuery(String s) {
		query = s;
	}

	/**
	 * This method searches through the given WebIndex based on the query to find
	 * all the matched results.
	 * 
	 * @return a Set of WebDoc that is found based on the query and the given
	 *         WebIndex.
	 * @param wind the WebIndex that is being searched based on the query.
	 * 
	 */
	@Override
	public Set<WebDoc> matches(WebIndex wind) {
		return wind.getMatches(query);
	}

	/**
	 * <p>
	 * This method returns a String that indicates the type of the query and the
	 * word that is searched. The word is indicated using '[' and ']'.
	 * </p>
	 * <p>
	 * E.g., banana -> ATOMIC:[banana]
	 * </p>
	 * 
	 * @return a string that indicates the type of this query as well as the word
	 *         that is searched.
	 */
	public String toString() {
		return "ATOMIC:[" + query + "]";
	}

}
