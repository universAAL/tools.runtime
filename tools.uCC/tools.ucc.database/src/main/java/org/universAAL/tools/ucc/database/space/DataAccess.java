package org.universAAL.tools.ucc.database.space;

import java.util.ArrayList;
import java.util.HashMap;

import org.universAAL.tools.ucc.model.jaxb.OntologyInstance;
import org.universAAL.tools.ucc.model.jaxb.Subprofile;

/**
 * DataAccess allows database operations to CHE and JAXB XML files
 * 
 * @author Nicole Merkle
 *
 */

public interface DataAccess {
	// For JAXB usage
	/**
	 * Updates User in XML representation
	 * 
	 * @param file
	 *            xml file to update
	 * @param id
	 *            ID of the user
	 * @param subprofiles
	 *            A list of updated Subprofiles
	 */
	public void updateUserData(String file, String id, HashMap<String, ArrayList<Subprofile>> subprofiles);

	/**
	 * Gets the Datafields of a XML instance to generate a view for
	 * userinterface
	 * 
	 * @param file
	 *            xml file, which is unmarshalled
	 * @return a list of ontology instances
	 */
	public ArrayList<OntologyInstance> getFormFields(String file);

	/**
	 * Deletes a User from the XML file.
	 * 
	 * @param file
	 *            file where the user is persisted.
	 * @param instance
	 *            the user as instance
	 * @return the result of the delete operation
	 */
	public boolean deleteUserData(String file, String instance);

	/**
	 * Saves a new Userprofile
	 * 
	 * @param file
	 *            xml file in which to save
	 * @param ontologyInstnce
	 *            the Userinstance for saving
	 * @return the result of the save operation
	 */
	public boolean saveUserData(String file, OntologyInstance ontologyInstnce);

	/**
	 * Gets an empty ontology profile to create a user interface
	 * 
	 * @param profile
	 *            the xml file with the ontology profile
	 * @return a list with ontology instances
	 */
	public ArrayList<OntologyInstance> getEmptyProfile(String profile);

	// For CHE usage
	/**
	 * Saves user in CHE.
	 * 
	 * @param ont
	 *            the user instance
	 */
	public void saveUserDataInCHE(OntologyInstance ont);

	/**
	 * Gets empty ontology instances for creating a user interface
	 * 
	 * @param instance
	 *            the type (user/device) of ontology instance
	 * @return a list of ontology instances
	 */
	public ArrayList<OntologyInstance> getEmptyCHEFormFields(String instance);

	/**
	 * Updates a given user in CHE
	 * 
	 * @param id
	 *            Id of the user
	 * @param subprofiles
	 *            the Subprofiles of the user
	 */
	public void updateUserData(String id, HashMap<String, ArrayList<Subprofile>> subprofiles);

	/**
	 * Deletes a given user from CHE
	 * 
	 * @param instance
	 *            the user, who will be deleted
	 */
	public void deleteUserDataInChe(String instance);

}
