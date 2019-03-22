import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import javafx.animation.Interpolator;

/**
 * This class is used to handle the prefix AndQuery, e.g., and(banana,apple). It
 * is part of the recursion that the matches() method will call the
 * QueryBuilder.parse() to parse the sub-query of this AndQuery object, the
 * sub-query can be an AndQuqery object as well.
 * 
 * @author 180139796
 *
 */
public class AndQuery implements Query {

	/**
	 * It represents the sub-query of this AndQuery, it can be more than two in case
	 * of the prefix form.
	 */
	private TreeSet<String> subQuery;

	/**
	 * The normal query refers to the query that is not NotQuery (AndQuery, OrQuery,
	 * AtomicQuery). The result refers to the result of the matches() of the
	 * sub-query. Thus, it's a collection of results of the 'normal' query.
	 */
	private ArrayList<Set<WebDoc>> normalQueryResult;

	/**
	 * It is a collection of results of the matches() of the NotQuery.
	 */
	private ArrayList<Set<WebDoc>> notQueryResult;

	/**
	 * Constructor of AndQuery that initialises the instance variables(normalQueryResult,
	 * notQueryResult and subQuery).
	 * 
	 * @param subQuery a TreeSet<String> of sub-query.
	 */
	public AndQuery(TreeSet<String> subQuery) {
		this.subQuery = subQuery;
		normalQueryResult = new ArrayList<>();
		notQueryResult = new ArrayList<>();
	}

	/**
	 * This method search through the given WebIndex based on the query to find all
	 * the matched results. The AndQuery finds the common parts of each sub-queries
	 * except the NotQuery, if there is NotQuery, the final result should exclude
	 * the results of the NotQuery.
	 * 
	 * @return a Set<WebDoc> that is found based on the query and the given
	 *         WebIndex.
	 * @param wind the WebIndex that is used to search through based on the query.
	 * @Override
	 */
	public Set<WebDoc> matches(WebIndex wind) {
		// Get the results of all the sub-queries
		for (String eachQuery : subQuery) {
			Query subQuery = QueryBuilder.parse(eachQuery);
			if (subQuery instanceof NotQuery) {
				notQueryResult.add(subQuery.matches(wind));
			} else {
				normalQueryResult.add(subQuery.matches(wind));
			}
		}
		/*
		 * Retain all the common elements for the sub-queries that are not NotQuery, and
		 * put them into the finalNormalQueryResult.
		 */
		Set<WebDoc> finalNormalQueryResult = new TreeSet<>();
		finalNormalQueryResult = normalQueryResult.get(0);
		for (Set<WebDoc> eachSet : normalQueryResult) {
			if (eachSet != null) {
				finalNormalQueryResult.retainAll(eachSet);
			} else { // If one of the result is null, then the final result should be null as well.
				finalNormalQueryResult = null;
				break;
			}
		}
		/*
		 * If the finalNormalQueryResult is not null, it retain all the common elements
		 * for all the NotQuery and find the difference between the
		 * finalNormalQueryResult and the finalNotQueryResult.
		 */
		Set<WebDoc> finalNotQueryResult = new TreeSet<>();
		if (!notQueryResult.isEmpty() && finalNormalQueryResult != null) {
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

	/**
	 * This method returns a String indicate the type of the query and the
	 * sub-query.The sub-query is indicated using '[' and ']'. 
	 * E.g., and(A,and(C,D) -> AND([A],[and(C,D)])
	 * 
	 * @return a string that indicates the type of this query as well as the
	 *         sub-query of this query.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("AND(");
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
