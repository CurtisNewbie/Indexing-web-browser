package com.curtisnewbie.webBrowserModel;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is used to handle the prefix NotQuery, e.g., not(banana).
 * Theoretically, NotQuery is the 'end' of the recursion.
 * 
 * @author Yongjie Zhuang
 *
 */
public class NotQuery implements Query {

	/**
	 * A subquery or simple word of this NotQuery
	 */
	private String query;

	/**
	 * Instantiate NotQuery
	 * 
	 * @param s the subquery or a simple word of this NotQuery.
	 */
	public NotQuery(String s) {
		query = s;
	}

	/**
	 * This method searches through the given WebIndex based on the query to find
	 * all the matched results. It returns a set that contains all the results
	 * excluding the results for the NotQuery.
	 * 
	 * @return a Set<WebDoc> contains all the results in the webIndex excluding the
	 *         results of this NotQuery.
	 * @param wind the WebIndex that is used to search through based on the query.
	 * 
	 */
	@Override
	public Set<WebDoc> matches(WebIndex wind) {
		Set<WebDoc> resultOfNotQuery;
		Map<String, Set<WebDoc>> webDocsMapCopy;
		webDocsMapCopy = wind.getWebDocsMap();

		Collection<Set<WebDoc>> allValues = webDocsMapCopy.values();
		Set<WebDoc> allWebDoc = new TreeSet<>();

		// get all the webdocs
		for (Set<WebDoc> set : allValues) {
			for (WebDoc doc : set) {
				allWebDoc.add(doc);
			}
		}
		// get the webdocs of not query
		resultOfNotQuery = webDocsMapCopy.get(query);

		// remove the webdocs of not query
		if (resultOfNotQuery == null) {
			return allWebDoc;
		} else {
			allWebDoc.removeAll(resultOfNotQuery);
			return allWebDoc;
		}
	}

	/**
	 * <p>
	 * This method returns a String that indicates the type of the query and its
	 * subquery. The subquery is indicated using '[' and ']'.
	 * </p>
	 * <p>
	 * E.g., not(banana) -> Not([banana])
	 * </p>
	 * 
	 * @return a string that indicates the type of this query as well as the
	 *         subquery of this query.
	 */
	@Override
	public String toString() {
		return "NOT([" + query + "])";
	}

}
