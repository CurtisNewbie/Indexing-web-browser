import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class HarshTestWebDoc {

	public static void main(String[] args) {
		
		System.out.println(QueryBuilder.parseSubquery("elephant,whale,and(fuck,dig),or(a,and(b,c))"));
//		String path = "http://www.googlasdasdsade.com";
//		try {
//			WebDoc doc1 = new WebDoc(path);
//			System.out.println(doc1.getContentWords().toString());
//			System.out.println(doc1.getEntry());
//		} catch (Exception e) {
//			System.out.println("Errors");
//		}
//		try {	
//		URL url = new URL(path);
//		BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
//		System.out.println(br.readLine());
//		br.close();
//		} catch (MalformedURLException e) {
//			// TODO: handle exception
//			System.out.println("URL incorrect");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		System.out.println(doc1.getContentWords().toString());
//	
//		System.out.println(doc1.getContent());
//		System.out.println(doc1.toString());

//		WebIndex obj = new WebIndex();
//		obj.add(doc1);
//		System.out.println(obj.getAllDocuments());
//		System.out.println(obj.getMatches("Google"));

////		WebDoc doc2 = new WebDoc("htt");
////		WebDoc doc3 = new WebDoc("file asdfasdf");
//		WebDoc doc4 = new WebDoc("fiLe:assd");

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

}
