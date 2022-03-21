/*
 * Created on Apr 18, 2004
 * $Id: BookmarkListener.java,v 1.9 2004/05/01 22:06:28 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import info.rueth.biblepresenter.BiblePresenterException;


/**
 * Handles events on the bookmark pane.
 * @author Ulrich Rueth
 */
public class BookmarkListener extends MouseAdapter
		implements
			ActionListener,
			TreeSelectionListener
{
	//**********************************************************
	// Constructors
	//**********************************************************

	/**
	 * Constructor.
	 * @param controllerWindow The controller window.
	 * @param dataSetter The data setter.
	 * @param resources The language resource.
	 */
	public BookmarkListener(ControllerWindow controllerWindow,
			DataSetter dataSetter, ResourceBundle resources)
	{
		super();
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
			String actionCommand = event.getActionCommand();
			if (actionCommand.equals(UP))
			{
				controllerWindow.moveBookmarkObjectUp();
			}
			else if (actionCommand.equals(DOWN))
			{
				controllerWindow.moveBookmarkObjectDown();
			}
			else if (actionCommand.equals(NEW))
			{
				controllerWindow.createNewBookmarkFolder();
			}
			else if (actionCommand.equals(ADD))
			{
				controllerWindow.addBookmark();
			}
			else if (actionCommand.equals(REMOVE))
			{
				controllerWindow.removeBookmark();
			}
			else if (actionCommand.equals(DISPLAYNEXTBOOKMARK))
			{
				controllerWindow.displayBookmark(/*next=*/true);
			}
			else if (actionCommand.equals(DISPLAYPREVIOUSBOOKMARK))
			{
				controllerWindow.displayBookmark(/*next=*/false);
			}
		}
		catch (BiblePresenterException e)
		{
			e.printStackTrace();
			GUILauncher.displayErrorMessage(controllerWindow, e.getMessage());
		}
	}

	/**
	 * @see TreeSelectionListener#valueChanged(TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent event)
	{
		controllerWindow.setSelectedNode();
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent event)
	{
		if (event.getClickCount() == 2)
		{
			try
			{
				controllerWindow.displayBookmark();
			}
			catch (BiblePresenterException e)
			{
				e.printStackTrace();
				GUILauncher.displayErrorMessage(controllerWindow, e.getMessage());
			}
		}
	}

	//**********************************************************
	// Private methods
	//**********************************************************

	//**********************************************************
	// Public variables
	//**********************************************************

	public static final String DOWN = "down";
	public static final String UP = "up";
	public static final String ADD = "add";
	public static final String REMOVE = "remove";
	public static final String NEW = "new";
	public static final String DISPLAYNEXTBOOKMARK = "nextbookmark";
	public static final String DISPLAYPREVIOUSBOOKMARK = "previousbookmark";

	//**********************************************************
	// Private variables
	//**********************************************************

	private ControllerWindow controllerWindow;
}