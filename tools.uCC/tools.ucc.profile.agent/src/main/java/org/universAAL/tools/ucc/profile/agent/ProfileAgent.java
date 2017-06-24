package org.universAAL.tools.ucc.profile.agent;

import java.util.List;

import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.profile.AppService;
import org.universAAL.ontology.profile.AppServiceProfile;
import org.universAAL.ontology.profile.Space;
import org.universAAL.ontology.profile.SpaceProfile;
import org.universAAL.ontology.profile.SubProfile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;

/**
 * Interface for the actual profile storage and retrieval.
 *
 * Implementations of this interface go in
 * {@link org.universAAL.tools.ucc.profile.agent.impl} package; this way the actual
 * storage of profiles can be expanded to other methods and service providers
 * can select from these methods the one that fits best and has best
 * performance, or they can implement their own storage method.
 *
 * @author
 */

public interface ProfileAgent {

	/*********** The following APIs use Profiling server ***************/

	public String addUser(User user);

	// public Resource getUser(User user);
	public User getUser(String uri);

	public List<User> getAllUsers();

	public boolean updateUser(User user);

	public boolean deleteUser(String uri);

	/**
	 * Returns an {@link org.universAAL.ontology.profile.UserProfile} object
	 * from the profile log that are associated with the given user.
	 *
	 * @param user
	 *
	 * @return the user profile of the user
	 */
	public String getUserProfile(User user);

	/**
	 * Stores the new {@link org.universAAL.ontology.profile.UserProfile} for
	 * the user with userID.
	 *
	 * @param user
	 *
	 * @param userProfile
	 *            The user profile to be added
	 *
	 * @return the result of the operation
	 */
	public String addUserProfile(User user, UserProfile userProfile);

	/**
	 * Changes the existing {@link org.universAAL.ontology.profile.UserProfile}
	 * for user with userID.
	 *
	 * @param user
	 *
	 * @param userProfile
	 *            The user profile to be changed
	 */
	public void changeUserProfile(User user, UserProfile userProfile);

	/**
	 * removes the existing {@link org.universAAL.ontology.profile.UserProfile}
	 * for user with userID.
	 *
	 * @param user
	 */
	public void removeUserProfile(User user);

	// ********** below are methods for user subprofiles (to be defined), e.g.,
	// for username/passwords/ etc.
	// ********** the subprofile is identified by MY_URI
	/**
	 * Returns all {@link org.universAAL.ontology.profile.SubProfile} objects
	 * from the profile log that are associated with the given user.
	 *
	 * @param user
	 *
	 * @return the user subprofiles of the user
	 */
	// TODO: change to List<SubProfile>?
	public String getUserSubprofiles(User user);

	public List getUserSubprofiles(UserProfile profile);

	/**
	 * Stores the new {@link org.universAAL.ontology.profile.SubProfile} for the
	 * user with userID.
	 *
	 * @param user
	 *
	 * @param subProfile
	 *            The user subprofile to be added
	 *
	 * @return the result of the operation
	 */
	public String addUserSubprofile(User user, SubProfile subProfile);

	public String addUserSubprofile(UserProfile profile, SubProfile subProfile);

	/**
	 * Changes the existing {@link org.universAAL.ontology.profile.UserProfile}
	 * for user with userID.
	 *
	 * @param user
	 *
	 * @param subProfile
	 *            The user subprofile to be changed
	 */
	public void changeUserSubprofile(User user, SubProfile subProfile);

	/**
	 * removes the existing {@link org.universAAL.ontology.profile.UserProfile}
	 * for user with userID.
	 *
	 * @param user
	 */
	public void removeUserSubprofile(User user, String subprofile_URI);

	/**
	 * Returns an {@link org.universAAL.ontology.profile.SpaceProfile} list
	 * from the profile log that are associated with the given user.
	 *
	 * @param user
	 *            The user who performed the operation
	 *
	 * @return list of AALSpace profiles, which owner is the given user
	 */
	public List<SpaceProfile> getAALSpaceProfiles(User user);

	/**
	 * Stores the new {@link org.universAAL.ontology.profile.SpaceProfile}
	 * that was performed by the user.
	 *
	 * @param user
	 *            The user who performed the action
	 *
	 * @param aalSpaceProfile
	 *            The AAL space profile for the user
	 */
	public void addAALSpaceProfile(User user, SpaceProfile aalSpaceProfile);

	/**
	 * Changes the existing
	 * {@link org.universAAL.ontology.profile.SpaceProfile} that was
	 * performed by the user.
	 *
	 * @param user
	 *
	 * @param aalSpaceProfile
	 *            The AAL space profile for the user to be changed
	 */
	public void changeAALSpaceProfile(User user, SpaceProfile aalSpaceProfile);

	/**
	 * Removes the existing
	 * {@link org.universAAL.ontology.profile.SpaceProfile} that was
	 * performed by the user.
	 *
	 * @param user
	 *            The user whose AAL space profile is to be removed
	 */
	public void removeAALSpaceProfile(User user);

	/************* The following APIs use Space server ***********************/

	/** For testing ***/

	public String addSpaceProfile(SpaceProfile aalSpaceProfile);

	/** Used by uCC ****/

	public String addSpace(Space space);

	public Space getSpace(Space space);

	public List<Space> getSpaces();

	// public String getDevice(Device device);
	public String addDevice(Device device, Space space);

	public Device getDevice(String uri);

	public boolean updateDevice(Device device);

	public boolean deleteDevice(String uri);

	public List<Device> getAllDevices(Space space);

	// TODO: check which methods below are really needed for uCC

	public String getSpaceProfile(SpaceProfile aalSpaceProfile);

	public String addService(AppService aalService);

	public String getService(AppService aalService);

	public String changeService(AppService aalService);

	public String removeService(AppService aalService);

	public String getServices();

	public String addServiceProf(AppServiceProfile aalServiceProfile);

	public String getServiceProf(AppServiceProfile aalServiceProfile);

	public String changeServiceProf(AppServiceProfile aalServiceProfile);

	public String removeServiceProf(AppServiceProfile aalServiceProfile);

	public String addServicesToSpace(Space aalSpace, AppService serv);

	public String getServicesOfSpace(Space aalSpace);

	public String getHROfServ(AppService aalService);

	public String getHWOfServ(AppService aalService);

	public String getAppOfServ(AppService aalService);

	/************* APIs for uCC/uStore Web services **********************/
	public String getAALSpaceProfile();

	public String getUserProfile(String userId);

}
