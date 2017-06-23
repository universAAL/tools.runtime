package org.universAAL.tools.ucc.model.jaxb;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.*;

/**
 * A Collection with simple datatypes.
 * 
 * @author Nicole Merkle
 *
 */

public class CollectionValues {

	private Collection<SimpleObject> collection;
	private String value_type;
	private String label;
	private String description;
	private boolean multiselect;
	private String name;
	private boolean required;

	public CollectionValues() {
		collection = new ArrayList<SimpleObject>();
	}

	/**
	 * Gets a collection
	 * 
	 * @return
	 */
	@XmlElementWrapper(name = "values")
	@XmlElements({ @XmlElement(name = "string", type = StringValue.class),
			@XmlElement(name = "integer", type = IntegerValue.class),
			@XmlElement(name = "boolean", type = BooleanValue.class),
			@XmlElement(name = "double", type = DoubleValue.class),
			@XmlElement(name = "calendar", type = CalendarValue.class) })
	public Collection<SimpleObject> getCollection() {
		return collection;
	}

	/**
	 * Sets a collection
	 * 
	 * @param collection
	 */
	public void setCollection(Collection<SimpleObject> collection) {
		this.collection = collection;
	}

	@XmlElement(name = "multiselection", required = true)
	public boolean isMultiselect() {
		return multiselect;
	}

	public void setMultiselect(boolean multiselect) {
		this.multiselect = multiselect;
	}

	@XmlAttribute(name = "value_type", required = true)
	public String getValue_type() {
		return value_type;
	}

	public void setValue_type(String value_type) {
		this.value_type = value_type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
