/*
 * Created on Apr 7, 2004
 * $Id: Chapter.java,v 1.3 2004/04/27 19:13:37 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter;

import java.io.Serializable;
import java.util.Vector;

/**
 * Represents a bible book's chapter.
 * @author Ulrich Rueth
 */
public class Chapter implements Serializable
{
	//**********************************************************
	// Constructors
	//**********************************************************
	
	/**
	 * Empty constructor.
	 */
	public Chapter()
	{
		this.versNumbers = new Vector();
	}
	
	/**
	 * Constructor.
	 * @param number The chapter number.
	 */
	public Chapter(String number)
	{
		this();
		this.number = number;
	}
	
	//**********************************************************
	// Public methods
	//**********************************************************
	
	/**
	 * @return Returns the vers numbers of this chapter.
	 */
	public String[] getVersNumbers()
	{
		String[] retVerses = new String[versNumbers.size()];
		versNumbers.copyInto(retVerses);
		return retVerses;
	}
	
	/**
	 * Sets a compete new set of vers numbers, all existing vers numbers
	 * are lost.
	 * @param newVersNumbers The new set of vers numbers.
	 */
	public void setVersNumbers(String[] newVersNumbers)
	{
		versNumbers = new Vector();
		for (int i = 0; i < newVersNumbers.length; i++)
		{
			versNumbers.addElement(newVersNumbers[i]);
		}
	}
	
	/**
	 * Appends a vers number.
	 * @param versNumber The vers number to append.
	 */
	public void addVersNumber(String versNumber)
	{
		versNumbers.addElement(versNumber);
	}
	
	/**
	 * @param versNumber The vers number to verify.
	 * @return Returns true if the chapter contains the specified vers number.
	 */
	public boolean hasVersNumber(String versNumber)
	{
		for (int i = 0; i < versNumbers.size(); i++)
		{
			if (versNumbers.get(i).equals(versNumber))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return Returns the chapter number.
	 */
	public String getNumber()
	{
		return number;
	}
	
	//**********************************************************
	// Private variables
	//**********************************************************
	
	private Vector versNumbers;
	private String number;
}
