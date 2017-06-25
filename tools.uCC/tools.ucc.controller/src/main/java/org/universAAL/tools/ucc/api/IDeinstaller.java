package org.universAAL.tools.ucc.api;

import org.universAAL.middleware.managers.api.InstallationResultsDetails;

/**
 * Deinstalles a installed universAAL service and gives a message about the
 * installation result back To deinstall it is the serviceId and id of a universAAL
 * service needed.
 *
 * @author Shanshan Jiang
 */

public interface IDeinstaller {
	public InstallationResultsDetails requestToUninstall(String serviceId, String id);

}
