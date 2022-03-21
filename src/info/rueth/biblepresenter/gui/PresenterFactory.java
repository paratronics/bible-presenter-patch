/*
 * Created on Apr 30, 2004 $Id: PresenterFactory.java,v 1.1 2004/04/30 14:18:43 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import info.rueth.biblepresenter.Preferences;

import javax.swing.*;
import java.awt.*;


/**
 * @author Ulrich Rueth
 */
public class PresenterFactory
{
	//**********************************************************
	// Public methods
	//**********************************************************

	public static Presenter getPresenter(JFrame parentFrame,
			DataSetter dataSetter, Preferences preferences)
	{
		if (preferences.isDisplayOnSecondaryScreenDevice())
		{
			// look for first virtual graphics device
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice[] gs = ge.getScreenDevices();
			Rectangle targetBounds = null;
			for (int j = 0; j < gs.length; j++)
			{
				GraphicsDevice gd = gs[j];
				GraphicsConfiguration[] gc = gd.getConfigurations();
				for (int i = 0; i < gc.length; i++)
				{
					Rectangle deviceBounds = gc[i].getBounds();
					if (deviceBounds.getX() != 0.0 && targetBounds == null)
					{
						// this is a virtual screen
						targetBounds = deviceBounds;
					}
				}
			}
			if (targetBounds == null)
			{
				// no virtual device found, return PresenterDialog
				return createPresenterDialog(parentFrame, dataSetter,
						preferences);

			}
			else
			{
				// virtual device found, display there
				return createPresenterWindow(parentFrame, dataSetter,
						preferences, targetBounds);
			}
		}
		else
		{
			// simply display the PresenterDialog
			return createPresenterDialog(parentFrame, dataSetter, preferences);
		}
	}

	private static Presenter createPresenterDialog(JFrame parentFrame,
			DataSetter dataSetter, Preferences preferences)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new PresenterDialog(parentFrame, dataSetter, preferences,
				screenSize);
	}

	private static Presenter createPresenterWindow(JFrame parentFrame,
			DataSetter dataSetter, Preferences preferences,
			Rectangle targetBounds)
	{
		return new PresenterWindow(parentFrame, dataSetter, preferences,
				targetBounds);
	}
}