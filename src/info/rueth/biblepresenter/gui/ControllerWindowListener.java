/*
 * Created on Apr 14, 2004
 * $Id: ControllerWindowListener.java,v 1.4 2004/04/28 07:31:26 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import info.rueth.biblepresenter.BiblePresenterException;
import info.rueth.biblepresenter.Preferences;

/**
 * Handles all window events of the controller window.
 * @author Ulrich Rueth
 */
public class ControllerWindowListener extends WindowAdapter
{
	//**********************************************************
	// Constructors
	//**********************************************************
	
	/**
	 * Constructor.
	 * @param controllerWindow The controller window.
	 * @param dataSetter The data setter.
	 * @param preferences The user preferences.
	 */
	public ControllerWindowListener(ControllerWindow controllerWindow, DataSetter dataSetter, Preferences preferences)
	{
		super();
		this.controllerWindow = controllerWindow;
		this.dataSetter = dataSetter;
		this.preferences = preferences;
	}
	
	//**********************************************************
	// Public methods
	//**********************************************************
	
	/**
	 * @see java.awt.event.WindowListener#windowClosing(WindowEvent)
	 */
	public void windowClosing(WindowEvent event)
	{
		if (preferences.isSavePositionOnExit())
		{
			preferences.setCurrentBible(dataSetter.getCurrentBibleName());
			preferences.setCurrentBook(dataSetter.getCurrentBookName());
			preferences.setCurrentChapter(dataSetter.getCurrentChapterNumber());
			preferences.setCurrentStartVers(dataSetter.getCurrentStartVersNumber());
			preferences.setCurrentEndVers(dataSetter.getCurrentEndVersNumber());
		}
		else
		{
			preferences.setCurrentBible(null);
			preferences.setCurrentBook(null);
			preferences.setCurrentChapter(null);
			preferences.setCurrentStartVers(null);
			preferences.setCurrentEndVers(null);
		}
		try
		{
			preferences.save();
		}
		catch (BiblePresenterException e)
		{
			e.printStackTrace();
			GUILauncher.displayErrorMessage(controllerWindow, e.getMessage());
		}
	}
	
	//**********************************************************
	// Private variables
	//**********************************************************
	
	private DataSetter dataSetter;
	private Preferences preferences;
	private ControllerWindow controllerWindow;
}
