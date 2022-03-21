/*
 * Created on Apr 7, 2004
 * $Id: Book.java,v 1.3 2004/04/27 18:55:49 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Represents a bible book and holds its chapters.
 * @author Ulrich Rueth
 */
public class Book implements Serializable
{
	//**********************************************************
	// Constructors
	//**********************************************************
	
	/**
	 * Empty constructor.
	 */
	public Book()
	{
		this.chapters = new LinkedHashMap();
	}
	
	/**
	 * Constructor.
	 * @param number The book number.
	 * @param name The book name (e.g. 2 Petrus)
	 * @param shortName The short name of the book (e.g. 2 Pe)
	 * @param location The location of the book in the book cache.
	 */
	public Book(String number, String name, String shortName, File location)
	{
		this();
		this.number = number;
		this.name = name;
		this.shortName = shortName;
		this.location = location;
	}
	
	//**********************************************************
	// Public methods
	//**********************************************************
	
	/**
	 * @return Returns the chapters of this book.
	 */
	public Chapter[] getChapters()
	{
		Set allChaptersSet = chapters.entrySet();
		Chapter[] retChapters = new Chapter[allChaptersSet.size()];
		Object ignore = allChaptersSet.toArray(retChapters);
		return retChapters;
	}
	
	/**
	 * @param chapterNumber The chapter number.
	 * @return Returns the specified chapter.
	 */
	public Chapter getChapter(String chapterNumber)
	{
		return (Chapter)chapters.get(chapterNumber);
	}
	
	/**
	 * Adds a new chapter to the book.
	 * @param newChapter The new chapter.
	 */
	public void addChapter(Chapter newChapter)
	{
		chapters.put(newChapter.getNumber(), newChapter);
	}
	
	/**
	 * @param chapterNumber The chapter number.
	 * @return Returns true if the book contains the specified chapter.
	 */
	public boolean hasChapter(String chapterNumber)
	{
		return chapters.containsKey(chapterNumber);
	}
	
	/**
	 * @return Returns all chapter numbers of the book.
	 */
	public String[] getChapterNumbers()
	{
		Set chapterNumbersSet = chapters.keySet();
		String[] chapterNumbers = new String[chapterNumbersSet.size()];
		Object ignore = chapterNumbersSet.toArray(chapterNumbers);
		return chapterNumbers;
	}
	
	/**
	 * @return Returns the name of the book.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the name of the book.
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @return Returns the number of the book.
	 */
	public String getNumber()
	{
		return number;
	}
	
	/**
	 * Sets the number of the book.
	 * @param number The number to set.
	 */
	public void setNumber(String number)
	{
		this.number = number;
	}
	
	/**
	 * @return Returns the short name of the book.
	 */
	public String getShortName()
	{
		return shortName;
	}
	
	/**
	 * Sets the short name of the book.
	 * @param shortName The short name to set.
	 */
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	/**
	 * @return Returns the location of the book in the books' cache.
	 */
	public File getLocation()
	{
		return location;
	}

	/**
	 * Sets the location of the book in the books' cache.
	 * @param location The location of the book in the books' cache.
	 */
	public void setLocation(File location)
	{
		this.location = location;
	}
	
	//**********************************************************
	// Private variables
	//**********************************************************
	private LinkedHashMap chapters;
	private String number;
	private String name;
	private String shortName;
	private File location;
}
