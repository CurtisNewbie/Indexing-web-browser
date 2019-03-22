import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
		ArrayList<String> prefixQueryCollection = new ArrayList<>();
		ArrayList<String> infixQueryCollection = new ArrayList<>();

		// Read two txt files from the command args
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
					tempQuery = tempQuery.trim().toLowerCase();
					if (tempQuery.matches("(and\\s*\\(.+,.+\\))|(or\\s*\\(.+,.+\\))|(not\\s*\\(.+\\))")) {
						prefixQueryCollection.add(tempQuery);
					} else {
						infixQueryCollection.add(tempQuery);
					}
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

		contentMatchingResult = webIndexContent.getMatches("zebra");
		if (contentMatchingResult != null) {
			System.out.println(contentMatchingResult.toString() + "\n");
		}

		Set<WebDoc> keyMatchingResult = webIndexKey.getMatches("zebra");
		if (keyMatchingResult != null) {
			System.out.println(keyMatchingResult.toString() + "\n");
		}

		System.out.println("-------------------------------------------------");

		// Testing Query
		ArrayList<Set<WebDoc>> queryResult = new ArrayList<>();

//		System.out.println(QueryBuilder.parse("and(elephant,whale,number,yikes,ffff)").matches(webIndexContent));
//		System.out.println(QueryBuilder.parse("oR(Peanuts,elephant,not(yikes)").matches(webIndexContent));
//		System.out.println(QueryBuilder.parse("NoT(asdfasdf)").matches(webIndexContent));
//		System.out.println((QueryBuilder.parse("and(not(elephant),ass,NoT(extra))").matches(webIndexContent)));
//
		System.out.println("\n[1]" + QueryBuilder.parse("and(elephant,whale,number,yikes,ffff)").toString());
//		System.out.println("[2]" + QueryBuilder.parse("oR(Peanuts,elephant,not(yikes))").toString());
//		System.out.println("[3]" + QueryBuilder.parse("NoT(asdfasdf)").toString());
//		System.out.println("[4]" + QueryBuilder.parse("and(not(elephant),ass,NoT(extra))").toString());
		Iterator<String> prefixIterator = prefixQueryCollection.iterator();
		while (prefixIterator.hasNext()) {
			String queryStr = prefixIterator.next();
			// (and or) (and , or) (and and) (and , and) (or and) (or , and)
			if (queryStr.matches("(.*?and\\s*,*\\s*or.*?)|(.*?and\\s*,*\\s*and.*?)|(.*?or\\s*,*\\s*and.*?)")) {
				prefixIterator.remove();
			}
		}

		for (String queryStr : prefixQueryCollection) {
			try {
				System.out.println(QueryBuilder.parse(queryStr).matches(webIndexContent));
				System.out.println("PreFixQuery : " + QueryBuilder.parse(queryStr).toString());
				System.out.println();
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("Query format maybe illegal : " + queryStr);
			}

//		for (String queryStr : infixQueryCollection) {
////			System.out.println(QueryBuilder.parseInfixForm(queryStr).matches(webIndexContent));
//			System.out.println("InFixQuery : " + QueryBuilder.parseInfixForm(queryStr).toString());
//			System.out.println();
		}

	}

}
