/*
 * Created on Apr 11, 2004
 * $Id: Vers.java,v 1.2 2004/04/27 19:31:57 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter;

/**
 * Represents a vers of a bible chapter.
 * @author Ulrich Rueth
 */
public class Vers
{
	//*****************************************************
	// Constructors
	//*****************************************************

	/**
	 * Constructor.
	 * @param versNumber The vers number.
	 * @param vers The vers.
	 */
	public Vers(String versNumber, String vers)
	{
		this.versNumber = versNumber;
		this.vers = vers;
	}
	
	//*****************************************************
	// Private variables
	//*****************************************************

	/**
	 * @return Returns the vers.
	 */
	public String getVers()
	{
		return vers;
	}

	/**
	 * @return Returns the vers number.
	 */
	public String getVersNumber()
	{
		return versNumber;
	}

	//*****************************************************
	// Private variables
	//*****************************************************

	private String versNumber;
	private String vers;
}
