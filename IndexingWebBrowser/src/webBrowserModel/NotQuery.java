package webBrowserModel;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is used to handle the prefix NotQuery, e.g., not(banana).
 * Theoretically, NotQuery is the 'end' of the recursion.
 * 
 * @author 180139796
 *
 */
public class NotQuery implements Query {

	/**
	 * It indicates the sub-query of this NotQuery. As it's a NotQuery, it only has
	 * one sub-query.
	 */
	private String query;

	/**
	 * Constructor of NotQuery that assigns a value to the (instance variable)
	 * query.
	 * 
	 * @param s the sub-query of the NotQuery.
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
		Set<WebDoc> resultOfNotQuery = new TreeSet<>();
		Map<String, Set<WebDoc>> webDocsMapCopy;
		webDocsMapCopy = wind.getWebDocsMap();

		Set<String> allKeySet = webDocsMapCopy.keySet();
		Set<String> keySetWithoutNot = new TreeSet<>();
		for (String str : allKeySet) {
			if (!str.equals(query)) {
				keySetWithoutNot.add(str);
			}
		}

		for (String str : keySetWithoutNot) {
			try {
				resultOfNotQuery.addAll(wind.getMatches(str));
			} catch (NullPointerException e) {
				// If this result is null, skip it.
			}
		}
		return resultOfNotQuery;
	}

	/**
	 * This method returns a String indicate the type of the query and the
	 * sub-query.The sub-query is indicated using '[' and ']'. E.g., not(banana) ->
	 * Not([banana])
	 * 
	 * @return a string that indicates the type of this query as well as the
	 *         sub-query of this query.
	 */
	@Override
	public String toString() {
		return "NOT([" + query + "])";
	}

}
