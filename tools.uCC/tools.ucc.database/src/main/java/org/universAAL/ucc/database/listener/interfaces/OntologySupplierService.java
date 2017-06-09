package org.universAAL.ucc.database.listener.interfaces;

import java.util.ArrayList;

import org.universAAL.ucc.model.jaxb.OntologyInstance;

public interface OntologySupplierService {
	public void addListener(OntologyChangedListener listener);

	public void removeListener(OntologyChangedListener listener);

	public ArrayList<OntologyInstance> getOntology(String ontologyFile);

}
