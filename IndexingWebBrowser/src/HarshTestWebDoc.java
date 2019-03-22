import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import WebIndex.TypeOfWords;

import java.io.*;

public class HarshTestWebDoc {

	public static void main(String[] args) {
		WebDoc wd = new WebDoc("https://www.google.com");
		WebIndex wind = new WebIndex(WebIndex.TypeOfWords.CONTENT_WORD);
		wind.add(wd);
		try {
			System.out.println(QueryBuilder.parse("and(and,or)").matches(wind));
		} catch(StringIndexOutOfBoundsException e) {
			System.out.println("Query format may be incorrect");
		}
		
		
//		String wholeQuery = "abc";
//		if (wholeQuery.startsWith("(") && wholeQuery.endsWith(")")) {
//			wholeQuery = wholeQuery.substring(1, wholeQuery.length() - 1);
//		}
//		System.out.println(wholeQuery);
//		System.out.println(wholeQuery.matches("abc|bcd"));
//		System.out.println("(ate or cat)  and (dog or fisshe) and not jdg");
//		System.out.println(QueryBuilder.extractInfixQuery("ate or cat and dog"));
//		QueryBuilder.extractInfixQuery("(ate or cat)  and (dog or fisshe) and not jdg");
//		System.out.println(splitBracket("(not this and not peanuts) and almonds and (pistacchios and chestnuts)"));
//		System.out.println(splitOperator("a and b"));
//		System.out.println(parseCoveringBracket("((((abdnc))))"));
//		System.out.println(parseInfixQuery("(aaa and bbb) and cde"));
//		System.out.println(QueryBuilder.parse(QueryBuilder.parseInfixToPrefix("notfe")));
//		System.out.println(QueryBuilder.parse("and(  bfff, addff)").toString());
//		System.out.println(QueryBuilder.parseInfixForm((ate or cat) and (dog or fisshe))

//		System.out.println(QueryBuilder.parseSubquery("elephant,whale,and(fuck,dig),or(a,and(b,c))"));
//		String path = "file:Test.html";
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

	public static boolean containsBracket(String q) {
		if (q.contains("(") || q.contains(")")) {
			return true;
		} else {
			return false;
		}
	}

	public static String parseInfixQuery(String q) {
		String wholeQuery = q.toLowerCase();
		boolean hasBracket;
		wholeQuery = parseCoveringBracket(wholeQuery);
		hasBracket = containsBracket(wholeQuery);
		Stack<String> wordAndOperator = new Stack<>();

		ArrayList<String> splitedQuery;
		if (hasBracket) {
			splitedQuery = splitBracket(wholeQuery);
			for (int x = 0; x < splitedQuery.size(); x++) {
				if (containsBracket(splitedQuery.get(x))) {
					splitedQuery.set(x, parseCoveringBracket(splitedQuery.get(x)));
					// After getting rid of the outside covering bracket. (....)
					if (containsBracket(splitedQuery.get(x))) { // still has brackets
						splitedQuery.set(x, parseInfixQuery(splitedQuery.get(x)));
					} else {
						// there is no bracket left, which has been got rid of.
						splitedQuery.set(x, parseInfixQuery(splitedQuery.get(x)));
					}
				} else { // for whose that has no bracket, but may contains multiple operators.
					ArrayList<String> tempList = spliteWhiteSpace(splitedQuery.get(x));
					for(String str: tempList) {
						splitedQuery.add(x, str);
					}
				}
			}
			
			for (int x = 0; x < splitedQuery.size(); x++) {
				if(countOperators(splitedQuery.get(x)) > 1) {
					
				}
			}
			System.out.println(splitedQuery);
			System.out.println(combineParsedQuery(splitedQuery));
			return null;
		} else {
			if (countOperators(wholeQuery) > 1) {
				return combineParsedQuery(spliteWhiteSpace(wholeQuery));
			} else {
				return wholeQuery;
			}
			
		}
	}

	private static int countOperators(String tempString) {
		Pattern operatorPattern = Pattern.compile("(and)|(or)");
		Matcher operatorMatcher = operatorPattern.matcher(tempString);
		int count = 0;
		while (operatorMatcher.find()) {
			count++;
		}
		return count;
	}

	public static ArrayList<String> spliteWhiteSpace(String q) {
		q = q.trim();
		Stack<String> operators = new Stack<>();
		Stack<String> words = new Stack<>();
		String[] qArray = q.split(" ");
		ArrayList<String> list = new ArrayList<>();
		for (String str : qArray) {
			list.add(str);
		}
		return list;
	}

	private static String combineParsedQuery(ArrayList<String> parsedQuery) {
		ArrayList<String> queryCombination = new ArrayList<>();
		StringBuilder result = new StringBuilder();

		// Check whether it has operator: and / or
		boolean hasOperator = false;
		for (String str : parsedQuery) {
			if (str.matches("(and)|(or)")) {
				hasOperator = true;
			}
		}

		while (hasOperator) {
			// find Not, and combine them first
			for (int x = 0; x < parsedQuery.size(); x++) {
				String thisPart = parsedQuery.get(x);
				if (thisPart.equals("not")) {
					String nextPart = parsedQuery.get(x + 1);
					parsedQuery.set(x, thisPart + "(" + nextPart + ")");
					parsedQuery.remove(x + 1);
				}
			}

			// find 'and'/ 'or' and combine them
			for (int x = 0; x < parsedQuery.size(); x++) {
				String thisPart = parsedQuery.get(x);
//				if(x==0)
				if (thisPart.matches("(and)|(or)") && x > 0) {
					String lastPart = parsedQuery.get(x - 1);
					String nextPart = parsedQuery.get(x + 1);
					parsedQuery.set(x, thisPart + "(" + lastPart + "," + nextPart + ")");
					parsedQuery.remove(x - 1);
					parsedQuery.remove(x);
				}
			}

			hasOperator = false;
			// if there is still 'and' and 'or', do it again.
			for (String str : parsedQuery) {
				if (str.matches("(and)|(or)")) {
					hasOperator = true;
				}
			}
		}
		return parsedQuery.toString();
	}

	// recursive method
	public static String parseCoveringBracket(String q) {
		String thisQuery = q;
		if (thisQuery.charAt(0) == '(' && thisQuery.charAt(thisQuery.length() - 1) == ')') {
			String parsedString = thisQuery.substring(1, thisQuery.length() - 1);
			if (parsedString.charAt(0) == '(' && parsedString.charAt(parsedString.length() - 1) == ')') {
				thisQuery = parseCoveringBracket(parsedString);
			} else {
				thisQuery = parsedString;
			}
		}
		return thisQuery;
	}

	public static ArrayList splitBracket(String q) {
		String wholeQuery = q;
		StringBuilder eachPart = new StringBuilder();
		char thisChar;
		ArrayList<String> tempList = new ArrayList<>();
		Stack<Character> bracketStack = new Stack<>();

		for (int x = 0; x < wholeQuery.length(); x++) {
			thisChar = wholeQuery.charAt(x);
			if (thisChar == '(') {
				if (bracketStack.isEmpty() && eachPart.length() > 0) {
					tempList.add(eachPart.toString());
					eachPart = new StringBuilder();
				}
				eachPart.append(thisChar);
				bracketStack.push('(');
			} else if (thisChar == ')') {
				bracketStack.pop();
				eachPart.append(thisChar);
				if (bracketStack.isEmpty()) {
					tempList.add(eachPart.toString());
					eachPart = new StringBuilder();
				}
			} else {
				eachPart.append(thisChar);
			}

			if (x == wholeQuery.length() - 1 && eachPart.length() > 0) {// lash one
				tempList.add(eachPart.toString());
			}
		}
		StringBuilder result = new StringBuilder();
		for (String str : tempList) {
			result.append(str);
		}
		return tempList;
	}

	public static String solve(String wholeQuery) {
		// extract the operator in sequence
		ArrayList<String> operators = extractOperator(wholeQuery);
		// split the order by operators
		ArrayList<String> splitedQuery = splitQuery(wholeQuery);

		System.out.println(operators);
		System.out.println(splitedQuery);

		ArrayList<String> combinedQuery = new ArrayList<>();
		int index = 0;
		for (String str : operators) {
			combinedQuery.add(splitedQuery.get(index));
			combinedQuery.add(str);
			index++;
			combinedQuery.add(splitedQuery.get(index));
			index++;
		}
		return combinedQuery.toString();

	}

	public static ArrayList<String> splitQuery(String wholeQuery) {
		// Split the Query into parts based on the operators
		StringBuilder sb = new StringBuilder();
		String[] splitedQuery = wholeQuery.split("(and)|(or)");
		for (String str : splitedQuery) {
			sb.append(str + "\n");
		}
		ArrayList<String> resultList = new ArrayList<>();
		for (String str : splitedQuery) {
			resultList.add(str);
		}
		return resultList;
	}

	private static ArrayList<String> extractOperator(String wholeQuery) {
		ArrayList<String> operators = new ArrayList<>();
		StringBuilder eachWord = new StringBuilder();
		for (int x = 0; x < wholeQuery.length(); x++) {
			String thisWord = eachWord.toString();
			char thisChar = wholeQuery.charAt(x);
			if (thisChar == ' ' || thisChar == '(' || thisChar == ')') {
				if (thisWord.matches("(and)|(or)")) {
					operators.add(thisWord);
					eachWord = new StringBuilder();
				} else {
					eachWord = new StringBuilder();
				}
			} else {
				eachWord.append(thisChar);
			}
		}
		return operators;
	}

	public static String splitOperator(String q) {
		String thisQuery = q.trim();
		char[] thisQueryChar = thisQuery.toCharArray();
		ArrayList<String> splitedQuery = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		Stack<String> stackForString = new Stack<>();
		int countIndex = 0;
		for (char c : thisQueryChar) {
			if (c != ' ') {
				sb.append(c);
				if (countIndex == thisQueryChar.length - 1 && sb.length() > 0 && !stackForString.isEmpty()) {
					stackForString.push(sb.toString());
					StringBuilder tempString = new StringBuilder();
					for (String str : stackForString) {
						tempString.append(str);
					}
					stackForString.empty();
					splitedQuery.add(tempString.toString());
				}
			} else if (c == ' ' && stackForString.size() < 3) {
				sb.append(c);
				if (!stackForString.isEmpty() && stackForString.peek().equals("not") && stackForString.size() == 1) {
					stackForString.push(sb.toString());
					StringBuilder tempString = new StringBuilder();
					for (String str : stackForString) {
						tempString.append(str);
					}
					stackForString.empty();
					splitedQuery.add(tempString.toString());
					sb = new StringBuilder();
				} else if ((sb.toString().contains("and") || sb.toString().contains("or"))
						&& stackForString.isEmpty()) {
					splitedQuery.add(stackForString.pop());
					sb = new StringBuilder();
				} else if ((sb.toString().contains("and") || sb.toString().contains("or"))
						&& stackForString.size() == 1) {
					stackForString.push(sb.toString());
					sb = new StringBuilder();
				} else {
					stackForString.push(sb.toString());
					sb = new StringBuilder();
				}
			} else if (c == ' ' && stackForString.size() == 3) {
				sb.append(c);
				if (sb.toString().contains("not")) { // it is not finished yet.
					stackForString.push(sb.toString());
					sb = new StringBuilder();
				} else {
					stackForString.push(sb.toString());
					StringBuilder tempString = new StringBuilder();
					for (String str : stackForString) {
						tempString.append(str);
					}
					stackForString.empty();
					splitedQuery.add(tempString.toString());
					sb = new StringBuilder();
				}
			} else if (c == ' ' && stackForString.size() == 4) {
				stackForString.push(sb.toString());
				StringBuilder tempString = new StringBuilder();
				for (String str : stackForString) {
					tempString.append(str);
				}
				stackForString.empty();
				splitedQuery.add(tempString.toString());
				sb = new StringBuilder();
			}
			countIndex++;
		}
		return splitedQuery.toString();
	}

	public static String transformInfixToPrefix(String q) {
		String thisQuery = q.replaceAll("\\s", "");
		String left;
		String right;
		String[] splitedResult;
		if (q.contains("and")) {
			splitedResult = q.split("and");
			left = splitedResult[0];
			right = splitedResult[1];
			return "and(" + left + "," + right + ")";
		} else if (q.contains("or")) {
			splitedResult = q.split("or");
			left = splitedResult[0];
			right = splitedResult[1];
			return "or(" + left + "," + right + ")";
		} else if (q.contains("not")) {
			splitedResult = q.split("not");
			right = splitedResult[0];
			return "not(" + right + ")";
		} else {
			return thisQuery;
		}
	}

}
