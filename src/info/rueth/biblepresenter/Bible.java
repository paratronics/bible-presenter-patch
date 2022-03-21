/*
 * Created on Apr 7, 2004
 * $Id: Bible.java,v 1.4 2004/04/27 19:35:28 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Represents a bible which can hold bible books.
 * @author Ulrich Rueth
 */
public class Bible implements Serializable
{
	//**********************************************************
	// Constructors
	//**********************************************************
	
	/**
	 * Empty constructor.
	 */
	public Bible()
	{
		this.books = new LinkedHashMap();
	}
	
	/**
	 * Constructor.
	 * @param name The name of the bible.
	 * @param location The file location of the bible xml file.
	 */
	public Bible(String name, File location)
	{
		this();
		this.name = filterXMLSuffix(name);
		this.lastModified = location.lastModified();
	}
	
	//**********************************************************
	// Public methods
	//**********************************************************
	
	/**
	 * Checks whether the last stored bible is older that the current one.
	 * @param currentModified The age of the bible file to be compared with.
	 * @return Returns true if the current bible is newer that the stored one.
	 */
	public boolean needsUpdate(long currentModified)
	{
		return currentModified > lastModified;
	}
	
	/**
	 * @return Returns the bible name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return Returns the location of the bible.
	 */
	private File getLocation()
	{
		return location;
	}
	
	/**
	 * @return Returns all books of this bible.
	 */
	public Book[] getBooks()
	{
		Set allBooksSet = books.entrySet();
		Book[] retBooks = new Book[allBooksSet.size()];
		Object ignore = allBooksSet.toArray(retBooks);
		return retBooks;
	}
	
	/**
	 * @param bookName The name of the book to check.
	 * @return Returns true if this book is in the bible.
	 */
	public boolean hasBook(String bookName)
	{
		return books.containsKey(bookName);
	}
	
	/**
	 * @return Returns all book names of this bible.
	 */
	public String[] getBookNames()
	{
		Set bookNamesSet = books.keySet();
		String[] bookNames = new String[bookNamesSet.size()];
		Object ignore = bookNamesSet.toArray(bookNames);
		return bookNames;
	}
	
	/**
	 * @param bookName The name of the book to retrieve.
	 * @return Returns the book with the specified name.
	 */
	public Book getBook(String bookName)
	{
		return (Book)books.get(bookName);
	}
	
	/**
	 * Adds a new book to the bible.
	 * @param newBook The book to add.
	 */
	public void addBook(Book newBook)
	{
		books.put(newBook.getName(), newBook);
	}
	
	//**********************************************************
	// Private methods
	//**********************************************************
	
	/**
	 * Removes the " XML" suffix from the bibles in Zefania bible format.
	 */
	private String filterXMLSuffix(String name)
	{
		int suffixPosition = name.lastIndexOf(" XML");
		if (suffixPosition > 0)
		{
			return name.substring(0, suffixPosition);
		}
		else
		{
			return name;
		}
	}
	
	//**********************************************************
	// Private variables
	//**********************************************************
	
	private String name;
	private File location;
	private long lastModified;
	private LinkedHashMap books;
}
