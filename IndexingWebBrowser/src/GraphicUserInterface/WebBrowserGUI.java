package GraphicUserInterface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import javafx.scene.layout.Border;

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
	JTabbedPane webBrowserTabbedPane;

	// The navigation buttons in the menu bar
	JMenuItem htmlBrowser;
	JMenuItem queryBrowser;

	// The box for organising the buttons in htmlBrowserCard (on the top of the
	// screen)
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

	JPanel historyPanel;
	JPanel queryResultPanel;
	JPanel queryBrowserControlPanel;

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
		webBrowserTabbedPane = new JTabbedPane();
		webBrowserCard.add(webBrowserTabbedPane, BorderLayout.CENTER);

		// add the menu bar to this frame for navigating between cards
		addMenuBar();

		// set up the 'card' for HTML browser
		setUpHtmlBrowserCard();

		// set up the 'card' for query browser
		setUpQueryBrowserCard();

		// By default show the webBrowserCard first
		cardLayoutControl.show(cards, WEB_BROWSER_TAG);
		browserFrame.setVisible(true);
	}

	private void addMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 0));
		menu = new JMenu("Menu");
		menu.setBorder(BorderFactory.createRaisedBevelBorder());
		menu.setFont(menuFont);

		// Create menu itme and set up their font
		htmlBrowser = new JMenuItem("HTML Browser");
		htmlBrowser.setFont(menuFont);
		queryBrowser = new JMenuItem("Query Browser");
		queryBrowser.setFont(menuFont);

		menu.add(htmlBrowser);
		menu.add(queryBrowser);
		menuBar.add(menu);
		browserFrame.add(menuBar, BorderLayout.NORTH);

		htmlBrowser.addActionListener(new ActionListener() {
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

	private void setUpHtmlBrowserCard() {
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

		confirmButton.addActionListener(new ConfirmActionHandler(webBrowserTabbedPane, urlTextInput, this));
		urlTextInput.addActionListener(new ConfirmActionHandler(webBrowserTabbedPane, urlTextInput, this));
		closeTab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// remove the selected one.
				int selectedIndex = webBrowserTabbedPane.getSelectedIndex();
				if (selectedIndex != -1) {
					webBrowserTabbedPane.remove(selectedIndex);
				}
			}
		});
	}

	public JEditorPane addTabToHtmlBrowserCard() {
		JEditorPane jp = new JEditorPane();
		JScrollPane scrollPane = new JScrollPane(jp);
		webBrowserTabbedPane.addTab("New tab", scrollPane);
		return jp;
	}

	private void setUpQueryBrowserCard() {
		historyPanel = new JPanel(new BorderLayout());
		queryResultPanel = new JPanel(new BorderLayout());
		queryBrowserControlPanel = new JPanel();

		// JComponents in the queryBrowserControlPanel (On the right side of the screen)
		JTextField overallQueryControllerTitle = new JTextField("Query Handling:");
		JTextField infixControllerTitle = new JTextField("Infix Query:");
		JTextField infixQuery = new JTextField(10);
		JTextField prefixControllerTitle = new JTextField("Prefix Query:");
		JTextField prefixQuery = new JTextField(10);
		JButton infixSearchButton = new JButton("Search");
		JButton prefixSearchButton = new JButton("Search");

		// setup the control panel on the right side of the screen using group layout
		GroupLayout groupLayout = new GroupLayout(queryBrowserControlPanel);
		queryBrowserControlPanel.setLayout(groupLayout);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		// create horizontal and vertical groups
		GroupLayout.SequentialGroup horizontalGroups = groupLayout.createSequentialGroup();
		horizontalGroups
				.addGroup(groupLayout.createParallelGroup().addComponent(overallQueryControllerTitle)
						.addComponent(infixControllerTitle).addComponent(prefixControllerTitle))
				.addGroup(groupLayout.createParallelGroup().addComponent(infixQuery).addComponent(prefixQuery))
				.addGroup(groupLayout.createParallelGroup().addComponent(infixSearchButton)
						.addComponent(prefixSearchButton));
		groupLayout.setHorizontalGroup(horizontalGroups);

		GroupLayout.SequentialGroup verticalGroups = groupLayout.createSequentialGroup();
		verticalGroups
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(overallQueryControllerTitle))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(infixControllerTitle)
						.addComponent(infixQuery).addComponent(infixSearchButton))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(prefixControllerTitle)
						.addComponent(prefixQuery).addComponent(prefixSearchButton));
		groupLayout.setVerticalGroup(verticalGroups);

		queryBrowserCard.add(queryBrowserControlPanel, BorderLayout.EAST);
		queryBrowserCard.add(historyPanel, BorderLayout.SOUTH);
		queryBrowserCard.add(queryResultPanel, BorderLayout.CENTER);

	}

	public static void main(String[] args) {
		WebBrowserGUI brow = new WebBrowserGUI();
	}
}
