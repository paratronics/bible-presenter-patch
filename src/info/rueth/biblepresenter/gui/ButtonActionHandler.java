/*
 * Created on Apr 8, 2004
 * $Id: ButtonActionHandler.java,v 1.8 2004/05/02 18:37:05 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import info.rueth.biblepresenter.BiblePresenterException;


/**
 * Handles all events on buttons.
 * @author Ulrich Rueth
 */
public class ButtonActionHandler implements ActionListener
{
	//**********************************************************
	// Constructors
	//**********************************************************
	
	/**
	 * Constructor.
	 * @param controllerWindow The controller window.
	 */
	public ButtonActionHandler(ControllerWindow controllerWindow)
	{
		this.controllerWindow = controllerWindow;
	}

	//**********************************************************
	// Public methods
	//**********************************************************
	
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			String buttonActionCommand = event.getActionCommand();
			

			if (buttonActionCommand.equals(SHOW))
			{
				controllerWindow.getPresenterWindow().updateContent();
				if (!controllerWindow.getPresenterWindow().isVisible())
				{
					controllerWindow.getPresenterWindow().setVisible(true);
				}
			}
			else if (buttonActionCommand.equals(HIDE))
			{
				controllerWindow.getPresenterWindow().setVisible(false);
			}
			else if (buttonActionCommand.equals(NEXT))
			{
				controllerWindow.increaseVerses();
				if (controllerWindow.getPresenterWindow().isVisible())
				{
					controllerWindow.getPresenterWindow().updateContent();
				}
			}
			else if (buttonActionCommand.equals(PREVIOUS))
			{
				controllerWindow.decreaseVerses();
				if (controllerWindow.getPresenterWindow().isVisible())
				{
					controllerWindow.getPresenterWindow().updateContent();
				}
			}
		}
		catch (BiblePresenterException e)
		{
			e.printStackTrace();
			GUILauncher.displayErrorMessage(controllerWindow, e.getMessage());
		}
	}

	//**********************************************************
	// Package variables
	//**********************************************************
	
	static final String SHOW = "show";
	static final String HIDE = "hide";
	static final String NEXT = "next";
	static final String PREVIOUS = "previous";
	
	//**********************************************************
	// Private variables
	//**********************************************************

	private ControllerWindow controllerWindow;
}