package webBrowserGUI;

import java.awt.CardLayout;
import java.awt.Font;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import webBrowserModel.Query;
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
			htmlContent = view.addTabToHtmlBrowserCard();
			String url = urlInput.getText();
			File localFileEntry;
			Pattern entryPattern = Pattern.compile("([Ff][Ii][Ll][Ee]:)(.++)");
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
		JTextField infixQuery;
		JTextField prefixQuery;
		CardLayout layoutControl;
		JPanel cards;

		public SearchActionHandler() {
			infixQuery = view.getInfixQuery();
			prefixQuery = view.getPrefixQuery();
			layoutControl = view.getCardLayoutControl();
			cards = view.getCards();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String query;
			boolean isValid;
			String queryType;
			Set<WebDoc> keywordResult;
			Set<WebDoc> contentWordResult;
			JPanel keywordPanel = view.getKeywordResultPanel();
			JPanel contentWordPanel = view.getContentWordResultPanel();
			if (e.getActionCommand().equals("infix")) {
				query = infixQuery.getText().trim().toLowerCase();
				isValid = validateInfixQuery(query);
				queryType = "infix query";

				if (isValid) {
					Query infixQuery = QueryBuilder.parseInfixForm(query);
					if (infixQuery != null) {
						keywordResult = infixQuery.matches(webIndex_Keyword);
						contentWordResult = infixQuery.matches(webIndex_ContentWord);
						addResults(keywordPanel, keywordResult);
						addResults(contentWordPanel, contentWordResult);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Unfortunately" + queryType + " you entered is invalid",
							"Content Word Result", JOptionPane.WARNING_MESSAGE);
				}
			} else { // (e.getActionCommand().equals("prefix"))
				query = prefixQuery.getText().trim().toLowerCase();
				isValid = validatePrefixQuery(query);
				queryType = "prefix query";

				if (isValid) {
					Query prefixQuery = QueryBuilder.parse(query);
					if (prefixQuery != null) {
						keywordResult = prefixQuery.matches(webIndex_Keyword);
						contentWordResult = prefixQuery.matches(webIndex_ContentWord);
						addResults(keywordPanel, keywordResult);
						addResults(contentWordPanel, contentWordResult);
					} else {
						JOptionPane.showMessageDialog(null, "Unfortunately" + queryType + " you entered is invalid",
								"Content Word Result", JOptionPane.WARNING_MESSAGE);
					}
				}
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

		private void addResults(JPanel resultPanel, Set<WebDoc> resultOfWebDocs) {
			JButton eachResult;
			resultPanel.removeAll();
			if (resultOfWebDocs != null) {
				for (WebDoc doc : resultOfWebDocs) {
					String urlEntry = doc.getEntry();
					eachResult = new JButton(urlEntry);
					eachResult.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
					eachResult.setFont(new Font("Arial", Font.BOLD, 17));
					resultPanel.add(eachResult);
					eachResult.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							JEditorPane htmlContent = view.addTabToHtmlBrowserCard();
							
							if (urlEntry.matches("([Ff][Ii][Ll][Ee]:)(.++)")) { // local html file
								String fileEntry = urlEntry.substring(5, urlEntry.length());
								try {
									htmlContent.setPage(new File(fileEntry).toURI().toURL());
									layoutControl.show(cards, view.HTML_BROWSER_TAG);
								} catch (MalformedURLException e1) {
									JOptionPane.showMessageDialog(null, "Incorrect form of file path", "Error",
											JOptionPane.WARNING_MESSAGE);
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(null, "File path not accessible", "Error",
											JOptionPane.WARNING_MESSAGE);
								}
							} else { // url link
								try {
									htmlContent.setPage(urlEntry);
									layoutControl.show(cards, view.HTML_BROWSER_TAG);
								} catch (MalformedURLException e1) {
									JOptionPane.showMessageDialog(null, "Incorrect form of URL", "Error",
											JOptionPane.WARNING_MESSAGE);
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(null,
											"URL not accessible, please check internet connect", "Error",
											JOptionPane.WARNING_MESSAGE);
								}
							}
						}
					});
					
					resultPanel.repaint();
					resultPanel.revalidate();
				}
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
			if (command.equals(view.HTML_BROWSER_TAG)) {
				layoutControl.show(cards, view.HTML_BROWSER_TAG);
			} else if (command.equals(view.QUERY_BROWSER_TAG))
				layoutControl.show(cards, view.QUERY_BROWSER_TAG);
		}
	}
}
