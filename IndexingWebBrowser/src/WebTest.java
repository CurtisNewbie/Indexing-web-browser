import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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

		// Read a txt file from the command args
		try {
			BufferedReader fileInput = new BufferedReader(new FileReader(args[0]));
			System.out.println("--------------------------Reading File:" + args[0] + "-----------------------------");

			String tempEntry;
			while ((tempEntry = fileInput.readLine()) != null) {
				if (!tempEntry.matches("\\s*")) {
					webDocCollection.add(new WebDoc(tempEntry));
				}
			}
			fileInput.close();
			System.out.println("-----------------------------Reading finished--------------------------------------");

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("You should enter the path of the file!");
		} catch (FileNotFoundException e) {
			System.out.println("File is not found! Try this format - C:\\foldername\\filename.txt");
		} catch (IOException e) {
			System.out.println("IOException occurs.");
			e.printStackTrace();
		}

		// Create a new object of WebIndex.
		WebIndex wi = new WebIndex();

		System.out.println("\n-----Statistics Summary: (Excluding the files that cannot be accessed)-----");
		
		for (WebDoc ob : webDocCollection) {
			
			// Only the objects of WebDoc that successfully read their content from the URL or files.
			if (ob.getFileStatus() == WebDoc.FileStatus.SUCCESSFUL_READING) {
				System.out.println(ob.toString()); // Call toString() of all the objects of WebDoc.
				wi.add(ob);  //add objects into the object of WebIndex
			}
		}

		System.out.println("\n" + wi.getAllDocuments()); // Get all the documents stored in the object of WebIndex.
		
		System.out.println(wi.getMatches("elephant")); // Return the entry of the web document that contains this word.
		System.out.println(wi.getMatches("helvetica"));
		
		System.out.println("\n" + wi.toString()); // Get all the documents stored in the object of WebIndex

		Scanner keyboard = new Scanner(System.in); // Get the string keyboard input to search through the web index.
		System.out.println("\nWhat do you want to search?");
		System.out.println(wi.getMatches(keyboard.nextLine()));
		

	}

}
