package GraphicUserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WebBrowserController {

	private WebBrowserView view;

	public WebBrowserController(WebBrowserView view) {
		this.view = view;
		view.addConfirmActionListener(new ConfirmActionHandler());
		view.addSearchAction(new SearchActionHandler());
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
			if(e.getActionCommand().equals("infix")) {
				resultTextArea.setText(infixQuery.getText());
			} else if(e.getActionCommand().equals("prefix")) {
				resultTextArea.setText(prefixQuery.getText());
			}
		}
		
		
	}

}
