import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It is a builder class that is responsible for building the objects of Query
 * (AndQuery, OrQuery, NotQuery, AtomicQuery). It can deal with both the prefix
 * form query and the infix form query.
 * 
 * @author 180139796
 *
 */
public class QueryBuilder {

	/**
	 * It deals with the prefix form query. It returns an object of Query based on
	 * the given String. This object returned, represents the highest level of the
	 * hierarchy, as a Query object can consist of other type of Query objects.
	 * E.g., and(and(cat,dog),sheep).
	 * 
	 * @param q the given query in the form of String.
	 * @return a Query object
	 */
	public static Query parse(String q) {
		String wholeQuery = q.toLowerCase();
		wholeQuery = wholeQuery.replaceAll("\\s", "");

		if (!wholeQuery.contains("and") && !wholeQuery.contains("or") && !wholeQuery.contains("not")) {
			return new AtomicQuery(wholeQuery);
		} else {
			String notQuery;
			int Starting_Index;
			if (wholeQuery.startsWith("and")) {
				Starting_Index = 4; // "and(" starting from 4
				String subQueryInBracket = wholeQuery.substring(Starting_Index, wholeQuery.length() - 1);
				return new AndQuery(parsePrefixSubquery(subQueryInBracket));
			} else if (wholeQuery.startsWith("not")) {
				Starting_Index = 4;
				notQuery = wholeQuery.substring(Starting_Index, wholeQuery.length() - 1);
				return new NotQuery(notQuery);
			} else { // wholeQuery.startsWith("or");
				Starting_Index = 3; // "or(" starting from 3
				String subQueryInBracket = wholeQuery.substring(Starting_Index, wholeQuery.length() - 1);
				return new OrQuery(parsePrefixSubquery(subQueryInBracket));
			}
		}
	}

	/**
	 * This method is used to support the .parse(String q) method. It splits the
	 * given String into a number of sub-query based on the ',', and put the
	 * sub-query into a TreeSet<String> for initialising an object of Query(e.g.,
	 * AndQuery, OrQuery).
	 * 
	 * E.g., "and(apple, and(banana,cat))";In this case, the given String will be
	 * "apple, and(banana,cat)"
	 * 
	 * @param q the given String that represents the everything within the bracket
	 *          of the AndQuery or OrQuery.
	 * @return a TreeSet<String> of all the sub-query.
	 */
	private static TreeSet<String> parsePrefixSubquery(String q) {
		TreeSet<String> subQuery = new TreeSet<>();
		Stack<Character> bracket = new Stack<>();
		int startIndex = 0;
		int endIndex;

		for (int x = 0; x < q.length(); x++) {
			char tempChar = q.charAt(x);
			if (tempChar == ',' && bracket.empty()) {
				endIndex = x;
				subQuery.add(q.substring(startIndex, endIndex));
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
	 * This method parse the infix form query into prefix form query, and returns an
	 * object of Query in prefix form. It is also used to initiate the recursion for
	 * processing infix query.
	 * 
	 * @param q the query in the forms of String
	 * @return a Query object
	 */
	public static Query parseInfixForm(String q) {
		String prefixQuery = parseInfixString(q);
		return parse(prefixQuery);
	}

	/**
	 * This method is part of the recursion for processing infix query. It takes the
	 * input String, and identifies whether it's a notQuery, andQuery, orQuery, or
	 * atomicQuery. It treats everything within the bracket as a whole, and it scans
	 * the input String from left to right. The first operator that is found outside
	 * the bracket will be used to determine which type of query it is. Once it has
	 * identified the type of the query, the associated method is called, such as
	 * .parseAndInfixString(), .parseOrInfixString(), .parseNoInfixString(). The
	 * atomic query is returned directly.
	 * 
	 * Infix query example: "(apple or banana) and cat and dog" - See explanation.
	 * The first operator is "and" and it is not covered by the bracket, so it is
	 * converted into "and((apple or banana),cat and dog)." Same logic applied for
	 * other types of query.
	 * 
	 * @param str a query (that may consist of sub-queries)
	 * @return a parsed query
	 */
	private static String parseInfixString(String str) {
		String query = str.trim();
		query = parseCoveringBracket(query); // get rid of all the outside closing bracket.
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
			return parseAndInfixString(query.substring(0, startIndex), query.substring(endIndex));
		} else if (operator.equals("or")) {
			return parseOrInfixString(query.substring(0, startIndex), query.substring(endIndex));
		} else if (operator.equals("not")) {
			return parseNotInfixString(query.substring(endIndex));
		} else if (operator.equals("atomic")) {
			return query;
		} else { // it is for debugging, in normal situations, it should never occur.
			System.out.println("parseInfixString(String q) -> Incorrect Format! Query:[" + query + "]");
			return null;
		}
	}

	/**
	 * This method deals with the andQuery in infix form, it's part of the recursion
	 * for processing infix query. The QueryBuilder.parseInfixSting() is called for
	 * its sub-queries. This forms the recursion.
	 * 
	 * @param left  sub-query on the left
	 * @param right sub-query on the right
	 * @return prefix form query
	 */
	private static String parseAndInfixString(String left, String right) {
		String leftQuery = parseInfixString(left);
		String rightQuery = parseInfixString(right);
		return "and(" + leftQuery + "," + rightQuery + ")";
	}

	/**
	 * This method deals with the orQuery in infix form, it's part of the recursion
	 * for processing infix query. The QueryBuilder.parseInfixSting() is called for
	 * its sub-queries. This forms the recursion.
	 * 
	 * @param left  sub-query on the left
	 * @param right sub-query on the right
	 * @return prefix form query
	 */
	private static String parseOrInfixString(String left, String right) {
		String leftQuery = parseInfixString(left);
		String rightQuery = parseInfixString(right);
		return "or(" + leftQuery + "," + rightQuery + ")";
	}

	/**
	 * This method deals with the notQuery in infix form, it's part of the recursion
	 * for processing infix query. The QueryBuilder.parseInfixSting() is called for
	 * its sub-queries. This forms the recursion.
	 * 
	 * @param right sub-query on the right
	 * @return prefix form query
	 */
	private static String parseNotInfixString(String right) {
		String rightQuery = parseInfixString(right);
		return "not(" + rightQuery + ")";
	}

	/**
	 * This is a recursive method. It helps get rid of the outside bracket. See this
	 * example: "(((banana)))" -> "banana"
	 * 
	 * @param q a query
	 * @return a processed query
	 */
	private static String parseCoveringBracket(String q) {
		String thisQuery = q;
		if (thisQuery.length() > 0 && thisQuery.charAt(0) == '(' && thisQuery.charAt(thisQuery.length() - 1) == ')') {
			thisQuery = thisQuery.substring(1, thisQuery.length() - 1);
			if (thisQuery.length() > 0 && thisQuery.charAt(0) == '('
					&& thisQuery.charAt(thisQuery.length() - 1) == ')') {
				thisQuery = parseCoveringBracket(thisQuery);
			}
		}
		return thisQuery;
	}
}
