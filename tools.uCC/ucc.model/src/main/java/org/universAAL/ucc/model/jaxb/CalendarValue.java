package org.universAAL.ucc.model.jaxb;

import javax.xml.bind.annotation.*;

public class CalendarValue extends SimpleObject {

	private String calendar;


	@XmlElement(name="value")
	public String getCalendar() {
		return calendar;
	}

	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}

}
