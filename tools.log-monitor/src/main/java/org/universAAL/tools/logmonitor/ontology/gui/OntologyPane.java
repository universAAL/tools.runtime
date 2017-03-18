/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.ontology.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;

import org.universAAL.middleware.owl.BoundedValueRestriction;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.OntClassInfo;
import org.universAAL.middleware.owl.Ontology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.TypeExpression;
import org.universAAL.middleware.owl.TypeURI;
import org.universAAL.middleware.rdf.RDFClassInfo;
import org.universAAL.tools.logmonitor.util.ClipboardHandling;
import org.universAAL.tools.logmonitor.util.HTMLVisibilityPane;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class OntologyPane extends HTMLVisibilityPane {

    private static final long serialVersionUID = 1L;

    private String ontURI;
    private Ontology ont = null;
    private String link = "lnk";

    public OntologyPane() {
	setEditable(false);
	setContentType("text/html");
	setCaret(new DefaultCaret());
	((DefaultCaret) getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

	// overwrite ctrl-c
	final OntologyPane pane = this;
	getInputMap()
		.put(KeyStroke.getKeyStroke(KeyEvent.VK_C,
			InputEvent.CTRL_DOWN_MASK), "uaal_copy");
	getActionMap().put(
		"uaal_copy",
		new ClipboardHandling(new HashMap<String, String>(),
			getTransferHandler(), pane));
    }

    protected void updateAfterHyperlink() {
	showHTML();
    }

    public void show(String ontURI) {
	this.ontURI = ontURI;
	ont = OntologyManagement.getInstance().getOntology(ontURI);
	showHTML();
    }

    private void showHTML() {
	StringBuilder s = new StringBuilder(
		"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><html><body>\n");
	// s.append("Future version will show the selected profiles/patterns here.\n");
	// if (ontURI != null)
	// s.append(ontURI);
	if (ont != null) {
	    createOverviewHTML(s);

	    if (isVisible(link)) {
		s.append(getLinkHTML(link, "Hide full URI"));
		setShowFullURI(true);
	    } else {
		s.append(getLinkHTML(link, "Show full URI"));
		setShowFullURI(false);
	    }

	    createRDFClassesHTML(s);
	    createOntClassesHTML(s);
	}
	s.append("\n</body></html>");
	setText(s.toString());
    }

    private void createOverviewHTML(StringBuilder s) {
	s.append("<h2>Overview</h2>");
	s.append(getTableStartHTML());
	s.append(getVTableRowWithTitleHTML("Namespace", getURIHTML(ontURI)));
	s.append(getVTableRowWithTitleHTML("Label", ont.getInfo()
		.getResourceLabel()));
	s.append(getVTableRowWithTitleHTML("Comment", ont.getInfo()
		.getResourceComment()));
	s.append(getVTableRowWithTitleHTML("RDF Classes",
		"" + ont.getRDFClassInfo().length));
	s.append(getVTableRowWithTitleHTML("Ontology Classes",
		"" + ont.getOntClassInfo().length));

	String imp = "";
	Object tmp = ont.getInfo().getProperty(Ontology.PROP_OWL_IMPORT);
	if (tmp instanceof ArrayList) {
	    boolean first = true;
	    for (Object o : (ArrayList) tmp) {
		if (!first)
		    imp += "<br>";
		first = false;
		imp += getURIHTML(o.toString());
	    }
	}
	s.append(getVTableRowWithTitleHTML("Imports", imp));

	s.append(getTableEndHTML());
    }

    private void sort(RDFClassInfo[] arr) {
	Arrays.sort(arr, new Comparator<RDFClassInfo>() {
	    @Override
	    public int compare(RDFClassInfo o1, RDFClassInfo o2) {
		return o1.getURI().compareTo(o2.getURI());
	    }
	});
    }

    private void createRDFClassesHTML(StringBuilder s) {
	RDFClassInfo info[] = ont.getRDFClassInfo();
	if (info.length == 0)
	    return;
	sort(info);

	s.append("<h2>RDF classes</h2>");
	for (int i = 0; i < info.length; i++)
	    s.append(getURIHTML(info[i].getURI()) + "<br>");
    }

    private void createOntClassesHTML(StringBuilder s) {
	OntClassInfo info[] = ont.getOntClassInfo();
	if (info.length == 0)
	    return;
	sort(info);

	s.append("<h2>Ontology classes</h2>");
	s.append(getTableStartHTML());
	s.append(getTableRowTitleHTML("Class", "Property", "Type"));
	for (OntClassInfo ci : info) {
	    String c = ci.getURI();

	    String superClasses[] = ci.getNamedSuperClasses(false, true);
	    {
		String propURI = TypeExpression.PROP_RDFS_SUB_CLASS_OF;
		for (String sc : superClasses) {
		    s.append(getTableRowHTML(getURIHTML(c),
			    getURIHTML(propURI), getURIHTML(sc)));
		    c = "";
		    propURI = "";
		}
	    }

	    String propURIs[] = ci.getDeclaredPropertyURIs();
	    if (propURIs.length == 0 && superClasses.length == 0) {
		s.append(getTableRowHTML(getURIHTML(c), "", ""));
	    } else {
		for (String propURI : propURIs) {
		    String o = "";

		    MergedRestriction res = ci.getRestrictionsOnProp(propURI);

		    // res = MergedRestriction
		    // .getAllValuesRestrictionWithCardinality(
		    // "PROP_SOURCE_BRIGHTNESS", new FloatRestriction(
		    // new Integer(0), true, new Integer(100), false),
		    // 1, 1);

		    if (res != null) {
			Object con = res
				.getConstraint(MergedRestriction.allValuesFromID);
			if (con instanceof TypeURI) {
			    o = ((TypeURI) con).getURI();
			} else if (con instanceof BoundedValueRestriction) {
			    BoundedValueRestriction bv = (BoundedValueRestriction) con;
			    o = bv.getTypeURI() + "  ";
			    if (bv.getLowerbound() != null)
				o += bv.getLowerbound();
			    o += " .. ";
			    if (bv.getUpperbound() != null)
				o += bv.getUpperbound();
			}
		    }

		    s.append(getTableRowHTML(getURIHTML(c),
			    getURIHTML(propURI), getURIHTML(o)));
		    c = "";
		}
	    }
	}
	s.append(getTableEndHTML());
    }
}
