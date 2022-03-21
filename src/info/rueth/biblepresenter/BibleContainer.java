/*
 * Created on Apr 7, 2004
 * $Id: BibleContainer.java,v 1.4 2004/04/27 17:19:10 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Keeps and maintains all loaded bibles.
 * @author Ulrich Rueth
 */
public class BibleContainer implements Serializable
{
	//**********************************************************
	// Constructors
	//**********************************************************
	
	/**
	 * Sole constructor.
	 */
	public BibleContainer()
	{
		this.bibles = new LinkedHashMap();
		this.namesMapping = new LinkedHashMap();
	}
	
	//**********************************************************
	// Public methods
	//**********************************************************
	
	/**
	 * Adds a bible to the container.
	 * @param id The id of the bible.
	 * @param bible The bible.
	 */
	public void addBible(String id, Bible bible)
	{
		bibles.put(id, bible);
		namesMapping.put(bible.getName(), id);
	}
	
	/**
	 * @param id The id of the bible.
	 * @return Returns the bible with the specified id.
	 */
	public Bible getBible(String id)
	{
		return (Bible)bibles.get(id);
	}
	
	/**
	 * @param bibleName The name of the bible.
	 * @return Returns the bible with the specified name.
	 */
	public Bible getBibleWithName(String bibleName)
	{
		String id = (String)namesMapping.get(bibleName);
		return (Bible)bibles.get(id);
	}
	
	/**
	 * @return Returns all loaded bibles.
	 */
	public Bible[] getBibles()
	{
		Set bibleSet = bibles.entrySet();
		Bible[] biblesArray = new Bible[bibleSet.size()];
		Object ignore = bibleSet.toArray(biblesArray);
		return biblesArray;
	}
	
	/**
	 * @return Returns the names of all loaded bibles.
	 */
	public String[] getBibleNames()
	{
		Set bibleNamesSet = namesMapping.keySet();
		String[] bibleNames = new String[bibleNamesSet.size()];
		Object ignore = bibleNamesSet.toArray(bibleNames);
		return bibleNames;
	}
	
	/**
	 * @param id The id of the bible.
	 * @return Returns true if the specified bible is in the container.
	 */
	public boolean hasBible(String id)
	{
		return bibles.containsKey(id);
	}
	
	/**
	 * @param bibleName The name of the bible.
	 * @return Returns true if the specified bible is in the container.
	 */
	public boolean hasBibleWithName(String bibleName)
	{
		return namesMapping.get(bibleName) != null;
	}
	
	/**
	 * Removes the specified bible from the container.
	 * @param id The id of the bible.
	 */
	public void removeBible(String id)
	{
		if (hasBible(id))
		{
			namesMapping.remove(getBible(id).getName());
			bibles.remove(id);
		}
	}
	
	/**
	 * @param bibleName The name of the bible.
	 * @return Returns the id of the bible with the specified name.
	 */
	public String getBibleIdWithName(String bibleName)
	{
		return (String)namesMapping.get(bibleName);
	}
	
	//**********************************************************
	// Private variables
	//**********************************************************
	
	private LinkedHashMap bibles;
	private LinkedHashMap namesMapping;
}
