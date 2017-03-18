package org.universAAL.ucc.startup.api;

import java.util.List;

import org.universAAL.ucc.startup.model.UserAccountInfo;

/**
 * Database operations for user account controlling.
 * 
 * @author Nicole Merkle
 *
 */
public interface Setup {
	/**
	 * Saves a list of Users
	 * @param users a list of users
	 * @param file the file for persistence
	 */
	public void saveUsers(List<UserAccountInfo>users, String file);
	
	/**
	 * Gets a list of users.
	 * @param file the file for persistence
	 * @return a list of users
	 */
	public List<UserAccountInfo> getUsers(String file);
	
	/**
	 * Deletes a user.
	 * @param user the user to delete
	 * @param file the file for persistence
	 */
	public void deleteUser(UserAccountInfo user, String file);
	
	/**
	 * Updates a user
	 * @param user the user to update
	 * @param file the file for persistence
	 */
	public void updateUser(UserAccountInfo user, String file);
	
	/**
	 * Deletes all users.
	 * @param file the file for persistence
	 */
	public void deleteAllUsers(String file);
	
	/**
	 * Saves a user.
	 * @param user the user to save
	 * @param file the file for persistence
	 */
	public void saveUser(UserAccountInfo user, String file);
}
