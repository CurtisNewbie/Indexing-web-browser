import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.ws.WebEndpoint;

/**
 * This class will be used to read the web documents(entries: files or URLs)
 * indicated in a text file, and then calls the method of objects of WebDoc to
 * produce the summary statistics about these web documents. A WebIndex will
 * then be created to provide additional functionalities, such as searching and
 * providing a overall summary of the WebDoc in this WebIndex.
 * 
 * @author 180139796
 * 
 */
public class WebTest {

	public static void main(String[] args) {

		// temporary collections for objects of WebDoc
		ArrayList<WebDoc> webDocCollection = new ArrayList<>();
		ArrayList<String> queryCollection = new ArrayList<>();

		// Read a txt file from the command args
		try {
			BufferedReader fileInput = new BufferedReader(new FileReader(args[0]));
			BufferedReader QueryFileInput = new BufferedReader(new FileReader(args[1]));

			String tempEntry;
			while ((tempEntry = fileInput.readLine()) != null) {
				if (!tempEntry.matches("\\s*")) {
					webDocCollection.add(new WebDoc(tempEntry));
				}
			}
			fileInput.close();
			
			String tempQuery;
			while ((tempQuery = QueryFileInput.readLine()) != null) {
				if (!tempQuery.matches("\\s*")) {
					queryCollection.add(tempQuery);
				}
			}
			QueryFileInput.close();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("You should enter the path of the file!");
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.out.println("File is not found! Try this format - C:\\foldername\\filename.txt");
		} catch (IOException e) {
			System.out.println("IOException occurs.");
			e.printStackTrace();
		}

		// Create a new object of WebIndex.
		WebIndex webIndexContent = new WebIndex(WebIndex.TypeOfWords.CONTENT_WORD);
		WebIndex webIndexKey = new WebIndex(WebIndex.TypeOfWords.KEYWORD);

		System.out.println("\n-----Statistics Summary: (Excluding the files that cannot be accessed)-----");

		for (WebDoc wd : webDocCollection) {

			// Only the objects that successfully read their content from the URL or files.
			if (wd.getFileStatus() == WebDoc.FileStatus.SUCCESSFUL_READING) {
				System.out.println(wd.toString()); // Call toString() of all the objects of WebDoc.
				webIndexContent.add(wd); // add objects into the object of WebIndex
				webIndexKey.add(wd);
			}
		}

//		System.out.println("\n" + webIndexContent.getAllDocuments()); // Get all the documents stored in the object of WebIndex.
//		System.out.println("\n" + webIndexKey.getAllDocuments());
		System.out.println("\n" + webIndexContent.toString() + "\n");
		Set<WebDoc> contentResult = webIndexContent.getMatches("elephant");
		for (WebDoc wd : contentResult) {
			System.out.println(wd.getEntry() + "\n");
		}

		contentResult = webIndexContent.getMatches("document");
		for (WebDoc wd : contentResult) {
			System.out.println(wd.getEntry() + "\n");
		}

		System.out.println("-------------------------------------");
		Set<WebDoc> keyResult = webIndexKey.getMatches("peanuts");
		for (WebDoc wd : keyResult) {
			System.out.println(wd.getEntry() + "\n");
		}

		
		ArrayList<Set<WebDoc>> queryResult = new ArrayList<>();
		queryResult.add(QueryBuilder.parse("and(elephant,whale)").matches(webIndexContent));
		queryResult.add(QueryBuilder.parse("or(Peanuts,elephant)").matches(webIndexContent));
		queryResult.add(QueryBuilder.parse("and(elephant,NoT(elephant)").matches(webIndexContent));
		
	}

}
