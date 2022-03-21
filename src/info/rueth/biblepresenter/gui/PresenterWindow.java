/*
 * Created on Apr 9, 2004 $Id: PresenterDialog.java,v 1.10 2004/04/28 09:53:59
 * Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import info.rueth.biblepresenter.BiblePresenterException;
import info.rueth.biblepresenter.Preferences;
import info.rueth.biblepresenter.Vers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 * The windows showing the verses, the presenter window.
 * 
 * @author Ulrich Rueth
 */
public class PresenterWindow extends JWindow implements Presenter
{
	//**********************************************************
	// Constructors
	//**********************************************************

	/**
	 * Constructor.
	 * 
	 * @param parentFrame
	 *            The parent frame.
	 * @param dataSetter
	 *            The data setter.
	 * @param preferences
	 *            The user preferences.
	 */
	public PresenterWindow(JFrame parentFrame, DataSetter dataSetter,
			Preferences preferences, Rectangle targetBounds)
	{
		super(parentFrame);
		this.dataSetter = dataSetter;
		this.preferences = preferences;
		this.graphics = (Graphics2D) getGraphics();

		// create text pane
		this.textPane = new JTextPane();
		this.p = new JPanel();

		// add key bindings
		// ESC -> hide
		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "hide");
		textPane.getActionMap().put("hide", new AbstractAction()
		{
			public void actionPerformed(ActionEvent event)
			{
				PresenterWindow.this.setVisible(false);
			}
		});

		// F5 -> show
		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "show");
		textPane.getActionMap().put("show", new AbstractAction()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					PresenterWindow.this.updateContent();
				}
				catch (BiblePresenterException e)
				{
					e.printStackTrace();
					GUILauncher.displayErrorMessage(null, e.getMessage());
				}
			}
		});

		// CTRL+RIGHT -> next vers
		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(
						KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,
								InputEvent.CTRL_MASK), "nextvers");
		textPane.getActionMap().put("nextvers", new AbstractAction()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					((ControllerWindow) PresenterWindow.this.getParent())
							.increaseVerses();
					PresenterWindow.this.updateContent();
				}
				catch (BiblePresenterException e)
				{
					e.printStackTrace();
					GUILauncher.displayErrorMessage(null, e.getMessage());
				}
			}
		});

		// CTRL+LEFT -> previous vers
		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK),
				"previousvers");
		textPane.getActionMap().put("previousvers", new AbstractAction()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					((ControllerWindow) PresenterWindow.this.getParent())
							.decreaseVerses();
					PresenterWindow.this.updateContent();
				}
				catch (BiblePresenterException e)
				{
					e.printStackTrace();
					GUILauncher.displayErrorMessage(null, e.getMessage());
				}
			}
		});

		// CTRL+UP -> previous bookmark
		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK),
				"previousbookmark");
		textPane.getActionMap().put("previousbookmark", new AbstractAction()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					((ControllerWindow) PresenterWindow.this.getParent())
							.displayBookmark(false);
					PresenterWindow.this.updateContent();
				}
				catch (BiblePresenterException e)
				{
					e.printStackTrace();
					GUILauncher.displayErrorMessage(null, e.getMessage());
				}
			}
		});

		// CTRL+DOWN -> next bookmark
		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK),
				"nextbookmark");
		textPane.getActionMap().put("nextbookmark", new AbstractAction()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					((ControllerWindow) PresenterWindow.this.getParent())
							.displayBookmark(true);
					PresenterWindow.this.updateContent();
				}
				catch (BiblePresenterException e)
				{
					e.printStackTrace();
					GUILauncher.displayErrorMessage(null, e.getMessage());
				}
			}
		});


		// virtual device found, display there
		this.setBounds(targetBounds);
		this.setSize((int) targetBounds.getWidth(), (int) targetBounds
				.getHeight());
		textPane.setPreferredSize(new Dimension((int) targetBounds.getWidth() - 80,
				(int) targetBounds.getHeight() - 80));
		p.setBorder(new EmptyBorder(40, 40, 40, 40));
		p.add(textPane);

		// non editable
		textPane.setEditable(false);
		getContentPane().add(p);

		// pack
		pack();
	}

	//**********************************************************
	// Public methods
	//**********************************************************

	/**
	 * Updates the content of the presenter window using the data setter values
	 * for the selected bible location.
	 */
	public void updateContent() throws BiblePresenterException
	{
		String biblePosition = dataSetter.toString();
		Vers[] verses = dataSetter.getVerses();

		p.setBackground(preferences.getBackgroundColor());
		textPane.setOpaque(false);
		int adapt = 0;
		Document document = PresenterUtilities.createVerses(verses,
				biblePosition, preferences, adapt++);
		textPane.setDocument(document);
		if (preferences.isFitTextToWindow())
		{
			while (textPane.getPreferredSize().height > this.getPreferredSize().height
					&& PresenterUtilities.wasAdapted())
			{
				textPane.setDocument(PresenterUtilities.createVerses(verses,
						biblePosition, preferences, adapt++));
			}
		}
	}

	//**********************************************************
	// Private methods
	//**********************************************************

	//**********************************************************
	// Private variables
	//**********************************************************

	private DataSetter dataSetter;
	private Preferences preferences;
	private JTextPane textPane;
	private JPanel p;
	private Graphics2D graphics;
	private static final String TITLE_DELIMITER = " - ";
	private static final String FONT = "font";
}