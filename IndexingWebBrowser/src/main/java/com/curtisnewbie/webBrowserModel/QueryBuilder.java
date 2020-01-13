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
	 * This method is part of the recursion for processing infix query. It takes the
	 * input String, and identifies whether it's a notQuery, andQuery, orQuery, or
	 * atomicQuery. It treats everything within the bracket as a whole, and it scans
	 * the input String from left to right. The first operator that is found outside
	 * the bracket will be used to determine which type of query it is. Once it has
	 * identified the type of the query, the associated method is called, such as
	 * .convertAndInfixString(), .convertOrInfixString() and
	 * .convertNotInfixString(). The atomic query is returned directly.
	 * 
	 * Infix query example: "(apple or banana) and cat and dog" - See explanation.
	 * The first operator is "and" and it is not covered by the bracket, so it is
	 * converted into "and((apple or banana),cat and dog)." Same logic applied for
	 * other types of query.
	 * 
	 * @param str a query (that may consist of sub-queries)
	 * @return a (converted) prefix query
	 */
	private static String convertInfixString(String str) {
		String query = str.trim();
		query = removeCoveringBracket(query); // get rid of all the outside closing bracket.
		Stack<Character> bracket = new Stack<>();
		String operator = "default";
		int startIndex = 0;
		int endIndex = 0;
		StringBuilder stringBuilder = new StringBuilder();

		if (query.matches("(not\\s*\\(.+\\))|(not\\s*[A-Z-a-z]*)")) { // NotQuery
			operator = "not";
			endIndex = 3;
		} else if (!query.contains("and") && !query.contains("not") && !query.contains("or")
				|| query.matches("(and\\(.+,.+\\))|(or\\(.+,.+\\))")) {
			operator = "atomic"; // AtomicQuery
		} else {
			for (int x = 0; x < query.length(); x++) {
				char tempChar = query.charAt(x);
				if (tempChar == ' ' && bracket.isEmpty()) {
					if (stringBuilder.toString().contains("and")) { // AndQuery
						operator = "and";
						startIndex = x - 3;
						endIndex = x;
						break;
					} else if (stringBuilder.toString().contains("or")) { // OrQuery
						operator = "or";
						startIndex = x - 2;
						endIndex = x;
						break;
					}
				} else if (tempChar == '(' || tempChar == ')') { // see whether the current char is within the brackets
					if (!bracket.empty() && bracket.peek() != tempChar) {
						bracket.pop();
					} else {
						bracket.push(tempChar);
					}
				} else if (tempChar != ' ' && tempChar != '(' && tempChar != ')' && bracket.empty()) {
					// only check the next operator that is outside the bracket
					stringBuilder.append(tempChar);
				}
			}
		}
		// check what type of query it is.
		if (operator.equals("and")) {
			return convertAndInfixString(query.substring(0, startIndex), query.substring(endIndex));
		} else if (operator.equals("or")) {
			return convertOrInfixString(query.substring(0, startIndex), query.substring(endIndex));
		} else if (operator.equals("not")) {
			return convertNotInfixString(query.substring(endIndex));
		} else if (operator.equals("atomic")) {
			return query;
		} else { // it is for debugging, in normal situations, it should never occur.
			// System.out.println("parseInfixString(String q) -> Incorrect Format! Query:["
			// + query + "]");
			return null;
		}
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
