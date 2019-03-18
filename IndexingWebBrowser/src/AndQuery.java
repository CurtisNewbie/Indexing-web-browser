import java.util.Set;

public class AndQuery implements Query {

	private String queryLeft;
	private String queryRight;
	
	public AndQuery(String left, String right) {
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
			if ((leftResult instanceof NotQuery) && (rightResult instanceof NotQuery)) {
				return null;
			} else if ((leftResult instanceof NotQuery) && !(rightResult instanceof NotQuery)) {
				rightResult.removeAll(leftResult);
				return rightResult;
			} else if (!(leftResult instanceof NotQuery) && (rightResult instanceof NotQuery)) {
				leftResult.removeAll(rightResult);
				return leftResult;
			} else {
				rightResult.retainAll(leftResult);
				return rightResult;
			}
		} else {
			return null;
		}
	}
	
	public String toString() {
		return queryLeft + " " + queryRight;
	}

}
