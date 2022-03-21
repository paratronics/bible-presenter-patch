/*
 * Created on Apr 6, 2004 $Id: ControllerWindow.java,v 1.13 2004/04/28 09:49:52
 * Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import info.rueth.biblepresenter.BiblePresenterException;
import info.rueth.biblepresenter.Bookmark;
import info.rueth.biblepresenter.Bookmarks;
import info.rueth.biblepresenter.Preferences;
import info.rueth.biblepresenter.Vers;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;


/**
 * The core graphical user interface component, the controller window.
 * 
 * @author Ulrich Rueth
 */
public class ControllerWindow extends JFrame
{
	//**********************************************************
	// Constructors
	//**********************************************************

	/**
	 * Constructor.
	 * 
	 * @param dataSetter
	 *            The data container.
	 * @param resources
	 *            The language resource.
	 * @param preferences
	 *            The user preferences.
	 */
	public ControllerWindow(DataSetter dataSetter, ResourceBundle resources,
			Preferences preferences)
	{
		this.dataSetter = dataSetter;
		this.resources = resources;
		this.preferences = preferences;
		this.comboboxListener = new ComboboxActionHandler(this);
		this.buttonListener = new ButtonActionHandler(this);
		this.bookmarkListener = new BookmarkListener(this, dataSetter,
				resources);
		this.menuListener = new MenuActionHandler(this, dataSetter,
				preferences, resources);

		// set the locale of the GUI
		this.setLocale(preferences.getLocale());

		// child windows
		this.presenterWindow = PresenterFactory.getPresenter(this, dataSetter,
				preferences);

		// add listeners
		this.addWindowListener(new ControllerWindowListener(this, dataSetter,
				preferences));

		// try to set icon
		try
		{
			setIconImage(GUILauncher.loadImageIcon(ICON).getImage());
		}
		catch (IOException e)
		{
			// do nothing, use default icon
			e.printStackTrace();
		}
	}

	//**********************************************************
	// Public methods
	//**********************************************************

	/**
	 * Builds up and shows the controller window.
	 * 
	 * @param splashScreen
	 *            The splash screen which will be disposed as soon as the
	 *            controller window is built up and shown.
	 */
	public void createAndShow(SplashScreen splashScreen)
			throws BiblePresenterException
	{
		// Create and set up the window.
		setTitle(resources.getString("cw_title"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set menu bar
		setJMenuBar(createMenuBar());

		// Set layout manager
		getContentPane().setLayout(new BorderLayout());

		// Add presenter in center
		getContentPane().add(BorderLayout.CENTER, createComboBoxPanel());

		// Add preview window in south
		getContentPane().add(BorderLayout.SOUTH, createPreviewArea());

		// Add bookmark window
		getContentPane().add(BorderLayout.WEST, createBookmarkPanel());

		// Display the window.
		pack();
		setVisible(true);
		splashScreen.kill();
	}

	/**
	 * Increases the start and end vers combo box values by one.
	 * 
	 * @throws BiblePresenterException
	 */
	public void increaseVerses() throws BiblePresenterException
	{
		int versStartIndex = versStartChooser.getSelectedIndex();
		int versEndIndex = versEndChooser.getSelectedIndex();
		int startVersesNumber = versStartChooser.getItemCount();
		int endVersesNumber = versEndChooser.getItemCount();

		if (versStartIndex + 1 < startVersesNumber)
		{
			versStartChooser.setSelectedIndex(versStartIndex + 1);
		}
		if (versEndIndex + 1 < endVersesNumber)
		{
			versEndChooser.setSelectedIndex(versEndIndex + 1);
		}

		dataSetter.setCurrentStartVersNumber((String) versStartChooser
				.getSelectedItem());
		dataSetter.setCurrentEndVersNumber((String) versEndChooser
				.getSelectedItem());
		updatePreviewArea();
	}

	/**
	 * Decreases the start and end vers combo box values by one.
	 * 
	 * @throws BiblePresenterException
	 */
	public void decreaseVerses() throws BiblePresenterException
	{
		int versStartIndex = versStartChooser.getSelectedIndex();
		int versEndIndex = versEndChooser.getSelectedIndex();

		if (versStartIndex - 1 >= 0)
		{
			versStartChooser.setSelectedIndex(versStartIndex - 1);
		}
		if (versEndIndex - 1 >= 0)
		{
			versEndChooser.setSelectedIndex(versEndIndex - 1);
		}

		dataSetter.setCurrentStartVersNumber((String) versStartChooser
				.getSelectedItem());
		dataSetter.setCurrentEndVersNumber((String) versEndChooser
				.getSelectedItem());
		updatePreviewArea();
	}

	/**
	 * Updates the bible location combo boxes and the data setter. Does not
	 * update the presenter window though!
	 * 
	 * @param bible
	 *            The new bible name.
	 * @param book
	 *            The new book name.
	 * @param chapter
	 *            The new chapter number.
	 * @param startVers
	 *            The new start vers number.
	 * @param endVers
	 *            The new end vers number.
	 * @throws BiblePresenterException
	 */
	public void updateSelection(String bible, String book, String chapter,
			String startVers, String endVers) throws BiblePresenterException
	{
		bibleChooser.setSelectedItem(bible);
		dataSetter.setCurrentBibleName(bible);
		bookChooser.setSelectedItem(book);
		dataSetter.setCurrentBookName(book);
		chapterChooser.setSelectedItem(chapter);
		dataSetter.setCurrentChapterNumber(chapter);
		versStartChooser.setSelectedItem(startVers);
		dataSetter.setCurrentStartVersNumber(startVers);
		versEndChooser.setSelectedItem(endVers);
		dataSetter.setCurrentEndVersNumber(endVers);
	}

	/**
	 * Updates the preview area with the currently selected bible location.
	 * 
	 * @throws BiblePresenterException
	 */
	public void updatePreviewArea() throws BiblePresenterException
	{
		Vers[] verses = dataSetter.getVerses();
		DefaultStyledDocument document = new DefaultStyledDocument();
		SimpleAttributeSet attSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attSet, "Helvetica");
		StyleConstants.setFontSize(attSet, 10);
		for (int i = 0; i < verses.length; i++)
		{
			try
			{
				document.insertString(document.getLength(), verses[i]
						.getVersNumber()
						+ " ", attSet);
				document.insertString(document.getLength(), verses[i].getVers()
						+ "\n", attSet);
			}
			catch (BadLocationException e)
			{
				throw new BiblePresenterException(e.getMessage(), e);
			}
		}
		previewArea.setDocument(document);
	}

	/**
	 * @return Returns the presenter window.
	 */
	public Presenter getPresenterWindow()
	{
		return presenterWindow;
	}

	/**
	 * Updates the bible chooser combo box using the data setter values. If the
	 * bible selection has changed, it calls the updateBooks() method.
	 * 
	 * @throws BiblePresenterException
	 */
	public void updateBibles() throws BiblePresenterException
	{
		Object selectedBible = bibleChooser.getSelectedItem();
		DefaultComboBoxModel newBibleModel = new DefaultComboBoxModel(
				dataSetter.getBibleNames());
		if (newBibleModel.getIndexOf(selectedBible) >= 0)
		{
			newBibleModel.setSelectedItem(selectedBible);
			bibleChooser.setModel(newBibleModel);
		}
		else
		{
			// the previously selected bible has been removed
			// so update the other boxes
			bibleChooser.setModel(newBibleModel);
			dataSetter.setCurrentBibleName((String) newBibleModel
					.getSelectedItem());
			updateBooks();
		}
	}

	/**
	 * Updates the book chooser combo box using the data setter values. It calls
	 * the updateChapters() method.
	 * 
	 * @throws BiblePresenterException
	 */
	public void updateBooks() throws BiblePresenterException
	{
		String bibleName = (String) bibleChooser.getSelectedItem();
		dataSetter.setCurrentBibleName(bibleName);
		String[] books = dataSetter.getBookNames(bibleName);
		dataSetter.setCurrentBookName(books[0]);
		ComboBoxModel bookModel = new DefaultComboBoxModel(books);
		bookChooser.setModel(bookModel);
		updateChapters();
	}

	/**
	 * Updates the chapter chooser combo box using the data setter values. It
	 * calls the updateVerses() method.
	 * 
	 * @throws BiblePresenterException
	 */
	public void updateChapters() throws BiblePresenterException
	{
		String bibleName = (String) bibleChooser.getSelectedItem();
		dataSetter.setCurrentBibleName(bibleName);
		String bookName = (String) bookChooser.getSelectedItem();
		dataSetter.setCurrentBookName(bookName);
		String[] chapters = dataSetter.getChapterNumbers(bibleName, bookName);
		dataSetter.setCurrentChapterNumber(chapters[0]);
		ComboBoxModel chapterModel = new DefaultComboBoxModel(chapters);
		chapterChooser.setModel(chapterModel);
		updateVerses();
	}

	/**
	 * Updates the start and end vers chooser combo boxes using the data setter
	 * values.
	 * 
	 * @throws BiblePresenterException
	 */
	public void updateVerses() throws BiblePresenterException
	{
		String bibleName = (String) bibleChooser.getSelectedItem();
		dataSetter.setCurrentBibleName(bibleName);
		String bookName = (String) bookChooser.getSelectedItem();
		dataSetter.setCurrentBookName(bookName);
		String chapterNumber = (String) chapterChooser.getSelectedItem();
		dataSetter.setCurrentChapterNumber(chapterNumber);

		// set and store start vers number
		String[] verses = dataSetter.getVersNumbers(bibleName, bookName,
				chapterNumber);
		dataSetter.setCurrentStartVersNumber(verses[0]);
		ComboBoxModel startVersModel = new DefaultComboBoxModel(verses);
		versStartChooser.setModel(startVersModel);

		// set and store end vers number
		ComboBoxModel endVersModel = new DefaultComboBoxModel(verses);
		versEndChooser.setModel(endVersModel);
		if (preferences.getVersOffset() < endVersModel.getSize())
		{
			versEndChooser.setSelectedIndex(preferences.getVersOffset());
		}
		else
		{
			versEndChooser.setSelectedIndex(endVersModel.getSize() - 1);
		}
		dataSetter.setCurrentEndVersNumber((String) versEndChooser
				.getSelectedItem());
	}

	/**
	 * Updates the end vers chooser combo box using the selected start vers and
	 * the default vers offset from the user preferences.
	 */
	public void updateEndVers()
	{
		dataSetter.setCurrentStartVersNumber((String) versStartChooser
				.getSelectedItem());
		int currentStartVersIndex = versStartChooser.getSelectedIndex();
		int currentEndVersIndex = versEndChooser.getSelectedIndex();
		if (currentStartVersIndex > currentEndVersIndex)
		{
			int maxVersIndices = versStartChooser.getItemCount();
			if (currentStartVersIndex + preferences.getVersOffset() < maxVersIndices)
			{
				versEndChooser.setSelectedIndex(currentStartVersIndex
						+ preferences.getVersOffset());
			}
			else
			{
				versEndChooser.setSelectedIndex(maxVersIndices);
			}
			dataSetter.setCurrentEndVersNumber((String) versEndChooser
					.getSelectedItem());
		}
	}

	/**
	 * Updates the start vers chooser combo box using the selected end vers and
	 * the default vers offset from the user preferences.
	 */
	public void updateStartVers()
	{
		dataSetter.setCurrentEndVersNumber((String) versEndChooser
				.getSelectedItem());
		int currentEndVersIndex = versEndChooser.getSelectedIndex();
		int currentStartVersIndex = versStartChooser.getSelectedIndex();
		if (currentEndVersIndex < currentStartVersIndex)
		{
			if (currentEndVersIndex - preferences.getVersOffset() >= 0)
			{
				versStartChooser.setSelectedIndex(currentEndVersIndex
						- preferences.getVersOffset());
			}
			else
			{
				versStartChooser.setSelectedIndex(0);
			}
			dataSetter.setCurrentStartVersNumber((String) versStartChooser
					.getSelectedItem());
		}
	}

	/**
	 * Goes through all bookmarks and checks if the referenced bibles are still
	 * available, if not removes the bookmark.
	 */
	public void updateBookmarks()
	{
		preferences.getBookmarks().removeInvalidBookmarks(
				dataSetter.getBibleNames());
		bookmarkTree.setModel(new DefaultTreeModel(
				createBookmarkNodes(preferences.getBookmarks())));
	}

	/**
	 * Displays the currently selected bookmark.
	 * 
	 * @throws BiblePresenterException
	 */
	public void displayBookmark() throws BiblePresenterException
	{
		if (selectedNode == null)
		{
			return;
		}
		if (selectedNode.getUserObject() instanceof Bookmark)
		{
			showBookmark(selectedNode, false);
		}

	}

	/**
	 * Displays the next or previous bookmark of the selected bookmark.
	 * 
	 * @param next
	 *            If true, the next bookmark is displayed, else the previous
	 *            one.
	 * @throws BiblePresenterException
	 */
	public void displayBookmark(boolean next) throws BiblePresenterException
	{
		DefaultMutableTreeNode parent = null;
		if (selectedNode == null)
		{
			parent = (DefaultMutableTreeNode) bookmarkTree.getModel().getRoot();
		}
		else if (selectedNode.getUserObject() instanceof Bookmark)
		{
			parent = (DefaultMutableTreeNode) selectedNode.getParent();
		}
		else
		{
			parent = selectedNode;
			selectedNode = null;
		}
		if (next)
			displayNextBookmark(parent, selectedNode);
		else
			displayPreviousBookmark(parent, selectedNode);
		bookmarkTree.updateUI();
	}

	/**
	 * Updates the bible location and optionally the presenter window with the
	 * specified bookmark.
	 * 
	 * @param selectedBookmark
	 *            The selected bookmark.
	 * @param updatePresenterWindow
	 *            If true, also updates the presenter window.
	 * @throws BiblePresenterException
	 */
	public void showBookmark(DefaultMutableTreeNode selectedBookmark,
			boolean updatePresenterWindow) throws BiblePresenterException
	{
		// select element in tree
		TreeNode[] treenodes = selectedBookmark.getPath();
		TreePath path = new TreePath(treenodes);
		bookmarkTree.expandPath(path);
		bookmarkTree.setSelectionPath(path);
		bookmarkTree.updateUI();

		Bookmark bookmark = (Bookmark) selectedBookmark.getUserObject();

		updateSelection(bookmark.getBible(), bookmark.getBook(), bookmark
				.getChapter(), bookmark.getStartVers(), bookmark.getEndVers());
		if (updatePresenterWindow)
		{
			getPresenterWindow().updateContent();
		}
	}

	/**
	 * Moves the selected bookmark object up by one.
	 */
	public void moveBookmarkObjectUp()
	{
		if (selectedNode == null)
		{
			showWarning(resources.getString("warning_select_node"));
			return;
		}
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode
				.getParent();
		int childIndex = parent.getIndex(selectedNode);
		if (childIndex > 0)
		{
			parent.remove(childIndex);
			parent.insert(selectedNode, childIndex - 1);
			Bookmarks parentFolder = (Bookmarks) parent.getUserObject();
			parentFolder.moveUp(childIndex);
		}
		else
		{
			// ignore this, it's the first element already
			return;
		}
		bookmarkTree.updateUI();
	}

	/**
	 * Moves the selected bookmark object down by one.
	 */
	public void moveBookmarkObjectDown()
	{
		if (selectedNode == null)
		{
			showWarning(resources.getString("warning_select_node"));
			return;
		}
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode
				.getParent();
		int childIndex = parent.getIndex(selectedNode);
		if (childIndex < parent.getChildCount() - 1)
		{
			parent.remove(childIndex);
			parent.insert(selectedNode, childIndex + 1);
			Bookmarks parentFolder = (Bookmarks) parent.getUserObject();
			parentFolder.moveDown(childIndex);
		}
		else
		{
			// ignore this, it's the last element already
			return;
		}
		bookmarkTree.updateUI();
	}

	/**
	 * Creates a new bookmark folder
	 */
	public void createNewBookmarkFolder() throws BiblePresenterException
	{
		DefaultMutableTreeNode baseNode = null;
		int insertAtPosition = -1;

		if (selectedNode == null)
		{
			baseNode = (DefaultMutableTreeNode) bookmarkTree.getModel()
					.getRoot();
		}
		else if (selectedNode.getUserObject() instanceof Bookmark)
		{
			baseNode = (DefaultMutableTreeNode) selectedNode.getParent();
			insertAtPosition = baseNode.getIndex(selectedNode);
		}
		else
		{
			baseNode = selectedNode;
		}
		String title = (String) JOptionPane.showInputDialog(this, resources
				.getString("bm_titlemessage"), resources
				.getString("bm_titledialogtitle"), JOptionPane.PLAIN_MESSAGE,
				null, null, "");

		// If a string was returned, say so.
		if ((title != null) && (title.length() > 0))
		{
			Bookmarks newBookmarks = new Bookmarks(title);
			Bookmarks baseBookmarks = (Bookmarks) baseNode.getUserObject();
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
					newBookmarks);
			if (insertAtPosition >= 0)
			{
				baseNode.insert(newNode, insertAtPosition);
				baseBookmarks.addBookmark(insertAtPosition, newBookmarks);
			}
			else
			{
				baseNode.add(newNode);
				baseBookmarks.addBookmark(newBookmarks);
			}
			TreePath pathToNewElement = new TreePath(newNode.getPath());
			bookmarkTree.expandPath(pathToNewElement);
		}
		else if ((title != null) && (title.length() == 0))
		{
			showWarning(resources.getString("warning_empty_title"));
			return;
		}
		else
		{
			return;
		}
		bookmarkTree.updateUI();
	}

	/**
	 * Adds a new bookmark.
	 * 
	 * @throws BiblePresenterException
	 */
	public void addBookmark() throws BiblePresenterException
	{
		DefaultMutableTreeNode baseNode = null;
		int insertAtPosition = -1;

		if (selectedNode == null)
		{
			baseNode = (DefaultMutableTreeNode) bookmarkTree.getModel()
					.getRoot();
		}
		else if (selectedNode.getUserObject() instanceof Bookmark)
		{
			baseNode = (DefaultMutableTreeNode) selectedNode.getParent();
			insertAtPosition = baseNode.getIndex(selectedNode);
		}
		else
		{
			baseNode = selectedNode;
		}

		Bookmark newBookmark = createCurrentBookmark();
		Bookmarks baseBookmarks = (Bookmarks) baseNode.getUserObject();
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newBookmark);
		if (insertAtPosition >= 0)
		{
			baseNode.insert(newNode, insertAtPosition);
			baseBookmarks.addBookmark(insertAtPosition, newBookmark);
		}
		else
		{
			baseNode.add(newNode);
			baseBookmarks.addBookmark(newBookmark);
		}
		TreePath pathToNewElement = new TreePath(newNode.getPath());
		bookmarkTree.expandPath(pathToNewElement);
		bookmarkTree.updateUI();
	}

	/**
	 * Removes the currently selected bookmark object.
	 */
	public void removeBookmark()
	{
		if (selectedNode == null)
		{
			showWarning(resources.getString("warning_select_node"));
			return;
		}
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode
				.getParent();
		int position = parent.getIndex(selectedNode);
		parent.remove(position);
		Bookmarks parentFolder = (Bookmarks) parent.getUserObject();
		parentFolder.remove(position);
		selectedNode = null;
		bookmarkTree.updateUI();
	}

	/**
	 * Sets the selected node of the bookmark tree.
	 */
	public void setSelectedNode()
	{
		this.selectedNode = (DefaultMutableTreeNode) bookmarkTree
				.getLastSelectedPathComponent();
	}

	//**********************************************************
	// Private methods
	//**********************************************************

	/**
	 * Creates the bible location chooser combo boxes and puts it in a pane.
	 * 
	 * @return The combo box pane.
	 */
	private JPanel createComboBoxPanel() throws BiblePresenterException
	{
		// create panel holding all components of the presenter part
		JPanel presenterPanel = new JPanel();
		presenterPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(resources
						.getString("cw_versselection")), BorderFactory
						.createEmptyBorder(1, 1, 1, 1)));
		presenterPanel.setLayout(new BorderLayout());

		// create bible combo box and label
		JLabel bibleLabel = new JLabel(resources.getString("cw_bible"));
		this.bibleChooser = new JComboBox(dataSetter.getBibleNames());
		if (preferences.hasCurrentBible())
		{
			bibleChooser.setSelectedItem(preferences.getCurrentBible());
		}
		String selectedBible = (String) bibleChooser.getSelectedItem();
		dataSetter.setCurrentBibleName(selectedBible);
		bibleChooser.setActionCommand(ComboboxActionHandler.BIBLE);
		bibleChooser.addActionListener(comboboxListener);

		// create book combo box and label
		JLabel bookLabel = new JLabel(resources.getString("cw_book"));
		this.bookChooser = new JComboBox(dataSetter.getBookNames(selectedBible));
		if (preferences.hasCurrentBook())
		{
			bookChooser.setSelectedItem(preferences.getCurrentBook());
		}
		String selectedBook = (String) bookChooser.getSelectedItem();
		dataSetter.setCurrentBookName(selectedBook);
		bookChooser.setActionCommand(ComboboxActionHandler.BOOK);
		bookChooser.addActionListener(comboboxListener);

		// create chapter combo box and label
		JLabel chapterLabel = new JLabel(resources.getString("cw_chapter"));
		this.chapterChooser = new JComboBox(dataSetter.getChapterNumbers(
				selectedBible, selectedBook));
		if (preferences.hasCurrentChapter())
		{
			chapterChooser.setSelectedItem(preferences.getCurrentChapter());
		}
		String selectedChapter = (String) chapterChooser.getSelectedItem();
		dataSetter.setCurrentChapterNumber(selectedChapter);
		chapterChooser.setActionCommand(ComboboxActionHandler.CHAPTER);
		chapterChooser.addActionListener(comboboxListener);

		// create vers start combo box and label
		JLabel versStartLabel = new JLabel(resources.getString("cw_startvers"));
		String[] versNumbers = dataSetter.getVersNumbers(selectedBible,
				selectedBook, selectedChapter);
		this.versStartChooser = new JComboBox(versNumbers);
		if (preferences.hasCurrentStartVers())
		{
			versStartChooser.setSelectedItem(preferences.getCurrentStartVers());
		}
		dataSetter.setCurrentStartVersNumber((String) versStartChooser
				.getSelectedItem());
		versStartChooser.setActionCommand(ComboboxActionHandler.STARTVERS);
		versStartChooser.addActionListener(comboboxListener);

		// create vers end combo box and label
		JLabel versEndLabel = new JLabel(resources.getString("cw_endvers"));
		this.versEndChooser = new JComboBox(versNumbers);
		if (preferences.hasCurrentEndVers())
		{
			versEndChooser.setSelectedItem(preferences.getCurrentEndVers());
		}
		else
		{
			int defaultVersOffset = preferences.getVersOffset();
			if (defaultVersOffset < versEndChooser.getItemCount())
			{
				versEndChooser.setSelectedIndex(defaultVersOffset);
			}
			else
			{
				versEndChooser
						.setSelectedIndex(versEndChooser.getItemCount() - 1);
			}
		}
		dataSetter.setCurrentEndVersNumber((String) versEndChooser
				.getSelectedItem());
		versEndChooser.setActionCommand(ComboboxActionHandler.ENDVERS);
		versEndChooser.addActionListener(comboboxListener);

		// add to combo box panel
		JPanel comboBoxPanel = new JPanel();

		GridLayout gridLayout = new GridLayout(0, 1);

		JPanel biblePanel = new JPanel(gridLayout);
		biblePanel.add(bibleLabel);
		biblePanel.add(bibleChooser);
		comboBoxPanel.add(biblePanel);

		JPanel bookPanel = new JPanel(gridLayout);
		bookPanel.add(bookLabel);
		bookPanel.add(bookChooser);
		comboBoxPanel.add(bookPanel);

		JPanel chapterPanel = new JPanel(gridLayout);
		chapterPanel.add(chapterLabel);
		chapterPanel.add(chapterChooser);
		comboBoxPanel.add(chapterPanel);

		JPanel startVersPanel = new JPanel(gridLayout);
		startVersPanel.add(versStartLabel);
		startVersPanel.add(versStartChooser);
		comboBoxPanel.add(startVersPanel);

		JPanel endVersPanel = new JPanel(gridLayout);
		endVersPanel.add(versEndLabel);
		endVersPanel.add(versEndChooser);
		comboBoxPanel.add(endVersPanel);

		// add to presenter panel
		presenterPanel.add(comboBoxPanel, BorderLayout.CENTER);

		// create toolbar
		presenterPanel.add(createPresenterToolBar(), BorderLayout.NORTH);

		// return panel
		return presenterPanel;
	}

	/**
	 * Creates the preview area.
	 * 
	 * @return The preview area.
	 * @throws BiblePresenterException
	 */
	private Component createPreviewArea() throws BiblePresenterException
	{
		JPanel previewPane = new JPanel(new BorderLayout());
		previewPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(resources.getString("cw_preview")),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		// create preview text pane
		this.previewArea = new JTextPane();
		previewArea.setPreferredSize(new Dimension(500, 300));
		updatePreviewArea();
		previewArea.setEditable(false);

		// create scroll pane
		JScrollPane scrollPane = new JScrollPane(previewArea);
		previewPane.add(scrollPane, BorderLayout.CENTER);

		return previewPane;
	}

	/**
	 * Creates the navigation toolbar for the bible location chooser pane.
	 * 
	 * @return The navigation toolbar.
	 * @throws BiblePresenterException
	 */
	private JToolBar createPresenterToolBar() throws BiblePresenterException
	{
		JToolBar toolBar = new JToolBar(resources
				.getString("cw_presentationtoolbar"));

		// create show button
		final JButton showButton = new JButton(resources.getString("cw_show"));
		showButton.setActionCommand(ButtonActionHandler.SHOW);
		showButton.setMnemonic(((Integer) resources
				.getObject("cw_show_mnemonic")).intValue());
		showButton.setToolTipText(resources.getString("cw_show_tooltip"));
		showButton.addActionListener(buttonListener);

		toolBar.add(showButton);

		// create hide button
		JButton hideButton = new JButton(resources.getString("cw_hide"));
		hideButton.setActionCommand(ButtonActionHandler.HIDE);
		hideButton.setMnemonic(((Integer) resources
				.getObject("cw_hide_mnemonic")).intValue());
		hideButton.setToolTipText(resources.getString("cw_hide_tooltip"));
		hideButton.addActionListener(buttonListener);

		toolBar.add(hideButton);

		// Create and initialize the previous button.
		JButton downButton = createIconButton(
				"/toolbarButtonGraphics/navigation/Back16.gif",
				ButtonActionHandler.PREVIOUS, resources
						.getString("cw_previous_tooltip"), buttonListener,
				resources.getString("cw_previous_alttext"));
		toolBar.add(downButton);

		// Create and initialize the next button.
		JButton upButton = createIconButton(
				"/toolbarButtonGraphics/navigation/Forward16.gif",
				ButtonActionHandler.NEXT, resources
						.getString("cw_next_tooltip"), buttonListener,
				resources.getString("cw_next_alttext"));
		toolBar.add(upButton);

		// Create and initialize the next bookmark button.
		JButton nextBookmarkButton = createIconButton(
				"/toolbarButtonGraphics/navigation/Down16.gif",
				BookmarkListener.DISPLAYNEXTBOOKMARK, resources
						.getString("cw_nextbookmark_tooltip"),
				bookmarkListener, resources
						.getString("cw_nextbookmark_alttext"));
		toolBar.add(nextBookmarkButton);

		// Create and initialize the previous bookmark button.
		JButton previousBookmarkButton = createIconButton(
				"/toolbarButtonGraphics/navigation/Up16.gif",
				BookmarkListener.DISPLAYPREVIOUSBOOKMARK, resources
						.getString("cw_previousbookmark_tooltip"),
				bookmarkListener, resources
						.getString("cw_previousbookmark_alttext"));
		toolBar.add(previousBookmarkButton);

		// return tool bar
		return toolBar;
	}

	/**
	 * Creates a button with an icon.
	 * 
	 * @param imageLocation
	 *            The location of the button icon.
	 * @param actionCommand
	 *            The action command for the button.
	 * @param toolTipText
	 *            The tool tip text for the button.
	 * @param actionListener
	 *            The action listener for the button.
	 * @param altText
	 *            The alternative text of the button.
	 * @return The icon button.
	 * @throws BiblePresenterException
	 */
	private JButton createIconButton(String imageLocation,
			String actionCommand, String toolTipText,
			ActionListener actionListener, String altText)
			throws BiblePresenterException
	{
		URL imageURL = ControllerWindow.class.getResource(imageLocation);

		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(actionListener);

		if (imageURL != null)
		{
			//image found
			button.setIcon(new ImageIcon(imageURL, altText));
		}
		else
		{
			throw new BiblePresenterException(resources
					.getString("cw_ex_resourcenotfound")
					+ imageLocation);
		}
		return button;
	}

	/**
	 * Creates the controller window menu bar.
	 * 
	 * @return The controller window menu bar.
	 */
	private JMenuBar createMenuBar()
	{
		// create the menu bar
		JMenuBar menuBar = new JMenuBar();

		// build the file menu
		JMenu fileMenu = new JMenu(resources.getString("cw_menu_file"));
		fileMenu.setMnemonic(((Integer) resources
				.getObject("cw_menu_file_mnemonic")).intValue());
		menuBar.add(fileMenu);

		// create file menu entries
		JMenuItem fileImport = new JMenuItem(resources
				.getString("cw_menu_file_import"), ((Integer) resources
				.getObject("cw_menu_file_import_mnemonic")).intValue());
		fileImport.addActionListener(menuListener);
		fileImport.setActionCommand(MenuActionHandler.IMPORT);
		fileMenu.add(fileImport);

		JMenuItem fileRemove = new JMenuItem(resources
				.getString("cw_menu_file_remove"), ((Integer) resources
				.getObject("cw_menu_file_remove_mnemonic")).intValue());
		fileRemove.addActionListener(menuListener);
		fileRemove.setActionCommand(MenuActionHandler.REMOVE);
		fileMenu.add(fileRemove);

		fileMenu.addSeparator();

		JMenuItem fileExit = new JMenuItem(resources
				.getString("cw_menu_file_exit"), ((Integer) resources
				.getObject("cw_menu_file_exit_mnemonic")).intValue());
		fileExit.addActionListener(menuListener);
		fileExit.setActionCommand(MenuActionHandler.EXIT);
		fileMenu.add(fileExit);

		// build the bookmark menu
		JMenu bookmarkMenu = new JMenu(resources.getString("cw_menu_bookmark"));
		bookmarkMenu.setMnemonic(((Integer) resources
				.getObject("cw_menu_bookmark_mnemonic")).intValue());
		menuBar.add(bookmarkMenu);

		// create bookmark menu entries
		JMenuItem bookmarkAdd = new JMenuItem(resources
				.getString("cw_menu_bookmark_add"), ((Integer) resources
				.getObject("cw_menu_bookmark_add_mnemonic")).intValue());
		bookmarkAdd.addActionListener(bookmarkListener);
		bookmarkAdd.setActionCommand(BookmarkListener.ADD);
		bookmarkMenu.add(bookmarkAdd);

		JMenuItem bookmarkNew = new JMenuItem(resources
				.getString("cw_menu_bookmark_new"), ((Integer) resources
				.getObject("cw_menu_bookmark_new_mnemonic")).intValue());
		bookmarkNew.addActionListener(bookmarkListener);
		bookmarkNew.setActionCommand(BookmarkListener.NEW);
		bookmarkMenu.add(bookmarkNew);

		JMenuItem bookmarkRemove = new JMenuItem(resources
				.getString("cw_menu_bookmark_remove"), ((Integer) resources
				.getObject("cw_menu_bookmark_remove_mnemonic")).intValue());
		bookmarkRemove.addActionListener(bookmarkListener);
		bookmarkRemove.setActionCommand(BookmarkListener.REMOVE);
		bookmarkMenu.add(bookmarkRemove);

		JMenuItem bookmarkUp = new JMenuItem(resources
				.getString("cw_menu_bookmark_up"), ((Integer) resources
				.getObject("cw_menu_bookmark_up_mnemonic")).intValue());
		bookmarkUp.addActionListener(bookmarkListener);
		bookmarkUp.setActionCommand(BookmarkListener.UP);
		bookmarkMenu.add(bookmarkUp);

		JMenuItem bookmarkDown = new JMenuItem(resources
				.getString("cw_menu_bookmark_down"), ((Integer) resources
				.getObject("cw_menu_bookmark_down_mnemonic")).intValue());
		bookmarkDown.addActionListener(bookmarkListener);
		bookmarkDown.setActionCommand(BookmarkListener.DOWN);
		bookmarkMenu.add(bookmarkDown);

		// build the presentation menu
		JMenu presentationMenu = new JMenu(resources
				.getString("cw_menu_presentation"));
		presentationMenu.setMnemonic(((Integer) resources
				.getObject("cw_menu_presentation_mnemonic")).intValue());
		menuBar.add(presentationMenu);

		// create presentation menu entries
		JMenuItem presentationShow = new JMenuItem(resources
				.getString("cw_menu_presentation_show"), ((Integer) resources
				.getObject("cw_menu_presentation_show_mnemonic")).intValue());
		presentationShow.addActionListener(buttonListener);
		presentationShow.setActionCommand(ButtonActionHandler.SHOW);
		presentationShow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,
				0));
		presentationMenu.add(presentationShow);

		JMenuItem presentationHide = new JMenuItem(resources
				.getString("cw_menu_presentation_hide"), ((Integer) resources
				.getObject("cw_menu_presentation_hide_mnemonic")).intValue());
		presentationHide.addActionListener(buttonListener);
		presentationHide.setActionCommand(ButtonActionHandler.HIDE);
		presentationHide.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_ESCAPE, 0));
		presentationMenu.add(presentationHide);

		JMenuItem presentationNext = new JMenuItem(resources
				.getString("cw_menu_presentation_next"), ((Integer) resources
				.getObject("cw_menu_presentation_next_mnemonic")).intValue());
		presentationNext.addActionListener(buttonListener);
		presentationNext.setActionCommand(ButtonActionHandler.NEXT);
		presentationNext.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK));
		presentationMenu.add(presentationNext);

		JMenuItem presentationPrevious = new JMenuItem(resources
				.getString("cw_menu_presentation_previous"),
				((Integer) resources
						.getObject("cw_menu_presentation_previous_mnemonic"))
						.intValue());
		presentationPrevious.addActionListener(buttonListener);
		presentationPrevious.setActionCommand(ButtonActionHandler.PREVIOUS);
		presentationPrevious.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_LEFT, InputEvent.CTRL_MASK));
		presentationMenu.add(presentationPrevious);

		JMenuItem presentationNextBookmark = new JMenuItem(
				resources.getString("cw_menu_presentation_nextbookmark"),
				((Integer) resources
						.getObject("cw_menu_presentation_nextbookmark_mnemonic"))
						.intValue());
		presentationNextBookmark.addActionListener(bookmarkListener);
		presentationNextBookmark
				.setActionCommand(BookmarkListener.DISPLAYNEXTBOOKMARK);
		presentationNextBookmark.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DOWN, InputEvent.CTRL_MASK));
		presentationMenu.add(presentationNextBookmark);

		JMenuItem presentationPreviousBookmark = new JMenuItem(
				resources.getString("cw_menu_presentation_previousbookmark"),
				((Integer) resources
						.getObject("cw_menu_presentation_previousbookmark_mnemonic"))
						.intValue());
		presentationPreviousBookmark.addActionListener(bookmarkListener);
		presentationPreviousBookmark
				.setActionCommand(BookmarkListener.DISPLAYPREVIOUSBOOKMARK);
		presentationPreviousBookmark.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_UP, InputEvent.CTRL_MASK));
		presentationMenu.add(presentationPreviousBookmark);

		// build the window menu
		JMenu windowsMenu = new JMenu(resources.getString("cw_menu_window"));
		windowsMenu.setMnemonic(((Integer) resources
				.getObject("cw_menu_window_mnemonic")).intValue());
		menuBar.add(windowsMenu);

		// create window menu entries
		JMenuItem windowPreferences = new JMenuItem(resources
				.getString("cw_menu_window_settings"), ((Integer) resources
				.getObject("cw_menu_window_settings_mnemonic")).intValue());
		windowPreferences.addActionListener(menuListener);
		windowPreferences.setActionCommand(MenuActionHandler.PREFERENCES);
		windowsMenu.add(windowPreferences);

		// create the help menu
		JMenu helpMenu = new JMenu(resources.getString("cw_menu_help"));
		helpMenu.setMnemonic(((Integer) resources
				.getObject("cw_menu_help_mnemonic")).intValue());
		menuBar.add(helpMenu);

		// prepare help
		// Find the HelpSet file and create the HelpSet object:
		String helpHS = "BiblePresenterHelp.hs";
		ClassLoader cl = MenuActionHandler.class.getClassLoader();
		HelpSet hs = null;
		try
		{
			URL hsURL = HelpSet
					.findHelpSet(cl, helpHS, preferences.getLocale());
			hs = new HelpSet(null, hsURL);
		}
		catch (Exception ee)
		{
			// Say what the exception really is
			System.out.println("HelpSet " + ee.getMessage());
			System.out.println("HelpSet " + helpHS + " not found");
		}
		// Create a HelpBroker object:
		HelpBroker hb = hs.createHelpBroker();

		// create help menu entries
		JMenuItem helpTopics = new JMenuItem(resources
				.getString("cw_menu_help_topics"), ((Integer) resources
				.getObject("cw_menu_help_topics_mnemonic")).intValue());
		helpTopics.addActionListener(new CSH.DisplayHelpFromSource(hb));
		helpTopics.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		helpMenu.add(helpTopics);

		JMenuItem helpWeb = new JMenuItem(resources
				.getString("cw_menu_help_web"), ((Integer) resources
				.getObject("cw_menu_help_web_mnemonic")).intValue());
		helpWeb.addActionListener(menuListener);
		helpWeb.setActionCommand(MenuActionHandler.HELPWEB);
		helpMenu.add(helpWeb);

		helpMenu.addSeparator();

		JMenuItem helpAbout = new JMenuItem(resources
				.getString("cw_menu_help_about"), ((Integer) resources
				.getObject("cw_menu_help_about_mnemonic")).intValue());
		helpAbout.addActionListener(menuListener);
		helpAbout.setActionCommand(MenuActionHandler.HELPABOUT);
		helpMenu.add(helpAbout);

		// return the menu bar
		return menuBar;
	}

	/**
	 * Creates the bookmark pane.
	 * 
	 * @return The bookmark pane.
	 * @throws BiblePresenterException
	 */
	private Component createBookmarkPanel() throws BiblePresenterException
	{
		// create bookmark panel
		JPanel bookmarkPane = new JPanel();
		bookmarkPane.setLayout(new BoxLayout(bookmarkPane, BoxLayout.Y_AXIS));
		bookmarkPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(resources.getString("bookmarks")),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		// create toolbar
		JToolBar bookmarkToolbar = new JToolBar(resources
				.getString("bookmarks"));
		bookmarkPane.add(bookmarkToolbar);
		bookmarkPane.setPreferredSize(new Dimension(250, 200));

		// Create and initialize the add button.
		JButton addButton = createIconButton(
				"/toolbarButtonGraphics/general/Add16.gif",
				BookmarkListener.ADD, resources.getString("cw_add_tooltip"),
				bookmarkListener, resources.getString("cw_add_alttext"));
		bookmarkToolbar.add(addButton);

		// Create and initialize the new button.
		JButton newButton = createIconButton(
				"/toolbarButtonGraphics/general/New16.gif",
				BookmarkListener.NEW, resources.getString("cw_new_tooltip"),
				bookmarkListener, resources.getString("cw_new_alttext"));
		bookmarkToolbar.add(newButton);

		// Create and initialize the remove button.
		JButton removeButton = createIconButton(
				"/toolbarButtonGraphics/general/Delete16.gif",
				BookmarkListener.REMOVE, resources
						.getString("cw_remove_tooltip"), bookmarkListener,
				resources.getString("cw_remove_alttext"));
		bookmarkToolbar.add(removeButton);

		// Create and initialize the down button.
		JButton downButton = createIconButton(
				"/toolbarButtonGraphics/navigation/Down16.gif",
				BookmarkListener.DOWN, resources.getString("cw_down_tooltip"),
				bookmarkListener, resources.getString("cw_down_alttext"));
		bookmarkToolbar.add(downButton);

		// Create and initialize the up button.
		JButton upButton = createIconButton(
				"/toolbarButtonGraphics/navigation/Up16.gif",
				BookmarkListener.UP, resources.getString("cw_up_tooltip"),
				bookmarkListener, resources.getString("cw_up_alttext"));
		bookmarkToolbar.add(upButton);

		// create tree node model
		DefaultMutableTreeNode top = createBookmarkNodes(preferences
				.getBookmarks());
		this.bookmarkTree = new JTree(top);
		bookmarkTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		bookmarkTree.addTreeSelectionListener(bookmarkListener);
		bookmarkTree.addMouseListener(bookmarkListener);
		bookmarkTree.setRootVisible(false);

		// create and return bookmark pane
		JScrollPane bookmarkScrollPane = new JScrollPane(bookmarkTree);
		bookmarkPane.add(bookmarkScrollPane);

		// return bookmark pane
		return bookmarkPane;
	}

	/**
	 * Returns a tree node filled up with the passed bookmarks.
	 * 
	 * @param bookmarks
	 *            The bookmarks to set to the tree node.
	 * @return The filled up tree node.
	 */
	private DefaultMutableTreeNode createBookmarkNodes(Bookmarks bookmarks)
	{
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(bookmarks);
		ArrayList bookmarksList = bookmarks.getBookmarks();
		ArrayList bookmarksToRemove = new ArrayList();
		for (Iterator i = bookmarksList.iterator(); i.hasNext();)
		{
			Object bookmarkObject = i.next();
			DefaultMutableTreeNode newNode = null;
			if (bookmarkObject instanceof Bookmark)
			{
				// this is a bookmark
				Bookmark newBookmark = (Bookmark) bookmarkObject;
				if (!dataSetter.isValidBookmark(newBookmark))
				{
					JOptionPane.showMessageDialog(this, resources
							.getString("error_invalidbookmark_line1")
							+ "\n"
							+ newBookmark.toString()
							+ "\n"
							+ resources
									.getString("error_invalidbookmark_line2"),
							resources.getString("error_invalidbookmark_title"),
							JOptionPane.ERROR_MESSAGE);

					// schedule bookmark for removal
					bookmarksToRemove.add(newBookmark);
				}
				else
				{
					newNode = new DefaultMutableTreeNode(newBookmark, false);
					top.add(newNode);
				}
			}
			else if (bookmarkObject instanceof Bookmarks)
			{
				// this is a bookmark folder
				newNode = createBookmarkNodes((Bookmarks) bookmarkObject);
				top.add(newNode);
			}
		}
		// remove bookmarks scheduled for removal
		for (Iterator i = bookmarksToRemove.iterator(); i.hasNext();)
		{
			Bookmark bookmarkToRemove = (Bookmark) i.next();
			bookmarksList.remove(bookmarkToRemove);
		}
		return top;
	}

	/**
	 * Displays the next bookmark of the selected bookmark.
	 * 
	 * @param parent
	 *            The parent bookmarks folder.
	 * @param child
	 *            The selected bookmark, null if none is selected.
	 * @throws BiblePresenterException
	 */
	private void displayNextBookmark(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode child) throws BiblePresenterException
	{
		if (child == null)
		{
			// get first Bookmark child available
			for (int i = 0; i < parent.getChildCount(); i++)
			{
				DefaultMutableTreeNode selection = (DefaultMutableTreeNode) parent
						.getChildAt(i);
				Object bookmarkObject = selection.getUserObject();
				if (bookmarkObject instanceof Bookmark)
				{
					showBookmark(selection, true);
					return;
				}
			}
		}
		else
		{
			// there is a selected child
			TreeNode childToDisplay = parent.getChildAfter(child);
			while (childToDisplay != null)
			{
				DefaultMutableTreeNode selection = (DefaultMutableTreeNode) childToDisplay;
				Object bookmarkObject = selection.getUserObject();
				if (bookmarkObject instanceof Bookmark)
				{
					showBookmark(selection, true);
					return;
				}
				childToDisplay = parent.getChildAfter(childToDisplay);
			}
		}
	}

	/**
	 * Displays the previous bookmark of the selected bookmark.
	 * 
	 * @param parent
	 *            The parent bookmarks folder.
	 * @param child
	 *            The selected bookmark, null if none is selected.
	 * @throws BiblePresenterException
	 */
	private void displayPreviousBookmark(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode child) throws BiblePresenterException
	{
		if (child == null)
		{
			// get last Bookmark child available
			for (int i = parent.getChildCount() - 1; i >= 0; i--)
			{
				DefaultMutableTreeNode selection = (DefaultMutableTreeNode) parent
						.getChildAt(i);
				Object bookmarkObject = selection.getUserObject();
				if (bookmarkObject instanceof Bookmark)
				{
					showBookmark(selection, true);
					return;
				}
			}
		}
		else
		{
			// there is a selected child
			TreeNode childToDisplay = parent.getChildBefore(child);
			while (childToDisplay != null)
			{
				DefaultMutableTreeNode selection = (DefaultMutableTreeNode) childToDisplay;
				Object bookmarkObject = selection.getUserObject();
				if (bookmarkObject instanceof Bookmark)
				{
					showBookmark(selection, true);
					return;
				}
				childToDisplay = parent.getChildBefore(childToDisplay);
			}
		}
	}

	/**
	 * Creates a bookmark from the currently selected bible location.
	 * 
	 * @return Returns the new bookmark.
	 */
	private Bookmark createCurrentBookmark()
	{
		Bookmark bookmark = new Bookmark(dataSetter.getCurrentBibleName(),
				dataSetter.getCurrentBookName(), dataSetter
						.getCurrentChapterNumber(), dataSetter
						.getCurrentStartVersNumber(), dataSetter
						.getCurrentEndVersNumber());
		return bookmark;
	}

	/**
	 * Shows a warning dialog.
	 * 
	 * @param message
	 *            The message of the dialog.
	 */
	private void showWarning(String message)
	{
		JOptionPane.showMessageDialog(this, message, resources
				.getString("bookmarks"), JOptionPane.WARNING_MESSAGE);
	}

	//**********************************************************
	// Private variables
	//**********************************************************

	private Presenter presenterWindow;
	private DataSetter dataSetter;
	private ResourceBundle resources;
	private Preferences preferences;
	private ComboboxActionHandler comboboxListener;
	private final ButtonActionHandler buttonListener;
	private BookmarkListener bookmarkListener;
	private MenuActionHandler menuListener;

	private DefaultMutableTreeNode selectedNode;

	private JComboBox bibleChooser;
	private JComboBox bookChooser;
	private JComboBox chapterChooser;
	private JComboBox versStartChooser;
	private JComboBox versEndChooser;
	private JTextPane previewArea;
	private JTree bookmarkTree;

	private static final String ICON = "etc/images/biblepresenter.gif";
}