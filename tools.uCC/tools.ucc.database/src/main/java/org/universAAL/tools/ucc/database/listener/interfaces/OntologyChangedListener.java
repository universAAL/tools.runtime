package org.universAAL.tools.ucc.database.listener.interfaces;

import org.universAAL.tools.ucc.model.jaxb.OntologyInstance;

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
