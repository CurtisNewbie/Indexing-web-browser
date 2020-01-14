package com.curtisnewbie.webBrowserModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is used to handle the prefix AndQuery, e.g., and(banana,apple). A
 * AndQuery consists of a number of subqueries, where the sub-query can be an
 * AndQuqery object as well. Consider a query that can be nested with many
 * layers.
 * 
 * @author Yongjie Zhuang
 */
public class AndQuery implements Query {

	/**
	 * A collection of sub-queries of this AndQuery, it can have more than two
	 * subqueryies in case of the prefix form.
	 */
	private TreeSet<String> subQueryCollection;

	/**
	 * Instantiate AndQuery with one or more subqueries.
	 * 
	 * @param subQuery a TreeSet<String> of subqueries.
	 */
	public AndQuery(TreeSet<String> subQuery) {
		this.subQueryCollection = subQuery;
	}

	/**
	 * <p>
	 * It is part of the recursion that the matches() method will call the
	 * QueryBuilder.parse() to parse the sub-query of this AndQuery object, and then
	 * calls the matches() of each subquery object to get the result for each
	 * subquery.
	 * </p>
	 * <p>
	 * This method searches through the given WebIndex based on its subqueries.
	 * Since this is an AndQuery, it finds and combine the common parts of each
	 * subquery.
	 * </p>
	 * 
	 * @return a Set<WebDoc> that is found based on the query and the given
	 *         WebIndex.
	 * @param wind the WebIndex that is used to search through based on the query.
	 * @Override
	 */
	public Set<WebDoc> matches(WebIndex wind) {
		List<Set<WebDoc>> queryResults = new ArrayList<>();
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
	 * <p>
	 * This method returns a String indicating the type of the query and its
	 * subqueries (that may not yet been parsed). The subqueries are indicated using
	 * '[' and ']'.
	 * </p>
	 * <p>
	 * E.g., and(A,and(C,D)) -> AND([A],[and(C,D)])
	 * </p>
	 * 
	 * @return a string that indicates the type of this query as well as its
	 *         subqueries.
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
