import java.util.Set;

public class AtomicQuery implements Query {

	private String query;

	public AtomicQuery(String s) {
		query = s;
	}

	@Override
	public Set<WebDoc> matches(WebIndex wind) {
		return wind.getMatches(query);
	}
	
	public String toString() {
		return query;
	}

}
