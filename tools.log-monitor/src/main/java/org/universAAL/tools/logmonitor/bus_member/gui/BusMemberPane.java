/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.bus_member.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;

import org.universAAL.middleware.interfaces.PeerCard;
import org.universAAL.middleware.interfaces.space.SpaceCard;
import org.universAAL.middleware.interfaces.space.SpaceDescriptor;
import org.universAAL.tools.logmonitor.MemberData;
import org.universAAL.tools.logmonitor.service_bus_matching.URI;
import org.universAAL.tools.logmonitor.util.ClipboardHandling;
import org.universAAL.tools.logmonitor.util.HTMLBusOperationsPane;
import org.universAAL.tools.logmonitor.util.PatternInfo;
import org.universAAL.tools.logmonitor.util.ProfileInfo;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class BusMemberPane extends HTMLBusOperationsPane {

	private static final long serialVersionUID = 1L;

	enum enType {
		NOTHING, PEER, SPACE, MEMBER
	}

	private enType type = enType.NOTHING;
	private PeerCard peerCard = null;
	private SpaceDescriptor space = null;
	private MemberData member = null;

	public BusMemberPane() {
		setEditable(false);
		setContentType("text/html");
		setCaret(new DefaultCaret());
		((DefaultCaret) getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		// overwrite ctrl-c
		final BusMemberPane pane = this;
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "uaal_copy");
		getActionMap().put("uaal_copy",
				new ClipboardHandling(new HashMap<String, String>(), getTransferHandler(), pane));
	}

	protected void updateAfterHyperlink() {
		showHTML();
	}

	public void show() {
		type = enType.NOTHING;
		setNull();
		showHTML();
	}

	public void show(PeerCard peerCard) {
		type = enType.PEER;
		setNull();
		this.peerCard = peerCard;
		showHTML();
	}

	public void show(SpaceDescriptor space) {
		type = enType.SPACE;
		setNull();
		this.space = space;
		showHTML();
	}

	public void show(MemberData m) {
		type = enType.MEMBER;
		setNull();
		this.member = m;
		showHTML();
	}

	private void setNull() {
		peerCard = null;
		space = null;
	}

	private void showHTML() {
		StringBuilder s = new StringBuilder(
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><html><body>\n");
		// s.append("Future version will show the selected profiles/patterns
		// here.\n");
		switch (type) {
		case NOTHING:
			s.append("");
			break;
		case PEER:
			createPeerHTML(s, peerCard);
			break;
		case SPACE:
			createSpaceHTML(s, space);
			break;
		case MEMBER:
			// s.append("Future version will show member information here.");
			createMemberHTML(s, member);
			break;
		}
		s.append("\n</body></html>");
		setText(s.toString());
	}

	private void createMemberHTML(StringBuilder s, MemberData m) {
		s.append("<h1>Bus Member</h1>\n");
		if (m == null) {
			s.append("no member data available<br>\n");
			return;
		}

		// ///////////////////
		// overview of member
		s.append(getTableStartHTML());
		s.append(getVTableRowWithTitleHTML("Member ID", m.id));
		s.append(getVTableRowWithTitleHTML("Label", m.label));
		s.append(getVTableRowWithTitleHTML("Comment", m.comment));
		s.append(getVTableRowWithTitleHTML("Space", m.space));
		s.append(getVTableRowWithTitleHTML("Peer ID", m.peer));
		s.append(getVTableRowWithTitleHTML("Module", m.module));
		s.append(getVTableRowWithTitleHTML("Type", m.type));
		s.append(getVTableRowWithTitleHTML("Bus ID", m.busName));
		s.append(getVTableRowWithTitleHTML("Bus name", m.busNameReadable));
		s.append(getVTableRowWithTitleHTML("Member Number", String.valueOf(m.number)));
		s.append(getVTableRowWithTitleHTML("Param Number", String.valueOf(m.getNumberOfRegParams())));
		s.append(getTableEndHTML());

		// ///////////////////
		// details of parameters
		if (m.hasProfiles()) {
			for (ProfileInfo info : m.profiles) {
				s.append("<hr><h2>Details for " + URI.get(info.serviceURI, true) + "</h2>\n\n");
				getAllServiceProfileHTML(s, info);
			}
		}
		if (m.hasPatterns()) {
			for (PatternInfo info : m.patterns) {
				s.append("<hr><h2>Details for ContextEventPattern</h2>\n\n");
				getAllContextEventPatternHTML(s, info);
			}
		}
	}

	private void createPeerHTML(StringBuilder s, PeerCard pc) {
		s.append("<h1>Peer</h1>\n");
		if (pc == null) {
			s.append("no peer card available<br>\n");
			return;
		}
		s.append(getTableStartHTML());
		s.append(getVTableRowWithTitleHTML("Peer ID", pc.getPeerID()));
		s.append(getVTableRowWithTitleHTML("Platform", pc.getPLATFORM_UNIT()));
		s.append(getVTableRowWithTitleHTML("Container", pc.getCONTAINER_UNIT()));
		s.append(getVTableRowWithTitleHTML("OS", pc.getOS()));
		s.append(getVTableRowWithTitleHTML("Role", pc.getRole().toString()));
		s.append(getTableEndHTML());
	}

	private void createSpaceHTML(StringBuilder s, SpaceDescriptor space) {
		s.append("<h1>Space</h1>\n");
		if (space == null) {
			s.append("no space descriptor available<br>\n");
			return;
		}
		SpaceCard sc = space.getSpaceCard();
		if (sc == null) {
			s.append("no space card available<br>\n");
			return;
		}
		s.append(getTableStartHTML());
		s.append(getVTableRowWithTitleHTML("Space name", sc.getSpaceName()));
		s.append(getVTableRowWithTitleHTML("Space ID", sc.getSpaceID()));
		s.append(getVTableRowWithTitleHTML("Description", sc.getDescription()));
		s.append(getVTableRowWithTitleHTML("Coordinator peer ID", sc.getPeerCoordinatorID()));
		s.append(getVTableRowWithTitleHTML("Peering channel", sc.getPeeringChannel()));
		s.append(getVTableRowWithTitleHTML("Peering channel name", sc.getPeeringChannelName()));
		s.append(getVTableRowWithTitleHTML("Retry", String.valueOf(sc.getRetry())));
		s.append(getVTableRowWithTitleHTML("Space life time", String.valueOf(sc.getSpaceLifeTime())));
		s.append(getTableEndHTML());
	}
}
