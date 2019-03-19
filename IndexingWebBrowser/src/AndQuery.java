import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javafx.animation.Interpolator;

public class AndQuery implements Query {

	private TreeSet<String> queryStr;
	private ArrayList<Set<WebDoc>> normalQueryResult;
	private ArrayList<Set<WebDoc>> notQueryResult;

	public AndQuery(TreeSet<String> SubQuery) {
		queryStr = SubQuery;
	}

	@Override
	public Set<WebDoc> matches(WebIndex wind) {
		// Get the results of all the sub-queries
		for (String eachQuery : queryStr) {
			Query subQuery = QueryBuilder.parse(eachQuery);
			if (subQuery instanceof NotQuery) {
				notQueryResult.add(subQuery.matches(wind));
			} else {
				normalQueryResult.add(subQuery.matches(wind));
			}
		}
		this.removeNull();

		// Retain all the common elements for the sub-queries that
		// are not NotQuery
		for (Set<WebDoc> eachSet : normalQueryResult) {
			normalQueryResult.get(0).retainAll(eachSet);
		}

		// Put all the Sets of NotQuery together.
		for (Set<WebDoc> eachSet : notQueryResult) {
			notQueryResult.get(0).addAll(eachSet);
		}
		normalQueryResult.get(0).removeAll(notQueryResult.get(0)); // The final result.
		return normalQueryResult.get(0);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> eachQuery = queryStr.iterator();

		while (eachQuery.hasNext()) {
			sb.append(eachQuery.next() + " ");
		}
		return sb.toString();
	}

	private void removeNull() {
		Iterator<Set<WebDoc>> iteratorOfSet = normalQueryResult.iterator();
		while (iteratorOfSet.hasNext()) {
			Set<WebDoc> eachSet = iteratorOfSet.next();
			if (eachSet == null) {
				iteratorOfSet.remove();
			}
		}

		Iterator<Set<WebDoc>> iteratorOfNotQuery = notQueryResult.iterator();
		while (iteratorOfNotQuery.hasNext()) {
			Set<WebDoc> eachSet = iteratorOfSet.next();
			if (eachSet == null) {
				iteratorOfSet.remove();
			}
		}
	}

}
