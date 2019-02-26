import java.util.ArrayList;

/**
 * It is a web index that can check the web documents and see which contains a
 * particular string. It also contains the keywords or content words extracted
 * from each web document. It can also provide overall statistics summary of this
 * WebIndex.
 * 
 * @author 180139796
 * @version 1.5 last Updated on 25/02/2019
 */
public class WebIndex {

	/**
	 * A collection of objects of WebDoc
	 */
	private ArrayList<WebDoc> webDocs;

	/**
	 * Number of WebDoc in this web index.
	 */
	private int numOfDocs;

	/**
	 * Number of keywords read from these WebDoc.
	 */
	private int numOfKeywords;

	/**
	 * Number of content words read from these WebDoc.
	 */
	private int numOfContentWords;

	/**
	 * Initialise the webDocs. Assign 0 to numOfDocs and numOfKeywords.
	 */
	public WebIndex() {
		this.webDocs = new ArrayList<>();
		this.numOfDocs = 0;
		this.numOfKeywords = 0;
	}

	/**
	 * Add an object of WebDoc into the index and increment numOfDocs by one.
	 * Add the number of keywords and content words to the numOfKeywords and
	 * numOfContentWords.
	 * 
	 * @param doc An object of WebDoc
	 */
	public void add(WebDoc doc) {
		webDocs.add(doc);
		numOfDocs++;
		numOfKeywords += doc.getNumOfKeywords();
		numOfContentWords += doc.getNumOfContentWords();
	}

	/**
	 * Get all the WebDoc that are stored in this index as a String.
	 * 
	 * @return a String that summarises all the WebDoc in this index.
	 */
	public String getAllDocuments() {
		StringBuilder sb = new StringBuilder("This WebIndex includes these documents:\n");
		int index = 0;
		for (WebDoc wd : webDocs) {
			index++;
			sb.append("[" + index + "]" + wd.getEntry() + "\n");
		}
		return sb.toString();
	}

	/**
	 * Search a string through content words and keywords.
	 * 
	 * @param wd the String that is searched.
	 * @return a string that indicates all the entries that contains this string in
	 *         their content words and keywords.
	 */
	public String getMatches(String wd) {
		boolean keywordsFound = false;
		boolean contentWordsFound = false;
		StringBuilder keywordsResult = new StringBuilder(
				"Through searching keywords - Word:\"" + wd + "\" found in:\n");
		StringBuilder ContentWordsResult = new StringBuilder(
				"Through searching contentWords - Word:\"" + wd + "\" found in:\n");
		for (WebDoc doc : webDocs) {
			if (doc.getContentWords().contains(wd)) {
				ContentWordsResult.append(doc.getEntry() + "\n");
				contentWordsFound = true;
			}
			if (doc.getKeywords().contains(wd)) {
				keywordsResult.append(doc.getEntry() + "\n");
				keywordsFound = true;
			}
		}

		if (contentWordsFound == false) {
			ContentWordsResult.append("Nothing Found.");
		}

		if (keywordsFound == false) {
			keywordsResult.append("Nothing Found");
		}
		return keywordsResult.toString() + "\n" + ContentWordsResult.toString();
	}

	/**
	 * Get a string that provide the overall statistics summary of this WebIndex
	 * 
	 * @return a string of the overall statistics summary of this WebIndex.
	 */
	@Override
	public String toString() {
		return "WebIndex over keywords contains " + numOfKeywords + " from " + numOfDocs + " documents"
				+ "\nWebIndex over contents contains " + numOfContentWords + " from " + numOfDocs + " documents";
	}
}
