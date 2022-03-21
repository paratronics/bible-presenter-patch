/*
 * Created on Apr 6, 2004
 * $Id: BiblePresenterException.java,v 1.4 2004/04/28 06:59:20 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter;

/**
 * @author Ulrich Rueth
 */
public class BiblePresenterException extends Exception
{
	//**********************************************************
	// Constructors
	//**********************************************************
	/**
	 * @see Exception#Exception()
	 */
	public BiblePresenterException()
	{
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public BiblePresenterException(String arg0)
	{
		super(arg0);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public BiblePresenterException(Throwable arg0)
	{
		super(arg0);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public BiblePresenterException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

}
