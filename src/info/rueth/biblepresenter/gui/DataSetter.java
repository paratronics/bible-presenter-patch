/*
 * Created on Apr 4, 2004
 * $Id: DataSetter.java,v 1.4 2004/05/01 22:06:29 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import info.rueth.biblepresenter.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Manages all data retrieval and setting for the GUI components.
 * @author Ulrich Rueth
 */
public class DataSetter
{
	//**********************************************************
	// Constructors
	//**********************************************************

	/**
	 * Constructor.
	 * @param cachingDir The directory where the bible books, preferences
	 * and bible container are stored.
	 * @param preferences The user preferences.
	 * @param resources The language resource.
	 */
	public DataSetter(File cachingDir, Preferences preferences,
			ResourceBundle resources)
	throws IOException, JDOMException,
			BiblePresenterException
	{
		this.cachingDir = cachingDir;
		this.preferences = preferences;
		this.resources = resources;

		if (!createBibleContainer())
		{
			this.bibleContainer = new BibleContainer();

			// we need an initial bible
			if (!loadBible(new JFrame()))
			{
				GUILauncher.displayErrorMessage(null, resources.getString("error_needvalidbibleforfirststart"));
				System.exit(0);
			}
		}
	}

	//*****************************************************
	// Public methods
	//*****************************************************

	/**
	 * Returns all bible names.
	 * @return Returns the bible names.
	 */
	public String[] getBibleNames()
	{
		return bibleContainer.getBibleNames();
	}

	/**
	 * Returns all book names of the specified bible.
	 * @param bibleName The bible name.
	 * @return Returns the book names.
	 */
	public String[] getBookNames(String bibleName)
	{
		return bibleContainer.getBibleWithName(bibleName).getBookNames();
	}

	/**
	 * Returns all chapter numbers of the specified book in the
	 * specified bible.
	 * @param bibleName The bible name.
	 * @param bookName The book name.
	 * @return Returns the chapter numbers.
	 */
	public String[] getChapterNumbers(String bibleName, String bookName)
	{
		return bibleContainer.getBibleWithName(bibleName).getBook(bookName)
				.getChapterNumbers();
	}

	/**
	 * Returns all vers numbers of the specified chapter of the specified
	 * book in the specified bible.
	 * @param bibleName The bible name.
	 * @param bookName The book name.
	 * @param chapterNumber The chapter number.
	 * @return Returns the vers numbers.
	 */
	public String[] getVersNumbers(String bibleName, String bookName,
			String chapterNumber)
	{
		return bibleContainer.getBibleWithName(bibleName).getBook(bookName)
				.getChapter(chapterNumber).getVersNumbers();
	}

	/**
	 * Returns the current bible name.
	 * @param currentBibleName Returns the name of the currently selected
	 * bible.
	 */
	public void setCurrentBibleName(String currentBibleName)
	{
		this.currentBibleName = currentBibleName;
	}

	/**
	 * Sets the current book name.
	 * @param currentBookName The book name to set.
	 * @throws BiblePresenterException
	 */
	public void setCurrentBookName(String currentBookName)
			throws BiblePresenterException
	{
		this.currentBookName = currentBookName;

		// trigger the book to load
		loadBook();
	}

	/**
	 * Sets the current chapter number.
	 * @param currentChapterNumber The chapter number to set.
	 */
	public void setCurrentChapterNumber(String currentChapterNumber)
	{
		this.currentChapterNumber = currentChapterNumber;
	}

	/**
	 * Sets the current start vers number.
	 * @param currentStartVersNumber The start vers number to set.
	 */
	public void setCurrentStartVersNumber(String currentStartVersNumber)
	{
		this.currentStartVersNumber = currentStartVersNumber;
	}

	/**
	 * Sets the current end vers number.
	 * @param currentEndVersNumber The end vers number to set.
	 */
	public void setCurrentEndVersNumber(String currentEndVersNumber)
	{
		this.currentEndVersNumber = currentEndVersNumber;
	}

	/**
	 * Returns the current bible name.
	 * @return Returns the bible name.
	 */
	public String getCurrentBibleName()
	{
		return currentBibleName;
	}

	/**
	 * Returns the current book name.
	 * @return Returns the book name.
	 */
	public String getCurrentBookName()
	{
		return currentBookName;
	}

	/**
	 * Returns the current chapter number.
	 * @return Returns the chapter number.
	 */
	public String getCurrentChapterNumber()
	{
		return currentChapterNumber;
	}

	/**
	 * Returns the current start vers number.
	 * @return Returns the start vers number.
	 */
	public String getCurrentStartVersNumber()
	{
		return currentStartVersNumber;
	}

	/**
	 * Returns the current end vers number.
	 * @return Returns the end vers number.
	 */
	public String getCurrentEndVersNumber()
	{
		return currentEndVersNumber;
	}

	/**
	 * Returns the currently selected verses.
	 * @return Returns the verses.
	 * @throws BiblePresenterException If the current book chapters are null.
	 */
	public Vers[] getVerses() throws BiblePresenterException
	{
		if (currentBookChapters == null)
		{
			throw new BiblePresenterException("No verses available");
		}
		ArrayList versesList = new ArrayList();
		for (Iterator chapterIterator = currentBookChapters.iterator();
			chapterIterator.hasNext(); )
		{
			Element chapter = (Element) chapterIterator.next();
			String chapterNumber = chapter.getAttributeValue(ATT_CHAPTERNUMBER);
			if (chapterNumber.equals(currentChapterNumber))
			{
				List verses = chapter.getChildren(TAG_VERS);
				boolean record = false;
				for (Iterator versIterator = verses.iterator(); versIterator
						.hasNext();)
				{
					Element vers = (Element) versIterator.next();
					String versNumber = vers.getAttributeValue(ATT_VERSNUMBER);
					if (versNumber.equals(currentStartVersNumber))
					{
						record = true;
					}
					if (record)
					{
						versesList.add(new Vers(versNumber, vers.getText()));
					}
					if (versNumber.equals(currentEndVersNumber))
					{
						record = false;
					}
				}
			}
		}
		Vers[] verses = new Vers[versesList.size()];
		Object ignore = versesList.toArray(verses);
		return verses;
	}

	/**
	 * Verifies if the given bookmark is valid.
	 * @param bookmark The bookmark to verify.
	 * @return Returns true if it is a valid bookmark.
	 */
	public boolean isValidBookmark(Bookmark bookmark)
	{
		String bible = bookmark.getBible();
		if (!bibleContainer.hasBibleWithName(bible))
			return false;

		Bible selectedBible = bibleContainer.getBibleWithName(bible);
		String book = bookmark.getBook();
		if (!selectedBible.hasBook(book))
			return false;

		Book selectedBook = selectedBible.getBook(book);
		String chapter = bookmark.getChapter();
		if (!selectedBook.hasChapter(chapter))
			return false;

		Chapter selectedChapter = selectedBook.getChapter(chapter);
		String startVers = bookmark.getStartVers();
		String endVers = bookmark.getEndVers();
		if (!selectedChapter.hasVersNumber(startVers))
			return false;
		if (!selectedChapter.hasVersNumber(endVers))
			return false;

		return true;
	}

	/**
	 * Opens a file chooser dialog and lets the user select a xml file. It is
	 * not verified if the selected file is a valid bible xml file.
	 * @param parent The parent frame.
	 * @return Returns true if the user has selected a file, else false.
	 * @throws BiblePresenterException
	 */
	public boolean loadBible(JFrame parent) throws BiblePresenterException
	{
		JFileChooser chooser = new JFileChooser(preferences.getLatestBibleDirectory());
		chooser.setDialogTitle(resources.getString("filechooser_title"));
		BibleFileFilter filter = new BibleFileFilter();
		filter.addExtension("xml");
		filter.setDescription("XML Files");
		chooser.setFileFilter(filter);
//		int returnVal = chooser.showOpenDialog(parent);
//		if (returnVal == JFileChooser.APPROVE_OPTION)
//		{
			File selectedFile = new File("etc/bibles/bbe.xml");
			preferences.setLatestBibleDirectory(selectedFile.getParent());
			loadBible(selectedFile);
			return true;
//		}
//		return false;
	}

	/**
	 * Opens a dialog which lets the user choose a bible to remove. If there
	 * is only one bible left, a warning is issued and no removal dialog
	 * is opened.
	 * @param parent The parent frame.
	 * @throws BiblePresenterException
	 */
	public void removeBible(JFrame parent)
	throws BiblePresenterException
	{
		String[] bibleNames = getBibleNames();
		if (bibleNames.length <= 1)
		{
			JOptionPane.showMessageDialog(parent,
			    resources.getString("info_onlyonebibleleft"),
				resources.getString("info_onlyonebibleleft_title"),
				JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			String bibleToRemove = (String)JOptionPane.showInputDialog(
					parent,
					resources.getString("question_bibletoremove"),
					resources.getString("question_bibletoremove_title"),
					JOptionPane.QUESTION_MESSAGE, null,
					getBibleNames(), null);

			// If a string was returned, say so.
			if (bibleToRemove != null)
			{
				bibleContainer.removeBible(bibleContainer.getBibleIdWithName(bibleToRemove));
				updateBibleContainer();
			}
		}
	}

	//*****************************************************
	// Private methods
	//*****************************************************

	/**
	 * Loads the bible xml file with the specified file name.
	 * @throws BiblePresenterException If the file is null, does not exist,
	 * or cannot be read.
	 */
	private void loadBible(File filename) throws BiblePresenterException
	{
		if (filename == null)
		{
			throw new BiblePresenterException("Filename must not be null");
		}
		if (!filename.exists() || !filename.isFile() || !filename.canRead())
		{
			throw new BiblePresenterException(
					"File does not exist or is not readable: " + filename);
		}

		try
		{
			// read file
			String id = filename.getName();
			id = id.substring(0, id.lastIndexOf("."));
			if (bibleContainer.hasBible(id))
			{
				if (bibleContainer.getBible(id).needsUpdate(
						filename.lastModified()))
				{
					addBible(id, filename);
				}
			}
			else
			{
				addBible(id, filename);
			}
		}
		catch (IOException e)
		{
			throw new BiblePresenterException(e.getMessage(), e);
		}
	}

	/**
	 * Reads the specified bible xml file.
	 * @param filename The bible xml file.
	 * @return Returns the bible xml file as xml document.
	 * @throws BiblePresenterException
	 */
	private Document readBible(File filename) throws BiblePresenterException
	{
		try
		{
			SAXBuilder builder = new SAXBuilder(false);
			return builder.build(filename);
		}
		catch (JDOMException e)
		{
			throw new BiblePresenterException(e.getMessage(), e);
		}
		catch (IOException e)
		{
			throw new BiblePresenterException(e.getMessage(), e);
		}
	}

	/**
	 * Adds a bible to the bible container.
	 * @param id The id of the bible.
	 * @param filename The filename of the bible.
	 * @throws BiblePresenterException
	 * @throws IOException
	 */
	private void addBible(String id, File filename)
			throws BiblePresenterException, IOException
	{
		Document document = readBible(filename);
		String name = document.getRootElement()
				.getAttributeValue(ATT_BIBLENAME);
		bibleContainer.addBible(id, new Bible(name, filename));
		cacheBooks(id, document);
		updateBibleContainer();
	}

	/**
	 * Splits a bible xml document up into its single books and stores
	 * these books as separate xml file in the caching directory.
	 * @param bibleId The id of the bible.
	 * @param document The complete bible xml document.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void cacheBooks(String bibleId, Document document)
			throws FileNotFoundException, IOException
	{
		// create a subdirectory for each bible
		File bibleDir = new File(cachingDir.toString() + File.separator
				+ bibleId);
		if (!bibleDir.exists())
		{
			bibleDir.mkdir();
		}

		List bibleBookList = document.getRootElement().getChildren(
				TAG_BIBLEBOOK);
		Format format = Format.getRawFormat();
		format.setEncoding("ISO-8859-1");
		XMLOutputter out = new XMLOutputter(format);
		for (Iterator bookIterator = bibleBookList.iterator(); bookIterator
				.hasNext();)
		{
			Element bookElement = (Element) bookIterator.next();
			String bookNumber = bookElement.getAttributeValue(ATT_BOOKNUMBER);
			String bookName = bookElement.getAttributeValue(ATT_BOOKNAME);
			String shortName = bookElement.getAttributeValue(ATT_SHORTNAME);

			File bookLocation = new File(bibleDir + File.separator + bookNumber
					+ XML_EXTENSION);

			Bible bible = bibleContainer.getBible(bibleId);
			Book book = new Book(bookNumber, bookName, shortName, bookLocation);
			bible.addBook(book);

			// analyse chapters
			List chapterList = bookElement.getChildren(TAG_CHAPTER);
			for (Iterator chapterIterator = chapterList.iterator(); chapterIterator
					.hasNext();)
			{
				Element chapterElement = (Element) chapterIterator.next();
				Chapter chapter = new Chapter(chapterElement
						.getAttributeValue(ATT_CHAPTERNUMBER));
				book.addChapter(chapter);

				// analyse verses
				List versList = chapterElement.getChildren(TAG_VERS);
				for (Iterator versIterator = versList.iterator(); versIterator
						.hasNext();)
				{
					Element versElement = (Element) versIterator.next();
					String vers = versElement.getAttributeValue(ATT_VERSNUMBER);
					chapter.addVersNumber(vers);
				}
			}

			// write book to caching directory
			Element newBookElement = (Element) bookElement.clone();
			Document bookDoc = new Document(newBookElement);
			FileOutputStream outStream = new FileOutputStream(bookLocation);
			out.output(bookDoc, outStream);
		}
	}

	/**
	 * Reads the bible container stored on hard disk.
	 * @return Returns true if the bible container could be read, else false.
	 */
	private boolean createBibleContainer()
	{
		this.bibleContainer = null;

		try
		{
			FileInputStream fis = new FileInputStream(cachingDir
					+ File.separator + BIBLECONTAINER);
			ObjectInputStream objIs = new ObjectInputStream(fis);
			bibleContainer = (BibleContainer) objIs.readObject();
			fis.close();
			return true;
		}
		catch (NotSerializableException se)
		{
			return false;
		}
		catch (FileNotFoundException fe)
		{
			return false;
		}
		catch (IOException se)
		{
			return false;
		}
		catch (ClassNotFoundException ce)
		{
			return false;
		}
	}

	/**
	 * Writes the bible container to the hard disk in the caching directory.
	 * @throws BiblePresenterException
	 */
	public void updateBibleContainer() throws BiblePresenterException
	{

		try
		{
			FileOutputStream fos = new FileOutputStream(cachingDir
					+ File.separator + BIBLECONTAINER);
			ObjectOutputStream objOs = new ObjectOutputStream(fos);
			objOs.writeObject(bibleContainer);
			objOs.flush();
			fos.close();
		}
		catch (NotSerializableException se)
		{
			throw new BiblePresenterException(se.getMessage(), se);
		}
		catch (FileNotFoundException fe)
		{
			throw new BiblePresenterException(fe.getMessage(), fe);
		}
		catch (IOException se)
		{
			throw new BiblePresenterException(se.getMessage(), se);
		}
	}

	/**
	 * Loads a bible book from the caching directory.
	 * @throws BiblePresenterException
	 */
	private void loadBook() throws BiblePresenterException
	{
		Book book = bibleContainer.getBibleWithName(currentBibleName).getBook(
				currentBookName);
		File location = book.getLocation();
		SAXBuilder builder = new SAXBuilder(false);
		Document document = null;
		try
		{
			document = builder.build(location);
		}
		catch (JDOMException e)
		{
			throw new BiblePresenterException(e.getMessage(), e);
		}
		catch (IOException e)
		{
			throw new BiblePresenterException(e.getMessage(), e);
		}
		this.currentBookChapters = document.getRootElement().getChildren(
				TAG_CHAPTER);
	}

	/**
	 * Returns the currently set bible location in the form
	 * <pre><bible name> - <book name> <chapter number>, <start vers number> [- <end vers number>]
	 * @return Returns the currently set bible location.
	 */
	public String toString()
	{
		StringBuilder newTitle = new StringBuilder();
		newTitle.append(getCurrentBookName());
		newTitle.append(" ");
		newTitle.append(getCurrentChapterNumber());
		newTitle.append(":");
		String startVers = getCurrentStartVersNumber();
		newTitle.append(startVers);
		String endVers = getCurrentEndVersNumber();
		if (!endVers.equals(startVers))
		{
			newTitle.append("-");
			newTitle.append(endVers);
		}
		// newTitle.append(TITLE_DELIMITER);
		newTitle.append(" (");
		newTitle.append(getCurrentBibleName());
		newTitle.append(")");
		return newTitle.toString();
	}

	//**********************************************************
	// Public variables
	//**********************************************************


	//*****************************************************
	// Private variables
	//*****************************************************

	private BibleContainer bibleContainer;
	private ResourceBundle resources;
	private Preferences preferences;
	private File cachingDir;
	private String currentBibleName;
	private String currentBookName;
	private String currentChapterNumber;
	private String currentStartVersNumber;
	private String currentEndVersNumber;
	private List currentBookChapters;
	private static final String BIBLESCHEMA = "etc/bibles/xmlbible_pre_1008.xsd";
	private static final String BIBLECONTAINER = "biblecontainer";
	private static final String XML_EXTENSION = ".xml";
	private static final String ATT_BIBLENAME = "biblename";
	private static final String TAG_BIBLEBOOK = "BIBLEBOOK";
	private static final String ATT_BOOKNUMBER = "bnumber";
	private static final String ATT_BOOKNAME = "bname";
	private static final String ATT_SHORTNAME = "bsname";
	private static final String TAG_CHAPTER = "CHAPTER";
	private static final String ATT_CHAPTERNUMBER = "cnumber";
	private static final String TAG_VERS = "VERS";
	private static final String ATT_VERSNUMBER = "vnumber";
	private static final String TITLE_DELIMITER = " - ";
}
