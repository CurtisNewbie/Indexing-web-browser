import java.net.*;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.*;
import java.io.*;

public class WebDoc {

	public enum FileType {
		WEB_URL, LOCAL_URL, INCORRECT_URL
	}

	private String filePath;
	private TreeSet<String> contentWords;
	private String content; // this should be eliminated
	private int numOfWords;
	private String rangeOfWords;
	private TreeSet<String> keyWords;

	// Default Constructor
	public WebDoc(String entry, FileType type) {

		this.filePath = entry;

		if (type == FileType.WEB_URL) {
			System.out.println("Web Url:" + filePath);
			this.content = readWebUrl();
		} else if (type == FileType.LOCAL_URL) {
			System.err.println("Local url:" + filePath);
			this.content = readLocalFile();
		} else {
			System.out.println("It's not a valid file."); // Base on the logic, this should not be executed.
		}

		System.out.println(filePath);
		System.out.println(content);
		
		//Testing
//		this.keyWords = getContentWords(
//				"<title>Useful HTML Meta Tags - cache, no-cache, robots, refresh, content, keywords, description, expires, author, etc.</title>");
	}

	@Override
	public String toString() {

		return "\"" + filePath + "\": " + numOfWords + " " + rangeOfWords + " " + keyWords.size() + " "
				+ "well-formed/partly-formed/ill-formed";
	}

	/**
	 * It extracts the keywords from the input string, and return a string that
	 * contains all the keywords; there is a space between each keyword. If there is
	 * no keywords found, it returns a no length string.
	 * 
	 * @param content
	 * @return String a string that contains all the keywords, or a no-length string
	 */
	private static TreeSet<String> getKeyWords(String content) {

		TreeSet<String> result = new TreeSet<>();
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

	private static TreeSet<String> getContentWords(String content) {
		TreeSet<String> result = new TreeSet<>();
		Pattern contentWordPattern = Pattern.compile(">([^<]*)<");
		Matcher contentWordMatcher = contentWordPattern.matcher(content);

		StringBuilder tempOutput = new StringBuilder("");
		while (contentWordMatcher.find()) {
			tempOutput.append(contentWordMatcher.group(1));
		}
		System.out.println(tempOutput);

		return new TreeSet<String>();
	}

	private String readWebUrl() {
		String urlPath = this.filePath; // Avoid changing the original value of filePath.
		StringBuffer temp = new StringBuffer("");// Read the content into a temporary StringBuffer.

		try {
			URL url = new URL(urlPath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String eachLine = "";
			while ((eachLine = reader.readLine()) != null) {
				temp.append(eachLine);
			}
			reader.close();
		} catch (MalformedURLException e) {
			System.out.println("The format of this url may be incorrect.");
		} catch (FileNotFoundException e) {
			System.out.println("File:\"" + urlPath + "\" is not found. Check if the url is correct.");
		} catch (IOException e) {
			System.out.println("Connection fails, please make sure you have connected to the Internet.");
		}
		return temp.toString();
	}

	private String readLocalFile() {
		String urlPath = this.filePath; // Avoid changing the original value of filePath.
		StringBuilder result = new StringBuilder("");
		String localFilePath = urlPath;

		Pattern filePathPattern = Pattern.compile("([Ff][Ii][Ll][Ee]:)(.++)");
		Matcher filePathMatcher = filePathPattern.matcher(urlPath);
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
		} catch (IOException e) {
			System.out.println("IOError Occurs");
			e.printStackTrace(); // This should be eliminated!
		}
		return result.toString();
	}
}
