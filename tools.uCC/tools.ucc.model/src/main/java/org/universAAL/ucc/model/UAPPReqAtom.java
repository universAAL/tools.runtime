package org.universAAL.ucc.model;

import java.util.List;

public class UAPPReqAtom {
	String name;
	List<String> value;
	String criteria;

	public UAPPReqAtom() {

	}

	public UAPPReqAtom(String n, List<String> v, String c) {
		this.name = n;
		this.value = v;
		this.criteria = c;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> v) {
		this.value = v;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String c) {
		this.criteria = c;
	}

}
