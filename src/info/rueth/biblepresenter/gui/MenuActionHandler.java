/*
 * Created on Apr 12, 2004 $Id: MenuActionHandler.java,v 1.5 2004/04/28 09:53:59
 * Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import info.rueth.biblepresenter.BiblePresenterException;
import info.rueth.biblepresenter.Preferences;


/**
 * Handles all menu events of the controller window.
 * 
 * @author Ulrich Rueth
 */
public class MenuActionHandler implements ActionListener
{
	//**********************************************************
	// Constructors
	//**********************************************************

	/**
	 * Constructor.
	 * 
	 * @param controllerWindow
	 *            The controller window.
	 * @param dataSetter
	 *            The data setter.
	 * @param preferences
	 *            The user preferences.
	 * @param resources
	 *            The language resource.
	 */
	public MenuActionHandler(ControllerWindow controllerWindow,
			DataSetter dataSetter, Preferences preferences,
			ResourceBundle resources)
	{
		super();
		this.controllerWindow = controllerWindow;
		this.resources = resources;
		this.preferences = preferences;
		this.dataSetter = dataSetter;
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
			if (actionCommand.equals(PREFERENCES))
			{
				new PreferencesDialog(controllerWindow, resources
						.getString("pref_title"), resources, preferences);
			}
			else if (actionCommand.equals(EXIT))
			{
				controllerWindow.setVisible(false);
				controllerWindow.getWindowListeners()[0]
						.windowClosing(new WindowEvent(controllerWindow,
								WindowEvent.WINDOW_CLOSING));
				System.exit(0);
			}
			else if (actionCommand.equals(IMPORT))
			{
				dataSetter.loadBible(controllerWindow);
				controllerWindow.updateBibles();
			}
			else if (actionCommand.equals(REMOVE))
			{
				dataSetter.removeBible(controllerWindow);
				controllerWindow.updateBibles();
				controllerWindow.updateBookmarks();
			}
			else if (actionCommand.equals(HELPWEB))
			{
				BrowserLauncher.openURL(GUILauncher.WEBURL);
			}
			else if (actionCommand.equals(HELPABOUT))
			{
				SplashScreen splash = new SplashScreen(controllerWindow,
						new ImageIcon(new File(GUILauncher
								.getResourceDirectory(),
								GUILauncher.SPLASHIMAGE).toURL()),
						GUILauncher.VERSION);
				splash.setVisible(true);
			}
		}
		catch (BiblePresenterException e)
		{
			GUILauncher.displayErrorMessage(controllerWindow, e.getMessage());
		}
		catch (IOException e)
		{
			GUILauncher.displayErrorMessage(controllerWindow, e.getMessage());
		}
	}

	//**********************************************************
	// Package variables
	//**********************************************************

	static final String PREFERENCES = "preferences";
	static final String EXIT = "exit";
	static final String IMPORT = "import";
	static final String REMOVE = "remove";
	static final String HELPWEB = "helpweb";
	static final String HELPABOUT = "helpabout";

	//**********************************************************
	// Private variables
	//**********************************************************

	private ControllerWindow controllerWindow;
	private ResourceBundle resources;
	private Preferences preferences;
	private DataSetter dataSetter;

}