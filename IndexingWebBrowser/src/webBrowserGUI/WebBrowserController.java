package webBrowserGUI;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import webBrowserGUI.WebBrowserView.IndexTypeSelected;
import webBrowserModel.*;
import webBrowserModel.WebIndex.TypeOfWords;

public class WebBrowserController {

	private WebBrowserView view;
	private WebIndex webIndex_Keyword;
	private WebIndex webIndex_ContentWord;
	private ArrayList<WebDoc> webDocCollection;

	public WebBrowserController(WebBrowserView view) {
		this.view = view;
		webIndex_Keyword = new WebIndex(TypeOfWords.KEYWORD);
		webIndex_ContentWord = new WebIndex(TypeOfWords.CONTENT_WORD);

		view.addConfirmActionListener(new ConfirmActionHandler());
		view.addSearchActionListener(new SearchActionHandler());
		view.addMenuItemActionListener(new MenuItemActionHandler());
	}

	private class ConfirmActionHandler implements ActionListener {
		JTextField urlInput;
		JEditorPane htmlContent;
		JTextArea historyTextArea;
		JTextArea indexTextArea;

		public ConfirmActionHandler() {
			this.urlInput = view.getUrlTextField();
			this.historyTextArea = view.getHistoryTextArea();
			this.indexTextArea = view.getIndexTextArea();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.htmlContent = view.addTabToHtmlBrowserCard();
			String url = urlInput.getText();
			try {
				htmlContent.setPage(new URL(url));
				WebDoc wd = new WebDoc(url);
				historyTextArea.append(wd.getEntry());

				webDocCollection.add(wd);
				webIndex_Keyword.add(wd);
				webIndex_ContentWord.add(wd);
				indexTextArea.append(wd.toString());

			} catch (MalformedURLException e1) {
				JOptionPane.showMessageDialog(null, "Incorrect form of URL", "Error", JOptionPane.WARNING_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "URL not accessible, please check internet connect", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private class SearchActionHandler implements ActionListener {
		JTextArea resultTextArea;
		JTextField infixQuery;
		JTextField prefixQuery;

		public SearchActionHandler() {
			resultTextArea = view.getQueryResultTextArea();
			infixQuery = view.getInfixQuery();
			prefixQuery = view.getPrefixQuery();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			WebBrowserView.IndexTypeSelected indexType = view.checkIndexRadioButtonSelected();

			if (e.getActionCommand().equals("infix")) {
				if (indexType.equals(IndexTypeSelected.CONTENTWORD_SELECTED)) {
					System.out.println("Content only");
				} else if (indexType.equals(IndexTypeSelected.KEYWORD_SELECTED)) {
					System.out.println("Keywords");
				} else if (indexType.equals(IndexTypeSelected.BOTH_SELECTED)) {
					System.out.println("Both");
				} else {
					resultTextArea.setText("Please select which type of words you want to search through...");
				}
//				resultTextArea.setText(infixQuery.getText());
			} else if (e.getActionCommand().equals("prefix")) {
				if (e.getActionCommand().equals("infix")) {
					if (indexType.equals(IndexTypeSelected.CONTENTWORD_SELECTED)) {
						System.out.println("Content only");
					} else if (indexType.equals(IndexTypeSelected.KEYWORD_SELECTED)) {
						System.out.println("Keywords");
					} else if (indexType.equals(IndexTypeSelected.BOTH_SELECTED)) {
						System.out.println("Both");
					} else {
						resultTextArea.setText("Please select which type of words you want to search through...");
					}
				}
			}
		}
	}

	private class MenuItemActionHandler implements ActionListener {

		public MenuItemActionHandler() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			CardLayout layoutControl = view.getCardLayoutControl();
			JPanel cards = view.getCards();
			if (command.equals(view.WEB_BROWSER_TAG)) {
				layoutControl.show(cards, view.WEB_BROWSER_TAG);
			} else if (command.equals(view.QUERY_BROWSER_TAG))
				layoutControl.show(cards, view.QUERY_BROWSER_TAG);
		}
	}
}
