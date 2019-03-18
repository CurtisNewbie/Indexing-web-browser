import java.util.Set;

public class OrQuery implements Query {

	private String queryLeft;
	private String queryRight;

	public OrQuery(String left, String right) {

		queryLeft = left;
		queryRight = right;
	}

	@Override
	public Set<WebDoc> matches(WebIndex wind) {

		Set<WebDoc> leftResult;
		Set<WebDoc> rightResult;

		Query left = QueryBuilder.parse(queryLeft);
		Query right = QueryBuilder.parse(queryRight);

		leftResult = left.matches(wind);
		rightResult = right.matches(wind);
		
		if (leftResult != null & rightResult != null) {
			for (WebDoc wd : leftResult) {
				rightResult.add(wd);
			}
			return rightResult;
		} else if (leftResult == null && rightResult != null) {
			return rightResult;
		} else if (leftResult != null && rightResult == null) {
			return leftResult;
		} else {
			return null;
		}
	}

	public String toString() {
		return queryLeft + " " + queryRight;
	}

}
