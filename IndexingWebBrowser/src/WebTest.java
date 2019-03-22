import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class will be used to read the web documents(entries: files or URLs)
 * indicated in a text file as well as to read the query from another test file.
 * It then calls the method of objects of WebDoc to produce the summary
 * statistics about these web documents. A WebIndex will then be created to
 * provide additional functionalities, such as searching and providing a overall
 * summary of the WebDoc in this WebIndex. The query (infix and prefix) will be
 * used to search for content words and keywords.
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
			try {
				String tempEntry;
				while ((tempEntry = fileInput.readLine()) != null) {
					if (!tempEntry.matches("\\s*")) {
						webDocCollection.add(new WebDoc(tempEntry));
					}
				}

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
			} finally {
				fileInput.close();
				QueryFileInput.close();
			}
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

		// Calling the toString() of each WebDoc - For Stage 1.
		for (WebDoc wd : webDocCollection) {
			// Only the objects that successfully read their content from the URL or files.
			if (wd.getFileStatus() == WebDoc.FileStatus.SUCCESSFUL_READING) {
				System.out.println(wd.toString());
				webIndexContent.add(wd);
				webIndexKey.add(wd);
			}
		}
		System.out.println();

		// Testing WebIndex .toString() - For Stage 1.
		System.out.println(webIndexContent.toString());
		System.out.println(webIndexKey.toString() + "\n");

		// Testing WebIndex .getMatches() - For Stage 1.
//		Set<WebDoc> contentMatchingResult = webIndexContent.getMatches("elephant");
//		if (contentMatchingResult != null) {
//			System.out.println(contentMatchingResult.toString() + "\n");
//		}
//
//		Set<WebDoc> keyMatchingResult = webIndexKey.getMatches("zebra");
//		if (keyMatchingResult != null) {
//			System.out.println(keyMatchingResult.toString() + "\n");
//		}

		// Get rid of the illegal prefixQuery - For Stage 2.
		Iterator<String> prefixIterator = prefixQueryCollection.iterator();
		while (prefixIterator.hasNext()) {
			String queryStr = prefixIterator.next();
			/*
			 * (and or); (and , or); (and and); (and , and); (or and); (or , and); and
			 * finally, more than two '(' or')' sticking together.
			 */
			if (queryStr.matches(
					"(.*?and\\s*,*\\s*or.*?)|(.*?and\\s*,*\\s*and.*?)|(.*?or\\s*,*\\s*and.*?)|(.*?\\({1,}\\){1,}.*?)")) {
				prefixIterator.remove();
			}
		}
		// Get rid of the illegal infixQuery - For Stage 2.
		Iterator<String> infixIterator = infixQueryCollection.iterator();
		while (infixIterator.hasNext()) {
			String queryStr = infixIterator.next();
			/*
			 * It filters following illegal situations: (and and); (and or); (or and); (or
			 * or); (not and); (not not); (not or); and finally, more than two '(' or')'
			 * sticking together.
			 */
			if (queryStr.matches(
					"(.*?and\\s*and.*?)|(.*?and\\s*or.*?)|(.*?or\\s*and.*?)|(.*?or\\s*or.*?)|(.*?not\\s*and.*?)|(.*?not\\s*not.*?)|(.*?not\\s*or.*?)|(.*?\\({1,}\\){1,}.*?)")) {
				infixIterator.remove();
			}
		}

		/*
		 * For Stage 2. Calling the .matches() method of each (prefix) Query object.
		 * Calling the .toString() method of each (infix) Query object.
		 */
		try {
			System.out.println("prefixQuery:");
			for (String queryStr : prefixQueryCollection) {
				System.out.println("Query:::" + queryStr);
				if (queryStr.toLowerCase().matches("\\s*not\\s*\\(\\s*[A-Za-z]*\\s*\\)\\s*")) {
					// It's a pure NotQuery that its sub-query is a atomic query. The result will be
					// meaningless.
					System.out.println("Result-> NULL");
					System.out.println();
				} else {
					System.out.println("Result->" + QueryBuilder.parse(queryStr).matches(webIndexContent));
					System.out.println();
				}
			}

			System.out.println("infixQuery:");
			for (String queryStr : infixQueryCollection) {
				System.out.println("Query:::" + queryStr);
				System.out.println("Result->" + QueryBuilder.parseInfixForm(queryStr).toString());
				System.out.println();
			}
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("This Query may be illegal.");
		}

		/*
		 * For Stage 2. Further demonstrating how the QueryBuilder.parseInfixForm()
		 * works: [each element]: e.g., A and B -> and([A],[B])
		 */
		System.out.println(
				"\n:::Further demonstrating how the QueryBuilder.parseInfixForm() works, transforming from Infix to Prefix:");
		System.out.println(":::Note:[each element]: e.g., A and B -> and([A],[B]):");
		System.out.println("Manual Transformation to InfixFrom: (whale and fish) and not elephant");
		System.out.println("Result of QueryBuilder.parseInfixForm().toString() : "
				+ QueryBuilder.parseInfixForm("(whale and fish) and not elephant").toString());

		// More tests for processing prefix query - For Stage 2.
		System.out.println("\n:::More tests for processing prefix query:");
		System.out.println(QueryBuilder.parse("and(elephant,NoT   (asdfasdf),number,yikes,banana)").toString());
		System.out.println(QueryBuilder.parse("oR    (Peanuts,elephant,not(yikes))").toString());
		System.out.println(QueryBuilder.parse("and(not(elephant),birdy,NoT(extra))").toString());

		// More tests for processing infix query - For Stage 2.
		System.out.println("\n:::More tests for processing infix query:");
		System.out.println(QueryBuilder.parseInfixForm("Banana and (cat and dog) and bird or not coffee").toString());
		System.out.println(QueryBuilder.parseInfixForm("Banana and ((cat and dog) and bird) or coffee").toString());
		System.out.println(QueryBuilder.parseInfixForm("not Banana").toString());
		System.out.println(QueryBuilder.parseInfixForm("not Banana and not Chocolate").toString());

	}

}
