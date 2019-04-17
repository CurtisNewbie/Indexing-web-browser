package webBrowserGUI;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WebBrowserController {

	private WebBrowserView view;

	public WebBrowserController(WebBrowserView view) {
		this.view = view;
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
				historyTextArea.append(url);
				indexTextArea.append(url);
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
			if (e.getActionCommand().equals("infix")) {
				resultTextArea.setText(infixQuery.getText());
			} else if (e.getActionCommand().equals("prefix")) {
				resultTextArea.setText(prefixQuery.getText());
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
