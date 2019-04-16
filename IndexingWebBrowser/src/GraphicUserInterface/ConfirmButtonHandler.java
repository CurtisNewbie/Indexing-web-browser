package GraphicUserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

// The listener for confirm button in the webBrowserCard
public class ConfirmButtonHandler implements ActionListener {

	JTabbedPane tabbedPane;
	JTextField urlInput;
	JEditorPane htmlContent;

	public ConfirmButtonHandler(JTabbedPane tabbedPane, JTextField urlInput, WebBrowserGUI webBrowserObj) {
		this.tabbedPane = tabbedPane;
		this.urlInput = urlInput;
		this.htmlContent = webBrowserObj.addTabToWebBrowserCard();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String url = urlInput.getText();
		try {
			htmlContent.setPage(new URL(url));
		} catch (MalformedURLException e1) {
			JOptionPane.showMessageDialog(null, "Incorrect Form of URL", "Error", JOptionPane.WARNING_MESSAGE);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
