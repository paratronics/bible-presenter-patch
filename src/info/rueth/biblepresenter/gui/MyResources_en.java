/*
 * Created on Apr 12, 2004
 * $Id: MyResources_en.java,v 1.3 2004/05/03 20:29:15 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;

import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;

/**
 * The default language resource, English.
 * @author Ulrich Rueth
 */
public class MyResources_en extends ListResourceBundle
{
	//**********************************************************
	// Protected methods
	//**********************************************************
	
	/**
	 * @see ListResourceBundle#getContents()
	 */
	protected Object[][] getContents()
	{
        return contents;
    }
	
	//**********************************************************
	// Private variables
	//**********************************************************
	
	static final Object[][] contents =
	{
			// LOCALIZE THIS
			 	 {"splash_pleasewait", "Please wait..."},
				 {"splash_loadingbibles", "Loading bibles..."},
			 	 {"splash_createcontrollerwindow", "Creating Controller window..."},
				 {"ok", "Ok"},
				 {"cancel", "Cancel"},
				 {"yes", "Yes"},
				 {"no", "No"},
		         {"cw_title", "Bible Presenter"},
				 {"cw_versselection", "Vers Selection"},
				 {"cw_bible", "Bible"},
				 {"cw_book", "Book"},
				 {"cw_chapter", "Chapter"},
				 {"cw_startvers", "Start vers"},
				 {"cw_endvers", "End vers"},
				 {"cw_preview", "Text preview"},
				 {"cw_presentationtoolbar", "Presenter window"},
				 {"cw_show", "Show / Update"},
				 {"cw_show_mnemonic", new Integer(KeyEvent.VK_S)},
				 {"cw_show_tooltip", "Shows selected vers(es) in presenter window"},
				 {"cw_hide", "Hide"},
				 {"cw_hide_tooltip", "Hides presenter window"},
				 {"cw_hide_mnemonic", new Integer(KeyEvent.VK_H)},
				 {"cw_previous_tooltip", "Display previous vers(es)"},
				 {"cw_previous_alttext", "Previous"},
				 {"cw_next_tooltip", "Display next vers(es)"},
				 {"cw_next_alttext", "Next"},
				 {"cw_nextbookmark_tooltip", "Display next bookmark"},
				 {"cw_nextbookmark_alttext", "Next bookmark"},
				 {"cw_previousbookmark_tooltip", "Display previous bookmark"},
				 {"cw_previousbookmark_alttext", "Previous bookmark"},
				 {"cw_ex_resourcenotfound", "Resource not found: "},
				 {"cw_menu_file", "File"},
				 {"cw_menu_file_mnemonic", new Integer(KeyEvent.VK_F)},
				 {"cw_menu_file_import", "Import a new bible"},
				 {"cw_menu_file_import_mnemonic", new Integer(KeyEvent.VK_I)},
				 {"cw_menu_file_remove", "Remove a bible"},
				 {"cw_menu_file_remove_mnemonic", new Integer(KeyEvent.VK_R)},
				 {"cw_menu_file_exit", "Exit"},
				 {"cw_menu_file_exit_mnemonic", new Integer(KeyEvent.VK_X)},
				 {"cw_menu_window", "Window"},
				 {"cw_menu_window_mnemonic", new Integer(KeyEvent.VK_W)},
				 {"cw_menu_window_settings", "Preferences ..."},
				 {"cw_menu_window_settings_mnemonic", new Integer(KeyEvent.VK_P)},
				 {"cw_menu_presentation", "Presentation"},
				 {"cw_menu_presentation_mnemonic", new Integer(KeyEvent.VK_P)},
				 {"cw_menu_presentation_show", "Show"},
				 {"cw_menu_presentation_show_mnemonic", new Integer(KeyEvent.VK_S)},
				 {"cw_menu_presentation_hide", "Hide"},
				 {"cw_menu_presentation_hide_mnemonic", new Integer(KeyEvent.VK_H)},
				 {"cw_menu_presentation_next", "Next verses"},
				 {"cw_menu_presentation_next_mnemonic", new Integer(KeyEvent.VK_N)},
				 {"cw_menu_presentation_previous", "Previous verses"},
				 {"cw_menu_presentation_previous_mnemonic", new Integer(KeyEvent.VK_P)},
				 {"cw_menu_presentation_nextbookmark", "Next bookmark"},
				 {"cw_menu_presentation_nextbookmark_mnemonic", new Integer(KeyEvent.VK_E)},
				 {"cw_menu_presentation_previousbookmark", "Previous bookmark"},
				 {"cw_menu_presentation_previousbookmark_mnemonic", new Integer(KeyEvent.VK_V)},
				 {"cw_menu_bookmark", "Bookmark"},
				 {"cw_menu_bookmark_mnemonic", new Integer(KeyEvent.VK_B)},
				 {"cw_menu_bookmark_add", "Add"},
				 {"cw_menu_bookmark_add_mnemonic", new Integer(KeyEvent.VK_A)},
				 {"cw_menu_bookmark_new", "New..."},
				 {"cw_menu_bookmark_new_mnemonic", new Integer(KeyEvent.VK_N)},
				 {"cw_menu_bookmark_remove", "Remove"},
				 {"cw_menu_bookmark_remove_mnemonic", new Integer(KeyEvent.VK_R)},
				 {"cw_menu_bookmark_up", "Up"},
				 {"cw_menu_bookmark_up_mnemonic", new Integer(KeyEvent.VK_U)},
				 {"cw_menu_bookmark_down", "Down"},
				 {"cw_menu_bookmark_down_mnemonic", new Integer(KeyEvent.VK_D)},
				 {"cw_menu_help", "Help"},
				 {"cw_menu_help_mnemonic", new Integer(KeyEvent.VK_H)},
				 {"cw_menu_help_topics", "Topics..."},
				 {"cw_menu_help_topics_mnemonic", new Integer(KeyEvent.VK_T)},
				 {"cw_menu_help_web", "BiblePresenter in the Internet"},
				 {"cw_menu_help_web_mnemonic", new Integer(KeyEvent.VK_I)},
				 {"cw_menu_help_about", "About"},
				 {"cw_menu_help_about_mnemonic", new Integer(KeyEvent.VK_A)},
				 {"pref_title", "Preferences"},
				 {"pref_presenterwindowpanel_title", "Presenter Window"},
				 {"pref_controllerwindowpanel_title", "Controller Window"},
				 {"pref_backgroundcolor", "Background color"},
				 {"pref_presenterwindow_vers", "Verses"},
				 {"pref_presenterwindow_versnumber", "Vers Numbers"},
				 {"pref_presenterwindow_bibleposition", "Bible Position Display"},
				 {"pref_displaybibleposition", "Display bible position"},
				 {"pref_presenterwindow_color", "Color"},
				 {"pref_presenterwindow_font", "Font name"},
				 {"pref_presenterwindow_size", "Font size"},
				 {"pref_superscript", "Superscript"},
				 {"pref_versoffset", "Default Vers Offset"},
				 {"pref_linespacing", "Line Spacing"},
				 {"pref_fittexttowindow", "Adapt text to window size if excessing height"},
				 {"pref_savepositiononexit", "Remember bible position when closing"},
				 {"pref_language", "Language"},
				 {"pref_displayonsecscreendevice", "Display on secondary screen device (if available)"},
				 {"pref_restart_necessary", "You need to restart the program for this change to take effect."},
				 {"pref_restart_necessary_title", "Message"},
				 {"bookmarks", "Bookmarks"},
				 {"cw_down_tooltip", "Move selected bookmark element down"},
				 {"cw_down_alttext", "Down"},
				 {"cw_up_tooltip", "Move selected bookmark element up"},
				 {"cw_up_alttext", "Up"},
				 {"cw_add_tooltip", "Add selected bible verses"},
				 {"cw_add_alttext", "Add"},
				 {"cw_remove_tooltip", "Remove selected bookmark element"},
				 {"cw_remove_alttext", "Remove"},
				 {"cw_new_tooltip", "Create new bookmark folder"},
				 {"cw_new_alttext", "New"},
				 {"filechooser_title", "Select a valid bible XML file"},
				 {"bm_titlemessage", "Title:"},
				 {"bm_titledialogtitle", "New bookmark folder"},
				 {"error_invalidbookmark_title", "Invalid Bookmark"},
				 {"error_invalidbookmark_line1", "Cannot load verses:"},
				 {"error_invalidbookmark_line2", "Removing bookmark!"},
				 {"warning_select_node", "Please select a bookmark object first."},
				 {"warning_empty_title", "Please type a title."},
				 {"common_error_title", "Common error"},
				 {"common_error", "A common error has happened:"},
				 {"info_onlyonebibleleft", "There is only one bible left.\nCannot remove them."},
				 {"info_onlyonebibleleft_title", "The last bible"},
				 {"question_bibletoremove", "Please select bible to remove."},
				 {"question_bibletoremove_title", "Remove bible"},
				 {"error_needvalidbibleforfirststart", "Need a valid bible XML file for first start of program!\nDownload bibles at http://sourceforge.net/projects/zefania-sharp/\nWill exit."},
		    // END OF MATERIAL TO LOCALIZE
	};
}
