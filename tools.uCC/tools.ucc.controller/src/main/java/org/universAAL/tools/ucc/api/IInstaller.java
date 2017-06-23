package org.universAAL.tools.ucc.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.universAAL.middleware.interfaces.PeerCard;
import org.universAAL.middleware.managers.api.InstallationResultsDetails;
import org.universAAL.middleware.managers.api.MatchingResult;
import org.universAAL.middleware.managers.api.UAPPPackage;

/**
 * Installer Interface for installing AAL services.
 * 
 * @author Shanshan Jiang
 *
 */
public interface IInstaller {

	// interface with MW: call AALSpaceManager
	public Map getPeers();

	public MatchingResult getMatchingPeers(Map<String, Serializable> filter);

	public Map<String, Serializable> getPeerAttributes(List<String> attributes, PeerCard target);

	// interface with MW: call DeployManager
	public InstallationResultsDetails requestToInstall(UAPPPackage app);

}
