package org.universAAL.tools.logmonitor.bus_member;

import java.util.Map;

import org.universAAL.middleware.container.SharedObjectListener;
import org.universAAL.middleware.interfaces.PeerCard;
import org.universAAL.middleware.interfaces.space.SpaceDescriptor;
import org.universAAL.middleware.interfaces.space.SpaceStatus;
import org.universAAL.middleware.managers.api.SpaceListener;
import org.universAAL.middleware.managers.api.SpaceManager;
import org.universAAL.tools.logmonitor.Activator;
import org.universAAL.tools.logmonitor.bus_member.gui.BusMemberGui;

/**
 *
 * @author Carsten Stockloew
 *
 */
public class MySpaceListener implements SpaceListener, Runnable, SharedObjectListener {

	private SpaceManager theSpaceManager = null;
	private BusMemberGui gui = null;

	public MySpaceListener(BusMemberGui gui) {
		this.gui = gui;
	}

	public void start() {
		// get Space Manager to register this listener
		Object[] o = Activator.mc.getContainer().fetchSharedObject(Activator.mc,
				new Object[] { SpaceManager.class.getName() }, this);
		if (o != null) {
			if (o.length != 0) {
				if (o[0] instanceof SpaceManager) {
					sharedObjectAdded(o[0], null);
				}
			}
		}
	}

	public void stop() {
		// remove me as SpaceListener
		synchronized (this) {
			if (theSpaceManager != null) {
				theSpaceManager.removeSpaceListener(this);
			}
		}
	}

	public PeerCard getMyPeerCard() {
		synchronized (this) {
			if (theSpaceManager != null) {
				return theSpaceManager.getMyPeerCard();
			}
		}
		return null;
	}

	@Override
	public void spaceJoined(SpaceDescriptor spaceDescriptor) {
		gui.setSpace(spaceDescriptor);
	}

	@Override
	public void spaceLost(SpaceDescriptor spaceDescriptor) {
		gui.setSpace(spaceDescriptor);
	}

	@Override
	public void peerJoined(PeerCard peer) {
		gui.add(peer);
	}

	@Override
	public void peerLost(PeerCard peer) {
		gui.remove(peer);
	}

	@Override
	public void spaceStatusChanged(SpaceStatus status) {
		// not needed
	}

	@Override
	public void run() {
		// TODO polling thread that looks for new spaces
	}

	@Override
	public void sharedObjectAdded(Object sharedObj, Object removeHook) {
		if (sharedObj instanceof SpaceManager) {
			synchronized (this) {
				theSpaceManager = (SpaceManager) sharedObj;
				theSpaceManager.addSpaceListener(this);
				gui.setSpace(theSpaceManager.getSpaceDescriptor());

				Map<String, PeerCard> peers = theSpaceManager.getPeers();
				if (peers != null) {
					for (PeerCard pc : peers.values()) {
						gui.add(pc);
					}
				}
				gui.add(theSpaceManager.getMyPeerCard());
			}
		}
	}

	@Override
	public void sharedObjectRemoved(Object removeHook) {
		if (removeHook instanceof SpaceManager) {
			synchronized (this) {
				theSpaceManager = null;
			}
		}
	}
}
