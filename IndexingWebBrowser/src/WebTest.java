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

		ArrayList<WebDoc> webDocCollection = new ArrayList<>();
		ArrayList<String> queryCollection = new ArrayList<>();

		// Read two txt files from the command args
		try {
//			BufferedReader fileInput = new BufferedReader(new FileReader(args[0]));
//			BufferedReader QueryFileInput = new BufferedReader(new FileReader(args[1]));
			BufferedReader fileInput = new BufferedReader(new FileReader("testFile.txt"));
			BufferedReader QueryFileInput = new BufferedReader(new FileReader("sampleQueryFileStage2.txt"));

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

		// New objects of WebIndex, one for keywords, one for content words.
		WebIndex webIndexContent = new WebIndex(WebIndex.TypeOfWords.CONTENT_WORD);
		WebIndex webIndexKey = new WebIndex(WebIndex.TypeOfWords.KEYWORD);

		for (WebDoc wd : webDocCollection) {
			// Only the objects that successfully read their content from the URL or files.
			if (wd.getFileStatus() == WebDoc.FileStatus.SUCCESSFUL_READING) {
				System.out.println(wd.toString());
				webIndexContent.add(wd);
				webIndexKey.add(wd);
			}
		}
		System.out.println();

		// Testing WebIndex .toString()
		System.out.println(webIndexContent.toString());
		System.out.println(webIndexKey.toString() + "\n");

		// Testing WebIndex .getMatches()
		Set<WebDoc> contentMatchingResult = webIndexContent.getMatches("elephant");
		if (contentMatchingResult != null) {
			System.out.println(contentMatchingResult.toString() + "\n");
		}

		contentMatchingResult = webIndexContent.getMatches("document");
		if (contentMatchingResult != null) {
			System.out.println(contentMatchingResult.toString() + "\n");
		}

		Set<WebDoc> keyMatchingResult = webIndexKey.getMatches("peanuts");
		if (keyMatchingResult != null) {
			System.out.println(keyMatchingResult.toString() + "\n");
		}

		// Testing Query
		ArrayList<Set<WebDoc>> queryResult = new ArrayList<>();
		try {
			Query q = QueryBuilder.parse("and(elephant,whale)");
			queryResult.add(q.matches(webIndexContent));
			
//		queryResult.add(QueryBuilder.parse("or(Peanuts,elephant)").matches(webIndexContent));
//		queryResult.add(QueryBuilder.parse("and(elephant,NoT(extra)").matches(webIndexContent));
//		queryResult.add(QueryBuilder.parse("and(elephant,NoT(extra)").matches(webIndexContent));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

//		System.out.println("[1]" + QueryBuilder.parse("and(elephant,whale)").toString());
//		System.out.println("[2]" + QueryBuilder.parse("or(Peanuts,elephant)").toString());
//		System.out.println("[3]" + QueryBuilder.parse("and(elephant,NoT(extra))").toString());
//		for(String queryStr : queryCollection) {
//			queryResult.add(QueryBuilder.parse(queryStr).matches(webIndexContent));
//		}

//		for(String query : queryCollection) {
//			queryResult.add(QueryBuilder.parse(query).matches(webIndexContent));
//			System.out.println(QueryBuilder.parse(query).toString());
//		}
		System.out.println("------------------------------------------");
		int n = 0;
		for (Set<WebDoc> setWd : queryResult) {
			n++;
			System.out.print("[" + n + "]");
			if (setWd != null) {
				System.out.println(setWd.toString() + "\n");
			}
		}

	}

}
