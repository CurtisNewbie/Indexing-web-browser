package GraphicUserInterface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Component;

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

	// The box for organising the buttons in webBrowser (on the top of the screen)
	Box webBrowserInputOrganiser;

	// TextField for url input
	JTextField urlTextInput;

	// Default Menu Font
	Font menuFont = new Font("Arial", Font.BOLD, 19);

	// Default Content Font
	Font contentFont = new Font("Arial", Font.BOLD, 17);

	// Confirm Button
	JButton confirmButton;

	// Close tab Button
	JButton closeTab;

	public WebBrowserGUI() {
		browserFrame = new JFrame("WebBrowser :D");
		browserFrame.setLayout(new BorderLayout());
		browserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set properties
		Dimension pcScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		browserFrame.setSize(pcScreenSize.width / 2, pcScreenSize.height / 2);

		// Use a panel (cards) with cardLayout as a way to switch different contents
		cardLayoutControl = new CardLayout();
		cards = new JPanel(cardLayoutControl);

		// set up each card
		webBrowserCard = new JPanel(new BorderLayout());
		queryBrowserCard = new JPanel(new BorderLayout());
		cards.add(webBrowserCard, WEB_BROWSER_TAG);
		cards.add(queryBrowserCard, QUERY_BROWSER_TAG);

		// add the panel (with card layout) to the contentPane of the frame.
		browserFrame.getContentPane().add(cards);

		// set up the tabbed pane for the webBrowserCard
		webBrowserContentPane = new JTabbedPane();
		webBrowserCard.add(webBrowserContentPane, BorderLayout.CENTER);

		// add the menu bar to this frame for navigating between cards
		addMenuBar();

		// add the box to the webBrowserCard for url inputs and buttons
		addWebBrowserCardInputBox();

		// By default show the webBrowserCard first
		cardLayoutControl.show(cards, WEB_BROWSER_TAG);
		browserFrame.setVisible(true);
	}

	private void addWebBrowserCardInputBox() {
		webBrowserInputOrganiser = Box.createHorizontalBox();
		webBrowserInputOrganiser.setAlignmentX(Component.LEFT_ALIGNMENT);
		webBrowserInputOrganiser.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		confirmButton = new JButton("Confirm");
		closeTab = new JButton("Close This Tab");

		urlTextInput = new JTextField("..Type the url here..", 20);
		webBrowserInputOrganiser.add(Box.createHorizontalStrut(30));
		webBrowserInputOrganiser.add(urlTextInput);
		webBrowserInputOrganiser.add(Box.createHorizontalStrut(10));
		webBrowserInputOrganiser.add(confirmButton);
		webBrowserInputOrganiser.add(Box.createHorizontalStrut(10));
		webBrowserInputOrganiser.add(closeTab);
		webBrowserInputOrganiser.add(Box.createHorizontalStrut(20));
		webBrowserInputOrganiser.add(Box.createGlue());
		webBrowserCard.add(webBrowserInputOrganiser, BorderLayout.NORTH);

		
		confirmButton.addActionListener(new ConfirmButtonHandler(urlTextInput, addTabToWebBrowserCard()));

		closeTab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addTabToWebBrowserCard();
			}
		});
	}

	private void addMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 0));
		menu = new JMenu("Menu");
		menu.setBorder(BorderFactory.createRaisedBevelBorder());
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

	public JEditorPane addTabToWebBrowserCard() {
		JEditorPane jp = new JEditorPane();
		webBrowserContentPane.addTab("New tab", jp);
		return jp;
	}

	public static void main(String[] args) {
		WebBrowserGUI brow = new WebBrowserGUI();

	}
}
