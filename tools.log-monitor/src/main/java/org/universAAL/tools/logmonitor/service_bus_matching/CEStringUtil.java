package org.universAAL.tools.logmonitor.service_bus_matching;

import java.util.List;

import org.universAAL.middleware.owl.ManagedIndividual;
import org.universAAL.middleware.owl.PropertyRestriction;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.TypeURI;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.owls.process.ProcessInput;
import org.universAAL.middleware.service.owls.process.ProcessParameter;

// util for strings from classexpressions
/**
 * 
 * @author Carsten Stockloew
 *
 */
public class CEStringUtil {

    public static String toString(String indent, Object o, boolean shortForm) {
	if (o instanceof MergedRestriction)
	    return toString(indent, (MergedRestriction) o, shortForm);

	if (o instanceof PropertyRestriction)
	    return toString(indent, (PropertyRestriction) o, shortForm);

	if (o instanceof ManagedIndividual)
	    return toString(indent, (ManagedIndividual) o, shortForm);

	if (o instanceof Resource)
	    return toString(indent, (Resource) o, shortForm);

	return indent + "<unknown object: " + o.toString() + ">\n";
    }

    public static String toString(String indent, Resource r, boolean shortForm) {
	if (ProcessParameter.TYPE_OWLS_VALUE_OF.equals(r.getType())) {
	    ProcessInput theVar = (ProcessInput) r
		    .getProperty(ProcessParameter.PROP_OWLS_VALUE_OF_THE_VAR);
	    String type = "ProcessParameter";
	    if (ProcessInput.MY_URI.equals(theVar.getType()))
		type = "ProcessInput";
	    String parameterType = theVar.getParameterType();
	    int minCard = theVar.getMinCardinality();
	    int maxCard = theVar.getMaxCardinality();
	    return indent + type + ":\n" + indent + "   Type "
		    + URI.get(parameterType, shortForm) + "\n" + indent
		    + "   with cardinality (" + minCard + ", " + maxCard
		    + ")\n";
	}

	return indent + "<unknown object: "
		+ r.toStringRecursive(indent + "  ", false, null) + ">\n";
    }

    public static String toString(String indent, ManagedIndividual o,
	    boolean shortForm) {
	return indent + "individual: " + URI.get(o.getURI(), shortForm) + "\n";
    }

    public static String toString(String indent, TypeURI o, boolean shortForm) {
	return indent + "class: " + URI.get(o.getURI(), shortForm) + "\n";
    }

    public static String toString(String indent, PropertyRestriction restr,
	    boolean shortForm) {
	String s = indent + URI.get(restr.getClassURI(), true)
		+ " on property " + URI.get(restr.getOnProperty(), shortForm)
		+ "\n";
	s += toString(indent + "  ", restr.getConstraint(), shortForm);
	return s;
    }

    public static String toString(String indent, MergedRestriction restr,
	    boolean shortForm) {
	String s = indent + "MergedRestriction on property "
		+ URI.get(restr.getOnProperty(), shortForm) + "\n";
	List<?> l = restr.getRestrictions();
	for (int i = 0; i < l.size(); i++)
	    s += toString(indent + "  ", (PropertyRestriction) l.get(i),
		    shortForm);
	return s;
    }
}
