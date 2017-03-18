/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.util;

import org.universAAL.middleware.context.ContextEventPattern;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class PatternInfo {

    public ContextEventPattern pattern;

    public String serialized = null;
    public String serializedSubject = null;
    public String serializedPredicate = null;
    public String serializedObject = null;

    public PatternInfo(ContextEventPattern pattern) {
	this.pattern = pattern;
    }
}
