
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import webBrowserModel.Query;
import webBrowserModel.QueryBuilder;
import webBrowserModel.WebDoc;
import webBrowserModel.WebIndex;
import webBrowserModel.WebIndex.TypeOfWords;

/**
 * This is the controller class in the MVC architecture. It deals with the
 * interaction between the view (WebBrowserView class) and the model
 * (webBrowserModel package). In other words, it registers the listeners for the
 * view; uses the model to calculate and handle the requests; and updates the
 * view for showing the results.
 * 
 * @author 180139796
 *
 */
public class WebBrowserController {

	/**
	 * The WebBrowserView object as the view in (MVC architecture)
	 */
	private WebBrowserView view;

	/**
	 * The index for keyword, that is used for query searching.
	 */
	private WebIndex webIndex_Keyword;

	/**
	 * The index for content word, that is used for query searching.
	 */
	private WebIndex webIndex_ContentWord;

	/**
	 * The collection of WebDoc, which are the URLs that are successfully accessed.
	 */
	private Set<WebDoc> webdocCollection;

	/**
	 * The collection of URLs as String, which are successfully accessed.
	 */
	private Set<String> browsingHistory;

	/**
	 * The path of the file that is used to save the index and browsing history.
	 */
	private final String LOG_FILE_PATH = "log.txt";

	/**
	 * It represents when the log starts. It is used for reading browsing history.
	 */
	private final String INDEX_LOG_START = "[INDEX_LOG_START]";

	/**
	 * It represents when the log ends. It is used for reading browsing history.
	 */
	private final String INDEX_LOG_END = "[INDEX_LOG_END]";

	/**
	 * It initiates the indexes for keywords and content words, the TreeSet for
	 * browsing history and the collection of WebDoc. The addFrameWindowListener
	 * method, addConfirmActionListener method, addSearchActionListener method and
	 * the addMenuItemActionListener method of the WebBrowserView object are called
	 * to assign the listeners.
	 * 
	 * @param view The WebBrowserView object
	 */
	public WebBrowserController(WebBrowserView view) {
		this.view = view;
		webIndex_Keyword = new WebIndex(TypeOfWords.KEYWORD);
		webIndex_ContentWord = new WebIndex(TypeOfWords.CONTENT_WORD);
		browsingHistory = new TreeSet<>();
		webdocCollection = new TreeSet<>();

		view.addFrameWindowListener(new WindowEventHandler());
		view.addConfirmActionListener(new ConfirmActionHandler());
		view.addSearchActionListener(new SearchActionHandler());
		view.addMenuItemActionListener(new MenuItemActionHandler());
	}

	/**
	 * This inner class is used to handle the 'Confirm Action'. The reason why it's
	 * named the Confirm Action is that, when the user has entered the URL entry and
	 * press the confirm button or the 'Enter', it accepts the URL entries and
	 * displays the content in a new tab.
	 * 
	 *
	 */
	private class ConfirmActionHandler implements ActionListener {
		JTextField urlInput;
		JEditorPane htmlContent;
		JPanel historyRecordPanel;
		JTabbedPane tabbedPane;

		public ConfirmActionHandler() {
			this.urlInput = view.getUrlTextField();
			this.historyRecordPanel = view.getHistoryRecordPanel();
			this.tabbedPane = view.getWebBrowserTabbedPane();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// index of the new tab
			int index = tabbedPane.getTabCount();
			String title = "Tab " + (index + 1);
			htmlContent = view.addTabToHtmlBrowserCard(title);

			String urlEntry = urlInput.getText().trim();
			File localFileEntry;
			Pattern entryPattern = Pattern.compile("([Ff][Ii][Ll][Ee]:)(.++)");
			Matcher entryMatcher = entryPattern.matcher(urlEntry);

			if (entryMatcher.find()) { // if it is a local file
				localFileEntry = new File(entryMatcher.group(2)); // extract the true file path.
				try {
					htmlContent.setPage(localFileEntry.toURI().toURL());
					if (!browsingHistory.contains(urlEntry)) {
						WebDoc wd = new WebDoc(urlEntry);
						browsingHistory.add(urlEntry);
						webIndex_Keyword.add(wd);
						webIndex_ContentWord.add(wd);
						webdocCollection.add(wd);
					}
					addClickableLinksToPanel(historyRecordPanel, webdocCollection);
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(null, "Incorrect form of file path", "Error",
							JOptionPane.WARNING_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "File path not accessible", "Error",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {// if it's a URL entry
				try {
					htmlContent.setPage(new URL(urlEntry));
					if (!browsingHistory.contains(urlEntry)) {
						WebDoc wd = new WebDoc(urlEntry);
						browsingHistory.add(urlEntry);
						webIndex_Keyword.add(wd);
						webIndex_ContentWord.add(wd);
						webdocCollection.add(wd);
					}
					addClickableLinksToPanel(historyRecordPanel, webdocCollection);
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(null, "Incorrect form of URL", "Error", JOptionPane.WARNING_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "URL not accessible, please check internet connect", "Error",
							JOptionPane.WARNING_MESSAGE);
				}
			}
			tabbedPane.setSelectedIndex(index);
		}
	}

	/**
	 * This inner class is used to handle the 'Search Action'. The reason why it's
	 * named the Search Action Listener is that the action listener will initiate
	 * the infix query and prefix query searching.
	 * <p>
	 * The result of query will be added to the relevant panels in forms of buttons,
	 * which will be clickable. When the user clicks on the URL links, this software
	 * navigates to the htmlBrowserCard and shows the new tab created for this link.
	 * 
	 */
	private class SearchActionHandler implements ActionListener {
		JTextField infixQuery;
		JTextField prefixQuery;
		JPanel keywordPanel;
		JPanel contentWordPanel;

		public SearchActionHandler() {
			infixQuery = view.getInfixQuery();
			prefixQuery = view.getPrefixQuery();
			keywordPanel = view.getKeywordResult();
			contentWordPanel = view.getContentWordResult();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String query;
			boolean isValid;
			Set<WebDoc> keywordResult;
			Set<WebDoc> contentWordResult;

			if (e.getActionCommand().equals("infix")) {
				query = infixQuery.getText().trim().toLowerCase();
				isValid = validateInfixQuery(query); // validate the infix query before handling.

				if (isValid) {
					Query infixQuery = QueryBuilder.parseInfixForm(query);
					if (infixQuery != null) {
						keywordResult = infixQuery.matches(webIndex_Keyword);
						contentWordResult = infixQuery.matches(webIndex_ContentWord);
						addClickableLinksToPanel(keywordPanel, keywordResult);
						addClickableLinksToPanel(contentWordPanel, contentWordResult);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Unfortunately" + "infix query" + " you entered is invalid",
							"Content Word Result", JOptionPane.WARNING_MESSAGE);
				}
			} else if (e.getActionCommand().equals("prefix")) {
				query = prefixQuery.getText().trim().toLowerCase();
				isValid = validatePrefixQuery(query); // validate the prefix query before handling.

				if (isValid) {
					Query prefixQuery = QueryBuilder.parse(query);
					if (prefixQuery != null) {
						keywordResult = prefixQuery.matches(webIndex_Keyword);
						contentWordResult = prefixQuery.matches(webIndex_ContentWord);
						addClickableLinksToPanel(keywordPanel, keywordResult);
						addClickableLinksToPanel(contentWordPanel, contentWordResult);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Unfortunately" + "prefix query" + " you entered is invalid",
							"Content Word Result", JOptionPane.WARNING_MESSAGE);
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
					"(.*?and\\s*,*\\s*or.*?)|(.*?and\\s*,*\\s*and.*?)|(.*?or\\s*,*\\s*and.*?)|(.*?\\({1,}\\){1,}.*?)|(\\s*)")) {
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
					"(.*?and\\s*and.*?)|(.*?and\\s*or.*?)|(.*?or\\s*and.*?)|(.*?or\\s*or.*?)|(.*?not\\s*and.*?)|(.*?not\\s*not.*?)|(.*?not\\s*or.*?)|(.*?\\({1,}\\){1,}.*?)|(\\s*)")) {
				return false;
			} else {
				return true;
			}
		}

	}

	/**
	 * This inner class is used to handle the action for menu items in the menu bar.
	 * In order words, when the menu item is pressed by the user, it navigates to
	 * the relevant 'card'/interface.
	 * 
	 */
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

	/**
	 * This inner class is used to handle the window events. When the user closes
	 * the GUI, it saves the browsing history and index in the a local file. When
	 * the software is launched, a dialog is shown asking whether the user wants to
	 * read the previous browsing history and index. If so, the software loads the
	 * previously saved index and history.
	 *
	 */
	private class WindowEventHandler extends WindowAdapter {

		@Override
		public void windowOpened(WindowEvent e) {
			super.windowOpened(e);

			JOptionPane.showMessageDialog(null,
					"Wellcome using this software, the links in the query result (keyword and content word) and the history are clickable.\n"
							+ "When the application closes, the browsing history and the index will be saved."
							+ "\nWhen reading the previous browsing history, it takes time to load the index depending on the length of the history."
							+ "\nPlease be patient when the program is not responding.",
					"Instruction", JOptionPane.INFORMATION_MESSAGE);

			int answer = JOptionPane.showConfirmDialog(null,
					"Do you want to read the previous log and index?\n[Note:If yes, please wait until the program notifies successful reading.]",
					"Reading History", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (answer == 0) {// yes
				try {
					BufferedReader fileReader = new BufferedReader(new FileReader(new File(LOG_FILE_PATH)));
					boolean started = false;
					boolean ended = false;

					// reading the saved index:
					String thisLine = "";
					while ((thisLine = fileReader.readLine()) != null && !ended) {
						if (thisLine.startsWith(INDEX_LOG_START)) {
							started = true;
						} else if (thisLine.startsWith(INDEX_LOG_END)) {
							ended = true;
						} else if (started) {
							browsingHistory.add(thisLine);
						}
					}
					fileReader.close();

					for (String s : browsingHistory) {
						WebDoc thisDoc = new WebDoc(s);
						webIndex_ContentWord.add(thisDoc);
						webIndex_Keyword.add(thisDoc);
						webdocCollection.add(thisDoc);
					}
					// add the clickable links (previous browsing history) to the historyRecordPane.
					addClickableLinksToPanel(view.getHistoryRecordPanel(), webdocCollection);
					JOptionPane.showMessageDialog(null, "Successfully read the previous history and index", "Success",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e2) {
					JOptionPane.showMessageDialog(null, "Failed to read the previous log and index.", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

		@Override
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);

			try {
				BufferedWriter fileWriter = new BufferedWriter(new FileWriter(new File(LOG_FILE_PATH)));

				// saving index:
				fileWriter.write(INDEX_LOG_START);
				fileWriter.newLine();
				for (String s : browsingHistory) {
					fileWriter.write(s);
					fileWriter.newLine();
				}
				fileWriter.write(INDEX_LOG_END);
				fileWriter.close();
			} catch (IOException e1) {
				// failed to save the log.
			}
		}
	}

	/**
	 * This method creates the buttons for the URL links that the user has
	 * previously browsed, it then adds the buttons to the panel that is passed to
	 * this method. It is used for the history and query results. Each button/URL
	 * link is added an action listener. When the user clicks on the link, the
	 * action listener navigates to the htmlBrowserCard and creates a new tab for
	 * this link.
	 * 
	 * @param panel        The panel that will contain the list of clickable URLs
	 *                     links.
	 * @param setOfWebDocs The set of WebDoc objects.
	 */
	private void addClickableLinksToPanel(JPanel panel, Set<WebDoc> setOfWebDocs) {
		CardLayout layoutControl = view.getCardLayoutControl();
		JPanel cards = view.getCards();
		panel.removeAll();

		if (setOfWebDocs != null) {
			for (WebDoc doc : setOfWebDocs) {
				JButton eachResult = new JButton();
				String urlEntry = doc.getEntry();
				eachResult.setText(urlEntry);
				eachResult.setBackground(Color.WHITE);
				eachResult.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
				eachResult.setFont(new Font("Arial", Font.BOLD, 17));
				panel.add(eachResult);

				eachResult.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String title = "Tab " + (view.getWebBrowserTabbedPane().getTabCount() + 1);
						JEditorPane htmlContent = view.addTabToHtmlBrowserCard(title);
						JTabbedPane tabbedPane = view.getWebBrowserTabbedPane();
						tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

						if (urlEntry.matches("([Ff][Ii][Ll][Ee]:)(.++)")) { // it's a local HTML file
							String fileEntry = urlEntry.substring(5, urlEntry.length());// get the correct path
							try {
								htmlContent.setPage(new File(fileEntry).toURI().toURL()); // convert to URL object
								layoutControl.show(cards, view.HTML_BROWSER_TAG); // show the htmlBrowser interface/card
							} catch (MalformedURLException e1) {
								JOptionPane.showMessageDialog(null, "Incorrect form of file path", "Error",
										JOptionPane.WARNING_MESSAGE);
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, "File path not accessible", "Error",
										JOptionPane.WARNING_MESSAGE);
							}
						} else { // it's a web URL link
							try {
								htmlContent.setPage(urlEntry);
								layoutControl.show(cards, view.HTML_BROWSER_TAG); // show the htmlBrowser interface/card
							} catch (MalformedURLException e1) {
								JOptionPane.showMessageDialog(null, "Incorrect form of URL", "Error",
										JOptionPane.WARNING_MESSAGE);
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, "URL not accessible, please check internet connect",
										"Error", JOptionPane.WARNING_MESSAGE);
							}
						}
					}
				});
			}
		}
		// show the new components added to the panel
		panel.revalidate();
		panel.repaint();
	}
}
