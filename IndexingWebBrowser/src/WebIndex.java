import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * It is a web index that can check the web documents and see which contains a
 * particular string. It also contains the keywords or content words extracted
 * from each web document. It can also provide overall statistics summary of
 * this WebIndex.
 * 
 * @author 180139796
 */
public class WebIndex {

	/**
	 * It refers to the type of words that the web index stored.
	 */
	public enum TypeOfWords {
		KEYWORD, CONTENT_WORD
	}

	/**
	 * It indicates the type of words that the web index is trying to store.
	 * (keywords or content words)
	 */
	private TypeOfWords typeofWords;

	/**
	 * The 'keys' are the words (content words or keywords), the 'values' are the
	 * Set of WebDoc.
	 */
	private Map<String, Set<WebDoc>> webDocsMap;

	/**
	 * Number of WebDoc in this web index.
	 */
	private int numOfDocs;

	/**
	 * Number of words (keywords or content words) read from these WebDoc.
	 */
	private int numOfWords;

	/**
	 * Initialise the webDocs. Assign 0 to numOfDocs and numOfWord.
	 */
	public WebIndex(TypeOfWords type) {
		this.typeofWords = type;
		webDocsMap = new HashMap<>();
		this.numOfDocs = 0;
		this.numOfWords = 0;
	}

	/**
	 * Add an object of WebDoc into the index and increment numOfDocs by one. Assign
	 * the size of the map to the numOfWords.
	 * 
	 * @param doc An object of WebDoc
	 */
	public void add(WebDoc doc) {
		Set<String> tempWordSet;
		if (typeofWords == TypeOfWords.CONTENT_WORD) {
			tempWordSet = doc.getContentWords();
		} else {
			tempWordSet = doc.getKeywords();
		}
		if (!tempWordSet.isEmpty()) {
			for (String word : tempWordSet) {
				Set<WebDoc> newSet = new TreeSet<WebDoc>();
				newSet.add(doc);
				Set<WebDoc> temp = webDocsMap.putIfAbsent(word, newSet); // return null if the key is already associated
																			// with a key
				if (temp != null) { // If the key is already associated with a value.
					temp.add(doc);
				}
			}
		}
		numOfDocs++;
		numOfWords = webDocsMap.size();
	}

	/**
	 * This method searches through the web index and finds the results that match
	 * the given String. It returns a deep copy of the result as a Set<WebDoc>.
	 * 
	 * @param wd the String that is searched
	 * @return a Set<WebDoc> that matches the String.
	 */
	public Set<WebDoc> getMatches(String wd) {
		Set<WebDoc> deepCopySet = new TreeSet<>();
		Set<WebDoc> resultSet = webDocsMap.get(wd);
		if (resultSet != null) {
			deepCopySet.addAll(resultSet);
			return deepCopySet;
		} else {
			return null;
		}
	}

	/**
	 * This method returns a deep copy of webDocsMap(A HashMap) that contains all
	 * the keys and values stored in this webIndex.
	 * 
	 * @return a Hashmap contains all the keys and values stored in this webIndex.
	 */
	public Map<String, Set<WebDoc>> getWebDocsMap() {
		Map<String, Set<WebDoc>> deepCopySet = new HashMap<>();
		deepCopySet.putAll(webDocsMap);
		return deepCopySet;
	}

	/**
	 * Get a string that provide the overall statistics summary of this WebIndex
	 * 
	 * @return a string of the overall statistics summary of this WebIndex.
	 */
	@Override
	public String toString() {

		if (typeofWords == TypeOfWords.KEYWORD) {
			return "WebIndex over keywords contains " + numOfWords + " from " + numOfDocs + " documents";
		} else {
			return "WebIndex over contents contains " + numOfWords + " from " + numOfDocs + " documents";
		}
	}
}
