/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.service_bus_matching;

//import java.io.IOException;
//import java.io.StringWriter;
//import org.universAAL.middleware.rdf.TypeMapper;

import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.owls.process.OutputBinding;
import org.universAAL.middleware.service.owls.process.ProcessEffect;


// just testing....
// Possible improvements: SPARQLAS, TERP
/**
 * 
 * @author Carsten Stockloew
 *
 */
public class Sparul {

    private int ppcnt = 0;
    private static final int EFF_ADD = 0;
    private static final int EFF_CHANGE = 1;
    private static final int EFF_REMOVE = 2;

    public String getSparul(ServiceRequest sr) {
	ppcnt = 0;
	StringBuilder s = new StringBuilder();
	StringBuilder where = new StringBuilder();

	// outputs
	getOutputs(s, where, sr);

	// effects
	getEffects(s, where, sr);

	// instancelevel restrictions (to be added to 'where' clause)
	getRestrictions(where, sr);

	if (s.length() != 0) {
	    // from
	    s.append("FROM ");
	    s.append(sr.getRequestedService().getType());
	    s.append("\n");

	    // where
	    String whereString = where.toString();
	    if (whereString.length() != 0) {
		s.append("WHERE {\n");
		s.append(whereString);
		s.append("  }");
	    }
	}

	return s.toString();
    }

    private void getRestrictions(StringBuilder where, ServiceRequest sr) {
	// TODO: fill the where clause with instance level restrictions
    }

    private void getOutputs(StringBuilder s, StringBuilder where,
	    ServiceRequest sr) {
	Resource[] outputs = sr.getRequiredOutputs();
	if (outputs.length == 0)
	    return;

	boolean first = true;
	for (int i = 0; i < outputs.length; i++) {
	    // get info
	    Resource output = outputs[i];
	    if (output == null)
		continue;
	    Resource bind = (Resource) output
		    .getProperty(OutputBinding.PROP_OWLS_BINDING_TO_PARAM);
	    PropertyPath pp = (PropertyPath) output
		    .getProperty(OutputBinding.PROP_OWLS_BINDING_VALUE_FORM);
	    if (bind == null || pp == null)
		continue;
	    String[] p = pp.getThePath();
	    if (p.length == 0)
		continue;

	    // create select part
	    if (first)
		s.append("SELECT");
	    first = false;
	    s.append(" ?");
	    s.append(bind.getLocalName());

	    // create property path for where-clause
	    getPP(where, p, bind.getLocalName());
	}
	if (!first)
	    s.append("\n");
	return;
    }

    private void getPP(StringBuilder s, String[] p, String name) {
	s.append("    ?x" + ppcnt++ + " ");
	for (int i = 0; i < p.length; i++) {
	    s.append(p[i]);
	    if (i < p.length - 1)
		s.append(" / ");
	}
	s.append(" ?");
	s.append(name);
	s.append("\n");
    }

    private void getEffects(StringBuilder s, StringBuilder where,
	    ServiceRequest sr) {
	Resource[] effects = sr.getRequiredEffects();
	if (effects.length == 0)
	    return;

	for (int i = 0; i < effects.length; i++) {
	    // get info
	    Resource effect = effects[i];
	    if (effect == null)
		continue;
	    PropertyPath pp = (PropertyPath) effect
		    .getProperty(ProcessEffect.PROP_PROCESS_AFFECTED_PROPERTY);
	    if (pp == null)
		continue;

	    String typeURI = effect.getType();
	    int type = 0;
	    if (ProcessEffect.TYPE_PROCESS_ADD_EFFECT.equals(typeURI)) {
		type = EFF_ADD;
	    } else if (ProcessEffect.TYPE_PROCESS_CHANGE_EFFECT.equals(typeURI)) {
		type = EFF_CHANGE;
	    } else if (ProcessEffect.TYPE_PROCESS_REMOVE_EFFECT.equals(typeURI)) {
		type = EFF_REMOVE;
	    } else
		continue;

	    Object value = effect
		    .getProperty(ProcessEffect.PROP_PROCESS_PROPERTY_VALUE);
	    if (value == null)
		if (type != EFF_REMOVE)
		    continue;

	    String[] p = pp.getThePath();
	    if (p.length == 0)
		continue;

	    // create header part
	    switch (type) {
	    case EFF_ADD:
		s.append("INSERT DATA {\n");
		s.append("    TBD\n");
		s.append("  }\n");
		break;
	    case EFF_CHANGE:
		s.append("INSERT {\n");
		s.append("    TBD\n");
		getEffectValue(s, value);
		s.append("  }\n");
		s.append("DELETE {\n");
		s.append("    TBD\n");
		s.append("  }\n");
		break;
	    case EFF_REMOVE:
		s.append("DELETE DATA {\n");
		s.append("    TBD\n");
		s.append("  }\n");
		break;
	    }

	    // create property path for where-clause
	    // getPP(where, p, bind.getLocalName());
	}
	return;
    }

    private void getEffectValue(StringBuilder s, Object val) {
	
	//Activator.serialize(val)
	
	//TurtleParser t = new TurtleParser();
	//s.append(t.serialize(val));
	

//	if (val instanceof Resource) {
//
//	} else if (val instanceof Boolean || val instanceof Double
//		|| val instanceof Integer)
//	    s.append(val.toString());
//	else {
//	    String[] pair = TypeMapper.getXMLInstance(val);
//	    writeLiteral(s, pair[0], pair[1]);
//
//	}
    }

//    private void writeLiteral(StringBuilder s, String lexicalForm,
//	    String datatype) throws IOException {
//	if (lexicalForm.indexOf('\n') > -1 || lexicalForm.indexOf('\r') > -1
//		|| lexicalForm.indexOf('\t') > -1) {
//	    // Write label as long string
//	    s.append("\"\"\"");
//	    s.append(TurtleUtil.encodeLongString(lexicalForm));
//	    s.append("\"\"\"");
//	} else {
//	    // Write label as normal string
//	    s.append("\"");
//	    s.append(TurtleUtil.encodeString(lexicalForm));
//	    s.append("\"");
//	}
//
//	if (datatype != null) {
//	    // Append the literal's datatype (possibly written as an abbreviated
//	    // URI)
//	    s.append("^^");
//	    writeURI(datatype);
//	}
//    }

}
