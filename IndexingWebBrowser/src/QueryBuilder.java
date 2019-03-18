
public class QueryBuilder {

	// Or (A,C)
	// And (B,C)

	public static Query parse(String q) {

		String str = q.toLowerCase();
		str.trim();

		if (!str.contains("and") && !str.contains("or") && !str.contains("not")) {
			return new AtomicQuery(str);
		} else if (str.startsWith("not")) {
			return new NotQuery(str);
		} else {
			String leftQuery;
			String rightQuery;
			int Starting_Index;
			int first_Comma_Index = str.indexOf(',');
			
			if (str.startsWith("and")) {
				Starting_Index = 4; // "and(" starting from 4
				leftQuery = str.substring(Starting_Index, first_Comma_Index);
				rightQuery = str.substring(first_Comma_Index + 1, str.length() - 1); //Ending before the closing bracket.
				return new AndQuery(leftQuery, rightQuery);
			} else { // str.startsWith("or");
				Starting_Index = 3; // "or(" starting from 3
				leftQuery = str.substring(Starting_Index, first_Comma_Index);
				rightQuery = str.substring(first_Comma_Index + 1, str.length() - 1); //Ending before the closing bracket.
				return new OrQuery(leftQuery, rightQuery);
			}
		}
	}

	public static Query parseInfixForm(String q) {
		return null;
	}
}
