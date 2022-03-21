/*
 * Created on Apr 30, 2004 $Id: Presenter.java,v 1.1 2004/04/30 14:36:08 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import info.rueth.biblepresenter.BiblePresenterException;


/**
 * The interface for the presenter windows.
 * @author Ulrich Rueth
 */
public interface Presenter
{
	/**
	 * Updates the content of the presenter window using the data setter values
	 * for the selected bible location.
	 */
	void updateContent() throws BiblePresenterException;
	
	/**
	 * @see java.awt.Component#isVisible()
	 */
	boolean isVisible();
	
	/**
	 * @see java.awt.Component#setVisible(boolean)
	 */
	void setVisible(boolean visible);
}