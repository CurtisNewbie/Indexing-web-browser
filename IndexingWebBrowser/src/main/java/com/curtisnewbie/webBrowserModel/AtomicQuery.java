package webBrowserModel;
import java.util.Set;

/**
 * This class is used to handle the prefix AtomicQuery, e.g., banana.
 * Theoretically, AtomicQuery is the 'end' of the recursion.
 * 
 * @author 180139796
 *
 */
public class AtomicQuery implements Query {
	/**
	 * It's the AtomicQuery itself or the word that is searched.
	 */
	private String query;

	/**
	 * Constructor of AtomicQuery that assigns the value to its (instance variable)
	 * query.
	 * 
	 * @param s the AtomicQuery itself or the word that is searched.
	 */
	public AtomicQuery(String s) {
		query = s;
	}

	/**
	 * This method searches through the given WebIndex based on the query to find
	 * all the matched results.
	 * 
	 * @return a Set<WebDoc> that is found based on the query and the given
	 *         WebIndex.
	 * @param wind the WebIndex that is used to search through based on the query.
	 * 
	 */
	@Override
	public Set<WebDoc> matches(WebIndex wind) {
		return wind.getMatches(query);
	}

	/**
	 * This method returns a String indicate the type of the query and the
	 * sub-query.The sub-query is indicated using '[' and ']'. E.g., banana ->
	 * ATOMIC:[banana]
	 * 
	 * @return a string that indicates the type of this query as well as the
	 *         sub-query of this query.
	 */
	public String toString() {
		return "ATOMIC:[" + query + "]";
	}

}
