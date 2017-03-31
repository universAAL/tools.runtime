package org.universAAL.ucc.model;

/**
 * The provider of a usrv file. Has a name, a telephone-number, email and
 * optionally website.
 * 
 * @author Nicole Merkle
 * 
 */

public class Provider {
    private String name;
    private String tel;
    private String email;
    private String website;

    public Provider(String name, String tel, String email, String web) {
	this.name = name;
	this.tel = tel;
	this.email = email;
	this.website = web;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getTel() {
	return tel;
    }

    public void setTel(String tel) {
	this.tel = tel;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getWebsite() {
	return website;
    }

    public void setWebsite(String website) {
	this.website = website;
    }

}
