/*
 * Created on Apr 4, 2004 $Id: GUILauncher.java,v 1.10 2004/04/30 22:55:09
 * Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import info.rueth.biblepresenter.BiblePresenterException;
import info.rueth.biblepresenter.Preferences;


/**
 * Main class, launches the graphical user interface.
 * 
 * @author Ulrich Rueth
 */
public class GUILauncher
{

	//*****************************************************
	// Public methods
	//*****************************************************

	/**
	 * Main method, no arguments needed.
	 */
	public static void main(String[] args)
	{
		try
		{
			new GUILauncher().show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			displayErrorMessage(null, e.getMessage());
		}
	}

	/**
	 * Sets the look and feel, loads resources, sets the caching directory to
	 * the user home directory (System.getProperty("user.home")), launches the
	 * splash screen, fills the data setter and starts the GUI building process.
	 * 
	 * @throws Exception
	 */
	public void show() throws Exception
	{
		// set look and feel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		String userHomeString = System.getProperty("user.home");
		String cachingDirString = userHomeString + File.separator
				+ CACHINGDIRNAME;
		File cachingDir = new File(cachingDirString);
		if (!cachingDir.exists())
		{
			File userHome = new File(userHomeString);
			if (!userHome.isDirectory() && !userHome.canWrite())
			{
				throw new BiblePresenterException("Need write access to "
						+ userHomeString);
			}
			cachingDir.mkdir();
		}
		if (!cachingDir.isDirectory() && !cachingDir.canWrite())
		{
			throw new BiblePresenterException("Need write access to "
					+ cachingDirString);
		}

		// load preferences
		Preferences preferences = loadPreferences(cachingDir);

		// localise
		ResourceBundle resources = ResourceBundle.getBundle(
				"info.rueth.biblepresenter.gui.MyResources", preferences
						.getLocale());

		// show splash screen first
		Frame f = new Frame();
		// try to get image
		ImageIcon imageIcon = null;
		try
		{
			imageIcon = GUILauncher.loadImageIcon(SPLASHIMAGE);
		}
		catch (IOException e)
		{
			// do nothing, just go w/o images
		}
		SplashScreen splashScreen = new SplashScreen(f, imageIcon, resources
				.getString("splash_pleasewait"));
		splashScreen.setVisible(true);

		// create DataSetter
		splashScreen.addText(resources.getString("splash_loadingbibles"));
		DataSetter dataSetter = new DataSetter(cachingDir, preferences,
				resources);

		// create ControllerWindow
		splashScreen.addText(resources
				.getString("splash_createcontrollerwindow"));
		ControllerWindow controllerWindow = new ControllerWindow(dataSetter,
				resources, preferences);
		controllerWindow.createAndShow(splashScreen);

		preferences.save();
	}

	/**
	 * Returns the current working directory of the application if launched
	 * normally (per batch or in IDE), but if using jexepack for making a
	 * Windows executable, it returns the resource directory of jexepack.
	 * 
	 * @return The resource directory.
	 * @see <a href="http://www.duckware.com/jexepack/">JExePack Documentation
	 *      </a>
	 */
	public static File getResourceDirectory()
	{
		String p = System.getProperty("jexepack.resdir");
		return (p != null) ? new File(p) : getCurrentWorkingDirectory();
	}

	/**
	 * Returns the user directory.
	 * 
	 * @return The user directory.
	 * @see <a href="http://www.duckware.com/jexepack/">JExePack Documentation
	 *      </a>
	 */
	public static File getCurrentWorkingDirectory()
	{
		return new File(System.getProperty("user.dir", "."));
	}
	
	public static ImageIcon loadImageIcon(String imageName)
	throws IOException
	{
		File imageFile = new File(GUILauncher.getResourceDirectory(),
				imageName);
		ImageIcon imageIcon = null;
		if (imageFile.exists())
		{
			return new ImageIcon(imageFile.toURL());
		}
		else
		{
			// try to get it from biblepresenter.jar
			JarResources jar = new JarResources(BIBLEPRESENTERJAR);
			Image image = Toolkit.getDefaultToolkit().createImage(
					jar.getResource(SPLASHIMAGE));
			return new ImageIcon(image);
		}
	}

	/**
	 * Displays an error message window.
	 * 
	 * @param parent
	 *            The parent frame, can be null.
	 * @param message
	 *            The message to display.
	 */
	public static void displayErrorMessage(JFrame parent, String message)
	{
		JFrame parentFrame = (parent == null) ? new JFrame() : parent;
		JOptionPane.showMessageDialog(parentFrame, message, "Internal error",
				JOptionPane.ERROR_MESSAGE);
	}

	//*****************************************************
	// Private methods
	//*****************************************************

	/**
	 * Loads the user preferences from the file system.
	 * 
	 * @param cachingDir
	 *            The caching directory.
	 */
	private Preferences loadPreferences(File cachingDir)
			throws BiblePresenterException
	{
		File prefFile = new File(cachingDir, PREFFILENAME);
		try
		{
			FileInputStream fis = new FileInputStream(prefFile);
			ObjectInputStream objIs = new ObjectInputStream(fis);
			Preferences preferences = (Preferences) objIs.readObject();
			fis.close();
			return preferences;
		}
		catch (NotSerializableException se)
		{
			se.printStackTrace();
			return new Preferences(prefFile);
		}
		catch (FileNotFoundException fe)
		{
			fe.printStackTrace();
			return new Preferences(prefFile);
		}
		catch (IOException se)
		{
			se.printStackTrace();
			return new Preferences(prefFile);
		}
		catch (ClassNotFoundException ce)
		{
			ce.printStackTrace();
			return new Preferences(prefFile);
		}
	}

	//**********************************************************
	// Public variables
	//**********************************************************

	public static final String SPLASHIMAGE = "etc/images/splash.jpg";
	public static final String VERSION = "Version 1.0, 2004-05-03";
	public static final String WEBURL = "http://www.rueth.info/software/";

	//**********************************************************
	// Private variables
	//**********************************************************

	private static final String BIBLEPRESENTERJAR = "lib/biblepresenter.jar";
	private static final String CACHINGDIRNAME = "biblepresenter";
	private static final String PREFFILENAME = "preferences";
	private final String DEFAULTBIBLESUBDIR = "bibles";

}