/*
 * Created on Apr 12, 2004 $Id: Preferences.java,v 1.10 2004/05/01 22:05:17
 * Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter;


import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;


/**
 * Stores the user's preferences.
 * 
 * @author Ulrich Rueth
 */
public class Preferences implements Serializable
{
	//**********************************************************
	// Constructors
	//**********************************************************

	/**
	 * Constructor.
	 * 
	 * @param prefFile
	 *            The file where the preferences are stored.
	 */
	public Preferences(File prefFile)
	{
		this.prefFile = prefFile;
		this.bookmarks = new Bookmarks("dummy");
	}

	//**********************************************************
	// Public methods
	//**********************************************************

	/**
	 * @return Returns the backgroundColor.
	 */
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	/**
	 * @param backgroundColor
	 *            The backgroundColor to set.
	 */
	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}

	public Integer[] getFontSizes()
	{
		return fontSizes;
	}

	/**
	 * @return Returns the font color of the verses in the presenter window.
	 */
	public Color getVersColor()
	{
		return versColor;
	}

	/**
	 * Sets the font color of the verses.
	 * 
	 * @param versColor
	 *            The verses' font color.
	 */
	public void setVersColor(Color versColor)
	{
		this.versColor = versColor;
	}

	/**
	 * @return Returns the font name of the verses.
	 */
	public String getVersFontName()
	{
		return versFontName;
	}

	/**
	 * Sets the verses' font name.
	 * 
	 * @param versFontName
	 *            The verses' font name.
	 */
	public void setVersFontName(String versFontName)
	{
		this.versFontName = versFontName;
	}

	/**
	 * @return Returns the verses' font size.
	 */
	public Integer getVersFontSize()
	{
		return versFontSize;
	}

	/**
	 * Sets the verses' font size.
	 * 
	 * @param versFontSize
	 *            The verses' font size.
	 */
	public void setVersFontSize(Integer versFontSize)
	{
		this.versFontSize = versFontSize;
	}

	/**
	 * @return Returns the vers numbers' font color.
	 */
	public Color getVersNumberColor()
	{
		return versNumberColor;
	}

	/**
	 * Sets the vers numbers' font color.
	 * 
	 * @param versNumberColor
	 *            The vers numbers' font color.
	 */
	public void setVersNumberColor(Color versNumberColor)
	{
		this.versNumberColor = versNumberColor;
	}

	/**
	 * @return Returns the vers numbers' font name.
	 */
	public String getVersNumberFontName()
	{
		return versNumberFontName;
	}

	/**
	 * Sets the vers numbers' font name.
	 * 
	 * @param versNumberFontName
	 *            The vers numbers' font name.
	 */
	public void setVersNumberFontName(String versNumberFontName)
	{
		this.versNumberFontName = versNumberFontName;
	}

	/**
	 * @return Returns the vers numbers' font size.
	 */
	public Integer getVersNumberFontSize()
	{
		return versNumberFontSize;
	}

	/**
	 * Sets the vers numbers' font size.
	 * 
	 * @param versNumberFontSize
	 *            The vers numbers' font size.
	 */
	public void setVersNumberFontSize(Integer versNumberFontSize)
	{
		this.versNumberFontSize = versNumberFontSize;
	}

	/**
	 * @return Returns the default offset between the start and the end vers.
	 */
	public int getVersOffset()
	{
		return versOffset;
	}

	/**
	 * Sets the default offset between the start and the end vers.
	 * 
	 * @param versOffset
	 *            The default offset between the start and the end vers.
	 */
	public void setVersOffset(int versOffset)
	{
		this.versOffset = versOffset;
	}

	/**
	 * @return Returns the user interface languages.
	 */
	public String[] getLanguages()
	{
		return languages;
	}

	/**
	 * @return Returns the currently selected language.
	 */
	public String getLanguage()
	{
		for (int i = 0; i < locales.length; i++)
		{
			if (locale.equals(locales[i]))
			{
				return languages[i];
			}
		}
		return "";
	}

	/**
	 * Sets the user interface language.
	 * 
	 * @param language
	 *            The user interface language.
	 */
	public void setLanguage(String language)
	{
		for (int i = 0; i < languages.length; i++)
		{
			if (languages[i].equals(language))
			{
				locale = locales[i];
			}
		}
	}

	/**
	 * @return Returns the locale string for the selected language.
	 */
	public Locale getLocale()
	{
		return locale;
	}

	/**
	 * @return Returns true if the superscript option for the vers numbers is
	 *         chosen.
	 */
	public boolean isVersNumberSuperscript()
	{
		return versNumberSuperscript;
	}

	/**
	 * Sets the superscript option for the vers numbers.
	 * 
	 * @param versNumberSuperscript
	 *            The superscript option for the vers numbers.
	 */
	public void setVersNumberSuperscript(boolean versNumberSuperscript)
	{
		this.versNumberSuperscript = versNumberSuperscript;
	}

	/**
	 * @return Returns the biblePositionColor.
	 */
	public Color getBiblePositionColor()
	{
		return biblePositionColor;
	}

	/**
	 * @param biblePositionColor
	 *            The biblePositionColor to set.
	 */
	public void setBiblePositionColor(Color biblePositionColor)
	{
		this.biblePositionColor = biblePositionColor;
	}

	/**
	 * @return Returns the biblePositionFontName.
	 */
	public String getBiblePositionFontName()
	{
		return biblePositionFontName;
	}

	/**
	 * @param biblePositionFontName
	 *            The biblePositionFontName to set.
	 */
	public void setBiblePositionFontName(String biblePositionFontName)
	{
		this.biblePositionFontName = biblePositionFontName;
	}

	/**
	 * @return Returns the biblePositionFontSize.
	 */
	public Integer getBiblePositionFontSize()
	{
		return biblePositionFontSize;
	}

	/**
	 * @param biblePositionFontSize
	 *            The biblePositionFontSize to set.
	 */
	public void setBiblePositionFontSize(Integer biblePositionFontSize)
	{
		this.biblePositionFontSize = biblePositionFontSize;
	}

	/**
	 * @return Returns the displayBiblePosition.
	 */
	public boolean isDisplayBiblePosition()
	{
		return displayBiblePosition;
	}

	/**
	 * @param displayBiblePosition
	 *            The displayBiblePosition to set.
	 */
	public void setDisplayBiblePosition(boolean displayBiblePosition)
	{
		this.displayBiblePosition = displayBiblePosition;
	}

	/**
	 * @return Returns the lineSpacing.
	 */
	public Float[] getLineSpacings()
	{
		return lineSpacings;
	}

	/**
	 * @return Returns the lineSpacing.
	 */
	public Float getLineSpacing()
	{
		return lineSpacing;
	}

	/**
	 * @param lineSpacing
	 *            The lineSpacing to set.
	 */
	public void setLineSpacing(Float lineSpacing)
	{
		this.lineSpacing = lineSpacing;
	}

	/**
	 * @return Returns true if the bible text should be fit to the presenter
	 *         window if it gets longer than the window size.
	 */
	public boolean isFitTextToWindow()
	{
		return fitTextToWindow;
	}

	/**
	 * @param fitTextToWindow
	 *            The fitTextToWindow to set.
	 */
	public void setFitTextToWindow(boolean fitTextToWindow)
	{
		this.fitTextToWindow = fitTextToWindow;
	}

	/**
	 * @return Returns true if the current bible position should be remembered
	 *         for the next program start.
	 */
	public boolean isSavePositionOnExit()
	{
		return savePositionOnExit;
	}

	/**
	 * Sets if the current bible position should be remembered for the next
	 * program start.
	 * 
	 * @param savePositionOnExit
	 *            If the current bible position should be remembered for the
	 *            next program start.
	 */
	public void setSavePositionOnExit(boolean savePositionOnExit)
	{
		this.savePositionOnExit = savePositionOnExit;
	}

	/**
	 * @return Returns true if a bible name is set in the preferences.
	 */
	public boolean hasCurrentBible()
	{
		return currentBible != null;
	}

	/**
	 * @return Returns the currently set bible name.
	 */
	public String getCurrentBible()
	{
		return currentBible;
	}

	/**
	 * Sets the current bible name.
	 * 
	 * @param currentBible
	 *            The current bible name to set.
	 */
	public void setCurrentBible(String currentBible)
	{
		this.currentBible = currentBible;
	}

	/**
	 * @return Returns true if a book name is set in the preferences.
	 */
	public boolean hasCurrentBook()
	{
		return currentBook != null;
	}

	/**
	 * @return Returns the currently set book name.
	 */
	public String getCurrentBook()
	{
		return currentBook;
	}

	/**
	 * Sets the current book name.
	 * 
	 * @param currentBook
	 *            The current book name to set.
	 */
	public void setCurrentBook(String currentBook)
	{
		this.currentBook = currentBook;
	}

	/**
	 * @return Returns true if a chapter number is set in the preferences.
	 */
	public boolean hasCurrentChapter()
	{
		return currentChapter != null;
	}

	/**
	 * @return Returns the currently set chapter number.
	 */
	public String getCurrentChapter()
	{
		return currentChapter;
	}

	/**
	 * Sets the current chapter number.
	 * 
	 * @param currentChapter
	 *            The current chapter number to set.
	 */
	public void setCurrentChapter(String currentChapter)
	{
		this.currentChapter = currentChapter;
	}

	/**
	 * @return Returns true if a start vers is set in the preferences.
	 */
	public boolean hasCurrentEndVers()
	{
		return currentEndVers != null;
	}

	/**
	 * @return Returns the currently set start vers number.
	 */
	public String getCurrentEndVers()
	{
		return currentEndVers;
	}

	/**
	 * Sets the current end vers number.
	 * 
	 * @param currentEndVers
	 *            The current end vers number to set.
	 */
	public void setCurrentEndVers(String currentEndVers)
	{
		this.currentEndVers = currentEndVers;
	}

	/**
	 * @return Returns true if a end vers is set in the preferences.
	 */
	public boolean hasCurrentStartVers()
	{
		return currentStartVers != null;
	}

	/**
	 * @return Returns the currently set end vers number.
	 */
	public String getCurrentStartVers()
	{
		return currentStartVers;
	}

	/**
	 * Sets the current end vers number.
	 * 
	 * @param currentStartVers
	 *            The current end vers number to set.
	 */
	public void setCurrentStartVers(String currentStartVers)
	{
		this.currentStartVers = currentStartVers;
	}

	/**
	 * @return Returns the bookmarks.
	 */
	public Bookmarks getBookmarks()
	{
		return bookmarks;
	}

	/**
	 * Sets the bookmarks.
	 * 
	 * @param bookmarks
	 *            The bookmarks to set.
	 */
	public void setBookmarks(Bookmarks bookmarks)
	{
		this.bookmarks = bookmarks;
	}

	/**
	 * @return Returns the directory where a bible was imported from last time.
	 */
	public String getLatestBibleDirectory()
	{
		return latestBibleDirectory;
	}

	/**
	 * @param latestBibleDirectory
	 *            The directory where a bible was imported from last time.
	 */
	public void setLatestBibleDirectory(String latestBibleDirectory)
	{
		this.latestBibleDirectory = latestBibleDirectory;
	}

	/**
	 * @return Returns the displayOnSecondaryScreenDevice.
	 */
	public boolean isDisplayOnSecondaryScreenDevice()
	{
		return displayOnSecondaryScreenDevice;
	}

	/**
	 * @param displayOnSecondaryScreenDevice
	 *            The displayOnSecondaryScreenDevice to set.
	 */
	public void setDisplayOnSecondaryScreenDevice(
			boolean displayOnSecondaryScreenDevice)
	{
		this.displayOnSecondaryScreenDevice = displayOnSecondaryScreenDevice;
	}

	/**
	 * Saves the preferences.
	 * 
	 * @throws BiblePresenterException
	 */
	public void save() throws BiblePresenterException
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(prefFile);
			ObjectOutputStream objOs = new ObjectOutputStream(fos);
			objOs.writeObject(this);
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

	//**********************************************************
	// Private variables
	//**********************************************************

	private File prefFile;

	private int versOffset = 2;

	private Locale locale = Locale.ENGLISH;
	private Locale[] locales = new Locale[]
	{Locale.ENGLISH, Locale.GERMAN};
	private String[] languages = new String[]
	{"English", "Deutsch"};

	private Float[] lineSpacings = new Float[]
	{new Float(1.0f), new Float(1.5f), new Float(2.0f), new Float(2.5f),
			new Float(3.0f)};
	private Float lineSpacing = lineSpacings[1];

	private Color backgroundColor = new Color(0, 0, 102); // very dark blue

	private Integer[] fontSizes = new Integer[]
	{new Integer(8), new Integer(10), new Integer(12), new Integer(14),
			new Integer(16), new Integer(18), new Integer(20), new Integer(24),
			new Integer(28), new Integer(32), new Integer(36), new Integer(40),
			new Integer(44), new Integer(48), new Integer(52), new Integer(56),
			new Integer(60), new Integer(64)};

	private Color versColor = Color.YELLOW;
	private Integer versFontSize = fontSizes[10]; // font size 36
	private String versFontName = "Arial";

	private Color versNumberColor = Color.ORANGE;
	private Integer versNumberFontSize = fontSizes[7]; // font size 24
	private String versNumberFontName = "Arial";
	private boolean versNumberSuperscript = true;

	private Color biblePositionColor = Color.WHITE;
	private Integer biblePositionFontSize = fontSizes[6]; // font size 20
	private String biblePositionFontName = "Arial";
	private boolean displayBiblePosition = true;

	private boolean fitTextToWindow = true;

	private boolean savePositionOnExit = true;

	private String currentBible;
	private String currentBook;
	private String currentChapter;
	private String currentStartVers;
	private String currentEndVers;

	private String latestBibleDirectory;

	private boolean displayOnSecondaryScreenDevice = true;

	private Bookmarks bookmarks;
}