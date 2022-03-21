/*
 * Created on Apr 12, 2004 $Id: PreferencesDialog.java,v 1.8 2004/04/28 09:53:59
 * Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;


import info.rueth.biblepresenter.BiblePresenterException;
import info.rueth.biblepresenter.Preferences;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;


/**
 * The user preferences dialog.
 * 
 * @author Ulrich Rueth
 */
public class PreferencesDialog extends JDialog
		implements
			ActionListener,
			ChangeListener
{
	//**********************************************************
	// Constructors
	//**********************************************************

	/**
	 * Constructor.
	 * 
	 * @param controllerWindow
	 *            The controller window.
	 * @param title
	 *            The dialog title.
	 * @param resources
	 *            The language resource.
	 * @param preferences
	 *            The user preferences.
	 */
	public PreferencesDialog(ControllerWindow controllerWindow, String title,
			ResourceBundle resources, Preferences preferences)
	{
		super(controllerWindow, title, /* modal= */true);
		this.controllerWindow = controllerWindow;
		this.resources = resources;
		this.preferences = preferences;

		// initialise values
		this.backgroundColor = preferences.getBackgroundColor();
		this.versColor = preferences.getVersColor();
		this.versFontName = preferences.getVersFontName();
		this.versFontSize = preferences.getVersFontSize();
		this.versNumberColor = preferences.getVersNumberColor();
		this.versNumberFontName = preferences.getVersNumberFontName();
		this.versNumberFontSize = preferences.getVersNumberFontSize();
		this.versNumberSuperscript = preferences.isVersNumberSuperscript();
		this.biblePositionColor = preferences.getBiblePositionColor();
		this.biblePositionFontName = preferences.getBiblePositionFontName();
		this.biblePositionFontSize = preferences.getBiblePositionFontSize();
		this.displayBiblePosition = preferences.isDisplayBiblePosition();
		this.lineSpacing = preferences.getLineSpacing();
		this.fitTextToWindow = preferences.isFitTextToWindow();
		this.versOffset = preferences.getVersOffset();
		this.savePositionOnExit = preferences.isSavePositionOnExit();
		this.language = preferences.getLanguage();
		this.displayOnSecondaryScreenDevice = preferences
				.isDisplayOnSecondaryScreenDevice();
		// TODO initialise new preferences here

		// set BoxLayout to whole dialog
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		// create panel for presenter window properties
		JPanel presenterWindowPanel = new JPanel();
		presenterWindowPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(resources
						.getString("pref_presenterwindowpanel_title")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		presenterWindowPanel.setLayout(new BoxLayout(presenterWindowPanel,
				BoxLayout.Y_AXIS));
		getContentPane().add(presenterWindowPanel);

		// create panel for controller window properties
		JPanel controllerWindowPanel = new JPanel();
		controllerWindowPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(resources
						.getString("pref_controllerwindowpanel_title")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		controllerWindowPanel.setLayout(new BoxLayout(controllerWindowPanel,
				BoxLayout.Y_AXIS));
		getContentPane().add(controllerWindowPanel);

		// create vers font properties panel
		JPanel versFontPropPanel = createFontPropertiesPanel(resources
				.getString("pref_presenterwindow_vers"), VERSCOLOR,
				VERSFONTNAME, VERSFONTSIZE, versFontName, versFontSize);
		presenterWindowPanel.add(versFontPropPanel);

		// create vers number font properties panel
		JPanel versNumberFontPropPanel = createFontPropertiesPanel(resources
				.getString("pref_presenterwindow_versnumber"), VERSNUMBERCOLOR,
				VERSNUMBERFONTNAME, VERSNUMBERFONTSIZE, versNumberFontName,
				versNumberFontSize);
		presenterWindowPanel.add(versNumberFontPropPanel);

		// add additional superscript check box to vers number font panel
		JCheckBox superscriptBox = new JCheckBox(resources
				.getString("pref_superscript"), versNumberSuperscript);
		superscriptBox.setActionCommand(VERSNUMBERSUPERSCRIPT);
		superscriptBox.addActionListener(this);
		versNumberFontPropPanel.add(superscriptBox);

		// create bible position font properties panel
		JPanel titleFontPropPanel = createFontPropertiesPanel(resources
				.getString("pref_presenterwindow_bibleposition"),
				BIBLEPOSITIONCOLOR, BIBLEPOSITIONFONTNAME,
				BIBLEPOSITIONFONTSIZE, biblePositionFontName,
				biblePositionFontSize);
		presenterWindowPanel.add(titleFontPropPanel);

		// add additional display title check box to bible position font panel
		JCheckBox displayBiblePositionBox = new JCheckBox(resources
				.getString("pref_displaybibleposition"), displayBiblePosition);
		displayBiblePositionBox.setActionCommand(DISPLAYBIBLEPOSITION);
		displayBiblePositionBox.addActionListener(this);
		titleFontPropPanel.add(displayBiblePositionBox);

		// create background color button
		JPanel backgroundColorPanel = new JPanel();
		((FlowLayout) backgroundColorPanel.getLayout())
				.setAlignment(FlowLayout.LEFT);
		presenterWindowPanel.add(backgroundColorPanel);
		JButton backgroundColorButton = new JButton(resources
				.getString("pref_backgroundcolor"));
		backgroundColorButton.setActionCommand(BACKGROUNDCOLOR);
		backgroundColorButton.addActionListener(this);
		backgroundColorPanel.add(backgroundColorButton);

		// create line spacing selection
		JPanel lineSpacingPanel = new JPanel();
		((FlowLayout) lineSpacingPanel.getLayout())
				.setAlignment(FlowLayout.LEFT);
		presenterWindowPanel.add(lineSpacingPanel);
		JLabel lineSpacingLabel = new JLabel(resources
				.getString("pref_linespacing"));
		JComboBox lineSpacingSelector = new JComboBox(preferences
				.getLineSpacings());
		lineSpacingSelector.setSelectedItem(lineSpacing);
		lineSpacingSelector.setActionCommand(LINESPACING);
		lineSpacingSelector.addActionListener(this);
		lineSpacingPanel.add(lineSpacingLabel);
		lineSpacingPanel.add(lineSpacingSelector);

		// fit text to window
		JPanel fitTextToWindowPanel = new JPanel();
		presenterWindowPanel.add(fitTextToWindowPanel);
		((FlowLayout) fitTextToWindowPanel.getLayout())
				.setAlignment(FlowLayout.LEFT);
		JCheckBox fitTextToWindowCheckBox = new JCheckBox(resources
				.getString("pref_fittexttowindow"), fitTextToWindow);
		fitTextToWindowCheckBox.setActionCommand(FITTEXTTOWINDOW);
		fitTextToWindowCheckBox.addActionListener(this);
		fitTextToWindowPanel.add(fitTextToWindowCheckBox);

		// display on secondary screen device
		JPanel screenDevicePanel = new JPanel();
		presenterWindowPanel.add(screenDevicePanel);
		((FlowLayout) screenDevicePanel.getLayout())
				.setAlignment(FlowLayout.LEFT);
		JCheckBox secondaryScreenDeviceBox = new JCheckBox(resources
				.getString("pref_displayonsecscreendevice"),
				displayOnSecondaryScreenDevice);
		secondaryScreenDeviceBox.setActionCommand(SCREENDEVICE);
		secondaryScreenDeviceBox.addActionListener(this);
		screenDevicePanel.add(secondaryScreenDeviceBox);

		// set default vers offset
		JPanel versOffsetOptionsPanel = new JPanel();
		controllerWindowPanel.add(versOffsetOptionsPanel);
		((FlowLayout) versOffsetOptionsPanel.getLayout())
				.setAlignment(FlowLayout.LEFT);
		versOffsetOptionsPanel.add(new JLabel(resources
				.getString("pref_versoffset")));
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(versOffset, 0,
				20, 1);
		JSpinner offsetSpinner = new JSpinner(spinnerModel);
		offsetSpinner.addChangeListener(this);
		versOffsetOptionsPanel.add(offsetSpinner);

		// set language
		JPanel languagePanel = new JPanel();
		controllerWindowPanel.add(languagePanel);
		((FlowLayout) languagePanel.getLayout()).setAlignment(FlowLayout.LEFT);
		JLabel languageLabel = new JLabel(resources.getString("pref_language"));
		languagePanel.add(languageLabel);
		JComboBox languageBox = new JComboBox(preferences.getLanguages());
		languageBox.setSelectedItem(language);
		languageBox.setActionCommand(LANGUAGE);
		languageBox.addActionListener(this);
		languagePanel.add(languageBox);

		// remember bible position
		JPanel biblePositionPanel = new JPanel();
		controllerWindowPanel.add(biblePositionPanel);
		((FlowLayout) biblePositionPanel.getLayout())
				.setAlignment(FlowLayout.LEFT);
		JCheckBox biblePositionCheckBox = new JCheckBox(resources
				.getString("pref_savepositiononexit"), savePositionOnExit);
		biblePositionCheckBox.setActionCommand(BIBLEPOSITION);
		biblePositionCheckBox.addActionListener(this);
		biblePositionPanel.add(biblePositionCheckBox);

		// button panel
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel);

		// Ok and Cancel buttons
		JButton okButton = new JButton(resources.getString("ok"));
		okButton.addActionListener(this);
		okButton.setActionCommand(PREF_OK);
		buttonPanel.add(okButton);

		JButton cancelButton = new JButton(resources.getString("cancel"));
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand(PREF_CANCEL);
		buttonPanel.add(cancelButton);

		// pack and show
		pack();
		setVisible(true);
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
			if (actionCommand.equals(PREF_OK))
			{
				savePreferences();
				setVisible(false);
				this.dispose();
			}
			else if (actionCommand.equals(PREF_CANCEL))
			{
				setVisible(false);
				this.dispose();
			}
			else if (actionCommand.equals(BACKGROUNDCOLOR))
			{
				backgroundColor = setColor(resources
						.getString("pref_backgroundcolor"), backgroundColor);
			}
			else if (actionCommand.equals(VERSCOLOR))
			{
				versColor = setColor(resources
						.getString("pref_presenterwindow_vers"), versColor);
			}
			else if (actionCommand.equals(VERSFONTNAME))
			{
				JComboBox fontChooser = (JComboBox) event.getSource();
				versFontName = (String) fontChooser.getSelectedItem();
			}
			else if (actionCommand.equals(VERSFONTSIZE))
			{
				JComboBox comboBox = (JComboBox) event.getSource();
				versFontSize = (Integer) comboBox.getSelectedItem();
			}
			else if (actionCommand.equals(VERSNUMBERCOLOR))
			{
				versNumberColor = setColor(resources
						.getString("pref_presenterwindow_versnumber"),
						versNumberColor);
			}
			else if (actionCommand.equals(VERSNUMBERFONTNAME))
			{
				JComboBox fontChooser = (JComboBox) event.getSource();
				versNumberFontName = (String) fontChooser.getSelectedItem();
			}
			else if (actionCommand.equals(VERSNUMBERFONTSIZE))
			{
				JComboBox comboBox = (JComboBox) event.getSource();
				versNumberFontSize = (Integer) comboBox.getSelectedItem();
			}
			else if (actionCommand.equals(VERSNUMBERSUPERSCRIPT))
			{
				JCheckBox superscriptCheckbox = (JCheckBox) event.getSource();
				versNumberSuperscript = superscriptCheckbox.isSelected();
			}
			else if (actionCommand.equals(BIBLEPOSITIONCOLOR))
			{
				biblePositionColor = setColor(resources
						.getString("pref_presenterwindow_bibleposition"),
						biblePositionColor);
			}
			else if (actionCommand.equals(BIBLEPOSITIONFONTNAME))
			{
				JComboBox fontChooser = (JComboBox) event.getSource();
				biblePositionFontName = (String) fontChooser.getSelectedItem();
			}
			else if (actionCommand.equals(BIBLEPOSITIONFONTSIZE))
			{
				JComboBox comboBox = (JComboBox) event.getSource();
				biblePositionFontSize = (Integer) comboBox.getSelectedItem();
			}
			else if (actionCommand.equals(DISPLAYBIBLEPOSITION))
			{
				JCheckBox biblePositionCheckbox = (JCheckBox) event.getSource();
				displayBiblePosition = biblePositionCheckbox.isSelected();
			}
			else if (actionCommand.equals(LINESPACING))
			{
				JComboBox lineSpacingBox = (JComboBox) event.getSource();
				lineSpacing = (Float) lineSpacingBox.getSelectedItem();
			}
			else if (actionCommand.equals(FITTEXTTOWINDOW))
			{
				JCheckBox fitTextToWindowCheckBox = (JCheckBox) event
						.getSource();
				fitTextToWindow = fitTextToWindowCheckBox.isSelected();
			}
			else if (actionCommand.equals(BIBLEPOSITION))
			{
				JCheckBox biblePositionBox = (JCheckBox) event.getSource();
				savePositionOnExit = biblePositionBox.isSelected();
			}
			else if (actionCommand.equals(LANGUAGE))
			{
				JComboBox languageBox = (JComboBox) event.getSource();
				language = (String) languageBox.getSelectedItem();
				showRestartNecessary();
			}
			else if (actionCommand.equals(SCREENDEVICE))
			{
				JCheckBox screenDeviceBox = (JCheckBox) event.getSource();
				displayOnSecondaryScreenDevice = screenDeviceBox.isSelected();
				showRestartNecessary();
			}
		}
		catch (BiblePresenterException e)
		{
			e.printStackTrace();
			GUILauncher.displayErrorMessage(controllerWindow, e.getMessage());
		}
	}

	/**
	 * @see ChangeListener#stateChanged(ChangeEvent)
	 */
	public void stateChanged(ChangeEvent event)
	{
		SpinnerNumberModel offsetSpinnerModel = (SpinnerNumberModel) ((JSpinner) event
				.getSource()).getModel();
		versOffset = offsetSpinnerModel.getNumber().intValue();
	}

	//**********************************************************
	// Private methods
	//**********************************************************

	/**
	 * Saves the user preferences.
	 * 
	 * @throws BiblePresenterException
	 */
	private void savePreferences() throws BiblePresenterException
	{
		preferences.setBackgroundColor(backgroundColor);
		preferences.setVersColor(versColor);
		preferences.setVersFontName(versFontName);
		preferences.setVersFontSize(versFontSize);
		preferences.setVersNumberColor(versNumberColor);
		preferences.setVersNumberFontName(versNumberFontName);
		preferences.setVersNumberFontSize(versNumberFontSize);
		preferences.setVersNumberSuperscript(versNumberSuperscript);
		preferences.setBiblePositionColor(biblePositionColor);
		preferences.setBiblePositionFontName(biblePositionFontName);
		preferences.setBiblePositionFontSize(biblePositionFontSize);
		preferences.setDisplayBiblePosition(displayBiblePosition);
		preferences.setVersOffset(versOffset);
		preferences.setLineSpacing(lineSpacing);
		preferences.setFitTextToWindow(fitTextToWindow);
		preferences.setSavePositionOnExit(savePositionOnExit);
		preferences.setLanguage(language);
		preferences
				.setDisplayOnSecondaryScreenDevice(displayOnSecondaryScreenDevice);
		preferences.save();
		// TODO add new preferences here!
	}

	/**
	 * Creates a bordered font properties pane.
	 * 
	 * @param panelTitle
	 *            The title of the pane.
	 * @param colorActionCommand
	 *            The action command for the color.
	 * @param fontActionCommand
	 *            The action command for the font.
	 * @param sizeActionCommand
	 *            The action command for the font size.
	 * @param currentFont
	 *            The currently selected font.
	 * @param currentSize
	 *            The corrently selected font size.
	 * @return The font properties pane.
	 */
	private JPanel createFontPropertiesPanel(String panelTitle,
			String colorActionCommand, String fontActionCommand,
			String sizeActionCommand, String currentFont, Integer currentSize)
	{
		// create panel
		JPanel fontPropPanel = new JPanel();
		((FlowLayout) fontPropPanel.getLayout()).setAlignment(FlowLayout.LEFT);
		fontPropPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(panelTitle), BorderFactory
						.createEmptyBorder(5, 5, 5, 5)));

		// color selector
		JButton colorButton = new JButton(resources
				.getString("pref_presenterwindow_color"));
		colorButton.setActionCommand(colorActionCommand);
		colorButton.addActionListener(this);
		fontPropPanel.add(colorButton);

		// font selector
		JLabel fontLabel = new JLabel(resources
				.getString("pref_presenterwindow_font"));
		fontPropPanel.add(fontLabel);
		JComboBox fontComboBox = createFontSelector(fontActionCommand,
				currentFont);
		fontPropPanel.add(fontComboBox);

		// font size selector
		JLabel fontSizeLabel = new JLabel(resources
				.getString("pref_presenterwindow_size"));
		fontPropPanel.add(fontSizeLabel);

		JComboBox fontSizeComboBox = new JComboBox(preferences.getFontSizes());
		fontSizeComboBox.setSelectedItem(currentSize);
		fontSizeComboBox.addActionListener(this);
		fontSizeComboBox.setActionCommand(sizeActionCommand);
		fontPropPanel.add(fontSizeComboBox);

		return fontPropPanel;
	}

	/**
	 * Creates a font selector combo box.
	 * 
	 * @param comboboxActionCommand
	 *            The combo box action command.
	 * @param currentFont
	 *            The currently selected font.
	 * @return The font selector combo box.
	 */
	private JComboBox createFontSelector(String comboboxActionCommand,
			String currentFont)
	{
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] fonts = ge.getAllFonts();
		String[] fontNames = new String[fonts.length];
		int currentSelection = -1;

		for (int i = 0; i < fonts.length; i++)
		{
			fontNames[i] = fonts[i].getFontName();
		}

		// create font combo box
		JComboBox fontComboBox = new JComboBox(fontNames);
		fontComboBox.setSelectedItem(currentFont);
		fontComboBox.addActionListener(this);
		fontComboBox.setActionCommand(comboboxActionCommand);
		return fontComboBox;
	}

	/**
	 * Opens a JColorChooser dialog.
	 * 
	 * @param dialogTitle
	 *            The dialog title.
	 * @param color
	 *            The currently set color.
	 * @return The new color.
	 * @see JColorChooser
	 */
	private Color setColor(String dialogTitle, Color color)
	{
		String name = dialogTitle + " - "
				+ resources.getString("pref_presenterwindow_color");
		Color tempColor = JColorChooser.showDialog(this, dialogTitle, color);
		if (tempColor != null)
		{
			return tempColor;
		}
		return color;
	}

	/**
	 * Opens a dialog with the message to restart the program for this change to
	 * take effect.
	 */
	private void showRestartNecessary()
	{
		JOptionPane.showMessageDialog(this, resources
				.getString("pref_restart_necessary"), resources
				.getString("pref_restart_necessary_title"),
				JOptionPane.INFORMATION_MESSAGE);
	}

	//**********************************************************
	// Private variables
	//**********************************************************

	private ControllerWindow controllerWindow;
	private Preferences preferences;
	private ResourceBundle resources;

	private Color backgroundColor;

	private Color versColor;
	private String versFontName;
	private Integer versFontSize;

	private Color versNumberColor;
	private String versNumberFontName;
	private Integer versNumberFontSize;
	private boolean versNumberSuperscript;

	private Color biblePositionColor;
	private String biblePositionFontName;
	private Integer biblePositionFontSize;
	private boolean displayBiblePosition;

	private int versOffset;
	private Float lineSpacing;
	private boolean fitTextToWindow;
	private boolean savePositionOnExit;
	private String language;
	private boolean displayOnSecondaryScreenDevice;

	private static final String PREF_OK = "pref_ok";
	private static final String PREF_CANCEL = "pref_cancel";
	private static final String BACKGROUNDCOLOR = "backgroundcolor";
	private static final String VERSCOLOR = "verscolor";
	private static final String VERSFONTNAME = "versfontname";
	private static final String VERSFONTSIZE = "versfontsize";
	private static final String VERSNUMBERCOLOR = "versnumbercolor";
	private static final String VERSNUMBERFONTNAME = "versnumberfontname";
	private static final String VERSNUMBERFONTSIZE = "versnumberfontsize";
	private static final String VERSNUMBERSUPERSCRIPT = "versnumbersuperscript";
	private static final String BIBLEPOSITIONCOLOR = "biblepositioncolor";
	private static final String BIBLEPOSITIONFONTNAME = "biblepositionfontname";
	private static final String BIBLEPOSITIONFONTSIZE = "biblepositionfontsize";
	private static final String DISPLAYBIBLEPOSITION = "displaybibleposition";
	private static final String LINESPACING = "linespacing";
	private static final String FITTEXTTOWINDOW = "fittexttowindow";
	private static final String BIBLEPOSITION = "bibleposition";
	private static final String LANGUAGE = "language";
	private static final String SCREENDEVICE = "screendevice";
}