package org.universAAL.ucc.database.preferences;

import org.universAAL.ucc.model.preferences.Preferences;

/**
 * Database operations on user account information and persistence of user
 * preferences.
 * 
 * @author Nicole Merkle
 *
 */
public interface UserAccountDB {
	/**
	 * Saves access information for uStore.
	 * 
	 * @param pref
	 *            a instance of preference data
	 * @param filepath
	 *            the property file for persisting the preferences
	 */
	public void saveStoreAccessData(Preferences pref, String filepath);

	/**
	 * Gets the preferences data
	 * 
	 * @param file
	 *            file where the current data is stored
	 * @return a preference instance
	 */
	public Preferences getPreferencesData(String file);

	/**
	 * Updates the Preference.
	 */
	public void updatePreferencesData();
}
