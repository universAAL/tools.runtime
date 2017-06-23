package org.universAAL.tools.ucc.deploymanagerservice;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * The interface for uStore to interact with uCC (the local deploy manager)
 * 
 * @author sji
 * 
 */
@WebService(serviceName = "DeployManagerService", portName = "DeployManagerServicePort")
public interface DeployManagerService {
	/**
	 * install a service as specified in the .usrv file
	 * 
	 * @param sessionKey
	 *            : the sessionKey for the interaction obtained when uCC
	 *            registers with uStore
	 * @param usrvfile
	 *            : the link to download the .usrv file, serviceId from uStore
	 *            is provided in .usrv file.
	 */
	public void install(@WebParam(name = "sessionKey") String sessionKey,
			@WebParam(name = "serviceId") String serviceId, @WebParam(name = "serviceLink") String serviceLink);

	/**
	 * update a service as specified in the .usrv file
	 * 
	 * @param sessionKey
	 *            : the sessionKey for the interaction obtained when uCC
	 *            registers with uStore
	 * @param usrvfile
	 *            : the link to download the .usrv file, serviceId from uStore
	 *            is provided in .usrv file.
	 */
	public void update(@WebParam(name = "sessionKey") String sessionKey, @WebParam(name = "serviceId") String serviceId,
			@WebParam(name = "serviceLink") String serviceLink);

	/**
	 * uninstall a service
	 * 
	 * @param sessionKey
	 *            : the sessionKey for the interaction obtained when uCC
	 *            registers with uStore
	 * @param serviceId
	 *            : the uStore service id for the service to be uninstalled
	 * 
	 */
	public void uninstall(@WebParam(name = "sessionKey") String sessionKey,
			@WebParam(name = "serviceId") String serviceId);

	/**
	 * get all installed services
	 * 
	 * @param sessionKey
	 *            : the sessionKey for the interaction obtained when uCC
	 *            registers with uStore
	 * @return The list of uStore service Ids in the following template:
	 *         <services> <service>serviceId</service>
	 *         <service>serviceId</service> ... </services>
	 */
	public String getInstalledServices(@WebParam(name = "sessionKey") String sessionKey);

	/**
	 * get the installed application units for a service
	 * 
	 * @param sessionKey
	 *            : the sessionKey for the interaction obtained when uCC
	 *            registers with uStore
	 * @param serviceId
	 * @return The list of application bundles with their version in the
	 *         following template: <serviceUnits>
	 *         <unit><id>bundleId</id><version>bundleVersion</version></unit>
	 *         <unit><id>bundleId</id><version>bundleVersion</version></unit>
	 *         ... </serviceUnits>
	 */
	public String getInstalledUnitsForService(@WebParam(name = "sessionKey") String sessionKey,
			@WebParam(name = "serviceId") String serviceId);

	/**
	 * get the AAL space profile
	 * 
	 * @return The AAL space profile in the following template: TODO: decide
	 *         what to return, whether to use AALSpaceCard or AALSpaceDescriptor
	 *         as defined in the mw.interfaces.aalspace. Suggest to return a
	 *         list of properties of capabilities and functionalities in the
	 *         form of <propertyName, propValue, criteria>
	 */
	public String getAALSpaceProfile();

	/**
	 * 
	 * @param sessionKey
	 *            : the sessionKey for the interaction obtained when uCC
	 *            registers with uStore
	 * @return The user profile in the following template: TODO: decide what to
	 *         return, what info should be contained in a user profile
	 */
	public String getUserProfile(@WebParam(name = "sessionKey") String sessionKey);

	public String getSessionKey(@WebParam(name = "userName") String userName,
			@WebParam(name = "password") String password);
}
