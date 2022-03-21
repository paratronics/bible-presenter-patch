/*
 * Created on May 2, 2004 $Id: PresenterUtilities.java,v 1.1 2004/05/02 18:37:29
 * Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import info.rueth.biblepresenter.BiblePresenterException;
import info.rueth.biblepresenter.Preferences;
import info.rueth.biblepresenter.Vers;


/**
 * @author Ulrich Rueth
 */
public class PresenterUtilities
{
	//**********************************************************
	// Package methods
	//**********************************************************

	/**
	 * Creates a document containing the verses.
	 * 
	 * @param verses
	 *            The verses to display.
	 * @param biblePosition
	 *            The bible position string.
	 * @param preferences
	 *            The user preferences.
	 * @param adapt
	 *            The adaptation level.
	 * @return The document to be displayed.
	 */
	static Document createVerses(Vers[] verses, String biblePosition,
			Preferences preferences, int adapt) throws BiblePresenterException
	{
		adapted = false;
		DefaultStyledDocument document = new DefaultStyledDocument();
		try
		{
			SimpleAttributeSet versAttSet = createVersAttributeSet(preferences,
					adapt);
			SimpleAttributeSet versNumAttSet = createVersNumberAttributeSet(
					preferences, adapt);
			for (int i = 0; i < verses.length; i++)
			{
				document.insertString(document.getLength(), verses[i]
						.getVersNumber()
						+ " ", versNumAttSet);
				document.insertString(document.getLength(), verses[i].getVers()
						+ " ", versAttSet);
			}

			if (preferences.isDisplayBiblePosition())
			{
				document.insertString(document.getLength() - 1, "\n",
						versAttSet);
				document.insertString(document.getLength() - 1, biblePosition,
						createTitleAttributeSet(preferences, adapt));
			}

			// set overall style: line spacing
			int docLength = document.getLength();
			SimpleAttributeSet overallSet = new SimpleAttributeSet();
			float lineSpacing = preferences.getLineSpacing().floatValue() - 1.0f;
			if (lineSpacing - adapt * 0.5f >= 0.0f)
			{
				adapted = adapted || true;
				lineSpacing = lineSpacing - adapt * 0.5f;
			}
			else
			{
				// minimum already reached
				lineSpacing = 0.0f;
			}
			StyleConstants.setLineSpacing(overallSet, lineSpacing);
			document.setParagraphAttributes(0, docLength, overallSet, true);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
			throw new BiblePresenterException(e.getMessage(), e);
		}
		return document;
	}

	//**********************************************************
	// Private methods
	//**********************************************************

	/**
	 * Creates the look of the verses from the user preferences.
	 * 
	 * @param preferences
	 *            The user preferences.
	 * @param adapt
	 *            The adaptation level.
	 * 
	 * @return The look of the verses.
	 */
	private static SimpleAttributeSet createVersAttributeSet(
			Preferences preferences, int adapt)
	{
		SimpleAttributeSet versAttributeSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(versAttributeSet, preferences
				.getVersFontName());
		int fontSize = preferences.getVersFontSize().intValue();
		if (fontSize - adapt * 4 >= MINVERSFONTSIZE)
		{
			adapted = adapted || true;
			fontSize = fontSize - adapt * 4;
		}
		else
		{
			// minimum already reached
			fontSize = MINVERSFONTSIZE;
		}
		StyleConstants.setFontSize(versAttributeSet, fontSize);
		StyleConstants.setForeground(versAttributeSet, preferences
				.getVersColor());
		return versAttributeSet;
	}

	/**
	 * Creates the look of the vers numbers from the user preferences.
	 * 
	 * @param preferences
	 *            The user preferences.
	 * @param adapt
	 *            The adaptation level.
	 * @return The look of the vers numbers.
	 */
	private static SimpleAttributeSet createVersNumberAttributeSet(
			Preferences preferences, int adapt)
	{
		SimpleAttributeSet versNumberAttributeSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(versNumberAttributeSet, preferences
				.getVersNumberFontName());
		int fontSize = preferences.getVersNumberFontSize().intValue();
		if (fontSize - adapt * 4 >= MINVERSNUMBERFONTSIZE)
		{
			adapted = adapted || true;
			fontSize = fontSize - adapt * 4;
		}
		else
		{
			// minimum already reached
			fontSize = MINVERSNUMBERFONTSIZE;
		}
		StyleConstants.setFontSize(versNumberAttributeSet, fontSize);
		StyleConstants.setSuperscript(versNumberAttributeSet, preferences
				.isVersNumberSuperscript());
		StyleConstants.setForeground(versNumberAttributeSet, preferences
				.getVersNumberColor());
		return versNumberAttributeSet;
	}

	/**
	 * Creates the look of the title from the user preferences.
	 * 
	 * @param preferences
	 *            The user preferences.
	 * @param adapt
	 *            The adaptation level.
	 * @return The look of the title.
	 */
	private static SimpleAttributeSet createTitleAttributeSet(
			Preferences preferences, int adapt)
	{
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attributeSet, preferences
				.getBiblePositionFontName());
		int fontSize = preferences.getBiblePositionFontSize().intValue();
		if (fontSize - adapt * 4 >= MINBIBLEPOSITIONFONTSIZE)
		{
			adapted = adapted || true;
			fontSize = fontSize - adapt * 4;
		}
		else
		{
			// minimum already reached
			fontSize = MINBIBLEPOSITIONFONTSIZE;
		}
		StyleConstants.setFontSize(attributeSet, fontSize);
		StyleConstants.setForeground(attributeSet, preferences
				.getBiblePositionColor());
		return attributeSet;
	}

	/**
	 * @return Returns true if any property was adapted.
	 */
	static boolean wasAdapted()
	{
		return adapted;
	}

	//**********************************************************
	// Private variables
	//**********************************************************

	private static boolean adapted = false;
	private static int MINVERSFONTSIZE = 16;
	private static int MINVERSNUMBERFONTSIZE = 12;
	private static int MINBIBLEPOSITIONFONTSIZE = 12;
}