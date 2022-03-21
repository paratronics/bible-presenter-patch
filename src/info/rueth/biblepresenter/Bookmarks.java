/*
 * Created on Apr 18, 2004
 * $Id: Bookmarks.java,v 1.3 2004/04/27 19:10:04 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a folder containing bookmarks and/or other bookmark folders.
 * This allows to build up a hierarchical tree of bookmarks.
 * @author Ulrich Rueth
 */
public class Bookmarks implements Serializable
{
	//**********************************************************
	// Constructors
	//**********************************************************
	
	/**
	 * Constructor.
	 * @param title The title of the bookmarks folder.
	 */
	public Bookmarks(String title)
	{
		this.title = title;
		this.bookmarks = new ArrayList();
	}
	
	//**********************************************************
	// Public methods
	//**********************************************************
	
	/**
	 * Adds a new bookmark object to the bookmark folder.
	 * @param bookmarkObject The bookmarkObject (a Bookmark or a Bookmarks object) to add.
	 * @throws BiblePresenterException If the bookmarkObject is not either
	 * an instance of Bookmark or Bookmarks. 
	 */
	public void addBookmark(Object bookmarkObject)
	throws BiblePresenterException
	{
		if (!(bookmarkObject instanceof Bookmark)
				&& !(bookmarkObject instanceof Bookmarks))
		{
			throw new BiblePresenterException("Only Bookmark or Bookmarks objects allowed.");
		}
		bookmarks.add(bookmarkObject);
	}
	
	/**
	 * Adds a new bookmark object to the bookmark folder at the specified position.
	 * @param position The position where to insert the bookmark.
	 * @param bookmarkObject The bookmarkObject (a Bookmark or a Bookmarks object) to add.
	 * @throws BiblePresenterException If the bookmarkObject is not either
	 * an instance of Bookmark or Bookmarks.
	 */
	public void addBookmark(int position, Object bookmarkObject)
	throws BiblePresenterException
	{
		if (!(bookmarkObject instanceof Bookmark)
				&& !(bookmarkObject instanceof Bookmarks))
		{
			throw new BiblePresenterException("Only Bookmark or Bookmarks objects allowed.");
		}
		bookmarks.add(position, bookmarkObject);
	}
	
	/**
	 * Moves the specified bookmarkObject up in the list.
	 * @param i The position of the bookmarkObject to move.
	 */
	public void moveUp(int i)
	{
		if (i > 0 && i < bookmarks.size())
		{
			Object bookmarkObject = bookmarks.remove(i);
			bookmarks.add(i-1, bookmarkObject);
		}
	}
	
	/**
	 * Moves the specified bookmarkObject down in the list.
	 * @param i The position of the bookmarkObject to move.
	 */
	public void moveDown(int i)
	{
		if (i < bookmarks.size() - 1)
		{
			Object bookmarkObject = bookmarks.remove(i);
			bookmarks.add(i+1, bookmarkObject);
		}
	}

	/**
	 * Removes the specified bookmarkObject.
	 * @param i The position of the bookmarkObject to remove.
	 */
	public void remove(int i)
	{
		bookmarks.remove(i);
	}
	
	/**
	 * @return Returns a list of all bookmarkObjects.
	 */
	public ArrayList getBookmarks()
	{
		return bookmarks;
	}
	
	/**
	 * @return Returns the title of the bookmark folder.
	 */
	public String toString()
	{
		return title;
	}
	
	/**
	 * Removes bookmarks where the bible does no longer exist.
	 * @param bibleNames An array of valid bible names.
	 */
	public void removeInvalidBookmarks(String[] bibleNames)
	{
		ArrayList removeUs = new ArrayList();
		for (Iterator i = bookmarks.iterator(); i.hasNext(); )
		{
			Object bookmarkObject = i.next();
			if (bookmarkObject instanceof Bookmarks)
			{
				((Bookmarks)bookmarkObject).removeInvalidBookmarks(bibleNames);
			}
			else
			{
				String bibleName = ((Bookmark)bookmarkObject).getBible();
				boolean valid = false;
				for (int j = 0; j < bibleNames.length; j++)
				{
					if (bibleNames[j].equals(bibleName)) valid = true;
				}
				if (!valid) removeUs.add(bookmarkObject);
			}
		}
		bookmarks.removeAll(removeUs);
	}
	
	//**********************************************************
	// Private variables
	//**********************************************************
	
	private String title;
	private ArrayList bookmarks;
}
