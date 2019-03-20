import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import javafx.animation.Interpolator;

public class AndQuery implements Query {

	private TreeSet<String> queryStr;
	private ArrayList<Set<WebDoc>> normalQueryResult;
	private ArrayList<Set<WebDoc>> notQueryResult;

	public AndQuery(TreeSet<String> SubQuery) {
		queryStr = SubQuery;
		normalQueryResult = new ArrayList<>();
		notQueryResult = new ArrayList<>();
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

		// Retain all the common elements for the sub-queries that
		// are not NotQuery
		Set<WebDoc> finalNormalQueryResult = new TreeSet<>();
		for (Set<WebDoc> eachSet : normalQueryResult) {
			if (eachSet != null) {
				finalNormalQueryResult.retainAll(eachSet);
			}
		}

		// Retain all the common elements for all the NotQuery
		Set<WebDoc> finalNotQueryResult = new TreeSet<>();
		if (!notQueryResult.isEmpty()) {
			// Put all the Sets of NotQuery together.
			for (Set<WebDoc> eachSet : notQueryResult) {
				if (eachSet != null) {
					finalNotQueryResult.addAll(eachSet);
				}
			}
			finalNormalQueryResult.removeAll(finalNotQueryResult);
		}
		return finalNormalQueryResult;
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

}
