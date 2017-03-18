/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.owl.Intersection;
import org.universAAL.middleware.owl.OntClassInfo;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.PropertyRestriction;
import org.universAAL.middleware.owl.TypeExpression;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.owl.Service;
import org.universAAL.middleware.service.owls.process.OutputBinding;
import org.universAAL.middleware.service.owls.process.ProcessEffect;
import org.universAAL.middleware.service.owls.process.ProcessInput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.tools.logmonitor.Activator;
import org.universAAL.tools.logmonitor.service_bus_matching.URI;

/**
 * HTML-based Pane that handles bus operations, i.e. service profiles/requests
 * and context events/event patterns.
 * 
 * @author Carsten Stockloew
 * 
 */
public class HTMLBusOperationsPane extends HTMLVisibilityPane {
    private static final long serialVersionUID = 1L;

    /**
     * Get the html-code for all representations (serialized, abstract..) of a
     * context event pattern.
     * 
     * @param s
     * @param info
     * @param profileURI
     */
    protected void getAllContextEventPatternHTML(StringBuilder s,
	    PatternInfo info) {
	ContextEventPattern cep = info.pattern;
	String link = "ContextEventPattern" + cep.getURI();
	String link_abstract = link + "_abstract";
	String link_serialized = link + "_serialized";

	// ServiceProfile serialized
	if (isVisible(link_serialized)) {
	    s.append(getLinkHTML(link_serialized, "hide serialized"));

	    if (info.serialized == null) // create on first use
		info.serialized = Activator.serialize(cep);
	    s.append("<pre>\n" + turtle2HTML(info.serialized) + "\n</pre>\n");
	} else {
	    s.append(getLinkHTML(link_serialized, "show serialized"));
	}

	// ServiceProfile abstract
	if (isVisible(link_abstract)) {
	    s.append(getLinkHTML(link_abstract, "hide abstract<br><br>\n"));
	    getContextEventPatternHTML(s, info);
	} else {
	    s.append(getLinkHTML(link_abstract, "show abstract"));
	}
    }

    private List<PropertyRestriction> getRestrictions(ContextEventPattern cep,
	    String onProperty) {
	List<PropertyRestriction> lst = new ArrayList<PropertyRestriction>();
	Object o = cep.getProperty(TypeExpression.PROP_RDFS_SUB_CLASS_OF);
	if (o == null)
	    return lst;
	if (!(o instanceof List))
	    return lst;
	for (Object el : (List<?>) o) {
	    if (el instanceof PropertyRestriction) {
		PropertyRestriction pr = (PropertyRestriction) el;
		if (onProperty.equals(pr.getOnProperty())) {
		    lst.add(pr);
		}
	    }
	}
	return lst;
    }

    private String getCEPSerialization(ContextEventPattern cep, String prop) {
	String ret = "";
	List<PropertyRestriction> lst = getRestrictions(cep, prop);
	if (lst == null)
	    return "-";
	if (lst.size() == 0) {
	    return "-";
	} else if (lst.size() == 1) {
	    ret = Activator.serialize(lst.get(0));
	} else {
	    Intersection i = new Intersection();
	    for (PropertyRestriction p : lst)
		i.addType(p);
	    ret = Activator.serialize(i);
	}
	return "<pre>\n" + turtle2HTML(ret) + "\n</pre>\n";
    }

    protected void getContextEventPatternHTML(StringBuilder s, PatternInfo info) {
	// get context event pattern in an abstract view
	s.append("<b>Context Event Pattern</b><br>");

	// create on first use
	ContextEventPattern cep = info.pattern;
	if (info.serializedSubject == null)
	    info.serializedSubject = getCEPSerialization(cep,
		    ContextEvent.PROP_RDF_SUBJECT);
	if (info.serializedPredicate == null)
	    info.serializedPredicate = getCEPSerialization(cep,
		    ContextEvent.PROP_RDF_PREDICATE);
	if (info.serializedObject == null)
	    info.serializedObject = getCEPSerialization(cep,
		    ContextEvent.PROP_RDF_OBJECT);

	s.append(getTableStartHTML());
	s.append(getVTableRowWithTitleHTML("subject", info.serializedSubject));
	s.append(getVTableRowWithTitleHTML("predicate",
		info.serializedPredicate));
	s.append(getVTableRowWithTitleHTML("object", info.serializedObject));
	s.append(getTableEndHTML());
	s.append("<br>\n");
    }

    protected String getServiceOutputHTML(Resource output) {
	StringBuilder s = new StringBuilder("");

	Object form = output
		.getProperty(OutputBinding.PROP_OWLS_BINDING_VALUE_FORM);

	s.append("output:");
	s.append(getTableStartHTML());
	s.append(getTableRowHTML("parameter binding:",
		getURIHTML(((Resource) output
			.getProperty(OutputBinding.PROP_OWLS_BINDING_TO_PARAM))
			.getURI())));
	if (form == null)
	    s.append(getTableRowHTML("--",
		    "- unknown value, perhaps a unit conversion? -"));
	else
	    s.append(getTableRowHTML("at the property path:",
		    getPropPathHTML((PropertyPath) form, false)));
	s.append(getTableEndHTML());

	return s.toString();
    }

    protected String getServiceEffectHTML(Resource effect) {
	StringBuilder s = new StringBuilder("");

	String type = effect.getType();
	if (ProcessEffect.TYPE_PROCESS_ADD_EFFECT.equals(type)) {
	    s.append("Add effect:");
	    s.append(getTableStartHTML());
	    s.append(getTableRowHTML(
		    "add the value:",
		    effect.getProperty(
			    ProcessEffect.PROP_PROCESS_PROPERTY_VALUE)
			    .toString()));
	    s.append(getTableRowHTML(
		    "to the property path:",
		    getPropPathHTML(
			    (PropertyPath) effect
				    .getProperty(ProcessEffect.PROP_PROCESS_AFFECTED_PROPERTY),
			    false)));
	    s.append(getTableEndHTML());
	} else if (ProcessEffect.TYPE_PROCESS_CHANGE_EFFECT.equals(type)) {
	    s.append("Change effect:");
	    s.append(getTableStartHTML());
	    s.append(getTableRowHTML("change the value to:", effect
		    .getProperty(ProcessEffect.PROP_PROCESS_PROPERTY_VALUE)
		    .toString()));
	    s.append(getTableRowHTML(
		    "at property path:",
		    getPropPathHTML(
			    (PropertyPath) effect
				    .getProperty(ProcessEffect.PROP_PROCESS_AFFECTED_PROPERTY),
			    false)));
	    s.append(getTableEndHTML());
	} else if (ProcessEffect.TYPE_PROCESS_REMOVE_EFFECT.equals(type)) {
	    s.append("Remove effect:");
	    s.append(getTableStartHTML());
	    s.append(getTableRowHTML(
		    "at property path:",
		    getPropPathHTML(
			    (PropertyPath) effect
				    .getProperty(ProcessEffect.PROP_PROCESS_AFFECTED_PROPERTY),
			    false)));
	    s.append(getTableEndHTML());
	}
	return s.toString();
    }

    private void getRestrictionsHTML(StringBuilder s, List<?> res) {
	// List<?> lst = (List<PropertyRestriction>) res;
	s.append("<br>\n- TODO ;-) but there are restrictions..<br>\n");
    }

    private HashSet<String> getNamedSuperClasses(Resource r) {
	HashSet<String> classes = new HashSet<String>();

	String types[] = r.getTypes();
	for (int i = 0; i < types.length; i++) {
	    String type = types[i];
	    classes.add(type);

	    OntClassInfo info = OntologyManagement.getInstance()
		    .getOntClassInfo(type);
	    if (info != null) {
		String superTypes[] = info.getNamedSuperClasses(true, false);
		for (int j = 0; j < superTypes.length; j++)
		    classes.add(superTypes[j]);
	    }
	}

	return classes;
    }

    /**
     * Get common parts (effects and outputs) of service profile and service
     * request as HTML.
     */
    private void getServiceCommonHTML(StringBuilder s, Resource[] effects,
	    Resource[] outputs) {
	int i;
	s.append("<b>Effects:</b>");
	if (effects.length == 0) {
	    s.append(" <i>no effects defined</i><br>\n");
	} else {
	    s.append("<br>\n");
	    for (i = 0; i < effects.length; i++)
		s.append(getServiceEffectHTML(effects[i]));
	    s.append("<br>\n");
	}

	s.append("<b>Outputs:</b>");
	if (outputs.length == 0) {
	    s.append(" <i>no outputs defined</i><br>\n");
	} else {
	    s.append("<br>\n");
	    for (i = 0; i < outputs.length; i++)
		s.append(getServiceOutputHTML(outputs[i]));
	    s.append("<br>\n");
	}
    }

    protected void getServiceProfileHTML(StringBuilder s, ServiceProfile prof) {
	// get service profile in an abstract view
	s.append("<b>Service profile:</b> ");
	s.append(getURIHTML(prof.getTheService().getURI()));

	s.append("<br><b>Service ontology class hierarchy:</b> ");
	s.append(getTableStartHTML());
	HashSet<String> types = getNamedSuperClasses(prof.getTheService());
	for (Iterator<String> i = types.iterator(); i.hasNext();)
	    s.append(getTableRowHTML(getURIHTML(URI.get(i.next(), false))));
	s.append(getTableEndHTML());
	s.append("<br>\n");

	s.append("<b>Inputs:</b>");
	Iterator<?> it = prof.getInputs();
	if (!it.hasNext()) {
	    s.append(" <i>no inputs defined</i><br>\n");
	} else {
	    s.append("<br>\n");
	    while (it.hasNext()) {
		ProcessInput in = (ProcessInput) it.next();
		s.append("input:");
		s.append(getTableStartHTML());
		s.append(getTableRowHTML("URI:",
			getURIHTML(URI.get(in.getURI(), false))));
		String type = in.getParameterType();
		s.append(getTableRowHTML("type:",
			getURIHTML(URI.get(type, false))));
		int minCard = in.getMinCardinality();
		int maxCard = in.getMaxCardinality();
		if (minCard == maxCard)
		    s.append(getTableRowHTML("exact cardinality:", "" + maxCard));
		else {
		    s.append(getTableRowHTML("min cardinality:", "" + minCard));
		    s.append(getTableRowHTML("max cardinality:", "" + maxCard));
		}
		s.append(getTableEndHTML());
	    }
	    s.append("<br>\n");
	}

	Resource[] effects = prof.getEffects();
	Resource[] outputs = prof.getOutputBindings();
	getServiceCommonHTML(s, effects, outputs);
    }

    protected void getServiceRequestHTML(StringBuilder s, ServiceRequest req,
	    String serviceURI) {
	// get service request in an abstract view
	s.append("<b>Requested service:</b> ");
	s.append(getURIHTML(serviceURI));

	s.append("<br><b>Filtering input:</b>");
	Object o = req.getRequestedService().getProperty(
		Service.PROP_INSTANCE_LEVEL_RESTRICTIONS);
	if (o instanceof List<?>) {
	    getRestrictionsHTML(s, (List<?>) o);
	} else
	    s.append(" <i>no filtering input defined</i><br>\n");

	Resource effects[] = req.getRequiredEffects();
	Resource outputs[] = req.getRequiredOutputs();
	getServiceCommonHTML(s, effects, outputs);
    }

    /**
     * Get the html-code for all representations (serialized, abstract..) of a
     * service request.
     */
    protected void getAllServiceRequestHTML(StringBuilder s,
	    ServiceRequest req, String serviceURI, String serializedRequest) {
	// details for request: serialized
	if (isVisible("requestSerialized")) {
	    s.append(getLinkHTML("requestSerialized", "hide serialized"));
	    s.append("<pre>\n" + turtle2HTML(serializedRequest) + "\n</pre>\n");
	} else {
	    s.append(getLinkHTML("requestSerialized", "show serialized"));
	}

	// details for request: abstract
	if (isVisible("requestAbstract")) {
	    s.append(getLinkHTML("requestAbstract", "hide abstract<br><br>\n"));
	    getServiceRequestHTML(s, req, serviceURI);
	} else {
	    s.append(getLinkHTML("requestAbstract", "show abstract"));
	}
    }

    /**
     * Get the html-code for all representations (serialized, abstract..) of a
     * service profile.
     * 
     * @param s
     * @param info
     * @param profileURI
     */
    protected void getAllServiceProfileHTML(StringBuilder s, ProfileInfo info) {
	ServiceProfile profile = info.profile;
	String link = "ServiceProfile_" + info.serviceURI;
	String link_abstract = link + "_abstract";
	String link_serialized = link + "_serialized";

	// ServiceProfile serialized
	if (isVisible(link_serialized)) {
	    s.append(getLinkHTML(link_serialized, "hide serialized"));

	    if (info.serialized == null) // create on first use
		info.serialized = Activator.serialize(profile);
	    s.append("<pre>\n" + turtle2HTML(info.serialized) + "\n</pre>\n");
	} else {
	    s.append(getLinkHTML(link_serialized, "show serialized"));
	}

	// ServiceProfile abstract
	if (isVisible(link_abstract)) {
	    s.append(getLinkHTML(link_abstract, "hide abstract<br><br>\n"));
	    getServiceProfileHTML(s, profile);
	} else {
	    s.append(getLinkHTML(link_abstract, "show abstract"));
	}
    }
}
