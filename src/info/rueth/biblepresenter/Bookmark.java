/*
 * Created on Apr 18, 2004
 * $Id: Bookmark.java,v 1.2 2004/04/27 18:59:22 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter;

import java.io.Serializable;

/**
 * A users' bookmark.
 * @author Ulrich Rueth
 */
public class Bookmark implements Serializable
{
	//**********************************************************
	// Constructors
	//**********************************************************
	
	/**
	 * Constructor.
	 * @param bible The bible name.
	 * @param book The book name.
	 * @param chapter The chapter number.
	 * @param startVers The start vers number.
	 * @param endVers The end vers number.
	 */
	public Bookmark(String bible, String book, String chapter, String startVers, String endVers)
	{
		this.bible = bible;
		this.book = book;
		this.chapter = chapter;
		this.startVers = startVers;
		this.endVers = endVers;
	}
	
	//**********************************************************
	// Public methods
	//**********************************************************
	
	/**
	 * @return Returns the bible.
	 */
	public String getBible()
	{
		return bible;
	}
	/**
	 * @return Returns the book.
	 */
	public String getBook()
	{
		return book;
	}
	/**
	 * @return Returns the chapter number.
	 */
	public String getChapter()
	{
		return chapter;
	}
	/**
	 * @return Returns the endVers number.
	 */
	public String getEndVers()
	{
		return endVers;
	}
	/**
	 * @return Returns the startVers number.
	 */
	public String getStartVers()
	{
		return startVers;
	}
	
	/**
	 * @return Returns the bookmark as a string in the form of:
	 * <div><bible name> - <book name> <chapter number>, <start vers number> [- <end vers number>]</div>
	 */
	public String toString()
	{
		StringBuffer retString = new StringBuffer(bible);
		retString.append(" - ");
		retString.append(book);
		retString.append(" ");
		retString.append(chapter);
		retString.append(", ");
		retString.append(startVers);
		if (!endVers.equals(startVers))
		{
			retString.append("-");
			retString.append(endVers);
		}
		return retString.toString();
	}
	
	//**********************************************************
	// Private variables
	//**********************************************************
	
	private String bible;
	private String book;
	private String chapter;
	private String startVers;
	private String endVers;
}
