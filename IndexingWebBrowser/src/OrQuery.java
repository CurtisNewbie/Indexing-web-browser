import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class OrQuery implements Query {

	private TreeSet<String> queryStr;
	private ArrayList<Set<WebDoc>> subQueryResult;

	public OrQuery(TreeSet<String> queryCol) {
		queryStr = queryCol;
		subQueryResult = new ArrayList<>();
	}

	@Override
	public Set<WebDoc> matches(WebIndex wind) {
		// Get the results of all the sub-queries
		for (String eachQuery : queryStr) {
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> eachQuery = queryStr.iterator();

		while (eachQuery.hasNext()) {
			sb.append(eachQuery.next() + " ");
		}
		return sb.toString();
	}

}
