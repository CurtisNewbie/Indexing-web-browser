import java.net.*;
import java.util.ArrayList;
import java.util.regex.*;
import java.io.*;

public class WebDoc {

	private String fileName;
	private String contentWords;
	private int numOfWords;
	private String rangeOfWords;
	private ArrayList<String> keyWords;

	// Default Constructor
	public WebDoc(String entry) {

		Pattern urlPattern = Pattern.compile("[Hh][Tt][Tt][Pp]"); // Identify whether it's a URL.
		Pattern localFilePattern = Pattern.compile("[Ff][Ii][Ll][Ee]:"); // Identify whether it's a local file.

		Matcher urlMatcher = urlPattern.matcher(entry);
		Matcher localFileMatcher = localFilePattern.matcher(entry);

		// Check whether the entry is a URL or a local file.
		if (urlMatcher.lookingAt()) { // for a URL.
			this.fileName = entry;
			try {
				URL url = new URL(fileName);
				BufferedReader urlReader = new BufferedReader(new InputStreamReader(url.openStream()));

				// Read the content into a temporary String.
				StringBuffer temp = new StringBuffer();
				String eachLine = "";
				
				while((eachLine = urlReader.readLine()) != null) {
					System.out.println(eachLine);
				}
//				Pattern contentPattern = Pattern.compile("[a-zA-z]*"); // Select the legitimate content words
//				Matcher contentMatcher = contentPattern.matcher(eachLine);
//
//				while ((eachLine = urlReader.readLine()) != null) { // Read text from the url.
//					for (int x = 0; x < eachLine.length(); x++) {
//						if (contentMatcher.find()
//								&& (contentMatcher.start() > 0 && contentMatcher.end() < eachLine.length())) {
//							temp.append(eachLine.substring(contentMatcher.start(), contentMatcher.end()));
//						}
//					}
//				}

			} catch (MalformedURLException e) {
				System.out.println("The URL of this entry cannot be reached. The format of this url may be incorrect.");
			} catch (FileNotFoundException e) {
				System.out.println("File:[" + fileName + "] is not found. Check if the url is correct.");
			} catch (IOException e) {
				System.out.println("Connection fails");
				e.printStackTrace();
			}
		} else if (localFileMatcher.lookingAt())

		{ // Check if it's a local htm file.
			this.fileName = entry;
		} else {
			System.out.println("//The format of this entry is not right. Values have been set to default.");
			this.fileName = "DefaultFileName";
			this.contentWords = "DefaultContent";
		}

	}

	@Override
	public String toString() {

		return fileName + " " + numOfWords + " " + rangeOfWords + " " + keyWords.size() + " "
				+ "well-formed/partly-formed/ill-formed";
	}
}
