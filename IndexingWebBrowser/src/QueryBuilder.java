import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String prefixQuery = parseInfixString(q);
		return parse(prefixQuery);
	}

	private static String parseInfixString(String str) {
		String query = str.trim();
		query = parseCoveringBracket(query); // get rid of all the outside closing bracket.
		Stack<Character> bracket = new Stack<>();
		String operator = "default"; 
		int startIndex = 0;
		int endIndex = 0;
		StringBuilder stringBuilder = new StringBuilder();

		if (query.matches("not\\s*\\(.+\\)")) { // NotQuery
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
			return parseAndInfix(query.substring(0, startIndex), query.substring(endIndex));
		} else if (operator.equals("or")) {
			return parseOrInfix(query.substring(0, startIndex), query.substring(endIndex));
		} else if (operator.equals("not")) {
			return parseNotInfix(query.substring(endIndex));
		} else if (operator.equals("atomic")) {
			return query;
		} else { // it is for debugging, normal situation it should never occur
			System.out.println("Incorrect Format!");
			System.out.println(query);
			return null;
		}
	}

	private static String parseAndInfix(String left, String right) {
		String leftQuery = parseInfixString(left);
		String rightQuery = parseInfixString(right);
		return "and(" + leftQuery + "," + rightQuery + ")";
	}

	private static String parseOrInfix(String left, String right) {
		String leftQuery = parseInfixString(left);
		String rightQuery = parseInfixString(right);
		return "or(" + leftQuery + "," + rightQuery + ")";
	}

	private static String parseNotInfix(String right) {
		String rightQuery = parseInfixString(right);
		return "not(" + rightQuery + ")";
	}

	// recursive method
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
