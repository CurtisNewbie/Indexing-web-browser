import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HarshTestWebDoc {

	public static void main(String[] args) {
		String path = "http://www.i18nguy.com/markup/metatags.html";
		WebDoc doc1 = new WebDoc(path, WebDoc.FileType.WEB_URL);

////		WebDoc doc2 = new WebDoc("htt");
////		WebDoc doc3 = new WebDoc("file asdfasdf");
////		WebDoc doc4 = new WebDoc("fiLe:assd");

//		String st = "as111df <21asdfjlj @123 / <[> a<asdf333kas2df?> ";
//		
//		String pattern = "^a";
//		Pattern pa = Pattern.compile(pattern);
//		Matcher matche = pa.matcher(st);
//		
//		ArrayList<String> ar = new ArrayList<>();
//		while(matche.find()) {
//			ar.add(matche.group(0));
//		}
//		
//		for(String st1 : ar) {
//			System.out.println(st1);
//		}
//		System.out.println(ar.toString());
//		

//		HarshTestWebDoc obj1 = new HarshTestWebDoc();
//		HarshTestWebDoc obj2 = new HarshTestWebDoc();
////				
//		System.out.println(obj1.getKeyWords("<html>\r\n" + "  <head>\r\n" + "    <title>test</title>\r\n"
//				+ "    <meta name=\"keywords\" contents=\"peanuts almonds,pistacchios,chestnuts,\">\r\n"
//				+ "  </head>\r\n" + "\r\n" + "  <body>\r\n" + "    <h1>A list</h1>\r\n" + "<ul>\r\n"
//				+ "<li>Peanuts</li>\r\n" + "<li>Almonds</li>\r\n" + "<li>Pistacchios</li>\r\n"
//				+ "<li>Chestnuts</li>\r\n" + "</ul>\r\n" + "    <address/>\r\n" + "  </body>\r\n" + "</html>\r\n"
//				+ "\r\n" + "\r\n" + ""));
//
//		System.out.println(obj2.getKeyWords("<meta name=\"copyright\" content=\"&copy; 2002, 2003, 2004 Tex Texin\">\r\n" + 
//				"<meta http-equiv=\"Content-Language\" content=\"en-US\">\r\n" + 
//				"<meta name=\"keywords\"content=\"html, http, meta tag, meta tags, cache, expires, refresh, robot, robots, web-bot, googlebot, crawler, w3c, web, consortium,\">\r\n" + 
//				"<meta name=\"keywords\" lang=\"en-us\" content=\"cultural differences, Texin, I18nGuy, XenCraft, consult, consultant, expert\">\r\n" + 
//				"<meta name=\"keywords\" lang=\"en-gb\" content=\"internationalisation, localisation, globalisation\">\r\n" + 
//				"<meta name=\"robots\" content=\"all\">"));
	}
	
	//testing
	
	private static ArrayList<String> getKeyWords(String content) {

		ArrayList<String> result = new ArrayList<>();
		Pattern keywordPattern = Pattern.compile("<meta name=\"keywords\"(.*)content.?=\"([^>]*)\">");
		Matcher keywordMatcher = keywordPattern.matcher(content);

		StringBuilder tempOutput = new StringBuilder(""); // temporary output of keywords that may contain space, punctuation marks.
		while (keywordMatcher.find()) {
			tempOutput.append(keywordMatcher.group(2));
		}
		
		Pattern wordFilterPattern = Pattern.compile("[a-zA-Z]++");
		Matcher wordFilterMatcher = wordFilterPattern.matcher(tempOutput);
		while (wordFilterMatcher.find()) { // refine the result; extract words from the temporary output.
			result.add(wordFilterMatcher.group(0) + " ");
		}

		return result; // return the arraylist; though it's passing the references of this object, but
						// this method will only initiate once.
	}

}
