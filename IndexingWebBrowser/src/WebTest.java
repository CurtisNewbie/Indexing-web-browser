import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class will be used to load the web documents(files or URLs) indicated in
 * a text file, and then produce the summary statistics about these web
 * documents. Formats of statistics: [source] [word count] [alphabetic range of
 * content words] [number of distinct keywords] [evaluation of the HTML format]
 * 
 * @author 180139796
 * @version 1.2 last Updated on 21/02/2019
 *
 */
public class WebTest {

	public static void main(String[] args) {

		// temporary collections for objects of WebDoc
		ArrayList<WebDoc> webDocCollection = new ArrayList<>();

		// Read a txt file from the command args
		try {
			// wrapping the fileReader with bufferedReader to improve efficiency.
			BufferedReader fileIn = new BufferedReader(new FileReader(args[0]));
			System.out.println("-------------------File:" + args[0] + " has been read.--------------------");

			String tempEntry;
			while ((tempEntry = fileIn.readLine()) != null) {
				if (checkFileType(tempEntry) == WebDoc.FileType.WEB_URL) {
					webDocCollection.add(new WebDoc(tempEntry, WebDoc.FileType.WEB_URL));
				} else if (checkFileType(tempEntry) == WebDoc.FileType.LOCAL_URL) {
					webDocCollection.add(new WebDoc(tempEntry, WebDoc.FileType.LOCAL_URL));
				} else {
					System.out.println("Warning:\"" + tempEntry
							+ "\"It's neither a web url, nor a local html file. Please makes sure it's correct");
				}
			}

			fileIn.close();
			System.out.println("Reading finished---------------------------------------------------------");

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("You should enter the path of the file!");
		} catch (FileNotFoundException e) {
			System.out.println("File is not found! Try this format - C:\\file\\abc.txt");
		} catch (IOException e) {
			System.out.println("Exceptions occurred:");
			e.printStackTrace();
		}

	}

	private static WebDoc.FileType checkFileType(String urlPath) {
		Pattern urlPattern = Pattern.compile("[Hh][Tt][Tt][Pp][:s]"); // Identify whether it's a URL.
		Pattern localFilePattern = Pattern.compile("[Ff][Ii][Ll][Ee]:"); // Identify whether it's a local file.

		Matcher urlMatcher = urlPattern.matcher(urlPath);
		Matcher localFileMatcher = localFilePattern.matcher(urlPath);

		// Check whether the entry is a URL or a local file.
		if (urlMatcher.lookingAt()) { // for a URL.
			return WebDoc.FileType.WEB_URL;
		} else if (localFileMatcher.lookingAt()) {
			return WebDoc.FileType.LOCAL_URL;
		} else {
			System.out.println(urlPath + "This URL is incorrect.");
			return WebDoc.FileType.INCORRECT_URL;
		}
	}
}
