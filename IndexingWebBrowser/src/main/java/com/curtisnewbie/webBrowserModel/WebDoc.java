package com.curtisnewbie.webBrowserModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import java.net.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.*;
import java.io.*;

/**
 * This class is for the web documents created based on the entries. The entries
 * can either be a web URL or a local web file. It will store the content of the
 * web document, provide access to the content words, and provide the statistics
 * summary of the document.
 * 
 * @author 180139796
 */
public class WebDoc implements Comparable<WebDoc> {
	/**
	 * Type of file. It is used to verify whether the entry is in the correct
	 * format, and whether it is a web URL or a local web document.
	 */
	public enum FileType {
		WEB_URL, LOCAL_WEB_DOC, INCORRECT_ENTRY_FORMAT
	}

	/**
	 * Status of file - whether it has successfully accessed to its content or not.
	 * It can be used to verify whether the object of WebDoc has successfully read
	 * its content and extracted the content words and keywords.
	 */
	public enum FileStatus {
		FAILED_READING, SUCCESSFUL_READING
	}

	/**
	 * Type of the file: web URL; local web document; or the entry with incorrect
	 * format.
	 */
	private FileType fileType;

	/**
	 * Status of the file that indicates whether it has successfully accessed to its
	 * content: FAILED_READING or SUCCESSFUL_READING.
	 */
	private FileStatus fileStatus;

	/**
	 * The urlString of this file. If it's a local web document, it may include
	 * "File:" at the beginning. If it's a web URL, it may include "http:" at the
	 * beginning.
	 */
	private String urlString;

	/**
	 * The original content of this file, including HTML or JS tags.
	 */
	private String content;

	/**
	 * The alphabetical range of words. E.g., apple-cat.
	 */
	private String rangeOfWords;

	/**
	 * The collection of keywords, excluding HTML and JS tags, numbers and duplicate
	 * words.
	 */
	private TreeSet<String> keywords;

	/**
	 * Quality of syntax - It refers to whether HTML or JS tags are correctly closed
	 * with closing tags and correctly nested.
	 */
	private String syntaxQuality;
	/**
	 * The collection of content words, excluding HTML and JS tags, numbers and
	 * duplicate words.
	 */
	private TreeSet<String> contentWords;

	/**
	 * The number of keywords, excluding the duplicate words.
	 */
	private int numOfKeywords;

	/**
	 * The number of contentWords, excluding the duplicate words.
	 */
	private int numOfContentWords;

	/**
	 * This constructor only set the value of legal entries. The entries that are
	 * identified as FileType.INCORRECT_ENTRY_FORMAT will be ignored and the
	 * FileStatus of the illegal entries will be set to the
	 * FileStatus.FAILED_READING. Trying to read the data of illegal entries may
	 * cause exceptions as there is not value given to the illegal entries. For the
	 * legal entries, a number of private methods will be called only once to read
	 * contents from the web URL or local web documents and assign the relevant
	 * values to the instance variables.
	 * 
	 * @param url The URL of this web document. It can either be a web URL or a
	 *            local web document.
	 * @throws IOException when it's unable to connect to the given URL string.
	 */
	public WebDoc(String url) throws IOException, IllegalArgumentException {

		this.urlString = url;
		this.fileType = checkFileType(url);
		// if (fileType != FileType.INCORRECT_ENTRY_FORMAT) { // Entry is in correct
		// format.
		// if (fileType == FileType.WEB_URL) {
		// this.content = readWebUrl();
		// } else if (fileType == FileType.LOCAL_WEB_DOC) {
		// this.content = readLocalFile();
		// }
		// this.syntaxQuality = this.checkQualityOfSyntax(); // check whether is well
		// formed.
		// this.keywords = this.extractKeywords();
		// this.numOfKeywords = this.keywords.size();
		// this.contentWords = this.extractContentWords();
		// this.numOfContentWords = this.contentWords.size();
		//
		// try {
		// this.rangeOfWords = this.contentWords.first() + "-" +
		// this.contentWords.last();
		// } catch (NoSuchElementException e) {
		// rangeOfWords = " "; // no content words.
		// }
		// }

		// GET request to url, and parse it into a document
		// Document document = Jsoup.connect(url).get();
		// String content = document.body().text();
		// String head = document.head().text();

	}

	/**
	 * It extracts the keywords from the URL, and return an object of TreeSet that
	 * contains all the keywords. This method only runs once.
	 * 
	 * @return a TreeSet of all the keywords.
	 */
	private TreeSet<String> extractKeywords() {
		TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Pattern keywordPatternNormalOrder = Pattern.compile(
				"<[Mm][Ee][Tt][Aa]\\s*[Nn][Aa][Mm][Ee]=\"[Kk][Ee][Yy][Ww][Oo][Rr][Dd][Ss]\"([^>]*?)[Cc][Oo][Nn][Tt][Ee][Nn][Tt][Ss]?=\"([^>]*)\"\\s?/?>");
		Matcher keywordMatcherNormalOrder = keywordPatternNormalOrder.matcher(content);
		Pattern keywordPatternReverseOrder = Pattern.compile(
				"<[Mm][Ee][Tt][Aa]\\s*[Cc][Oo][Nn][Tt][Ee][Nn][Tt][Ss]?=\"([^>]*)\"\\s*[Nn][Aa][Mm][Ee]=\"[Kk][Ee][Yy][Ww][Oo][Rr][Dd][Ss]\"\\s*/?>");
		Matcher keywordMatcherReverseOrder = keywordPatternReverseOrder.matcher(content);

		StringBuilder tempOutput = new StringBuilder("");
		while (keywordMatcherNormalOrder.find()) {
			tempOutput.append(keywordMatcherNormalOrder.group(2));
		}
		while (keywordMatcherReverseOrder.find()) {
			tempOutput.append(keywordMatcherReverseOrder.group(1));
		}
		// temporary output of keywords that may contain space and punctuation marks.
		Pattern wordFilterPattern = Pattern.compile("[a-zA-Z]+");
		Matcher wordFilterMatcher = wordFilterPattern.matcher(tempOutput);
		while (wordFilterMatcher.find()) { // refine the result; extract words from the temporary output.
			result.add(wordFilterMatcher.group(0).toLowerCase());
		}
		return result;
	}

	/**
	 * Extract the content words from the content. It will ignore the HTML and JS
	 * tags. Content words cannot be numbers, punctuation marks or anything that is
	 * not a-zA-Z. This method only runs once.
	 * 
	 * @return a TreeSet of all content words.
	 */
	private TreeSet<String> extractContentWords() {
		String tempOutput = content;
		// Replace the <script> ... </script> with a space.
		tempOutput = tempOutput.replaceAll("<script[^>]*>.*?</script>", " ");

		// Replace the <style> ... </style> with a space.
		tempOutput = tempOutput.replaceAll("<style[^>]*>.*?</style>", " ");

		// Replace the tags - <...> with a space
		tempOutput = tempOutput.replaceAll("(<[^>]*?>)", " ");

		Pattern wordFilterPattern = Pattern.compile("[a-zA-Z]++");
		Matcher wordFilterMatcher = wordFilterPattern.matcher(tempOutput);
		TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

		while (wordFilterMatcher.find()) { // refine the result; extract words from the temporary output.
			result.add(wordFilterMatcher.group(0).toLowerCase());
		}
		return result;
	}

	/**
	 * Read the content from the web URL. The content can be empty, while if
	 * exceptions occur, the fileStatus is set to FAILED_READING and this method
	 * returns an empty, no length string. This method only runs once.
	 * 
	 * @return a String that contains the content of the web URL, including all the
	 *         HTML and JS tags.
	 */
	private String readWebUrl() {
		StringBuffer result = new StringBuffer(""); // Read the content into a temporary StringBuffer.
		try {
			URL url = new URL(urlString);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

			String eachLine = "";
			while ((eachLine = reader.readLine()) != null) {
				result.append(eachLine + " ");
			}
			reader.close();
			fileStatus = FileStatus.SUCCESSFUL_READING;
		} catch (MalformedURLException e) {
			System.out.println("[Warning --- \"" + urlString + "\" --- The URL format is incorrect.]");
			fileStatus = FileStatus.FAILED_READING;
		} catch (IllegalArgumentException e) {
			System.out.println("[Warning --- \"" + urlString
					+ "\" --- The protocol is incorrect, please make sure the URL is correct.]");
			fileStatus = FileStatus.FAILED_READING;
		} catch (IOException e) {
			System.out.println("[Warning --- Cannot connect to \"" + urlString
					+ "\", please make sure that you have connected to the Internet or that the URL is correct.]");
			fileStatus = FileStatus.FAILED_READING;
		}
		return result.toString();
	}

	/**
	 * Read the content from the local HTML file (the url should have a prefix of
	 * "file:"). The content can be empty. This method only runs once.
	 * 
	 * @return a String that contains the content of the local HTML file, including
	 *         all the HTML and JS tags.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String readLocalFile(String url) throws FileNotFoundException, IOException {
		StringBuilder result = new StringBuilder("");
		Pattern prefixPattern = Pattern.compile("([Ff][Ii][Ll][Ee]:)(.++)");
		Matcher prefixMatcher = prefixPattern.matcher(url);

		String pathToFile;
		if (prefixMatcher.find()) {
			// extract the true file path.
			pathToFile = prefixMatcher.group(2);
			try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile));) {
				String temp;
				while ((temp = reader.readLine()) != null)
					result.append(temp + " ");
			}
		}
		return result.toString();
	}

	/**
	 * <p>
	 * Check whether the type of this entry is a web URL or a local web file or a
	 * illegal entry, by looking at its prefix.
	 * </p>
	 * <p>
	 * If the given url is a web url (with HTTP protocol), it's set to be of
	 * {@code FileType.WEB_URL}, else if it's a local file (with a prefix of
	 * "file:"), it's set to be of {@code FileType.LOCAL_WEB_DOC}.
	 * </p>
	 * 
	 * @param url url string
	 * @throws IllegalArgumentException when the format of the url is incorrect
	 *                                  (neither has a prefix of "https?:" or
	 *                                  "file:")
	 * @see FileType
	 */
	private FileType checkFileType(String url) throws IllegalArgumentException {
		Pattern urlPattern = Pattern.compile("https?:", Pattern.CASE_INSENSITIVE); // Identify whether it's a URL.
		Pattern localFilePattern = Pattern.compile("file:", Pattern.CASE_INSENSITIVE); // Identify whether it's a local
																						// file.
		Matcher urlMatcher = urlPattern.matcher(url);
		Matcher localFileMatcher = localFilePattern.matcher(url);

		// Check whether the entry is a URL or a local file.
		if (urlMatcher.lookingAt()) {
			return FileType.WEB_URL;
		} else if (localFileMatcher.lookingAt()) {
			return FileType.LOCAL_WEB_DOC;
		} else {
			fileType = FileType.INCORRECT_ENTRY_FORMAT;
			throw new IllegalArgumentException("[" + url
					+ "\" is neither a web url, nor a local html file. Please makes sure it's in correct entry format.]\n");
		}
	}

	/**
	 * Check the quality of syntax. It refers to whether HTML or JS tags are
	 * correctly closed with closing tags and correctly nested. When any syntax
	 * other than HTML is used, the accuracy cannot be guaranteed. A string is
	 * returned to indicate the quality of the syntax - "well-formed";
	 * "partly-formed"; "ill-formed".
	 * 
	 * @return a string that indicates the quality of the syntax.
	 */
	private String checkQualityOfSyntax() {
		Pattern syntaxPattern = Pattern.compile("<([!/a-zA-Z]*)[^>]*>");
		Matcher syntaxChecker = syntaxPattern.matcher(content);
		Stack<String> normalTagStack = new Stack<>(); // Stack for normal tags
		Stack<String> specialTagsStack = new Stack<>(); // stack for special tags
		final String meta_tag = "[Mm][Ee][Tt][Aa]"; // <meta>
		final String p_tag = "/{0,1}[Pp]"; // </p> or <p>
		final String hr_tag = "[Hh][Rr]"; // <hr>
		final String br_tag = "[Bb][Rr]"; // <br>
		final String link_tag = "[Ll][Ii][Nn][Kk]"; // <link>
		String tag; // Only the first word after '<'. E.g., <style ...>
		String wholeTag; // Anything that is within < and >

		if (syntaxChecker.find()) { // First tag can only be pushed into the stack
			tag = syntaxChecker.group(1);
			wholeTag = syntaxChecker.group(0);
			if (!wholeTag.matches("<.*/>") && !wholeTag.matches("<!.*>")) { // Allowed self-closing tags
				// Not including the special tags for partly well-formed input.
				if (!tag.matches(meta_tag) && !tag.matches(p_tag) && !tag.matches(hr_tag) && !tag.matches(br_tag)
						&& !tag.matches(link_tag)) {
					normalTagStack.push(tag);
				} else if (tag.matches(meta_tag) || tag.matches(p_tag) || tag.matches(hr_tag) || tag.matches(br_tag)
						|| tag.matches(link_tag)) { // Allowed tags for partly well-formed input
					specialTagsStack.push(tag);
				} else {
					// do nothing
				}
			}

			while (syntaxChecker.find()) { // The second or more tags are found.
				tag = syntaxChecker.group(1);
				wholeTag = syntaxChecker.group(0);
				if (!wholeTag.matches("<.*/>") && !wholeTag.matches("<!.*>")) { // Allowed self-closing tags
					// Not including the special tags for partly well-formed input.
					if (!tag.matches(meta_tag) && !tag.matches(p_tag) && !tag.matches(hr_tag) && !tag.matches(br_tag)
							&& !tag.matches(link_tag)) {
						// matches its closing tag or the stack is empty.
						if (!normalTagStack.empty() && ("/" + normalTagStack.peek()).equalsIgnoreCase(tag)) {
							normalTagStack.pop();
						} else {
							normalTagStack.push(tag);
						}
					} else if (tag.matches(meta_tag) || tag.matches(p_tag) || tag.matches(hr_tag) || tag.matches(br_tag)
							|| tag.matches(link_tag)) {// Allowed tags for partly well-formed input
						// matches its closing tag or the stack is empty.
						if (!specialTagsStack.empty() && ("/" + specialTagsStack.peek()).equalsIgnoreCase(tag)) {
							specialTagsStack.pop();
						} else {
							specialTagsStack.push(tag);
						}
					} else {
						// do nothing
					}
				}
			}
		}
		if (normalTagStack.size() == 0 && specialTagsStack.size() == 0) {
			return "well-formed";
		} else if (normalTagStack.size() == 0 && specialTagsStack.size() > 0) {
			return "partly-formed";
		} else {
			return "ill-formed";
		}
	}

	/**
	 * Return a String that provides the basic statistical summary of this file:
	 * [The name of this entry] [Number of content words] [Alphabetical range of
	 * content words] [Number of keywords] [Quality of the HTML syntax]
	 * 
	 * @return a String of the statistics summary.
	 * 
	 */
	@Override
	public String toString() {
		return urlString + " " + numOfContentWords + " (" + rangeOfWords + ") " + numOfKeywords + " " + syntaxQuality;
	}

	/**
	 * Get the file status of the file. If the file status returned is
	 * SUCCESSFUL_READING; it means the content of this file is successfully
	 * accessed. If the file status returned is FAILED_READING; it means the content
	 * of this file is not accessible.
	 * 
	 * @return the fileStatus of this file.
	 */
	public FileStatus getFileStatus() {
		return fileStatus;
	}

	/**
	 * Get the content words that have already been extracted from the URL.
	 * 
	 * @return a TreeSet (deep copy) of all content words.
	 */
	public TreeSet<String> getContentWords() {
		TreeSet<String> tempTreeSet = new TreeSet<>();
		for (String st : contentWords) {
			tempTreeSet.add(st);
		}
		return tempTreeSet;
	}

	/**
	 * Get the keywords that have already been extracted from the URL.
	 * 
	 * @return a TreeSet (deep copy) of all keywords.
	 */
	public TreeSet<String> getKeywords() {
		TreeSet<String> tempTreeSet = new TreeSet<>();
		for (String st : keywords) {
			tempTreeSet.add(st);
		}
		return tempTreeSet;
	}

	/**
	 * Get the entry as a String. If the web document is a web URL, it may be like
	 * this: "http://www.google.co.uk". If it's a local web document, it may be like
	 * this: "file:test111.htm".
	 * 
	 * @return the entry of this file.
	 */
	public String getEntry() {
		return urlString;
	}

	/**
	 * Get the number of keywords as an integer.
	 * 
	 * @return the number of keywords.
	 */
	public int getNumOfKeywords() {
		return numOfKeywords;
	}

	/**
	 * Get the number of content words.
	 * 
	 * @return the number of Content words.
	 */
	public int getNumOfContentWords() {
		return numOfContentWords;
	}

	/**
	 * It compares this object with the specified object for order, in terms of the
	 * entry of the WebDoc. This method is to enable storing the WebDoc into the
	 * TreeSet.
	 * 
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 */
	@Override
	public int compareTo(WebDoc webdoc) {
		return urlString.compareTo(webdoc.getEntry());
	}

	/**
	 * @return FileType
	 * @see FileType
	 */
	public FileType getFileType() {
		return fileType;
	}

}
