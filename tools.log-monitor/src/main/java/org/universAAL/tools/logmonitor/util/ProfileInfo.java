/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.util;

import org.universAAL.middleware.service.owls.profile.ServiceProfile;

/**
 * 
 * @author Carsten Stockloew
 *
 */
public class ProfileInfo {
    
    public ServiceProfile profile;
    
    public String serialized;
    
    // provider URI is the bus member URI
    public String providerURI;
    
    public String serviceURI;

    public ProfileInfo(ServiceProfile profile, String providerURI) {
	this.profile = profile;
	this.providerURI = providerURI;
    }
}
