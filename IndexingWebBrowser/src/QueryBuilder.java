import java.util.StringTokenizer;
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
			TreeSet<String> subQuery = new TreeSet<>();
			;
			String notQuery;
			int Starting_Index;

			if (wholeQuery.startsWith("and")) {
				Starting_Index = 4; // "and(" starting from 4
				String elements = wholeQuery.substring(Starting_Index, wholeQuery.length() - 1);
				StringTokenizer token = new StringTokenizer(elements, ",");
				while(token.hasMoreTokens()) {
					subQuery.add(token.nextToken());
				}
				return new AndQuery(subQuery);
			} else if (wholeQuery.startsWith("not")) {
				Starting_Index = 4;
				notQuery = wholeQuery.substring(Starting_Index, wholeQuery.length() - 1);
				return new NotQuery(notQuery);
			} else { // wholeQuery.startsWith("or");
				Starting_Index = 3; // "or(" starting from 3
				String elements = wholeQuery.substring(Starting_Index, wholeQuery.length() - 1);
				StringTokenizer token = new StringTokenizer(elements, ",");
				while(token.hasMoreTokens()) {
					subQuery.add(token.nextToken());
				}
				return new OrQuery(subQuery);
			}
		}
	}

	public static Query parseInfixForm(String q) {
		return null;
	}
}
