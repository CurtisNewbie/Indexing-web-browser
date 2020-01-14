package com.curtisnewbie.webBrowserModel;

import java.util.Stack;
import java.util.TreeSet;

/**
 * It is a builder class that is responsible for building the objects of Query
 * (AndQuery, OrQuery, NotQuery, AtomicQuery). It can deal with both the prefix
 * form query and the infix form query.
 * 
 * @author Yongjie Zhuang
 *
 */
public class QueryBuilder {

	/**
	 * <p>
	 * It deals with the prefix form query. It returns an object of Query based on
	 * the given String. The Query object returned represents the highest level of
	 * the hierarchy, as a Query object can consist of other type of Query objects.
	 * </p>
	 * <p>
	 * This methods uses the helper method {@link #parsePrefixSubQuery(String)} to
	 * create a collection of subqueries to instantiate propery Query objects.
	 * </p>
	 * <p>
	 * E.g., "and(and(cat,dog),sheep)" query will return a AndQuery which contains
	 * two subqueries: 1) "and(cat,doc)" and 2) "sheep". Its subquery can be nested,
	 * thus the nested queries can be further broken down until they are atomic
	 * query (simple word) or NotQuery.
	 * </p>
	 * 
	 * @param q a prefix query
	 * @return a Query object
	 */
	public static Query parse(String q) {
		String wholeQuery = q.toLowerCase();
		wholeQuery = wholeQuery.replaceAll("\\s", "");
		Query resultQuery;

		if (wholeQuery.startsWith("and")) {
			// "and(" starting from 4
			String subQueryInBracket = wholeQuery.substring(4, wholeQuery.length() - 1);
			resultQuery = new AndQuery(parsePrefixSubQuery(subQueryInBracket));
		} else if (wholeQuery.startsWith("not")) {
			// "not(" starting from 4
			String notQuery = wholeQuery.substring(4, wholeQuery.length() - 1);
			resultQuery = new NotQuery(notQuery);
		} else if (wholeQuery.startsWith("or")) {
			// "or(" starting from 3
			String subQueryInBracket = wholeQuery.substring(3, wholeQuery.length() - 1);
			resultQuery = new OrQuery(parsePrefixSubQuery(subQueryInBracket));
		} else {
			resultQuery = new AtomicQuery(wholeQuery); // an atomic query (a simple word that will be searched. E.g,.
														// "apple", which doesn't start with any operator)
		}
		return resultQuery;
	}

	/**
	 * <p>
	 * This method is a helper method to support the {@link #parse(String)} method.
	 * It splits the given subquery further into a number of sub-query based on the
	 * comma and parentheses, and put these subqueries into a TreeSet<String> for
	 * initialising an object of Query(e.g., AndQuery, OrQuery).
	 * </p>
	 * 
	 * @see {@link #parse(String)}
	 * 
	 * @param q the given String that represents the everything within the bracket
	 *          of the AndQuery or OrQuery.
	 * @return a TreeSet<String> of subqueries.
	 */
	private static TreeSet<String> parsePrefixSubQuery(String q) {
		TreeSet<String> subQuery = new TreeSet<>();
		Stack<Character> bracket = new Stack<>();
		int startIndex = 0;

		for (int x = 0; x < q.length(); x++) {
			char tempChar = q.charAt(x);
			if (tempChar == ',' && bracket.empty()) {
				subQuery.add(q.substring(startIndex, x));
				startIndex = x + 1;
			} else if (tempChar == '(' || tempChar == ')') {
				if (!bracket.empty() && bracket.peek() != tempChar) {
					bracket.pop();
				} else {
					bracket.push(tempChar);
				}
			}
		}
		// last subQuery
		subQuery.add(q.substring(startIndex, q.length()));
		return subQuery;
	}

	/**
	 * <p>
	 * This method parse the infix form query into prefix form query, and returns an
	 * object of Query in prefix form.
	 * </p>
	 * It internally uses the helper method {@link #convertInfixString(String)} to
	 * convert the prefix query to infix query, and then calls the method
	 * {@link #parse(String)} to parse this converted query.
	 * 
	 * @see {@link #parse(String)}
	 * @see {@link #convertInfixString(String)}
	 * @param q an infix query
	 * @return a Query object if successful, else {@code NULL}.
	 */
	public static Query parseInfixForm(String q) {
		String prefixQuery = convertInfixString(q);
		if (prefixQuery == null) {
			return null;
		} else {
			return parse(prefixQuery);
		}
	}

	/**
	 * <p>
	 * This is a helper method for converting the infix query to prefix query.
	 * </p>
	 * <p>
	 * It takes the input String, and identifies whether it's a notQuery, andQuery,
	 * orQuery, or atomicQuery. It treats everything within the bracket as a whole,
	 * and it scans the input String from left to right.
	 * </p>
	 * <p>
	 * The first operator that is found outside the bracket will be used to
	 * determine which type of query it is. Once it has identified the type of the
	 * query, the associated method is called to continue converting the subqueries
	 * within the subquery recursively, such as the
	 * {@link #convertAndInfixString(String, String)},
	 * {@link #convertOrInfixString(String, String)} and
	 * {@link #convertNotInfixString(String)}. The atomic query is returned
	 * directly, since it's only a simple word.
	 * <p>
	 * For example:, for the infix query: "(apple or banana) and cat and dog". <br>
	 * 
	 * The first operator is "and" outside the parentheses, so it is converted into
	 * "and((apple or banana),cat and dog)." Then this method identifies this query
	 * as an AND query, thus call the method
	 * {@link #convertAndInfixString(String, String)}. Same logic applied for other
	 * types of query.
	 * </p>
	 * 
	 * @see {@link #parseInfixForm(String)}
	 * @see {@link #convertAndInfixString(String, String)}
	 * @see {@link #convertOrInfixString(String, String)}
	 * @see {@link #convertNotInfixString(String)}
	 * 
	 * @param infixQuery a infix query
	 * @return a (converted) prefix query if successfully, else {@ocde NULL}.
	 */
	private static String convertInfixString(String infixQuery) {
		// preprocessing the query string
		String query = removeCoveringBracket(infixQuery.trim().toLowerCase());
		query = query.replaceAll("\\s{2,}", " ");

		if (!query.contains(" ") && !query.equals("and") && !query.equals("or") && !query.equals("not")) {
			// AtomicQuery
			return query;
		} else if (query.matches("(not\\s?\\(.+\\))|(not\\s[A-Z-a-z]*)")) {
			// NotQuery e.g., "not( ..nested subqueries... )" or "not apple".
			return convertNotInfixString(query.substring(3));
		} else {
			// AND query or OR query or ill-formed query
			Stack<Character> bracket = new Stack<>();
			StringBuilder stringBuilder = new StringBuilder();
			for (int x = 0; x < query.length(); x++) {
				char tempChar = query.charAt(x);
				if (tempChar == ' ' && bracket.isEmpty()) {
					if (stringBuilder.toString().contains("and")) {
						// AndQuery, e.g., with "apple and ..." we can already identify it as an
						// AndQuery
						return convertAndInfixString(query.substring(0, x - 3), query.substring(x));
					} else if (stringBuilder.toString().contains("or")) {
						// OrQuery, e.g., with "apple or ..." we can already identify it as an
						// OrQuery
						return convertOrInfixString(query.substring(0, x - 2), query.substring(x));
					}
				} else if (tempChar == '(' || tempChar == ')') {
					// see whether the current char is within the brackets
					if (!bracket.empty() && bracket.peek() != tempChar) {
						bracket.pop();
					} else {
						bracket.push(tempChar);
					}
				} else if (Character.isLetterOrDigit(tempChar) && bracket.empty()) {
					// only check the next operator that is outside the bracket
					stringBuilder.append(tempChar);
				}
			}
		}
		// ill-formed query
		return null;
	}

	/**
	 * This method deals with the andQuery in infix form, it's part of the recursion
	 * for processing infix query. The QueryBuilder.convertInfixString() is called
	 * for its sub-queries. This forms the recursion.
	 * 
	 * @param left  sub-query on the left
	 * @param right sub-query on the right
	 * @return prefix form query
	 */
	private static String convertAndInfixString(String left, String right) {
		String leftQuery = convertInfixString(left);
		String rightQuery = convertInfixString(right);
		return "and(" + leftQuery + "," + rightQuery + ")";
	}

	/**
	 * This method deals with the orQuery in infix form, it's part of the recursion
	 * for processing infix query. The QueryBuilder.convertInfixString() is called
	 * for its sub-queries. This forms the recursion.
	 * 
	 * @param left  sub-query on the left
	 * @param right sub-query on the right
	 * @return prefix form query
	 */
	private static String convertOrInfixString(String left, String right) {
		String leftQuery = convertInfixString(left);
		String rightQuery = convertInfixString(right);
		return "or(" + leftQuery + "," + rightQuery + ")";
	}

	/**
	 * This method deals with the notQuery in infix form, it's part of the recursion
	 * for processing infix query. The QueryBuilder.convertInfixString() is called
	 * for its sub-queries. This forms the recursion.
	 * 
	 * @param right sub-query on the right
	 * @return prefix form query
	 */
	private static String convertNotInfixString(String right) {
		String rightQuery = convertInfixString(right);
		return "not(" + rightQuery + ")";
	}

	/**
	 * <p>
	 * This is a helper method. It gets rid of pairs of the outside brackets
	 * iteratively.<br>
	 * </p>
	 * For example:
	 * <p>
	 * "(((banana)))" -> "banana"
	 * </p>
	 * <p>
	 * "(((banana)" -> "((banana"
	 * </p>
	 * 
	 * @param q a query
	 * @return a query without the "symmetric" outside brackets.
	 */
	private static String removeCoveringBracket(String q) {
		String thisQuery = q;
		if (thisQuery != null) {
			int len;
			while ((len = thisQuery.length()) > 1 && thisQuery.charAt(0) == '(' && thisQuery.charAt(len - 1) == ')') {
				if (thisQuery == "()")
					return "";
				else
					thisQuery = thisQuery.substring(1, len - 1);
			}
		}
		return thisQuery;
	}
}
