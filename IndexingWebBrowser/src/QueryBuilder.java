import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeSet;

public class QueryBuilder {

	// Or (A,C)
	// And (B,C)

	public static Query parse(String q) {

		String wholeQuery = q.toLowerCase();
		wholeQuery.trim();

		if (!wholeQuery.contains("and") && !wholeQuery.contains("or") && !wholeQuery.contains("not")) {
			return new AtomicQuery(wholeQuery);
		} else {
			String notQuery;
			int Starting_Index;

			if (wholeQuery.startsWith("and")) {
				Starting_Index = 4; // "and(" starting from 4
				String subQueryInBracket = wholeQuery.substring(Starting_Index, wholeQuery.length() - 1);
				return new AndQuery(parseSubquery(subQueryInBracket));
			} else if (wholeQuery.startsWith("not")) {
				Starting_Index = 4;
				notQuery = wholeQuery.substring(Starting_Index, wholeQuery.length() - 1);
				return new NotQuery(notQuery);
			} else { // wholeQuery.startsWith("or");
				Starting_Index = 3; // "or(" starting from 3
				String subQueryInBracket = wholeQuery.substring(Starting_Index, wholeQuery.length() - 1);
				
				return new OrQuery(parseSubquery(subQueryInBracket));
			}
		}
	}

	public static Query parseInfixForm(String q) {
		return null;
	}

	private static TreeSet<String> parseSubquery(String q) {
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
		//last subQuery
		subQuery.add(q.substring(startIndex, q.length()));
		return subQuery;
	}
}
