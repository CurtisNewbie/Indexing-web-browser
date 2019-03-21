import java.util.ArrayList;
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

//	// return query
//	public static Query parseInfixForm(String q) {
//		String wholeQuery = q.toLowerCase();
//		StringBuilder parsedQuery = new StringBuilder();
//
//		if (wholeQuery.startsWith("(") && wholeQuery.endsWith(")")) {
//			wholeQuery = wholeQuery.substring(1, wholeQuery.length() - 1);
//		}
//		// extract the operator in sequence
//		ArrayList<String> operators = extractOperator(wholeQuery);
//		// split the order by operators
//		ArrayList<String> splitedQuery = splitQuery(wholeQuery);
//
//		Stack<Character> brackets = new Stack<>();
//		ArrayList<String> combinedQuery = new ArrayList<>();
//		for (int x = 0; x < operators.size(); x++) {
//			if (operators.get(x).equals("not")) {
//				if (x == 0) {
//					combinedQuery.add("not" + splitedQuery.get(x));
//				} else {
//					combinedQuery.add("not" + splitedQuery.get(x + 1));
//				}
//			} else {
//				if (splitedQuery.get(x).contains("(")) {
//					// Found '(' and push them into the stack
//					for (int y = 0; y < splitedQuery.get(x).length(); y++) {
//						if (splitedQuery.get(x).charAt(y) == '(') {
//							brackets.push('(');
//						}
//					}
//					// Check the other splited query and see if there is ')'
//					int otherSplitedQuery = x;
//					boolean hasBracket = true;
//					while (!hasBracket && otherSplitedQuery < splitedQuery.size()) {
//						otherSplitedQuery++;
//						char[] splitedQueryChar = splitedQuery.get(otherSplitedQuery).toCharArray();
//						for (char c : splitedQueryChar) {
//							if (c == ')') {
//								brackets.pop();
//							} else if (c == '(') {
//								brackets.push('(');
//							}
//						}
//						splitedQuery.set(x,
//								splitedQuery.get(x) + operators.get(x) + splitedQuery.get(otherSplitedQuery));
//						splitedQuery.remove(otherSplitedQuery);
//						if (brackets.isEmpty()) {
//							hasBracket = false;
//						}
//					}
//				}
//			}
//		}
//		return splitedQuery.toString();
//	}

	// used recursion
	public static String extractInfixQuery(String q) {
		String wholeQuery = q.toLowerCase();
		wholeQuery = wholeQuery.replaceAll("\\s+", " "); // avoid multiple whitespace
		StringBuilder finalResult = new StringBuilder();
		char[] wholeQueryChar = wholeQuery.toCharArray();
		Stack<Character> brackets = new Stack<>();
		ArrayList<String> parsedQuery = new ArrayList<>();

		StringBuilder eachPart = new StringBuilder();
		for (char c : wholeQueryChar) {
			if (c == '(') {
				brackets.push('(');
				eachPart.append(c);
			} else if (c == ')') {
				brackets.pop();
				if (brackets.isEmpty()) {
					eachPart.append(c);
					String tempString = eachPart.toString().substring(1, eachPart.length() - 1);
					if (tempString.contains("(") || countOperators(tempString) > 1) {
						parsedQuery.add(extractInfixQuery(eachPart.toString()));
						eachPart = new StringBuilder();
					} else {
						parsedQuery.add(parseInfixToPrefix(tempString));
						eachPart = new StringBuilder();
					}
				}
			} else if (c == ' ') {
				if (eachPart.length() == 0) {
					// do nothing
				} else if (eachPart.toString().matches("(and)|(not)|(or)")) { // this part is purely an operator
					parsedQuery.add(eachPart.toString());
					eachPart = new StringBuilder();
				} else if (eachPart.toString().contains("(and)|(not)|(or)")) { // this part contains an operator
					parsedQuery.add(extractInfixQuery(eachPart.toString()));
					eachPart = new StringBuilder();
				}
			} else {
				eachPart.append(c);
			}
		}
		return combineParsedQuery(parsedQuery);
	}

	private static String combineParsedQuery(ArrayList<String> parsedQuery) {
		ArrayList<String> queryCombination = new ArrayList<>();
		System.out.println(parsedQuery.toString());
		StringBuilder result = new StringBuilder();
		boolean hasOperator = false;
		for (String str : parsedQuery) {
			if (str.matches("(and)|(or)")) {
				hasOperator = true;
			}
		}

		if (hasOperator) {
			for (int x = 0; x < parsedQuery.size(); x++) {
				if (parsedQuery.get(x).matches("(and)|(or)")) {
					queryCombination.add(
							parsedQuery.get(x) + "(" + parsedQuery.get(x - 1) + "," + parsedQuery.get(x + 1) + ")");
				} else if (parsedQuery.get(x).matches("not")) {
					queryCombination.add(parsedQuery.get(x) + "(" + parsedQuery.get(x + 1) + ")");
				}
			}
			for (String str : queryCombination) {
				result.append(str);
			}
		} else {
			for (String str : parsedQuery) {
				result.append(str);
			}
		}
		return result.toString();
	}

	private static int countOperators(String tempString) {
		Pattern operatorPattern = Pattern.compile("(and)|(not)|(or)");
		Matcher operatorMatcher = operatorPattern.matcher(tempString);
		int count = 0;
		while (operatorMatcher.find()) {
			count++;
		}
		return count;
	}

	public static ArrayList<String> splitQuery(String wholeQuery) {
		// Split the Query into parts based on the operators
		StringBuilder sb = new StringBuilder();
		String[] splitedQuery = wholeQuery.split("(and)|(not)|(or)");
		for (String str : splitedQuery) {
			sb.append(str + "\n");
		}
		ArrayList<String> resultList = new ArrayList<>();
		for (String str : splitedQuery) {
			resultList.add(str);
		}
		return resultList;
	}

	private static ArrayList<String> extractOperator(String wholeQuery) {
		ArrayList<String> operators = new ArrayList<>();
		StringBuilder eachWord = new StringBuilder();
		for (int x = 0; x < wholeQuery.length(); x++) {
			String thisWord = eachWord.toString();
			char thisChar = wholeQuery.charAt(x);
			if (thisChar == ' ' || thisChar == '(' || thisChar == ')') {
				if (thisWord.matches("(and)|(or)|(not)")) {
					operators.add(thisWord);
					eachWord = new StringBuilder();
				} else {
					eachWord = new StringBuilder();
				}
			} else {
				eachWord.append(thisChar);
			}
		}
		return operators;
	}

	private static String parseInfixToPrefix(String q) {
		String subQuery = q;
		StringBuilder newSubQuery = new StringBuilder();
		String operator = null;
		String leftQuery;
		String rightQuery;
		String notQuery;
		int startIndex = 0;
		int endIndex = 0;
		if (subQuery.contains("and")) {
			startIndex = subQuery.indexOf("and");
			endIndex = startIndex + 2;
			leftQuery = subQuery.substring(0, endIndex - 2);
			rightQuery = subQuery.substring(endIndex +1 , subQuery.length());
			newSubQuery.append("and" + "(" + leftQuery + "," + rightQuery + ")");
		} else if (subQuery.contains("or")) {
			startIndex = subQuery.indexOf("or");
			endIndex = startIndex + 1;
			leftQuery = subQuery.substring(0, endIndex - 2);
			rightQuery = subQuery.substring(endIndex +1 , subQuery.length());
			newSubQuery.append("or" + "(" + leftQuery + "," + rightQuery + ")");
		} else if (subQuery.contains("not")) {
			startIndex = subQuery.indexOf("not");
			endIndex = startIndex + 2;
			notQuery = subQuery.substring(endIndex +1 , subQuery.length());
			newSubQuery.append("not" + "(" + notQuery + ")");
		}
		return newSubQuery.toString();
	}

}
