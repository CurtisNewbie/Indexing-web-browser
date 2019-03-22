import java.util.Set;

/**
 * This class is used to handle the prefix NotQuery, e.g., not(banana). It
 * is part of the recursion that the matches() method will call the
 * QueryBuilder.parse() to parse the sub-query of this NotQuery object, the
 * sub-query can be an NotQuqery object as well.
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
	 * Constructor of NotQuery that assigns a value to the (instance variable) query.
	 * @param s the sub-query of the NotQuery.
	 */
	public NotQuery(String s) {
		query = s;
	}

	/**
	 * This method searches through the given WebIndex based on the query to find all
	 * the matched results. Although it's a notQuery, the matched result will be
	 * deal with in the object of AndQuery or the object of OrQuery.
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
	 * sub-query.The sub-query is indicated using '[' and ']'. E.g., not(A and B) ->
	 * Not([A and B])
	 * 
	 * @return a string that indicates the type of this query as well as the
	 *         sub-query of this query.
	 */
	@Override
	public String toString() {
		return "NOT([" + query + "])";
	}

}
