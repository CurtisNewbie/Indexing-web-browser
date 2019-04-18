package webBrowserModel;
import java.util.Set;

/**
 * This interface is used to make sure that all the subclasses used for
 * processing prefix query implement the matches() method.
 * 
 * @author 180139796
 *
 */
public interface Query {
	
	/**
	 * This method searches through the given WebIndex based on the query to find
	 * all the matched results.
	 * 
	 * @return a Set<WebDoc> that is found based on the query and the given
	 *         WebIndex.
	 * @param wind the WebIndex that is used to search through based on the query.
	 * 
	 */
	public Set<WebDoc> matches(WebIndex wind);
}
