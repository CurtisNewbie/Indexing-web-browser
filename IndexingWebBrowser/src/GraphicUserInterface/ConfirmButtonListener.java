package GraphicUserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

// The listener for confirm button in the webBrowserCard
public class ConfirmButtonListener implements ActionListener {

	JComponent urlInput;
	JComponent htmlContent;

	public ConfirmButtonListener(JComponent urlInput, JComponent htmlContent) {
		this.urlInput = urlInput;
		this.htmlContent = htmlContent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String url = ((JTextField) urlInput).getText();
		try {
			((JEditorPane) htmlContent).setPage(new URL(url));
		} catch (MalformedURLException e1) {
			JOptionPane.showMessageDialog(null, "Incorrect Form of URL", "Error", JOptionPane.WARNING_MESSAGE);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
