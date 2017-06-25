package org.universAAL.tools.ucc.api.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.universAAL.middleware.managers.api.InstallationResultsDetails;
import org.universAAL.middleware.managers.api.MatchingResult;
import org.universAAL.middleware.managers.api.UAPPPackage;
import org.universAAL.tools.ucc.api.IInstaller;
import org.universAAL.tools.ucc.controller.Activator;
import org.universAAL.middleware.interfaces.PeerCard;
import org.universAAL.middleware.interfaces.PeerRole;
import org.universAAL.middleware.managers.api.SpaceManager;
import org.universAAL.middleware.managers.api.DeployManager;

public class Installer implements IInstaller {

	/**
	 * get peers in Space from the SpaceManager
	 *
	 */
	public Map<String, PeerCard> getPeers() {
		SpaceManager spaceManager = Activator.getSpaceManager();
		Map peers = new HashMap();
		if (spaceManager != null) {
			peers = spaceManager.getPeers();
			System.out.println("[Installer.getPeers()] " + peers.toString());
		} else {
			// use faked data to test without really connected to DeployManager
			PeerCard card = new PeerCard(PeerRole.PEER, "karaf", "Java");
			System.out.println("[Installer.getPeers] peerCard1 for testing: " + card.getPeerID() + "/" + card.getOS()
					+ "/" + card.getPLATFORM_UNIT() + "/" + card.getCONTAINER_UNIT() + "/" + card.getRole());
			peers.put("Node1", card);
			card = new PeerCard(PeerRole.PEER, "karaf", "Java"); // to have a
			// different
			// unique
			// PeerId
			peers.put("Node2", card);
			System.out.println("[Installer.getPeers] peerCard2 for testing: " + card.getPeerID() + "/" + card.getOS()
					+ "/" + card.getPLATFORM_UNIT() + "/" + card.getCONTAINER_UNIT() + "/" + card.getRole());
			card = new PeerCard(PeerRole.PEER, "karaf", "C++"); // to have a
			// different
			// unique PeerId
			peers.put("Node3", card);
			System.out.println("[Installer.getPeers] peerCard3 for testing: " + card.getPeerID() + "/" + card.getOS()
					+ "/" + card.getPLATFORM_UNIT() + "/" + card.getCONTAINER_UNIT() + "/" + card.getRole());
		}
		return peers;
	}

	public InstallationResultsDetails requestToInstall(UAPPPackage app) {
		DeployManager deployManager = Activator.getDeployManager();
		if (deployManager == null) {
			System.out.println("[Installer.requestToInstall] DeployManager is null!");
			return null;
		}
		System.err.println("APP-ID: " + app.getId());
		System.err.println("SERVICE-ID: " + app.getServiceId());
		System.err.println("APP-LOcation: " + app.getFolder().getPath());
		InstallationResultsDetails results = deployManager.requestToInstall(app);
		System.out.println(
				"[Installer.requestToInstall] the installation results: " + results.getGlobalResult().toString());
		return results;
	}

	public MatchingResult getMatchingPeers(Map<String, Serializable> filter) {
		SpaceManager spaceManager = Activator.getSpaceManager();
		System.err.println("[[IInstaller]]: getMatchingPeers()");
		if (spaceManager != null) {
			System.err.println("[[Installer]] SPACEMANAGER is not NULL " + spaceManager.getPeers());
			System.err.println("[[Installer]] filer: " + filter.size() + " " + filter.toString());
			MatchingResult result = spaceManager.getMatchingPeers(filter);
			System.err.println(result.getPeers().toString());
			PeerCard[] peers = result.getPeers();
			System.err.println("PEERS SIZE: " + peers.length);
			for (int i = 0; i < peers.length; i++) {
				System.out.println("[Installer.getMatchingPeers()] has a matching peer: " + peers[i].getPeerID());
			}
			System.err.println("[[Installer] BEFORE return of MatchingResult");
			return result;
		}
		System.out.println("[Installer.getMatchingPeers()] can not get SpaceManager");
		return null;

	}

	public Map<String, Serializable> getPeerAttributes(List<String> attributes, PeerCard target) {
		SpaceManager spaceManager = Activator.getSpaceManager();

		if (spaceManager != null) {
			Map<String, Serializable> result = spaceManager.getPeerAttributes(attributes, target);

			for (String s : result.keySet()) {
				System.out.println("[Installer.getMatchingPeers()] has an attribute: " + result.get(s));
			}
			return result;
		}
		System.out.println("[Installer.getPeerAttributes()] can not get SpaceManager");
		return null;
	}

}
