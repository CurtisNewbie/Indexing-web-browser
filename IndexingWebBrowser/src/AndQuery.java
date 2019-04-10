import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is used to handle the prefix AndQuery, e.g., and(banana,apple). It
 * consist of a number of sub-query. The sub-query can be an AndQuqery object as
 * well.
 * 
 * @author 180139796
 */
public class AndQuery implements Query {

	/**
	 * It represents the sub-queries of this AndQuery, it can be more than two in
	 * case of the prefix form.
	 */
	private TreeSet<String> subQueryCollection;

	/**
	 * It's a collection of results of all subqueries.
	 */
	private ArrayList<Set<WebDoc>> queryResults;

	/**
	 * Constructor of AndQuery that initialises the instance
	 * variables(normalQueryResult, notQueryResult and subQueryCollection).
	 * 
	 * @param subQuery a TreeSet<String> of sub-query.
	 */
	public AndQuery(TreeSet<String> subQuery) {
		this.subQueryCollection = subQuery;
		queryResults = new ArrayList<>();
	}

	/**
	 * It is part of the recursion that the matches() method will call the
	 * QueryBuilder.parse() to parse the sub-query of this AndQuery object, and then
	 * calls the matches() of the sub-query.
	 * 
	 * This method searches through the given WebIndex based on the query to find
	 * all the matched results. The AndQuery object finds the common parts of each
	 * sub-queries.
	 * 
	 * @return a Set<WebDoc> that is found based on the query and the given
	 *         WebIndex.
	 * @param wind the WebIndex that is used to search through based on the query.
	 * @Override
	 */
	public Set<WebDoc> matches(WebIndex wind) {
		// Get the results of all the sub-queries
		for (String eachQuery : subQueryCollection) {
			Query subQuery = QueryBuilder.parse(eachQuery);
			queryResults.add(subQuery.matches(wind));
		}

		/*
		 * Retain all the common elements for the sub-queries that are not NotQuery, and
		 * put them into the finalNormalQueryResult.
		 */
		Set<WebDoc> finalQueryResult = new TreeSet<>();
		finalQueryResult = queryResults.get(0);
		for (Set<WebDoc> eachSet : queryResults) {
			if (eachSet != null) {
				finalQueryResult.retainAll(eachSet);
			} else { // If one of the result is null, then the final result should be null as well.
				finalQueryResult = null;
				break;
			}
		}
		return finalQueryResult;
	}

	/**
	 * This method returns a String indicate the type of the query and the
	 * sub-query. The sub-query is indicated using '[' and ']'. E.g.,
	 * and(A,and(C,D)) -> AND([A],[and(C,D)])
	 * 
	 * @return a string that indicates the type of this query as well as the
	 *         sub-query of this query.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("AND(");
		Iterator<String> eachQuery = subQueryCollection.iterator();

		while (eachQuery.hasNext()) {
			stringBuilder.append("[" + eachQuery.next() + "]");
			if (eachQuery.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
}
