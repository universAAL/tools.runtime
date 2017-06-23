package org.universAAL.tools.ucc.startup.model;

import java.util.List;

/**
 * The Account of a user in uCC.
 * 
 * @author Nicole Merkle
 *
 */

public class UserAccountInfo {
	private String name;
	private String password;
	private List<Role> role;
	private boolean checked;

	/**
	 * Gets the Name of a user.
	 * 
	 * @return the name of a user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of a user.
	 * 
	 * @param name
	 *            the name of a user
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the password of a user.
	 * 
	 * @return the password of a user
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of a user.
	 * 
	 * @param password
	 *            the password of a user
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the role of a user.
	 * 
	 * @return the role of a user
	 */
	public List<Role> getRole() {
		return role;
	}

	/**
	 * Sets the role of a user.
	 * 
	 * @param role
	 *            the role of a user
	 */
	public void setRole(List<Role> role) {
		this.role = role;
	}

	/**
	 * Shows, if checkbox is checked
	 * 
	 * @return true or false
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * Sets, if the checkbox is whether checked or not.
	 * 
	 * @param checked
	 *            true or false
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
