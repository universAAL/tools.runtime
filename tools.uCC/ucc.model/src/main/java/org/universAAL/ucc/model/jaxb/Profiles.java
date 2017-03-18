package org.universAAL.ucc.model.jaxb;

import java.util.ArrayList;
import javax.xml.bind.annotation.*;

@XmlRootElement(namespace="de.fzi.ipe.jcc.model.jaxb", name = "profiles")
public class Profiles {
	
	private ArrayList<OntologyInstance> ontologyInstances = new ArrayList<OntologyInstance>();
	
	@XmlElementWrapper(name="ontologyInstances")
	@XmlElement(name="ontologyInstance")
	public ArrayList<OntologyInstance> getOntologyInstances() {
		return ontologyInstances;
	}

	public void setOntologyInstances(ArrayList<OntologyInstance> ontologyInstances) {
		this.ontologyInstances = ontologyInstances;
	}
	
	

}
