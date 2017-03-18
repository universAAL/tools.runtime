package org.universAAL.tools.logmonitor.rdfvis;

import java.util.List;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.owl.Service;
import org.universAAL.middleware.service.owls.process.OutputBinding;
import org.universAAL.middleware.service.owls.process.ProcessResult;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;

/**
 * This class gets a {@link org.universAAL.middleware.rdf.Resource} and tries to
 * interpret the content to give a short description.
 * 
 * @author Carsten Stockloew
 */
public class ResourceInterpreter {

    private static final String ServiceRealization_uAAL_SERVICE_PROFILE = Resource.uAAL_VOCABULARY_NAMESPACE
	    + "theProfile";

    /**
     * Interpret a ServiceRealization.
     * 
     * @param r
     *            The Resource representing the ServiceRealization.
     * @return The interpretation string.
     */
    private static String getShortDescriptionForServiceRealization(Resource r) {
	Object o;
	String out;

	if (!((o = r.getProperty(ServiceRealization_uAAL_SERVICE_PROFILE)) instanceof Resource))
	    return "";
	ServiceProfile p = (ServiceProfile) o;

	if (!((o = p.getProperty(Service.PROP_OWLS_PRESENTED_BY)) instanceof Service))
	    return "";
	Service s = (Service) o;

	String uripart[] = s.getURI().split("#");
	if (uripart.length != 2)
	    return "";

	out = "ServiceRealization: " + uripart[1];

	if (!((o = p.getProperty(ServiceProfile.PROP_OWLS_PROFILE_HAS_OUTPUT)) instanceof List))
	    return out;
	r = (Resource) (((List) o).get(0));

	uripart = r.getURI().split("#");
	if (uripart.length != 2)
	    return out;

	return out + " (" + uripart[1] + ")";
    }

    /**
     * Interpret a ServiceRequest.
     * 
     * @param r
     *            The Resource representing the ServiceRequest.
     * @return The interpretation string.
     */
    private static String getShortDescriptionForServiceRequest(Resource r) {
	Object o;
	String out = "";

	if ((o = r.getProperty(ServiceRequest.PROP_REQUESTED_SERVICE)) instanceof Resource)
	    out = "ServiceRequest: " + o.getClass().getSimpleName();

	if (!((o = r.getProperty(ServiceRequest.PROP_REQUIRED_PROCESS_RESULT)) instanceof Resource))
	    return out;
	r = (Resource) o;

	if (!((o = r.getProperty(ProcessResult.PROP_OWLS_RESULT_WITH_OUTPUT)) instanceof List))
	    return out;
	r = (Resource) (((List) o).get(0));

	if (!((o = r.getProperty(OutputBinding.PROP_OWLS_BINDING_TO_PARAM)) instanceof Resource))
	    return out;
	r = (Resource) o;

	String uripart[] = r.getURI().split("#");
	if (uripart.length != 2)
	    return out;

	return out + " (" + uripart[1] + ")";
	// return "ServiceRequest: " + o.getClass().getSimpleName();
    }

    /**
     * Interpret a Resource.
     * 
     * @param r
     *            The resource.
     * @return The interpretation string.
     */
    public static String getShortDescription(Resource r) {

	if (r instanceof ServiceRequest)
	    return getShortDescriptionForServiceRequest(r);

	if ("org.universAAL.middleware.service.impl.ServiceRealization"
		.equals(r.getClass().getName()))
	    return getShortDescriptionForServiceRealization(r);

	return "";
    }
}
