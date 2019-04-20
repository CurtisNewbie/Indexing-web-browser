package webBrowserGUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WebBrowserView {

	/** The browser as a whole */
	JFrame browserFrame;

	/** The JMenuBar for navigating the interface */
	JMenuBar menuBar;

	/** The JMenu as a high-level button in the menu bar */
	JMenu menu;

	/**
	 * The navigation buttons in the menu bar, it navigates to the html browser
	 * interface (a card) (cardLayout is used)
	 */
	JMenuItem htmlBrowser;

	/**
	 * The navigation buttons in the menu bar, it navigates to the query browser
	 * interface (a card) (cardLayout is used)
	 */
	JMenuItem queryBrowser;

	/**
	 * The panel with CardLayout for showing different interfaces (htmlBrowser and
	 * queryBrowser)
	 */
	JPanel cards;

	/**
	 * CardLayout Manager of the JPanel (cards) for controlling the cards/interfaces
	 * switching
	 */
	CardLayout cardLayoutControl;

	/** The card (controlled by cardLayout) for the html browser interface */
	JPanel htmlBrowserCard;

	/** The card (controlled by cardLayout) for the query browser interface */
	JPanel queryBrowserCard;

	/** Tags that represents htmlBrowserCard */
	public final String WEB_BROWSER_TAG = "WebBrowserTag";

	/** Tags that represents queryBrowserCard */
	public final String QUERY_BROWSER_TAG = "QueryBrowserTag";

	/** JTabbedPane as a contentPane of the htmlBrowserCard */
	JTabbedPane webBrowserTabbedPane;

	/** TextField for url input */
	JTextField urlTextInput;

	/** Default Menu Font */
	Font menuFont = new Font("Arial", Font.BOLD, 19);

	/** Default content font */
	Font contentFont = new Font("Arial", Font.BOLD, 17);

	/** Confirm button in htmlBrowserCard */
	JButton confirmButton;

	/** Button in htmlBrowserCard for closing the current selected tab */
	JButton closeTab;

	/** TextArea in queryBrowserCard for displaying the html browsing history */
	JTextArea historyTextArea;

	/** Button in queryBrowserCard that initiates the infix query searching */
	JButton infixSearchButton;

	/** Button in queryBrowserCard that initiates the prefix query searching */
	JButton prefixSearchButton;

	/** TextField in queryBrowserCard for entering infix query */
	JTextField infixQuery;

	/** TextField in queryBrowserCard for entering prefix query */
	JTextField prefixQuery;

	JPanel keywordResultPanel;
	JPanel contentWordResultPanel;

	public WebBrowserView() {
		browserFrame = new JFrame("WebBrowser :D");
		browserFrame.setLayout(new BorderLayout());
		browserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Use a panel (cards) with cardLayout as a way to switch different contents
		cardLayoutControl = new CardLayout();
		cards = new JPanel(cardLayoutControl);

		htmlBrowserCard = new JPanel(new BorderLayout());
		queryBrowserCard = new JPanel(new BorderLayout());
		cards.add(htmlBrowserCard, WEB_BROWSER_TAG);
		cards.add(queryBrowserCard, QUERY_BROWSER_TAG);
		browserFrame.getContentPane().add(cards);

		addMenuBar();
		setUpHtmlBrowserCard();
		setUpQueryBrowserCard();

		// By default show the htmlBrowserCard first
		cardLayoutControl.show(cards, WEB_BROWSER_TAG);
		browserFrame.pack();
		browserFrame.setVisible(true);
	}

	private void addMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 0));
		menu = new JMenu("Menu");
		menu.setBorder(BorderFactory.createRaisedBevelBorder());
		menu.setFont(menuFont);

		// Create menu item and set up their font
		htmlBrowser = new JMenuItem("HTML Browser");
		htmlBrowser.setFont(menuFont);
		htmlBrowser.setActionCommand(WEB_BROWSER_TAG);
		queryBrowser = new JMenuItem("Query Browser");
		queryBrowser.setFont(menuFont);
		queryBrowser.setActionCommand(QUERY_BROWSER_TAG);

		menu.add(htmlBrowser);
		menu.add(queryBrowser);
		menuBar.add(menu);
		browserFrame.add(menuBar, BorderLayout.NORTH);
	}

	private void setUpHtmlBrowserCard() {
		webBrowserTabbedPane = new JTabbedPane();
		htmlBrowserCard.add(webBrowserTabbedPane, BorderLayout.CENTER);

		// The box for organising the buttons in htmlBrowserCard (on the top of the
		// screen)
		Box panelOrganiser = Box.createHorizontalBox();
		panelOrganiser.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelOrganiser.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		confirmButton = new JButton("Confirm");
		confirmButton.setFont(menuFont);
		closeTab = new JButton("Close This Tab");
		closeTab.setFont(menuFont);

		urlTextInput = new JTextField("..Type the url here..", 20);
		urlTextInput.setFont(contentFont);
		panelOrganiser.add(Box.createHorizontalStrut(30));
		panelOrganiser.add(urlTextInput);
		panelOrganiser.add(Box.createHorizontalStrut(10));
		panelOrganiser.add(confirmButton);
		panelOrganiser.add(Box.createHorizontalStrut(10));
		panelOrganiser.add(closeTab);
		panelOrganiser.add(Box.createHorizontalStrut(20));
		panelOrganiser.add(Box.createGlue());
		htmlBrowserCard.add(panelOrganiser, BorderLayout.NORTH);

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
		historyTextArea = new JTextArea(10, 30);
		historyTextArea.setFont(contentFont);
		historyTextArea.setEditable(false);

		// Set up the contorlPanel in this queryBrowserCard
		setUpControlPanel();

		// set up the historyTextArea
		JPanel historyPanel = new JPanel();
		historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
		JLabel historyLabel = new JLabel("History:");
		historyLabel.setFont(menuFont);
		historyPanel.add(historyLabel);
		historyPanel.add(new JScrollPane(historyTextArea));
		queryBrowserCard.add(historyPanel, BorderLayout.WEST);

		// set up keywordQueryResultTextArea and contentWordQueryResultTextArea
		JPanel indexTextPanel = new JPanel();
		indexTextPanel.setLayout(new BoxLayout(indexTextPanel, BoxLayout.Y_AXIS));
		JLabel indexLabel = new JLabel("Content Word Result:");
		indexLabel.setFont(menuFont);
		indexTextPanel.add(indexLabel);
		contentWordResultPanel = new JPanel();
		contentWordResultPanel.setLayout(new BoxLayout(contentWordResultPanel, BoxLayout.Y_AXIS));
		indexTextPanel.add(new JScrollPane(contentWordResultPanel));

		JPanel queryResultPanel = new JPanel();
		queryResultPanel.setLayout(new BoxLayout(queryResultPanel, BoxLayout.Y_AXIS));
		JLabel queryResultLabel = new JLabel("Keyword Result:");
		queryResultLabel.setFont(menuFont);
		queryResultPanel.add(queryResultLabel);
		keywordResultPanel = new JPanel();
		keywordResultPanel.setLayout(new BoxLayout(keywordResultPanel, BoxLayout.Y_AXIS));
		queryResultPanel.add(new JScrollPane(keywordResultPanel));

		queryBrowserCard.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, indexTextPanel, queryResultPanel),
				BorderLayout.CENTER);
	}

	// See setUpQueryBrowserCard() method
	private void setUpControlPanel() {
		// The panel in queryBrowserCard that consists of buttons and textfield for
		// handling the query
		JPanel controlPanel = new JPanel();

		// JComponents in the controlPanel (On the right side of the screen)
		JLabel overallQueryControllerTitle = new JLabel("Query Handling:");
		JLabel infixControllerTitle = new JLabel("Infix Query:");
		JLabel prefixControllerTitle = new JLabel("Prefix Query:");
		infixQuery = new JTextField(20);
		prefixQuery = new JTextField(20);
		infixQuery.setActionCommand("infix");
		prefixQuery.setActionCommand("prefix");
		infixSearchButton = new JButton("Search");
		prefixSearchButton = new JButton("Search");
		infixSearchButton.setActionCommand("infix");
		prefixSearchButton.setActionCommand("prefix");

		// set up the control panel on the right side of the screen using group layout
		GroupLayout groupLayout = new GroupLayout(controlPanel);
		controlPanel.setLayout(groupLayout);
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

		// Set up the fonts for all the child components in the controlPanel
		for (Component com : controlPanel.getComponents()) {
			com.setFont(contentFont);
		}
		queryBrowserCard.add(controlPanel, BorderLayout.EAST);
	}

	public void addConfirmActionListener(ActionListener actionListener) {
		confirmButton.addActionListener(actionListener);
		urlTextInput.addActionListener(actionListener);
	}

	public void addSearchActionListener(ActionListener actionListener) {
		infixQuery.addActionListener(actionListener);
		prefixQuery.addActionListener(actionListener);
		infixSearchButton.addActionListener(actionListener);
		prefixSearchButton.addActionListener(actionListener);
	}

	public void addMenuItemActionListener(ActionListener actionListener) {
		htmlBrowser.addActionListener(actionListener);
		queryBrowser.addActionListener(actionListener);
	}

	public JPanel getKeywordResultPanel() {
		return keywordResultPanel;
	}

	public JPanel getContentWordResultPanel() {
		return contentWordResultPanel;
	}

	public JTabbedPane getWebBrowserTabbedPane() {
		return webBrowserTabbedPane;
	}

	public JTextField getUrlTextField() {
		return urlTextInput;
	}

	public JTextField getInfixQuery() {
		return infixQuery;
	}

	public JTextField getPrefixQuery() {
		return prefixQuery;
	}

	public JTextArea getHistoryTextArea() {
		return historyTextArea;
	}

	public CardLayout getCardLayoutControl() {
		return cardLayoutControl;
	}

	public JPanel getCards() {
		return cards;
	}

	public static void main(String[] args) {
		WebBrowserView view = new WebBrowserView();
		WebBrowserController controller = new WebBrowserController(view);
	}
}
