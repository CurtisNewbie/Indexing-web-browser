package webBrowserGUI;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import webBrowserModel.QueryBuilder;
import webBrowserModel.WebDoc;
import webBrowserModel.WebIndex;
import webBrowserModel.WebIndex.TypeOfWords;

public class WebBrowserController {

	private WebBrowserView view;
	private WebIndex webIndex_Keyword;
	private WebIndex webIndex_ContentWord;
	private Set<String> browsingHistory;

	public WebBrowserController(WebBrowserView view) {
		this.view = view;
		webIndex_Keyword = new WebIndex(TypeOfWords.KEYWORD);
		webIndex_ContentWord = new WebIndex(TypeOfWords.CONTENT_WORD);
		browsingHistory = new TreeSet<String>();

		view.addConfirmActionListener(new ConfirmActionHandler());
		view.addSearchActionListener(new SearchActionHandler());
		view.addMenuItemActionListener(new MenuItemActionHandler());
	}

	private class ConfirmActionHandler implements ActionListener {
		JTextField urlInput;
		JEditorPane htmlContent;
		JTextArea historyTextArea;

		public ConfirmActionHandler() {
			this.urlInput = view.getUrlTextField();
			this.historyTextArea = view.getHistoryTextArea();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.htmlContent = view.addTabToHtmlBrowserCard();
			String url = urlInput.getText();
			
			File localFileEntry;
			Pattern entryPattern = Pattern.compile("(file:)(.++)");
			Matcher entryMatcher = entryPattern.matcher(url);
			if (entryMatcher.find()) { // it is a local file
				localFileEntry = new File(entryMatcher.group(2)); // extract the true file path.
				try {
					htmlContent.setPage(localFileEntry.toURI().toURL());
					if (!browsingHistory.contains(url)) {
						WebDoc wd = new WebDoc(url);
						browsingHistory.add(url);
						webIndex_Keyword.add(wd);
						webIndex_ContentWord.add(wd);
					}
					StringBuilder history = new StringBuilder();
					for (String u : browsingHistory) {
						history.append(u + "\n");
					}
					historyTextArea.setText(history.toString());
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(null, "Incorrect form of file path", "Error",
							JOptionPane.WARNING_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "File path not accessible", "Error",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {// it's a url
				try {
					url = url.toLowerCase();
					htmlContent.setPage(new URL(url));
					if (!browsingHistory.contains(url)) {
						WebDoc wd = new WebDoc(url);
						browsingHistory.add(url);
						webIndex_Keyword.add(wd);
						webIndex_ContentWord.add(wd);
					}
					StringBuilder history = new StringBuilder();
					for (String u : browsingHistory) {
						history.append(u + "\n");
					}
					historyTextArea.setText(history.toString());
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(null, "Incorrect form of URL", "Error", JOptionPane.WARNING_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "URL not accessible, please check internet connect", "Error",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	private class SearchActionHandler implements ActionListener {
		JTextArea keywordResultTextArea;
		JTextArea contentWordResultTextArea;
		JTextField infixQuery;
		JTextField prefixQuery;

		public SearchActionHandler() {
			keywordResultTextArea = view.getKeywordQueryResultTextArea();
			contentWordResultTextArea = view.getContentWordQueryResultTextArea();
			infixQuery = view.getInfixQuery();
			prefixQuery = view.getPrefixQuery();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String query;
			boolean isValid;
			String queryType;
			if (e.getActionCommand().equals("infix")) {
				query = infixQuery.getText().trim().toLowerCase();
				isValid = validateInfixQuery(query);
				queryType = "infix query";
			} else { // (e.getActionCommand().equals("prefix"))
				query = prefixQuery.getText().trim().toLowerCase();
				isValid = validatePrefixQuery(query);
				queryType = "prefix query";
			}

			if (isValid) {
				Set<WebDoc> keywordResult = QueryBuilder.parseInfixForm(query).matches(webIndex_Keyword);
				StringBuilder queryResult;
				if (keywordResult == null) {
					keywordResultTextArea.setText("Unfortunatelly, nothing found.");
				} else {
					queryResult = new StringBuilder();
					for (WebDoc doc : keywordResult) {
						queryResult.append(doc.toString() + "\n");
					}
					keywordResultTextArea.setText(queryResult.toString());
				}

				Set<WebDoc> contentWordResult = QueryBuilder.parseInfixForm(query).matches(webIndex_ContentWord);
				if (contentWordResult == null) {
					contentWordResultTextArea.setText("Unfortunatelly, nothing found.");
				} else {
					queryResult = new StringBuilder();
					for (WebDoc doc : contentWordResult) {
						queryResult.append(doc.toString() + "\n");
					}
					contentWordResultTextArea.setText(queryResult.toString());
				}
			} else {
				JOptionPane.showMessageDialog(null, "The " + queryType + " you entered is invalid",
						"Infix query invalid", JOptionPane.WARNING_MESSAGE);
			}
		}

		/**
		 * It validates the prefix query. It filters following illegal situations. (and
		 * or); (and , or); (and and); (and , and); (or and); (or , and); and finally,
		 * more than two '(' or')' sticking together.
		 * 
		 * @param q prefix query
		 * @return a boolean value represents whether the query is valid.
		 */
		private boolean validatePrefixQuery(String q) {
			if (q.matches(
					"(.*?and\\s*,*\\s*or.*?)|(.*?and\\s*,*\\s*and.*?)|(.*?or\\s*,*\\s*and.*?)|(.*?\\({1,}\\){1,}.*?)")) {
				return false;
			} else {
				return true;
			}
		}

		/**
		 * It validates the infix query. It filters following illegal situations: (and
		 * and); (and or); (or and); (or or); (not and); (not not); (not or); and
		 * finally more than two '(' or')' sticking together.
		 * 
		 * @param q infix query
		 * @return a boolean value represents whether the query is valid.
		 */
		private boolean validateInfixQuery(String q) {
			if (q.matches(
					"(.*?and\\s*and.*?)|(.*?and\\s*or.*?)|(.*?or\\s*and.*?)|(.*?or\\s*or.*?)|(.*?not\\s*and.*?)|(.*?not\\s*not.*?)|(.*?not\\s*or.*?)|(.*?\\({1,}\\){1,}.*?)")) {
				return false;
			} else {
				return true;
			}
		}
	}

	private class MenuItemActionHandler implements ActionListener {
		CardLayout layoutControl;
		JPanel cards;

		public MenuItemActionHandler() {
			layoutControl = view.getCardLayoutControl();
			cards = view.getCards();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals(view.WEB_BROWSER_TAG)) {
				layoutControl.show(cards, view.WEB_BROWSER_TAG);
			} else if (command.equals(view.QUERY_BROWSER_TAG))
				layoutControl.show(cards, view.QUERY_BROWSER_TAG);
		}
	}
}
