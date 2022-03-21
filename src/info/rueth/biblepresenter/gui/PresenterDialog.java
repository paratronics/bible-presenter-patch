/*
 * Created on Apr 9, 2004 $Id: PresenterDialog.java,v 1.10 2004/04/28 09:53:59
 * Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.Document;

import info.rueth.biblepresenter.BiblePresenterException;
import info.rueth.biblepresenter.Preferences;
import info.rueth.biblepresenter.Vers;


/**
 * The windows showing the verses, the presenter window.
 * 
 * @author Ulrich Rueth
 */
public class PresenterDialog extends JDialog implements Presenter
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
	 * @param screenSize
	 *            The screen size.
	 */
	public PresenterDialog(JFrame parentFrame, DataSetter dataSetter,
			Preferences preferences, Dimension screenSize)
	{
		super(parentFrame);
		this.dataSetter = dataSetter;
		this.preferences = preferences;
		this.graphics = (Graphics2D) getGraphics();

		// create text pane
		this.textPane = new JTextPane();

		// add key bindings
		// ESC -> hide
		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "hide");
		textPane.getActionMap().put("hide", new AbstractAction()
		{
			public void actionPerformed(ActionEvent event)
			{
				PresenterDialog.this.setVisible(false);
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
					PresenterDialog.this.updateContent();
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
					((ControllerWindow) PresenterDialog.this.getParent())
							.increaseVerses();
					PresenterDialog.this.updateContent();
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
					((ControllerWindow) PresenterDialog.this.getParent())
							.decreaseVerses();
					PresenterDialog.this.updateContent();
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
					((ControllerWindow) PresenterDialog.this.getParent())
							.displayBookmark(false);
					PresenterDialog.this.updateContent();
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
					((ControllerWindow) PresenterDialog.this.getParent())
							.displayBookmark(true);
					PresenterDialog.this.updateContent();
				}
				catch (BiblePresenterException e)
				{
					e.printStackTrace();
					GUILauncher.displayErrorMessage(null, e.getMessage());
				}
			}
		});

		// non editable
		this.scrollPane = new JScrollPane(textPane);
		scrollPane.setPreferredSize(new Dimension(
				(int) (screenSize.getWidth() * 0.8), (int) (screenSize
						.getHeight() * 0.8)));
		textPane.setEditable(false);
		getContentPane().add(scrollPane);

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

		// update title
		setTitle(biblePosition);

		textPane.setBackground(preferences.getBackgroundColor());
		int adapt = 0;
		Document document = PresenterUtilities.createVerses(verses,
				biblePosition, preferences, adapt++);
		textPane.setDocument(document);
		if (preferences.isFitTextToWindow())
		{
			while (textPane.getPreferredSize().height > scrollPane
					.getPreferredSize().height
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
	private JScrollPane scrollPane;
	private Graphics2D graphics;
	private static final String TITLE_DELIMITER = " - ";
	private static final String FONT = "font";
}