package org.universAAL.ucc.startup.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * All user in AAL space and uCC.
 * 
 * @author Nicole Merkle
 *
 */
@XmlRootElement
public class UccUsers {
	/**
	 * List of all users.
	 */
	List<UserAccountInfo> user = new ArrayList<UserAccountInfo>();

	/**
	 * Gets all user of uCC
	 * 
	 * @return a list of users
	 */
	@XmlElement(name = "user")
	public List<UserAccountInfo> getUser() {
		return user;
	}

	/**
	 * Sets all User of uCC.
	 * 
	 * @param allUser
	 *            a list of all users
	 */
	public void setUser(List<UserAccountInfo> allUser) {
		this.user = allUser;
	}

}
