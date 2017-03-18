package org.universAAL.ucc.database.listener.interfaces;

import org.universAAL.ucc.model.jaxb.OntologyInstance;

/**
 * 
 * @author Nicole Merkle
 *
 */

public interface OntologyChangedListener {
	public void ontologyDeleted(OntologyInstance ont);
	public void ontologyChanged(OntologyInstance ont);
	public void ontologySaved(OntologyInstance ont);
}
