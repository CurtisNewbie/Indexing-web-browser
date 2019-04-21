

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

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
import javax.swing.JTextField;

/**
 * This class is used for creating the graphical user interface. It also
 * represents the view in the MVC architecture. 
 * 
 * @author 180139796
 *
 */
public class WebBrowserView {

	/** The browser (top level container) as a whole */
	JFrame browserFrame;

	/** The JMenuBar for navigating between the interfaces/cards */
	JMenuBar menuBar;

	/** The JMenu as a high-level button in the menu bar */
	JMenu menu;

	/**
	 * The navigation buttons in the menu bar, it switches to the HTML browser
	 * interface/card (cardLayout is used)
	 */
	JMenuItem htmlBrowser;

	/**
	 * The navigation buttons in the menu bar, it switches to the query browser
	 * interface/card (cardLayout is used)
	 */
	JMenuItem queryBrowser;

	/**
	 * The JPanel with CardLayout for showing different interfaces (htmlBrowserCard
	 * and queryBrowserCard)
	 */
	JPanel cards;

	/**
	 * CardLayout Manager of the cards (JPanel with cardLayout) for controlling the
	 * cards/interfaces switching
	 */
	CardLayout cardLayoutControl;

	/** The card (controlled by cardLayoutControl) for the HTML browser interface */
	JPanel htmlBrowserCard;

	/**
	 * The card (controlled by cardLayoutControl) for the query browser interface
	 */
	JPanel queryBrowserCard;

	/** Tags that represents htmlBrowserCard */
	public final String HTML_BROWSER_TAG = "htmlBrowserTag";

	/** Tags that represents queryBrowserCard */
	public final String QUERY_BROWSER_TAG = "QueryBrowserTag";

	/** TextField for URL input entry */
	JTextField urlTextInput;

	/** Default menu font */
	Font menuFont = new Font("Arial", Font.BOLD, 19);

	/** Default content font */
	Font contentFont = new Font("Arial", Font.BOLD, 17);

	/** JTabbedPane as a contentPane of the htmlBrowserCard */
	JTabbedPane webBrowserTabbedPane;

	/** Confirm button in htmlBrowserCard */
	JButton confirmButton;

	/** Button in htmlBrowserCard for closing the current selected tab */
	JButton closeTabButton;

	/**
	 * JPanel in queryBrowserCard, it's the container that comprises the following
	 * components: historyRecordPanel and the historyTitleLabel in setUpHistoryPane
	 * method.
	 */
	JPanel historyPane;

	/**
	 * JPanel in queryBrowserCard for displaying the HTML browsing history, each
	 * record/link is the button that is clickable.
	 */
	JPanel historyRecordPanel;

	/**
	 * JPanel in queryBrowserCard, it's the container that comprises the following
	 * components: contentWordResult and the contentResultlabel in
	 * setUpQueryResultPane method.
	 */
	JPanel contentWordPane;

	/**
	 * JPanel in queryBrowserCard, it's the container that comprises the following
	 * components: keywordResult and the contentResultlabel in setUpQueryResultPane
	 * method.
	 */
	JPanel keywordPane;

	/**
	 * The JPanel in queryBrowserCard for displaying the keyword query result, each
	 * result/link is the button that is clickable.
	 */
	JPanel keywordResult;

	/**
	 * The JPanel in queryBrowserCard for displaying the content word query result,
	 * each result/link is the button that is clickable.
	 */
	JPanel contentWordResult;

	/**
	 * The JPanel in queryBrowserCard, it's the container that comprises a number of
	 * components such as infixSearchButton, prefixSearchButton, infixQuery and
	 * prefixQuery. (See setUpControlPane method)
	 */
	JPanel controlPane;

	/** Button in queryBrowserCard that initiates the infix query searching */
	JButton infixSearchButton;

	/** Button in queryBrowserCard that initiates the prefix query searching */
	JButton prefixSearchButton;

	/** TextField in queryBrowserCard for entering infix query */
	JTextField infixQuery;

	/** TextField in queryBrowserCard for entering prefix query */
	JTextField prefixQuery;

	/**
	 * Initiates the GUI of the browser, the major tasks include: setting up the
	 * 'cards/interfaces' using CardLayout, adding the menu bar, and setting up each
	 * card/interface.
	 */
	public WebBrowserView() {
		browserFrame = new JFrame("WebBrowser :D");
		browserFrame.setLayout(new BorderLayout());
		browserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Use a panel with cardLayout as a way to switch different interfaces/cards
		cardLayoutControl = new CardLayout();
		cards = new JPanel(cardLayoutControl);

		htmlBrowserCard = new JPanel(new BorderLayout());
		queryBrowserCard = new JPanel();
		cards.add(htmlBrowserCard, HTML_BROWSER_TAG);
		cards.add(queryBrowserCard, QUERY_BROWSER_TAG);
		browserFrame.getContentPane().add(cards);

		addMenuBar();
		setUpHtmlBrowserCard();
		setUpQueryBrowserCard();

		// By default show the htmlBrowserCard first
		cardLayoutControl.show(cards, HTML_BROWSER_TAG);
		browserFrame.pack();
		browserFrame.setVisible(true);
	}

	/**
	 * This method sets up the menu bar to the browser. The menu bar provides the
	 * way to navigate between the interfaces/cards (htmlBrowserCard and
	 * queryBrowserCard). For this purpose, the menu only consists of two
	 * buttons(menu items).
	 */
	private void addMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 0));
		menu = new JMenu("Menu");
		menu.setBorder(BorderFactory.createRaisedBevelBorder());
		menu.setFont(menuFont);

		// Create menu item and set up their font
		htmlBrowser = new JMenuItem("HTML Browser");
		htmlBrowser.setFont(menuFont);
		htmlBrowser.setActionCommand(HTML_BROWSER_TAG);
		queryBrowser = new JMenuItem("Query Browser");
		queryBrowser.setFont(menuFont);
		queryBrowser.setActionCommand(QUERY_BROWSER_TAG);

		menu.add(htmlBrowser);
		menu.add(queryBrowser);
		menuBar.add(menu);
		browserFrame.add(menuBar, BorderLayout.NORTH);
	}

	/**
	 * This method sets up the HTML browser graphical interface (htmlBrowserCard).
	 * CardLayout is used in this application for switching between two interfaces,
	 * thus it is referred to as a card.
	 * <p>
	 * The HTML browser interface uses a tabbedPane for showing different HTML
	 * pages, which is similar to the regular web browsers. The urlTextInput is used
	 * to gain the URL entry. The confirmButton initiates the process of showing the
	 * HTML, where the new unique HTML link is "stored" in the history. The
	 * closeTabButton closes the current selected tab.
	 */
	private void setUpHtmlBrowserCard() {
		webBrowserTabbedPane = new JTabbedPane();
		webBrowserTabbedPane.setFont(menuFont);
		htmlBrowserCard.add(webBrowserTabbedPane, BorderLayout.CENTER);

		// The box for organising the buttons in htmlBrowserCard (on the top of the
		// screen)
		Box panelOrganiser = Box.createHorizontalBox();
		panelOrganiser.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelOrganiser.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		confirmButton = new JButton("Confirm");
		confirmButton.setFont(menuFont);
		closeTabButton = new JButton("Close This Tab");
		closeTabButton.setFont(menuFont);

		urlTextInput = new JTextField("..Type the url here..", 20);
		urlTextInput.setFont(contentFont);
		panelOrganiser.add(Box.createHorizontalStrut(30));
		panelOrganiser.add(urlTextInput);
		panelOrganiser.add(Box.createHorizontalStrut(10));
		panelOrganiser.add(confirmButton);
		panelOrganiser.add(Box.createHorizontalStrut(10));
		panelOrganiser.add(closeTabButton);
		panelOrganiser.add(Box.createHorizontalStrut(20));
		panelOrganiser.add(Box.createGlue());
		htmlBrowserCard.add(panelOrganiser, BorderLayout.NORTH);

		closeTabButton.addActionListener(new ActionListener() {

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

	/**
	 * This method creates and adds a new tab to the htmlBrowserCard, which is the
	 * HTML browser interface. In this application, two graphical interfaces exists
	 * (HTML browser interface and the query browser interface); they are switched
	 * using the JPanel with CardLayout.
	 * <p>
	 * To understand what it means by 'card', see the constructor,
	 * setUpHtmlBrowserCard method and the setUpQueryBrowserCard method.
	 * 
	 * @param title title of the new tab
	 * @return a JEditorPane object. It is used as a object reference that allows
	 *         setting the content of the tab.
	 * 
	 *         For instance:
	 *         <p>
	 *         JEditorPane newTab = this.addTabToHtmlBrowserCard("NewTab");
	 *         <p>
	 *         newTab.setPage(URL);
	 */
	public JEditorPane addTabToHtmlBrowserCard(String title) {
		JEditorPane jp = new JEditorPane();
		webBrowserTabbedPane.addTab(title, new JScrollPane(jp));
		return jp;
	}

	/**
	 * This method sets up the query browser graphical interface (queryBrowserCard).
	 * CardLayout is used in this application for switching between two interfaces,
	 * thus it is referred to as a card.
	 * <p>
	 * To understand what it means by 'card', see also the constructor and
	 * setUpHtmlBrowserCard method.
	 * <p>
	 * The queryBrowserCard has three major areas/parts: the control area
	 * (controlPane), history area (historyPane), and the query result areas
	 * (contentWordResult and keywordResult). These three areas are set up using
	 * three methods: setUpControlPane(), setUpHistoryPane(), and
	 * setUpQueryResultPane(). Once they are set up, they are added to the
	 * queryBrowserCard.
	 */
	private void setUpQueryBrowserCard() {
		queryBrowserCard.setLayout(new GridLayout(0, 3));
		// Set up the contorlPane (on the right side of the screen)
		setUpControlPane();

		// Set up the historyPane (on the left side of the screen)
		setUpHistoryPane();

		/*
		 * Set up the areas (contentWordPane and keywordPane) for displaying query
		 * result (on the centre of the screen)
		 */
		setUpQueryResultPane();

		// add the panes that have been set up to the queryBrowserCard.
		// On left side of the screen
		queryBrowserCard.add(contentWordPane);
		queryBrowserCard.add(keywordPane);

		// On right side of the screen
		JSplitPane rightSide = new JSplitPane(JSplitPane.VERTICAL_SPLIT, controlPane, historyPane);
		queryBrowserCard.add(rightSide);
	}

	/**
	 * This method sets up the area for displaying the query result in the
	 * queryBrowserCard. This area consists of two JPanel, the contentWordPane and
	 * the keywordPane. They are the major containers used for this area. This
	 * method is only called by the setUpQueryBrowserCard method, once these two
	 * JPanel are set up, they are added to the queryBrowserCard.
	 * <p>
	 * [Note: the contentWordPane and the keywordPane are only the outer containers,
	 * they do not directly contain the result of the query. Instead, their
	 * components - contentWordResult and the keywordResult are used to store the
	 * clickable results.]
	 * <p>
	 * To understand what it means by 'card' or queryBrowserCard, see the
	 * constructor, setUpHtmlBrowserCard method and the setUpQueryBrowserCard
	 * method.
	 * 
	 */
	private void setUpQueryResultPane() {
		/*
		 * set up the area for displaying query results (content word). The
		 * contentWordPane is the outer container, it doesn't directly store the query
		 * result.
		 */
		contentWordPane = new JPanel();
		contentWordPane.setLayout(new BoxLayout(contentWordPane, BoxLayout.Y_AXIS));

		JLabel contentResultLabel = new JLabel("Content Word Result:");
		contentResultLabel.setFont(menuFont);

		// contentWordResult is the JPanel used to directly contain the results
		contentWordResult = new JPanel();
		contentWordPane.add(contentResultLabel);
		contentWordPane.add(new JScrollPane(contentWordResult));

		/*
		 * set up the area for displaying query results (keyword). The keywordPane is
		 * the outer container, it doesn't directly store the query result.
		 */
		keywordPane = new JPanel();
		keywordPane.setLayout(new BoxLayout(keywordPane, BoxLayout.Y_AXIS));

		JLabel queryResultLabel = new JLabel("Keyword Result:");
		queryResultLabel.setFont(menuFont);

		// keywordResult is the JPanel used to directly contain the results
		keywordResult = new JPanel();
		keywordResult.setLayout(new BoxLayout(keywordResult, BoxLayout.Y_AXIS));
		keywordPane.add(queryResultLabel);
		keywordPane.add(new JScrollPane(keywordResult));
	}

	/**
	 * This method sets up the history area in the queryBrowserCard. This area
	 * consists of one JPanel as the container of the components - historyPane. This
	 * method is only called by the setUpQueryBrowserCard method, once this JPanel
	 * is set up, it is added to the queryBrowserCard.
	 * <p>
	 * [Note: the historyPane is only the outer container, it does not directly
	 * contain the 'records' (URL links) of the history. Instead, its components -
	 * historyRecordPanel is used to store the clickable records/links.]
	 * <p>
	 * To understand what it means by 'card' or queryBrowserCard, see the
	 * constructor, setUpHtmlBrowserCard method and the setUpQueryBrowserCard
	 * method.
	 */
	private void setUpHistoryPane() {
		historyPane = new JPanel();
		historyPane.setLayout(new BoxLayout(historyPane, BoxLayout.Y_AXIS));
		historyPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		JLabel historyTitleLabel = new JLabel("	History:");
		historyTitleLabel.setFont(menuFont);

		historyRecordPanel = new JPanel();
		historyRecordPanel.setFont(contentFont);
		historyRecordPanel.setLayout(new BoxLayout(historyRecordPanel, BoxLayout.Y_AXIS));

		historyPane.add(historyTitleLabel);
		historyPane.add(new JScrollPane(historyRecordPanel));
	}

	/**
	 * This method sets up the control area in the queryBrowserCard. This area
	 * consists of one JPanel as the container of the components - controlPane. This
	 * method is only called by the setUpQueryBrowserCard method, once this JPanel
	 * is set up, it is added to the queryBrowserCard.
	 * <p>
	 * The controlPane is the outer container, it consists of the buttons and
	 * testFields for initiating the query searching. For example, the infixQuery
	 * and prefixQuery are the JTextField used to gain the query entries.
	 * <p>
	 * To understand what it means by 'card' or queryBrowserCard, see the
	 * constructor, setUpHtmlBrowserCard method and the setUpQueryBrowserCard
	 * method.
	 */
	private void setUpControlPane() {
		// Set up group layout for the controlPane
		controlPane = new JPanel();
		GroupLayout groupLayout = new GroupLayout(controlPane);
		controlPane.setLayout(groupLayout);

		// The JComponents in the controlPanel
		JLabel overallQueryControllerTitle = new JLabel("Query Handling:");
		JLabel infixControllerTitle = new JLabel("Infix Query:");
		JLabel prefixControllerTitle = new JLabel("Prefix Query:");
		infixQuery = new JTextField(15);
		prefixQuery = new JTextField(15);
		infixQuery.setActionCommand("infix");
		prefixQuery.setActionCommand("prefix");
		infixSearchButton = new JButton("Search");
		prefixSearchButton = new JButton("Search");
		infixSearchButton.setActionCommand("infix");
		prefixSearchButton.setActionCommand("prefix");

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

		// Set the fonts for all the child components in the controlPanel
		for (Component com : controlPane.getComponents()) {
			com.setFont(contentFont);
		}
	}

	/**
	 * This method adds the ActionListener to the confirmButton and urlTextInput in
	 * the htmlBrowserCard. Specifically, when the user enters the URL entry and
	 * presses the 'Enter', it generates the same type of event as the
	 * confirmButton.
	 * <p>
	 * The reason why it's named the Confirm Action Listener is that, when the user
	 * has entered the URL entry and press the confirm button or the 'Enter', it
	 * accepts the URL entries and displays the content in a new tab.
	 * 
	 * @param actionListener An object of ActionLisener
	 */
	public void addConfirmActionListener(ActionListener actionListener) {
		confirmButton.addActionListener(actionListener);
		urlTextInput.addActionListener(actionListener);
	}

	/**
	 * This method adds the ActionListener to the infixQuery, prefixQuery,
	 * infixSearchButton and prefixSearchButton. Similar to the
	 * addConfirmActionListener method, the JTextField infixQuery and prefixQuery
	 * also generate the same event as the buttons when the user press the 'Enter'.
	 * <p>
	 * The reason why it's named the Search Action Listener is that the action
	 * listener will initiate the query searching for these four components.
	 * However, with the use of command, the controller (of MVC) can identify
	 * whether the query searched is the infix query or the prefix query.
	 * 
	 * @param actionListener An object of ActionLisener
	 */
	public void addSearchActionListener(ActionListener actionListener) {
		infixQuery.addActionListener(actionListener);
		prefixQuery.addActionListener(actionListener);
		infixSearchButton.addActionListener(actionListener);
		prefixSearchButton.addActionListener(actionListener);
	}

	/**
	 * This method adds the ActionListener to the htmlBrowser and the queryBrowser,
	 * which are the menu items in the menu bar. The action listener will be used to
	 * navigate between the two graphical interfaces or cards (htmlBrowserCard and
	 * queryBrowserCard). This navigation is done by using the JPanel with a
	 * CardLayout.
	 * <p>
	 * To understand what it means by 'card' or queryBrowserCard, see the
	 * constructor, setUpHtmlBrowserCard method and the setUpQueryBrowserCard
	 * method.
	 *
	 * @param actionListener An object of ActionLisener
	 */
	public void addMenuItemActionListener(ActionListener actionListener) {
		htmlBrowser.addActionListener(actionListener);
		queryBrowser.addActionListener(actionListener);
	}

	/**
	 * This method adds the WindowListener to the browserFrame, which is the JFrame
	 * that the top level container in the GUI.
	 * <p>
	 * The window listener added to the JFrame will be responsible for saving the
	 * index and history before the software being closed, and loading the saved
	 * index and history when the user launches the software.
	 * 
	 * @param windowListener An object of ActionLisener
	 */
	public void addFrameWindowListener(WindowListener windowListener) {
		browserFrame.addWindowListener(windowListener);
	}

	/**
	 * Getter for the keywordResult
	 * 
	 * @return keywordResult
	 */
	public JPanel getKeywordResult() {
		return keywordResult;
	}

	/**
	 * Getter for the contentWordResult
	 * 
	 * @return contentWordResult
	 */
	public JPanel getContentWordResult() {
		return contentWordResult;
	}

	/**
	 * Getter for the webBrowserTabbedPane
	 * 
	 * @return webBrowserTabbedPane
	 */
	public JTabbedPane getWebBrowserTabbedPane() {
		return webBrowserTabbedPane;
	}

	/**
	 * Getter for the urlTextInput
	 * 
	 * @return urlTextInput
	 */
	public JTextField getUrlTextField() {
		return urlTextInput;
	}

	/**
	 * Getter for the infixQuery
	 * 
	 * @return infixQuery
	 */
	public JTextField getInfixQuery() {
		return infixQuery;
	}

	/**
	 * Getter for the prefixQuery
	 * 
	 * @return prefixQuery
	 */
	public JTextField getPrefixQuery() {
		return prefixQuery;
	}

	/**
	 * Getter for the historyRecordPanel
	 * 
	 * @return historyRecordPanel
	 */
	public JPanel getHistoryRecordPanel() {
		return historyRecordPanel;
	}

	/**
	 * Getter for the cardLayoutControl
	 * 
	 * @return cardLayoutControl
	 */
	public CardLayout getCardLayoutControl() {
		return cardLayoutControl;
	}

	/**
	 * Getter for the cards
	 * 
	 * @return cards
	 */
	public JPanel getCards() {
		return cards;
	}

	public static void main(String[] args) {
		WebBrowserView view = new WebBrowserView();
		WebBrowserController controller = new WebBrowserController(view);
	}
}
