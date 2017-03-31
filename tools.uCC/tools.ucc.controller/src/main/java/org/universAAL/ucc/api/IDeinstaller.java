package org.universAAL.ucc.api;

import org.universAAL.middleware.managers.api.InstallationResultsDetails;

/**
 * Deinstalles a installed AAL service and gives a message about the installation result back
 * To deinstall it is the serviceId and id of a AAL service needed.
 * 
 * @author Shanshan Jiang
 */

public interface IDeinstaller {
    public InstallationResultsDetails requestToUninstall(String serviceId,
	    String id);

}
