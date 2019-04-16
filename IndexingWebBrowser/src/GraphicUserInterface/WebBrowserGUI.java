package GraphicUserInterface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
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
public class WebBrowserGUI {

	// the JMenuBar for navigating the interface
	JMenuBar menuBar;
	JMenu menu;

	// The browser as a whole
	JFrame browserFrame;

	// The panel with CardLayout for showing different content
	JPanel cards;

	// CardLayout Manager for controlling the cards switching
	CardLayout cardLayoutControl;

	// The card for the web browser
	JPanel webBrowserCard;

	// The card for the query browser
	JPanel queryBrowserCard;

	// Tags for the two cards
	final String WEB_BROWSER_TAG = "WebBrowserTag";
	final String QUERY_BROWSER_TAG = "QueryBrowserTag";

	// JTabbedPane as a contentPane of the webBrowserCard
	JTabbedPane webBrowserContentPane;

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

	public WebBrowserGUI() {
		browserFrame = new JFrame("WebBrowser :D");
		browserFrame.setLayout(new BorderLayout());
		browserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set properties
//		Dimension pcScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		browserFrame.setSize(pcScreenSize.width / 2, pcScreenSize.height / 2);

		// Use a panel (cards) with cardLayout as a way to switch different contents
		cardLayoutControl = new CardLayout();
		cards = new JPanel(cardLayoutControl);

		// set up each card
		webBrowserCard = new JPanel();
		queryBrowserCard = new JPanel();
		cards.add(webBrowserCard, WEB_BROWSER_TAG);
		cards.add(queryBrowserCard, QUERY_BROWSER_TAG);

		// add the panel (with card layout) to the contentPane of the frame.
		browserFrame.getContentPane().add(cards);

		// set up the tabbed pane for the webBrowserCard
		webBrowserContentPane = new JTabbedPane();
		webBrowserCard.add(webBrowserContentPane);
		
		// add the menu bar to this frame for navigating between cards
		addMenuBar();

		browserFrame.pack();
		browserFrame.setVisible(true);
	}

	private void addMenuBar() {
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		menu.setBorder(new LineBorder(Color.BLACK));
		menu.setFont(menuFont);

		// Create menu itme and set up their font
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
				cardLayoutControl.show(cards, WEB_BROWSER_TAG);
			}
		});

		queryBrowser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayoutControl.show(cards, QUERY_BROWSER_TAG);

			}
		});
	}

	private void addTabToWebBrowserCard() {
		JPanel panel = new JPanel();
		webBrowserContentPane.addTab("new tab", panel);
	}

	public static void main(String[] args) {
		WebBrowserGUI brow = new WebBrowserGUI();

	}
}
