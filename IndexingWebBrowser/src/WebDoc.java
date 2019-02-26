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
 * @version 1.5 last Updated on 25/02/2019
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
	 * The entry of this file. If it's a local web document, it may include "File:"
	 * at the beginning. If it's a web URL, it may include "http" at the beginning.
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
			this.syntaxQuality = this.checkQualityOfSyntax();
			this.keywords = this.extractKeyWords();
			this.numOfKeyWords = this.keywords.size();
			this.contentWords = this.extractContentWords();
			this.numOfContentWords = this.contentWords.size();

			try {
				this.rangeOfWords = this.contentWords.first() + "-" + this.contentWords.last();
			} catch (NoSuchElementException e) {
				rangeOfWords = " "; // not content words.
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
	 * @return a String of the statistics summary.
	 * 
	 */
	@Override
	public String toString() {
		return entry + " " + numOfContentWords + " (" + rangeOfWords + ") " + numOfKeyWords + " " + syntaxQuality;
	}

	/**
	 * It extracts the keywords from the URL, and return a string that contains all
	 * the keywords; there is a space between each keyword. This method only runs
	 * once.
	 * 
	 * @return a TreeSet of all the keywords.
	 */
	private TreeSet<String> extractKeyWords() {
		TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Pattern keywordPattern = Pattern.compile("<meta name=\"keywords\"([^>]*?)contents?=\"([^>]*)\"\\s?/?>");
		Matcher keywordMatcher = keywordPattern.matcher(content);

		StringBuilder tempOutput = new StringBuilder("");
		while (keywordMatcher.find()) {
			tempOutput.append(keywordMatcher.group(2));
		}
		// temporary output of keywords may contain space and punctuation marks.
		Pattern wordFilterPattern = Pattern.compile("[a-zA-Z]+");
		Matcher wordFilterMatcher = wordFilterPattern.matcher(tempOutput);
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
			result.add(wordFilterMatcher.group(0));
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
	 * @return a String that contains the content of the local HTML file, including
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
	 * illegal entry. the
	 * 
	 * @return the FileType (Enum) of this file.
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

	/**
	 * Check the quality of syntax. It refers to whether HTML or JS tags are
	 * correctly closed with closing tags and correctly nested. A string is returned
	 * to refer the quality of the syntax - "well-formed"; "partly-formed";
	 * "ill-formed".
	 * 
	 * @return a string that indicates the quality of the syntax.
	 */
	private String checkQualityOfSyntax() {
		Pattern syntaxPattern = Pattern.compile("<([!/a-zA-Z]*)[^>]*>");
		Matcher syntaxChecker = syntaxPattern.matcher(content);
		Stack<String> syntaxStack = new Stack<>();
		Stack<String> specialTagsStack = new Stack<>();
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
					syntaxStack.push(tag);
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
						if (!syntaxStack.empty() && ("/" + syntaxStack.peek()).equalsIgnoreCase(tag)) {
							syntaxStack.pop();
						} else {
							syntaxStack.push(tag);
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

		if (syntaxStack.size() == 0 && specialTagsStack.size() == 0) {
			return "well-formed";
		} else if (syntaxStack.size() == 0 && specialTagsStack.size() > 0) {
			return "partly-formed";
		} else {
			return "ill-formed";
		}
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
	 * @return a TreeSet of all content words.
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
	 * @return a TreeSet of all keywords.
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
