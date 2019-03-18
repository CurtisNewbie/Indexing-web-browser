import java.util.ArrayList;
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

	public enum TypeOfWords {
		KEYWORD, CONTENT_WORD
	}

//	/**
//	 * A collection of objects of WebDoc
//	 */
//	private ArrayList<WebDoc> webDocs;

	private TypeOfWords type;

	/**
	 * The 'keys' are the words (content words or keywords), the 'values' are the
	 * collection of WebDoc.
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
		this.type = type;
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
		if (type == TypeOfWords.CONTENT_WORD) {
			tempWordSet = doc.getContentWords();
		} else {
			tempWordSet = doc.getKeywords();
		}
		
		if (!tempWordSet.isEmpty()) {
			for (String word : tempWordSet) {
				Set<WebDoc> newSet = new TreeSet<WebDoc>();
				newSet.add(doc);
				Set<WebDoc> temp = webDocsMap.putIfAbsent(word, newSet); // return null if the key is already associated with a key

				if (temp != null) { // If the key is already associated with a value.
					temp.add(doc);
				}
			}
		}
		
		numOfDocs++;
		numOfWords = webDocsMap.size();
	}

//	/**
//	 * Get all the WebDoc that are stored in this index as a String.
//	 * 
//	 * @return a String that summarises all the WebDoc in this index.
//	 */
//	public String getAllDocuments() {
//		return webDocsMap.keySet().toString();
//	}

	/**
	 * Search a string through content words and keywords.
	 * 
	 * @param wd the String that is searched.
	 * @return a string that indicates all the entries that contains this string in
	 *         their content words and keywords.
	 */
//	public String getMatches(String wd) {
//		boolean keywordsFound = false;
//		boolean contentWordsFound = false;
//		StringBuilder keywordsResult = new StringBuilder(
//				"Through searching keywords - Word:\"" + wd + "\" found in:\n");
//		StringBuilder ContentWordsResult = new StringBuilder(
//				"Through searching contentWords - Word:\"" + wd + "\" found in:\n");
//
//		for (WebDoc doc : webDocs) {
//			if (doc.getContentWords().contains(wd)) {
//				ContentWordsResult.append(doc.getEntry() + "\n");
//				contentWordsFound = true;
//			}
//			if (doc.getKeywords().contains(wd)) {
//				keywordsResult.append(doc.getEntry() + "\n");
//				keywordsFound = true;
//			}
//		}
//
//		if (contentWordsFound == false) {
//			ContentWordsResult.append("Nothing Found.");
//		}
//
//		if (keywordsFound == false) {
//			keywordsResult.append("Nothing Found");
//		}
//		return keywordsResult.toString() + "\n" + ContentWordsResult.toString();
//	}

	public Set<WebDoc> getMatches(String wd) {
		return webDocsMap.get(wd);
	}

	/**
	 * Get a string that provide the overall statistics summary of this WebIndex
	 * 
	 * @return a string of the overall statistics summary of this WebIndex.
	 */
	@Override
	public String toString() {

		if (type == TypeOfWords.KEYWORD) {
			return "WebIndex over keywords contains " + numOfWords + " from " + numOfDocs + " documents";
		} else {
			return "WebIndex over contents contains " + numOfWords + " from " + numOfDocs + " documents";
		}
	}
}
