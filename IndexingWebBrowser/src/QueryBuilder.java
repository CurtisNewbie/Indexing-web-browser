import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.prism.impl.shape.OpenPiscesRasterizer;

public class QueryBuilder {

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

	public static Query parseInfixForm(String q) {
		String prefixQuery = parseInfix(q);
		return parse(prefixQuery);
	}

	private static String parseInfix(String str) {
		String query = str.trim();
		query = parseCoveringBracket(query);
		Stack<Character> bracket = new Stack<>();
		String operator = "atomic";
		int startIndex = 0;
		int endIndex = 0;
		StringBuilder stringBuilder = new StringBuilder();

		if (query.startsWith("not") && !query.contains("and") && !query.contains("or")) {
			operator = "not";
			endIndex = 3;
		} else if (!query.contains("and") && !query.contains("not") && !query.contains("or")) {
			operator = "atomic";
		} else {
			for (int x = 0; x < query.length(); x++) {
				char tempChar = query.charAt(x);
				if (tempChar == ' ' && bracket.isEmpty()) {
					if (stringBuilder.toString().contains("and")) {
						operator = "and";
						startIndex = x - 3;
						endIndex = x;
						break;
					} else if (stringBuilder.toString().contains("or")) {
						operator = "or";
						startIndex = x - 2;
						endIndex = x;
						break;
					}
				} else if (tempChar == '(' || tempChar == ')') {
					if (!bracket.empty() && bracket.peek() != tempChar) {
						bracket.pop();
					} else {
						bracket.push(tempChar);
					}
				} else if (tempChar != ' ' && tempChar != '(' && tempChar != ')' && bracket.empty()) {
					stringBuilder.append(tempChar);
				}
			}
		}
		if (operator.equals("and")) {
			return andInfix(query.substring(0, startIndex), query.substring(endIndex));
		} else if (operator.equals("or")) {
			return orInfix(query.substring(0, startIndex), query.substring(endIndex));
		} else if (operator.equals("not")) {
			return notInfix(query.substring(endIndex));
		} else if (operator.equals("atomic")) {
			return query;
		} else {
			System.out.println("Error");
			return null;
		}
	}

	public static String andInfix(String left, String right) {
		String leftQuery = parseInfix(left);
		String rightQuery = parseInfix(right);

		return "and(" + leftQuery + "," + rightQuery + ")";
	}

	public static String orInfix(String left, String right) {
		String leftQuery = parseInfix(left);
		String rightQuery = parseInfix(right);
		return "or(" + leftQuery + "," + rightQuery + ")";
	}

	public static String notInfix(String right) {
		String rightQuery = parseInfix(right);
		return "not(" + rightQuery + ")";
	}

	// recursive method
	public static String parseCoveringBracket(String q) {
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
