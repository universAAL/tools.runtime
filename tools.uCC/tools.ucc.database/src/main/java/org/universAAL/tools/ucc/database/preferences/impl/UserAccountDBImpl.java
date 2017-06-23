package org.universAAL.tools.ucc.database.preferences.impl;

import java.io.File;

import javax.xml.bind.JAXB;

import org.universAAL.tools.ucc.database.preferences.UserAccountDB;
import org.universAAL.tools.ucc.model.preferences.Preferences;

/**
 * 
 * @author Nicole Merkle
 *
 */
public class UserAccountDBImpl implements UserAccountDB {

	public void saveStoreAccessData(Preferences pref, String filepath) {
		JAXB.marshal(pref, new File(filepath));

	}

	public Preferences getPreferencesData(String file) {
		Preferences pref = JAXB.unmarshal(new File(file), Preferences.class);
		return pref;
	}

	public void updatePreferencesData() {
		// TODO Auto-generated method stub

	}

}
