import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class OrQuery implements Query {

	private TreeSet<String> queryStr;
	private ArrayList<Set<WebDoc>> subQueryResult;

	public OrQuery(TreeSet<String> queryCol) {
		queryStr = queryCol;
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
		this.removeNull();
		
		for (Set<WebDoc> eachSet : subQueryResult) {
			subQueryResult.get(0).addAll(eachSet);
		}
		return subQueryResult.get(0);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> eachQuery = queryStr.iterator();

		while (eachQuery.hasNext()) {
			sb.append(eachQuery.next());
		}
		return sb.toString();
	}

	private void removeNull() {
		Iterator<Set<WebDoc>> iteratorOfSet = subQueryResult.iterator();
		while (iteratorOfSet.hasNext()) {
			Set<WebDoc> eachSet = iteratorOfSet.next();
			if (eachSet == null) {
				iteratorOfSet.remove();
			}
		}
	}

}
