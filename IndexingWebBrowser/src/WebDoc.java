import java.net.*;
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
 * @version 1.4 last Updated on 21/02/2019
 */
public class WebDoc {

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
	 * The entry of this file. If its a local web document, it may includes"File:"
	 * at the beginning.
	 */
	private String entry;

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
	private TreeSet<String> keyWords;

	/**
	 * The collection of content words, excluding HTML and JS tags, numbers and
	 * duplicate words.
	 */
	private TreeSet<String> contentWords;

	/**
	 * The number of keywords, excluding the duplicate words.
	 */
	private int numOfKeyWords;

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
	 */
	public WebDoc(String url) {
		this.entry = url;
		this.fileType = this.checkFileType();

		if (fileType != FileType.INCORRECT_ENTRY_FORMAT) { // Entry is in correct format.
			if (fileType == FileType.WEB_URL) {
				System.out.println("Web Url:" + entry);
				this.content = readWebUrl();
			} else if (fileType == FileType.LOCAL_WEB_DOC) {
				System.err.println("Local url:" + entry);
				this.content = readLocalFile();
			}

			// check whether is well formed?
			this.checkQualityOfFormat();
			this.keyWords = this.extractKeyWords();
			this.numOfKeyWords = this.keyWords.size();
			this.contentWords = this.extractContentWords();
			this.numOfContentWords = this.contentWords.size();

			try {
				this.rangeOfWords = this.contentWords.first() + "-" + this.contentWords.last();
			} catch (NoSuchElementException e) {
				rangeOfWords = "NoContentWords";
			}
		} else { // Entry is in incorrect format.
			System.out.println("Warning:\"" + entry
					+ "\" is neither a web url, nor a local html file. Please makes sure it's correct");
			this.fileStatus = FileStatus.FAILED_READING;
		}
	}

	/**
	 * Return a String that provides the basic statistical summary of this file:
	 * [The name of this entry] [Number of content words] [Alphabetical range of
	 * content words] [Number of keywords] [Quality of the HTML]
	 * 
	 * @return A String of the statistics summary.
	 * 
	 */
	@Override
	public String toString() {
		return entry + " " + numOfContentWords + " (" + rangeOfWords + ") " + numOfKeyWords + " "
				+ "well-formed/partly-formed/ill-formed";
	}

	/**
	 * It extracts the keywords from the URL, and return a string that contains all
	 * the keywords; there is a space between each keyword. This method only runs
	 * once.
	 * 
	 * @return A string that contains all the keywords.
	 */
	private TreeSet<String> extractKeyWords() {
		TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Pattern keywordPattern = Pattern.compile("<meta name=\"keywords\"([^>]*)content.?=\"([^>]*)\">");
		Matcher keywordMatcher = keywordPattern.matcher(content);
		StringBuilder tempOutput = new StringBuilder("");
		while (keywordMatcher.find()) {
			tempOutput.append(keywordMatcher.group(2));
		}

		Pattern wordFilterPattern = Pattern.compile("[a-zA-Z]++"); // temporary output of keywords may contain space,
																	// and
		Matcher wordFilterMatcher = wordFilterPattern.matcher(tempOutput); // punctuation marks.
		while (wordFilterMatcher.find()) { // refine the result; extract words from the temporary output.
			result.add(wordFilterMatcher.group(0));
		}
		return result;
	}

	/**
	 * Extract the content words from the content. It will ignore the HTML and JS
	 * tags. Content words cannot be numbers, punctuation marks or anything that is
	 * not a-zA-Z. This method only runs once.
	 * 
	 * @return result A TreeSet of content words.
	 */
	private TreeSet<String> extractContentWords() {
		String tempOutput = content;
		tempOutput = tempOutput.replaceAll("(<[^>]*>)", " "); // Replace the tags - <...> with a space

		Pattern wordFilterPattern = Pattern.compile("[a-zA-Z]++");
		Matcher wordFilterMatcher = wordFilterPattern.matcher(tempOutput);
		TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		while (wordFilterMatcher.find()) { // refine the result; extract words from the temporary output.
			result.add(wordFilterMatcher.group(0));
		}
		return result;
	}

	/**
	 * Read the content from the web URL. The content can be empty, while if
	 * exceptions occur, the fileStatus is set to FAILED_READING and this method
	 * returns an empty, no length string. This method only runs once.
	 * 
	 * @return A String that contains the content of the web URL, including all the
	 *         HTML and JS tags.
	 */
	private String readWebUrl() {
		StringBuffer result = new StringBuffer(""); // Read the content into a temporary StringBuffer.

		try {
			URL url = new URL(entry);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String eachLine = "";
			while ((eachLine = reader.readLine()) != null) {
				result.append(eachLine);
			}
			reader.close();
			fileStatus = FileStatus.SUCCESSFUL_READING;
		} catch (MalformedURLException e) {
			System.out.println("The format of this url may be incorrect.");
			fileStatus = FileStatus.FAILED_READING;
		} catch (FileNotFoundException e) {
			System.out.println("File:\"" + entry + "\" is not found. Check if the url is correct.");
			fileStatus = FileStatus.FAILED_READING;
		} catch (IOException e) {
			System.out.println("Connection fails, please make sure you have connected to the Internet.");
			fileStatus = FileStatus.FAILED_READING;
		}
		return result.toString();
	}

	/**
	 * Read the content from the local HTML file. The content can be empty, while if
	 * exceptions occur, the fileStatus is set to FAILED_READING and this method
	 * returns an empty, no length string. This method only runs once.
	 * 
	 * @return A String that contains the content of the local HTML file, including
	 *         all the HTML and JS tags.
	 */
	private String readLocalFile() {
		StringBuilder result = new StringBuilder("");
		String localentry = entry;

		Pattern entryPattern = Pattern.compile("([Ff][Ii][Ll][Ee]:)(.++)");
		Matcher entryMatcher = entryPattern.matcher(entry);
		if (entryMatcher.find()) {
			localentry = entryMatcher.group(2); // extract the true file path.
		}

		// Read File
		try {
			FileReader fileInput = new FileReader(localentry);
			BufferedReader reader = new BufferedReader(fileInput);
			String temp;
			while ((temp = reader.readLine()) != null) {
				result.append(temp);
			}
			reader.close();
			fileStatus = FileStatus.SUCCESSFUL_READING;
		} catch (FileNotFoundException e) {
			System.out.println("File:\"" + localentry + "\"cannot be found.");
			fileStatus = FileStatus.FAILED_READING;
		} catch (IOException e) {
			System.out.println("File:\"" + localentry + "\"cannot be accessed.");
			fileStatus = FileStatus.FAILED_READING;
		}
		return result.toString();
	}

	/**
	 * Check whether the type of this entry is a web URL or a local web file or a
	 * illegal entry.
	 * 
	 * @return The fileType of this file.
	 */
	private FileType checkFileType() {
		Pattern urlPattern = Pattern.compile("http[:s]", Pattern.CASE_INSENSITIVE); // Identify whether it's a URL.
		Pattern localFilePattern = Pattern.compile("file:", Pattern.CASE_INSENSITIVE); // Identify whether it's a local
																						// file.

		Matcher urlMatcher = urlPattern.matcher(entry);
		Matcher localFileMatcher = localFilePattern.matcher(entry);

		// Check whether the entry is a URL or a local file.
		if (urlMatcher.lookingAt()) { // for a URL.
			return FileType.WEB_URL;
		} else if (localFileMatcher.lookingAt()) {
			return FileType.LOCAL_WEB_DOC;
		} else {
			System.out.println(entry + "The format of this entry:\"" + entry + "\"is incorrect.");
			return FileType.INCORRECT_ENTRY_FORMAT;
		}
	}

	private void checkQualityOfFormat() {
		System.out.println("Checking HTML format....");
		Pattern syntaxPattern = Pattern.compile("<([!/a-zA-Z]*)[^>]*>");
		Matcher syntaxChecker = syntaxPattern.matcher(content);

		Stack<String> syntaxStack = new Stack<>();
		Stack<String> specialTagsStack = new Stack<>();

		String tag;
		String wholeTag;
		if (syntaxChecker.find()) { // First tag can only be pushed into the stack
			tag = syntaxChecker.group(1);
			wholeTag = syntaxChecker.group(0);
			if (!wholeTag.matches("<.*/>") && !wholeTag.matches("<!.*>")) { // Allowed self-closing tags
				if (!tag.matches("[Mm][Ee][Tt][Aa]") && !tag.matches("/{0,1}[Pp]") && !tag.matches("[Hh][Rr]")
						&& !tag.matches("[Bb][Rr]") && !tag.matches("[Ll][Ii][Nn][Kk]")) { // Not allowed tags for
																							// well-formed input
					syntaxStack.push(tag);
				} else if (tag.matches("[Mm][Ee][Tt][Aa]") || tag.matches("/{0,1}[Pp]") || tag.matches("[Hh][Rr]")
						|| tag.matches("[Bb][Rr]") || tag.matches("[Ll][Ii][Nn][Kk]")) { // Allowed tags for partly
																							// well-formed input
					specialTagsStack.push(tag);
				} else {
					// do nothing
				}
			}

			while (syntaxChecker.find()) { // The second or more tags are found.
				tag = syntaxChecker.group(1);
				wholeTag = syntaxChecker.group(0);
				if (!wholeTag.matches("<.*/>") && !wholeTag.matches("<!.*>")) { // Allowed self-closing tags
					if (!tag.matches("[Mm][Ee][Tt][Aa]") && !tag.matches("/{0,1}[/Pp]") && !tag.matches("[Hh][Rr]")
							&& !tag.matches("[Bb][Rr]") && !tag.matches("[Ll][Ii][Nn][Kk]")) { // Not allowed tags for
																								// well-formed input

						if (!syntaxStack.empty() && ("/" + syntaxStack.peek()).equalsIgnoreCase(tag)) { // matches its closing tag
							syntaxStack.pop(); // or the stack is empty.
						} else {
							syntaxStack.push(tag);
						}

					} else if (tag.matches("[Mm][Ee][Tt][Aa]") || tag.matches("/{0,1}[Pp]") || tag.matches("[Hh][Rr]")
							|| tag.matches("[Bb][Rr]") || tag.matches("[Ll][Ii][Nn][Kk]")) { // Allowed tags for partly
						
						if (!specialTagsStack.empty() && ("/" + specialTagsStack.peek()).equalsIgnoreCase(tag)) { // matches its closing tag
							specialTagsStack.pop(); // or the stack is empty.
						} else {
							specialTagsStack.push(tag);
						}
					} else {
						// do nothing
					}
				}
			}
		}
		System.out.println(syntaxStack.toString());
		System.out.println(specialTagsStack.toString());

		if (syntaxStack.size() == 0 && specialTagsStack.size() == 0) {
			System.out.println("Well Formed");
		} else if (syntaxStack.size() == 0 && specialTagsStack.size() > 0) {
			System.out.println("partly-formed");
		} else {
			System.out.println("ill-formed");
		}
	}

	/**
	 * Get the file status of the file. If the file status returned is
	 * SUCCESSFUL_READING; it means the content of this file is successfully
	 * accessed. If the file status returned is FAILED_READING; it means the content
	 * of this file is not accessible.
	 * 
	 * @return The fileStatus of this file.
	 */
	public FileStatus getFileStatus() {
		return fileStatus;
	}

	/**
	 * Get the content words that have already been extracted from the URL.
	 * 
	 * @return tempTreeSet A TreeSet of content words.
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
	 * @return tempTreeSet A TreeSet of keyWords.
	 */
	public TreeSet<String> getKeywords() {
		TreeSet<String> tempTreeSet = new TreeSet<>();
		for (String st : keyWords) {
			tempTreeSet.add(st);
		}
		return tempTreeSet;
	}

	/**
	 * Get the entry as a String.
	 * 
	 * @return entry The entry of this file.
	 */
	public String getEntry() {
		return entry;
	}

	/**
	 * Get the number of keywords as an integer.
	 * 
	 * @return numOfKeyWords The number of keywords.
	 */
	public int getNumOfKeywords() {
		return numOfKeyWords;
	}

	/**
	 * Get the number of content words.
	 * 
	 * @return numOfContentWords The number of Content words.
	 */
	public int getNumOfContentWords() {
		return numOfContentWords;
	}

}
