package GraphicUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

// This is the top level frame of the GUI
public class WebBrowserFrame extends JFrame {

	// the JMenuBar for navigating the interface
	JMenuBar menuBar;
	JMenu menu;

	// The browser as a whole
	JFrame browserFrame;

	// JTabbedPane as a contentPane of the browser
	JTabbedPane browserContentPane;
	
	// The navigation buttons in the menu bar
	JMenuItem webBrowser;
	JMenuItem queryBrowser;
	
	// Insets between navigation buttons in the menu bar
	final int insetsTop = 5;
	final int insetsBottom = 5;
	final int insetsLeft = 5;
	final int insetsRight = 5;
	
	// Default Menu Font
	Font menuFont = new Font("Arial", Font.BOLD, 19);
	
	// Default Content Font
	Font contentFont = new Font("Arial", Font.BOLD, 17);

	public WebBrowserFrame() {
		browserFrame = new JFrame("WebBrowser :D");
		browserFrame.setLayout(new BorderLayout());

		// Set properties
		browserFrame.setVisible(true);
		Dimension pcScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		browserFrame.setSize(pcScreenSize.width, pcScreenSize.height);

		// add the menu bar to this frame 
		this.addMenuBar();
		
		// set up the contentPane
		browserContentPane = new JTabbedPane();
		browserContentPane.setLayout(new BorderLayout());
		this.setContentPane(browserContentPane);	
	}

	private void addMenuBar() {
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		menu.setBorder(new LineBorder(Color.BLACK));
		menu.setFont(menuFont);
		
		webBrowser = new JMenuItem("Web Browser");
		webBrowser.setFont(menuFont);
		queryBrowser = new JMenuItem("Query Browser");
		queryBrowser.setFont(menuFont);
		
		menu.add(webBrowser);
		menu.add(queryBrowser);
		menuBar.add(menu);
		browserFrame.add(menuBar, BorderLayout.NORTH);
		
		webBrowser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addTabToBrowser();
			}
		});
	}
	
	private void addTabToBrowser() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.red);
		browserContentPane.addTab("Web1", panel);
		panel.add(new JButton("botton"));
		
	}

	public static void main(String[] args) {
		JFrame brow = new WebBrowserFrame();
		brow.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
