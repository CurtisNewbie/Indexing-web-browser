import java.io.*;
import java.util.ArrayList;

/**
 * This class will be used to load the web documents(files or URLs) indicated in
 * a text file, and then produce the summary statistics about these web
 * documents. Formats of statistics: [source] [word count] [alphabetic range of
 * content words] [number of distinct keywords] [evaluation of the HTML format]
 * 
 * @author 180139796
 * @version 1.1 Created on 11/01/2019
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
			System.out.println("---File:" + args[0] + " has been read.---");

			String temp;
			while ((temp = fileIn.readLine()) != null) {
				webDocCollection.add(new WebDoc(temp));
			}
			fileIn.close();

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("You should enter the path of the file!");
		} catch (FileNotFoundException e) {
			System.out.println("File is not found! Try this format - C:\\file\\abc.txt");
		} catch (IOException e) {
			System.out.println("Exceptions occurred:");
			e.printStackTrace();
		}

	}
}
