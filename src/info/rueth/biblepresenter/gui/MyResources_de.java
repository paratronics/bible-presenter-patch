/*
 * Created on Apr 12, 2004
 * $Id: MyResources_de.java,v 1.12 2004/05/03 20:29:15 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;

import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;

/**
 * The german resource file.
 * @author Ulrich Rueth
 */
public class MyResources_de extends ListResourceBundle
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
				 {"splash_pleasewait", "Bitte warten..."},
			 	 {"splash_loadingbibles", "Lade Bibeln..."},
			 	 {"splash_createcontrollerwindow", "Baue Steuerungsfenster auf..."},
			 	 {"ok", "Ok"},
				 {"cancel", "Abbrechen"},
				 {"yes", "Ja"},
				 {"no", "Nein"},
				 {"cw_versselection", "Versauswahl"},
		         {"cw_title", "Bibeltextanzeige"},
				 {"cw_bible", "Bibel"},
				 {"cw_book", "Buch"},
				 {"cw_chapter", "Kapitel"},
				 {"cw_startvers", "Startvers"},
				 {"cw_endvers", "Endvers"},
				 {"cw_preview", "Textvorschau"},
				 {"cw_presentationtoolbar", "Präsentationsfenster"},
				 {"cw_show", "Anzeigen / Auffrischen"},
				 {"cw_show_mnemonic", new Integer(KeyEvent.VK_Z)},
				 {"cw_show_tooltip", "Zeigt den ausgewählten Bibeltext im Präsentationsfenster an"},
				 {"cw_hide", "Ausblenden"},
				 {"cw_hide_tooltip", "Blendet das Präsentationsfenster aus"},
				 {"cw_hide_mnemonic", new Integer(KeyEvent.VK_A)},
				 {"cw_previous_tooltip", "Vorherige(n) Vers(e) anzeigen"},
				 {"cw_previous_alttext", "Zurück"},
				 {"cw_next_tooltip", "Nächste(n) Vers(e) anzeigen"},
				 {"cw_next_alttext", "Vor"},
				 {"cw_nextbookmark_tooltip", "Nächstes Lesezeichen anzeigen"},
				 {"cw_nextbookmark_alttext", "Nächstes Lesezeichen"},
				 {"cw_previousbookmark_tooltip", "Vorheriges Lesezeichen anzeigen"},
				 {"cw_previousbookmark_alttext", "Vorheriges Lesezeichen"},
				 {"cw_ex_resourcenotfound", "Ressource nicht gefunden: "},
				 {"cw_menu_file", "Datei"},
				 {"cw_menu_file_mnemonic", new Integer(KeyEvent.VK_D)},
				 {"cw_menu_file_import", "Neue Bibel importieren"},
				 {"cw_menu_file_import_mnemonic", new Integer(KeyEvent.VK_I)},
				 {"cw_menu_file_remove", "Bibel entfernen"},
				 {"cw_menu_file_remove_mnemonic", new Integer(KeyEvent.VK_R)},
				 {"cw_menu_file_exit", "Beenden"},
				 {"cw_menu_file_exit_mnemonic", new Integer(KeyEvent.VK_B)},
				 {"cw_menu_window", "Fenster"},
				 {"cw_menu_window_mnemonic", new Integer(KeyEvent.VK_F)},
				 {"cw_menu_window_settings", "Einstellungen ..."},
				 {"cw_menu_window_settings_mnemonic", new Integer(KeyEvent.VK_E)},
				 {"cw_menu_presentation", "Präsentation"},
				 {"cw_menu_presentation_mnemonic", new Integer(KeyEvent.VK_P)},
				 {"cw_menu_presentation_show", "Anzeigen / Auffrischen"},
				 {"cw_menu_presentation_show_mnemonic", new Integer(KeyEvent.VK_Z)},
				 {"cw_menu_presentation_hide", "Ausblenden"},
				 {"cw_menu_presentation_hide_mnemonic", new Integer(KeyEvent.VK_A)},
				 {"cw_menu_presentation_next", "Nächste Verse"},
				 {"cw_menu_presentation_next_mnemonic", new Integer(KeyEvent.VK_N)},
				 {"cw_menu_presentation_previous", "Vorherige Verse"},
				 {"cw_menu_presentation_previous_mnemonic", new Integer(KeyEvent.VK_V)},
				 {"cw_menu_presentation_nextbookmark", "Nächstes Lesezeichen"},
				 {"cw_menu_presentation_nextbookmark_mnemonic", new Integer(KeyEvent.VK_S)},
				 {"cw_menu_presentation_previousbookmark", "Vorheriges Lesezeichen"},
				 {"cw_menu_presentation_previousbookmark_mnemonic", new Integer(KeyEvent.VK_O)},
				 {"cw_menu_bookmark", "Lesezeichen"},
				 {"cw_menu_bookmark_mnemonic", new Integer(KeyEvent.VK_L)},
				 {"cw_menu_bookmark_add", "Hinzufügen"},
				 {"cw_menu_bookmark_add_mnemonic", new Integer(KeyEvent.VK_H)},
				 {"cw_menu_bookmark_new", "Neuer Ordner..."},
				 {"cw_menu_bookmark_new_mnemonic", new Integer(KeyEvent.VK_N)},
				 {"cw_menu_bookmark_remove", "Löschen"},
				 {"cw_menu_bookmark_remove_mnemonic", new Integer(KeyEvent.VK_L)},
				 {"cw_menu_bookmark_up", "Nach oben"},
				 {"cw_menu_bookmark_up_mnemonic", new Integer(KeyEvent.VK_O)},
				 {"cw_menu_bookmark_down", "Nach unten"},
				 {"cw_menu_bookmark_down_mnemonic", new Integer(KeyEvent.VK_U)},
				 {"cw_menu_help", "Hilfe"},
				 {"cw_menu_help_mnemonic", new Integer(KeyEvent.VK_H)},
				 {"cw_menu_help_topics", "Hilfethemen..."},
				 {"cw_menu_help_topics_mnemonic", new Integer(KeyEvent.VK_T)},
				 {"cw_menu_help_web", "BiblePresenter im Internet"},
				 {"cw_menu_help_web_mnemonic", new Integer(KeyEvent.VK_I)},
				 {"cw_menu_help_about", "Über..."},
				 {"cw_menu_help_about_mnemonic", new Integer(KeyEvent.VK_B)},
				 {"pref_title", "Einstellungen"},
				 {"pref_presenterwindowpanel_title", "Präsentationsfenster"},
				 {"pref_controllerwindowpanel_title", "Steuerungsfenster"},
				 {"pref_backgroundcolor", "Hintergrundfarbe"},
				 {"pref_presenterwindow_vers", "Verse"},
				 {"pref_presenterwindow_versnumber", "Versnummern"},
				 {"pref_presenterwindow_bibleposition", "Bibelpositionsanzeige"},
				 {"pref_displaybibleposition", "Bibelposition anzeigen"},
				 {"pref_presenterwindow_color", "Farbe"},
				 {"pref_presenterwindow_font", "Schriftart"},
				 {"pref_presenterwindow_size", "Schriftgröße"},
				 {"pref_superscript", "Hochgestellt"},
				 {"pref_versoffset", "Standardversabstand"},
				 {"pref_linespacing", "Zeilenabstand"},
				 {"pref_fittexttowindow", "Text bei Überlänge an Fenstergröße anpassen"},
				 {"pref_savepositiononexit", "Bibelstelle beim Beenden merken"},
				 {"pref_language", "Sprache"},
				 {"pref_displayonsecscreendevice", "Auf externem Monitor anzeigen (falls vorhanden)"},
				 {"pref_restart_necessary", "Sie müssen das Programm neu starten,\ndamit diese Auswahl wirksam wird."},
				 {"pref_restart_necessary_title", "Hinweis"},
				 {"bookmarks", "Lesezeichen"},
				 {"cw_down_tooltip", "Ausgewähltes Lesezeichenelement nach unten verschieben"},
				 {"cw_down_alttext", "Runter"},
				 {"cw_up_tooltip", "Ausgewähltes Lesezeichenelement nach oben verschieben"},
				 {"cw_up_alttext", "Nach oben"},
				 {"cw_add_tooltip", "Ausgewählte Bibelstelle hinzufügen"},
				 {"cw_add_alttext", "Hinzu"},
				 {"cw_remove_tooltip", "Ausgewähltes Lesezeichenelement löschen"},
				 {"cw_remove_alttext", "Löschen"},
				 {"cw_new_tooltip", "Neuen Ordner hinzufügen"},
				 {"cw_new_alttext", "Neu"},
				 {"filechooser_title", "Eine gültige Bibel-XML-Datei wählen"},
				 {"bm_titlemessage", "Titel:"},
				 {"bm_titledialogtitle", "Neuer Lesezeichenordner"},
				 {"error_invalidbookmark_title", "Ungültiges Lesezeichen"},
				 {"error_invalidbookmark_line1", "Kann folgende Verse nicht laden:"},
				 {"error_invalidbookmark_line2", "Lesezeichen wird entfernt!"},
				 {"warning_select_node", "Bitte zuerst ein Lesezeichenobjekt auswählen."},
				 {"warning_empty_title", "Bitte Titel eingeben."},
				 {"common_error_title", "Allgemeiner Fehler"},
				 {"common_error", "Ein Fehler ist aufgetreten:"},
				 {"info_onlyonebibleleft", "Es ist nur noch eine Bibel geladen.\nKann diese nicht entfernen."},
				 {"info_onlyonebibleleft_title", "Die letzte Bibel"},
				 {"question_bibletoremove", "Bitte zu entfernende Bibel wählen."},
				 {"question_bibletoremove_title", "Bibel entfernen"},
				 {"error_needvalidbibleforfirststart", "Gültige Bibel-XML-Datei benötigt für ersten Start des Programms.\nLaden Sie Bibeln herunter unter http://sourceforge.net/projects/zefania-sharp/\nBeende Programm."},
		    // END OF MATERIAL TO LOCALIZE
	};
}
