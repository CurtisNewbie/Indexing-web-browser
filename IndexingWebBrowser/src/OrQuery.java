import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is used to handle the prefix OrQuery, e.g.,
 * or(banana,apple). It is part of the recursion that the matches() method will
 * call the QueryBuilder.parse() to parse the sub-query of this OrQuery object,
 * the sub-query can be an OrQuqery object as well.
 * 
 * @author 180139796
 */
public class OrQuery implements Query {

	/**
	 * It represents the sub-query of this AndQuery, it can be more than two in case
	 * of the prefix form.
	 */
	private TreeSet<String> subQuery;

	/**
	 * It is a collection of results of the matches() of all the sub-query.
	 */
	private ArrayList<Set<WebDoc>> subQueryResult;

	/**
	 * Constructor of AndQuery that initialises the instance variables(subQuery and
	 * subQueryResult).
	 * 
	 * @param subQuery a TreeSet<String> of sub-query.
	 */
	public OrQuery(TreeSet<String> subQuery) {
		this.subQuery = subQuery;
		subQueryResult = new ArrayList<>();
	}

	/**
	 * This method searches through the given WebIndex based on the query to find
	 * all the matched results. The OrQuery object finds the matched results of all
	 * the sub-query and puts them together. As it is OrQuery, the notQuery is
	 * ignored, as it will not affect the final result.
	 * 
	 * @return a Set<WebDoc> that is found based on the query and the given
	 *         WebIndex.
	 * @param wind the WebIndex that is used to search through based on the query.
	 * 
	 */
	@Override
	public Set<WebDoc> matches(WebIndex wind) {
		// Get the results of all the sub-queries
		for (String eachQuery : subQuery) {
			Query subQuery = QueryBuilder.parse(eachQuery);
			if (!(subQuery instanceof NotQuery)) { // In OrQuery, the NotQuery is not important.
				subQueryResult.add(subQuery.matches(wind));
			}
		}
		Set<WebDoc> finalSubQueryResult = new TreeSet<>();
		for (Set<WebDoc> eachSet : subQueryResult) {
			if (eachSet != null) {
				finalSubQueryResult.addAll(eachSet);
			}
		}
		return finalSubQueryResult;
	}

	/**
	 * This method returns a String indicate the type of the query and the
	 * sub-query.The sub-query is indicated using '[' and ']'. E.g., or(A,and(C,D)
	 * -> OR([A],[and(C,D)])
	 * 
	 * @return a string that indicates the type of this query as well as the
	 *         sub-query of this query.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("OR(");
		Iterator<String> eachQuery = subQuery.iterator();

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
