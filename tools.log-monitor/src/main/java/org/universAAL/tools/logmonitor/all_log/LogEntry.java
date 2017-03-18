/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.all_log;

import java.util.Date;

/**
 * 
 * @author Carsten Stockloew
 */
public class LogEntry {

    public int logLevel;
    public String module;
    public String pkg;
    public String cls;
    public String method;
    public Object[] msgPart;
    public Throwable t;
    public Date date;

    LogEntry(int logLevel, String module, String pkg, String cls,
	    String method, Object[] msgPart, Throwable t) {
	this.logLevel = logLevel;
	this.module = module;
	this.pkg = pkg;
	this.cls = cls;
	this.method = method;
	this.msgPart = msgPart;
	this.t = t;
	this.date = new Date();
    }
}
