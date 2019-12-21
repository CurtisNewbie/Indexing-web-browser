package com.curtisnewbie.webBrowserModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import java.util.TreeSet;
import java.util.regex.*;
import java.io.*;

/**
 * A Web Document (a local or online web page)
 * <P>
 * This class is for the web documents created based on the given url string.
 * The url string can either be a web URL or a path pointing to a local web
 * file. Objects of this class will store the original content (before parsing)
 * and the extracted words (in head and body) of the webpage.
 * </p>
 * 
 * @author Yongjie Zhuang
 * 
 * @see Jsoup
 * @see Document
 */
public class WebDoc implements Comparable<WebDoc> {

	/**
	 * Type of file. It is used to verify whether it is a web URL or a local web
	 * document.
	 */
	public enum FileType {
		WEB_URL, LOCAL_WEB_DOC
	}

	/**
	 * Type of the file: web URL; local web document; or the entry with incorrect
	 * format.
	 */
	private FileType fileType;

	/**
	 * The urlString of this file. If it's a local web document, it may include
	 * "File:" at the beginning. If it's a web URL, it may include "http:" or
	 * "https:" at the beginning.
	 */
	private String urlString;

	/**
	 * The original content of this file, including HTML or JS tags.
	 */
	private String content;

	/**
	 * The collection of words in HTML Head, excluding HTML and JS tags, numbers and
	 * duplicate words.
	 */
	private TreeSet<String> headWords;

	/**
	 * The collection of words in HTML Body, excluding HTML and JS tags, numbers and
	 * duplicate words.
	 */
	private TreeSet<String> bodyWords;

	/**
	 * The number of words, excluding the duplicate words.
	 */
	private int numOfWords;

	/**
	 * <p>
	 * Webpage being parsed into this Document object.<br>
	 * </p>
	 * --------------------------------------------------
	 * <p>
	 * Should consider whether I should keep this document, as words are already
	 * extracted. I may keep it for more advanced functionalities in the future.
	 * </p>
	 * <p>
	 * --------------------------------------------------
	 * </p>
	 * 
	 * @see Document
	 */
	private Document document;

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
	 * @throws IOException              when it's unable to connect to the given URL
	 *                                  string.
	 * @throws IllegalArgumentException when the format of the url is incorrect
	 *                                  (neither has a prefix of "https?:" or
	 *                                  "file:")
	 * @throws FileNotFoundException    when the local html file is not found
	 */
	public WebDoc(String url) throws IOException, IllegalArgumentException, FileNotFoundException {
		this.urlString = url;
		this.fileType = checkFileType(url);

		// identify FileType, if neither matched, exceptions thrown
		if (fileType == FileType.WEB_URL) {
			// GET request to url, and parse it into a document
			this.document = Jsoup.connect(url).get();
			this.content = document.wholeText();
		} else if (fileType == FileType.LOCAL_WEB_DOC) {
			this.content = readLocalFile(url);
			this.document = Jsoup.parse(content);
		}

		// get body and head, and extract words in it
		String bodyTxt = document.body().text();
		String headTxt = document.head().text();
		this.bodyWords = extractWords(bodyTxt);
		this.headWords = extractWords(headTxt);
		this.numOfWords = bodyWords.size() + headWords.size();
	}

	/**
	 * Extracts the words from the a string, and return an object of TreeSet that
	 * contains all the words.
	 * 
	 * @return a TreeSet of all the words.
	 */
	private TreeSet<String> extractWords(String text) {
		TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Pattern wordFilterPattern = Pattern.compile("[a-zA-Z]+");
		Matcher wordFilterMatcher = wordFilterPattern.matcher(text);
		while (wordFilterMatcher.find()) {
			result.add(wordFilterMatcher.group(0).toLowerCase());
		}
		return result;
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
	public static String readLocalFile(String url) throws FileNotFoundException, IOException {
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
	public FileType checkFileType(String url) throws IllegalArgumentException {
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
			throw new IllegalArgumentException("[" + url
					+ "\" is neither a web url, nor a local html file. Please makes sure it's in correct entry format.]\n");
		}
	}

	/**
	 * Return a String that provides the basic statistical summary of this file:
	 * [location/url] [Number of words (non-duplicate)]
	 * 
	 * @return a String consists of [location/url] [Number of words (non-duplicate)]
	 * 
	 */
	@Override
	public String toString() {
		return urlString + " " + numOfWords;
	}

	/**
	 * Get a deep copy of the {@code TreeSet<String> headWords}.
	 * 
	 * @return deep copy of {@code TreeSet<String> headWords}
	 */
	public TreeSet<String> getHeadWords() {
		TreeSet<String> tempTreeSet = new TreeSet<>();
		for (String st : headWords) {
			tempTreeSet.add(st);
		}
		return tempTreeSet;
	}

	/**
	 * Get a deep copy of the {@code TreeSet<String> bodyWords}.
	 * 
	 * @return deep copy of {@code TreeSet<String> bodyWords}
	 */
	public TreeSet<String> getBodyWords() {
		TreeSet<String> tempTreeSet = new TreeSet<>();
		for (String st : bodyWords) {
			tempTreeSet.add(st);
		}
		return tempTreeSet;
	}

	/**
	 * <p>
	 * Get the original urlString.
	 * </p>
	 * <p>
	 * If the web document is a web URL, it may be like this:
	 * "http://www.google.co.uk" with a prefix of the http protocol. If it's a local
	 * web document, it may be like this: "file:test111.htm" with a prefix of
	 * "file:".
	 * </p>
	 * 
	 * @return the urlString of this webpage(online or local).
	 */
	public String getUrlString() {
		return urlString;
	}

	/**
	 * Get the number of words (unique).
	 * 
	 * @return the number of words.
	 */
	public int getNumOfWords() {
		return numOfWords;
	}

	/**
	 * It compares this object with the specified object for ordering based on the
	 * alphabetical order of their urls. This method is to enable storing the WebDoc
	 * into a {@code TreeSet}.
	 * 
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 */
	@Override
	public int compareTo(WebDoc webdoc) {
		return urlString.compareTo(webdoc.getUrlString());
	}

	/**
	 * Get FileType
	 * 
	 * @return FileType
	 * @see FileType
	 */
	public FileType getFileType() {
		return fileType;
	}

	/**
	 * Get original content (before parsing)
	 * 
	 * @return original content as a string
	 */
	public String getContent() {
		return content;
	}

}
