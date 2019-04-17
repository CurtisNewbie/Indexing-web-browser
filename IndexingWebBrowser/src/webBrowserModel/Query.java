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
	public Set<WebDoc> matches(WebIndex wind);
}
