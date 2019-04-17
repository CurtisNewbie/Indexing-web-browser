package webBrowserGUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sun.security.pkcs.ContentInfo;

// This is the top level frame of the GUI
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

	// The card for the html browser and query browser
	JPanel htmlBrowserCard;
	JPanel queryBrowserCard;

	// Tags for the two cards
	public final String WEB_BROWSER_TAG = "WebBrowserTag";
	public final String QUERY_BROWSER_TAG = "QueryBrowserTag";

	// JTabbedPane as a contentPane of the htmlBrowserCard
	JTabbedPane webBrowserTabbedPane;

	// The box for organising the buttons in htmlBrowserCard (on the top of the
	// screen)
	Box webBrowserInputOrganiser;

	// TextField for url input
	JTextField urlTextInput;

	// Default Menu Font and default content font
	Font menuFont = new Font("Arial", Font.BOLD, 19);
	Font contentFont = new Font("Arial", Font.BOLD, 17);

	// Confirm Button and Close tab Button in htmlBrowserCard
	JButton confirmButton;

	//
	JButton closeTab;

	JTextArea historyTextArea;
	JTextArea queryResultTextArea;
	JTextArea indexTextArea;
	JPanel controlPanel;
	JButton infixSearchButton;
	JButton prefixSearchButton;
	JTextField infixQuery;
	JTextField prefixQuery;
	JRadioButton keywordIndexButton;
	JRadioButton contentWordIndexButton;

	enum IndexTypeSelected {
		KEYWORD_SELECTED, CONTENTWORD_SELECTED, BOTH_SELECTED, NONE_SELECTED
	}

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
		
		// add the menu bar to this frame for navigating between cards
		addMenuBar();

		// set up the 'card' for HTML browser
		setUpHtmlBrowserCard();

		// set up the 'card' for query browser
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
		htmlBrowserCard.add(webBrowserInputOrganiser, BorderLayout.NORTH);

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
		queryResultTextArea = new JTextArea();
		queryResultTextArea.setFont(contentFont);
		queryResultTextArea.setEditable(false);
		indexTextArea = new JTextArea();
		indexTextArea.setFont(contentFont);
		indexTextArea.setEditable(false);
		controlPanel = new JPanel();

		// Set up the contorlPanel in this queryBrowserCard
		setUpControlPanel();
		queryBrowserCard.add(controlPanel, BorderLayout.EAST);

		// set up the historyTextArea
		JPanel historyPanel = new JPanel();
		historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
		JLabel historyLabel = new JLabel("History:");
		historyLabel.setFont(menuFont);
		historyPanel.add(historyLabel);
		historyPanel.add(new JScrollPane(historyTextArea));
		queryBrowserCard.add(historyPanel, BorderLayout.WEST);

		// set up queryResultTextArea and indexTextArea
		JPanel indexTextPanel = new JPanel();
		indexTextPanel.setLayout(new BoxLayout(indexTextPanel, BoxLayout.Y_AXIS));
		JLabel indexLabel = new JLabel("Index:	");
		indexLabel.setFont(menuFont);
		indexTextPanel.add(indexLabel);
		indexTextPanel.add(new JScrollPane(indexTextArea));

		JPanel queryResultPanel = new JPanel();
		queryResultPanel.setLayout(new BoxLayout(queryResultPanel, BoxLayout.Y_AXIS));
		JLabel queryResultLabel = new JLabel("Query Result:");
		queryResultLabel.setFont(menuFont);
		queryResultPanel.add(queryResultLabel);
		queryResultPanel.add(new JScrollPane(queryResultTextArea));

		queryBrowserCard.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, indexTextPanel, queryResultPanel),
				BorderLayout.CENTER);
	}

	// See setUpQueryBrowserCard() method
	private void setUpControlPanel() {
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
		keywordIndexButton = new JRadioButton("Searching through keywords");
		contentWordIndexButton = new JRadioButton("Searching through content words");

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
				.addGroup(groupLayout.createParallelGroup().addComponent(infixQuery).addComponent(prefixQuery)
						.addComponent(keywordIndexButton).addComponent(contentWordIndexButton))
				.addGroup(groupLayout.createParallelGroup().addComponent(infixSearchButton)
						.addComponent(prefixSearchButton));
		groupLayout.setHorizontalGroup(horizontalGroups);

		GroupLayout.SequentialGroup verticalGroups = groupLayout.createSequentialGroup();
		verticalGroups
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(overallQueryControllerTitle))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(infixControllerTitle)
						.addComponent(infixQuery).addComponent(infixSearchButton))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(prefixControllerTitle)
						.addComponent(prefixQuery).addComponent(prefixSearchButton))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(keywordIndexButton))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(contentWordIndexButton));
		groupLayout.setVerticalGroup(verticalGroups);

		// Set up the fonts for all the child components in the controlPanel
		for (Component com : controlPanel.getComponents()) {
			com.setFont(contentFont);
		}
	}

	public IndexTypeSelected checkIndexRadioButtonSelected() {
		if (keywordIndexButton.isSelected() && contentWordIndexButton.isSelected()) {
			return IndexTypeSelected.BOTH_SELECTED;
		} else if (keywordIndexButton.isSelected() && !contentWordIndexButton.isSelected()) {
			return IndexTypeSelected.KEYWORD_SELECTED;
		} else if (!keywordIndexButton.isSelected() && contentWordIndexButton.isSelected()) {
			return IndexTypeSelected.CONTENTWORD_SELECTED;
		} else {
			return IndexTypeSelected.NONE_SELECTED;
		}
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

	public JTabbedPane getWebBrowserTabbedPane() {
		return webBrowserTabbedPane;
	}

	public JTextField getUrlTextField() {
		return urlTextInput;
	}

	public JTextArea getQueryResultTextArea() {
		return queryResultTextArea;
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

	public JTextArea getIndexTextArea() {
		return indexTextArea;
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
