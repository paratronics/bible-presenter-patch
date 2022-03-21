/*
 * Created on Apr 8, 2004
 * $Id: ComboboxActionHandler.java,v 1.7 2004/04/30 14:36:07 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import info.rueth.biblepresenter.BiblePresenterException;

/**
 * Handles all events on the bible location selection combo boxes.
 * @author Ulrich Rueth
 */
public class ComboboxActionHandler implements ActionListener
{
	//*****************************************************
	// Constructors
	//*****************************************************
	
	/**
	 * Constructor.
	 * @param controllerWindow The controller window.
	 */
	public ComboboxActionHandler(ControllerWindow controllerWindow)
	{
		this.controllerWindow = controllerWindow;
	}
	
	//*****************************************************
	// Public methods
	//*****************************************************
	
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent event)
	{
        try
		{
			JComboBox cb = (JComboBox)event.getSource();
			String cbActionCommand = cb.getActionCommand();

			if (cbActionCommand.equals(BIBLE))
			{
				controllerWindow.updateBooks();
			}
			else if (cbActionCommand.equals(BOOK))
			{
				controllerWindow.updateChapters();
			}
			else if (cbActionCommand.equals(CHAPTER))
			{
				controllerWindow.updateVerses();
			}
			else if (cbActionCommand.equals(STARTVERS))
			{
				controllerWindow.updateEndVers();
			}
			else if (cbActionCommand.equals(ENDVERS))
			{
				controllerWindow.updateStartVers();
			}
			controllerWindow.updatePreviewArea();
		}
		catch (BiblePresenterException e)
		{
			e.printStackTrace();
			GUILauncher.displayErrorMessage(controllerWindow, e.getMessage());
		}
	}
	
	//*****************************************************
	// Package variables
	//*****************************************************
	
	static final String BIBLE = "bible";
	static final String BOOK = "book";
	static final String CHAPTER = "chapter";
	static final String STARTVERS = "startvers";
	static final String ENDVERS = "endvers";

	//*****************************************************
	// Private variables
	//*****************************************************
	
	private ControllerWindow controllerWindow;
}
