import java.net.*;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.regex.*;
import java.io.*;

public class WebDoc {

	public enum FileType {
		WEB_URL, LOCAL_URL, INCORRECT_URL
	}

	public enum FileStatus {
		FAILED_READING, SUCCESSFUL_READING
	}

	private FileType fileType;
	private FileStatus fileStatus;
	private String filePath;
	private String content; // this should be eliminated
	private String rangeOfWords;
	private TreeSet<String> keyWords;
	private TreeSet<String> contentWords;
	private int numOfKeyWords;
	private int numOfContentWords;

	// Default Constructor
	public WebDoc(String url, FileType type) {

		this.filePath = url;
		this.fileType = type;

		if (fileType == FileType.WEB_URL) {
			System.out.println("Web Url:" + filePath);
			this.content = readWebUrl();
		} else if (fileType == FileType.LOCAL_URL) {
			System.err.println("Local url:" + filePath);
			this.content = readLocalFile();
		} else {
			System.out.println("Error"); // Base on the logic, this should not be executed.
		}

		this.keyWords = this.getKeyWords();
		this.numOfKeyWords = this.keyWords.size();
		this.contentWords = getContentWords();
		this.numOfContentWords = contentWords.size();
		try {
			this.rangeOfWords = this.contentWords.first() + "-" + this.contentWords.last();
		} catch (NoSuchElementException e) {
			rangeOfWords = "NoContentWords";
		}
	}

	/**
	 * Return a String that provides the basic statistical summary of this file:
	 * [The name of this entry] [Number of content words] [Alphabetical range of content words] [Number of keywords] [Quality of the HTML]
	 * @Override
	 */
	public String toString() {

		return filePath + " " + numOfContentWords + " (" + rangeOfWords + ") " + numOfKeyWords + " "
				+ "well-formed/partly-formed/ill-formed";
	}

	/**
	 * It extracts the keywords from the input string, and return a string that
	 * contains all the keywords; there is a space between each keyword. If there is
	 * no keywords found, it returns a no length string. This method only runs once.
	 * 
	 * @return String a string that contains all the keywords, or a no-length string
	 */
	private TreeSet<String> getKeyWords() {
		TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Pattern keywordPattern = Pattern.compile("<meta name=\"keywords\"([^>]*)content.?=\"([^>]*)\">");
		Matcher keywordMatcher = keywordPattern.matcher(content);

		StringBuilder tempOutput = new StringBuilder(""); // temporary output of keywords may contain space, and
															// punctuation marks.
		while (keywordMatcher.find()) {
			tempOutput.append(keywordMatcher.group(2));
		}

		Pattern wordFilterPattern = Pattern.compile("[a-zA-Z]++");
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
	 * @return a TreeSet<String> of content words.
	 */
	private TreeSet<String> getContentWords() {
		TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

		String tempOutput = content;
		tempOutput = tempOutput.replaceAll("(<[^>]*>)", " ");

		Pattern wordFilterPattern = Pattern.compile("[a-zA-Z]++");
		Matcher wordFilterMatcher = wordFilterPattern.matcher(tempOutput);
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
		StringBuffer result = new StringBuffer("");// Read the content into a temporary StringBuffer.

		try {
			URL url = new URL(filePath);
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
			System.out.println("File:\"" + filePath + "\" is not found. Check if the url is correct.");
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
		String localFilePath = filePath;

		Pattern filePathPattern = Pattern.compile("([Ff][Ii][Ll][Ee]:)(.++)");
		Matcher filePathMatcher = filePathPattern.matcher(filePath);
		if (filePathMatcher.find()) {
			localFilePath = filePathMatcher.group(2); // extract the true file path.
		}

		// Read File
		try {
			FileReader fileInput = new FileReader(localFilePath);
			BufferedReader reader = new BufferedReader(fileInput);
			String temp;
			while ((temp = reader.readLine()) != null) {
				result.append(temp);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File:\"" + localFilePath + "\"cannot be found.");
			fileStatus = FileStatus.FAILED_READING;
		} catch (IOException e) {
			System.out.println("File:\"" + localFilePath + "\"cannot be accessed.");
			fileStatus = FileStatus.FAILED_READING;
		}
		return result.toString();
	}

	/**
	 * Get the file status of the file. If the file status returned
	 * is SUCCESSFUL_READING; it means the content of this file is
	 * successfully accessed. If the file status returned is FAILED_READING;
	 * it means the content of this file is not accessible.
	 * 
	 * @return the instance variable - fileStatus of this file.
	 */
	public FileStatus getFileStatus() {
		return fileStatus;
	}
}
