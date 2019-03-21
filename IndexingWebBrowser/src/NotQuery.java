import java.util.Set;

public class NotQuery implements Query{

	private String query;
	
	public NotQuery(String s) {
		query = s;
	}
	
	@Override
	public Set<WebDoc> matches(WebIndex wind) {
		return wind.getMatches(query);
	}
	
	public String toString() {
		return "NOT:" + query;
	}

}
