package org.universAAL.tools.ucc.model.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;

@XmlRegistry
public class MyFactory {
	private final static QName _profiles_ns = new QName("http://ipe.fzi.de/jcc", "profiles");

	public BooleanValue createBooleanValue() {
		return new BooleanValue();
	}

	public CalendarValue createCalendarValue() {
		return new CalendarValue();
	}

	public CollectionValues createCollectionValues() {
		return new CollectionValues();
	}

	public DoubleValue createDoubleValue() {
		return new DoubleValue();
	}

	public Profiles createProfiles() {
		return new Profiles();
	}

	public IntegerValue createIntegerValue() {
		return new IntegerValue();
	}

	public OntologyInstance createOntologyInstance() {
		return new OntologyInstance();
	}

	@XmlElementDecl(namespace = "http://ipe.fzi.de/jcc", name = "profiles")
	public JAXBElement<Profiles> createProfiles(Profiles profiles) {
		return new JAXBElement<Profiles>(_profiles_ns, Profiles.class, null, profiles);
	}

	public SimpleObject createSimpleObject() {
		return new SimpleObject();
	}

	public StringValue createStringValue() {
		return new StringValue();
	}

	public Subprofile createSubprofile() {
		return new Subprofile();
	}

}
