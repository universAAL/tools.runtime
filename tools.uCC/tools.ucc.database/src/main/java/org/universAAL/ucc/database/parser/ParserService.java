package org.universAAL.ucc.database.parser;

import org.universAAL.middleware.managers.deploy.uapp.model.AalUapp;
import org.universAAL.ucc.model.usrv.AalUsrv;

/**
 * ParserService for getting a UAPP or USRV Java Instance.
 * 
 * @author Nicole Merkle
 *
 */
public interface ParserService {
	/**
	 * Gets the UAPP representation of the current uapp config xml.
	 * 
	 * @param path
	 *            the path of the uapp config xml
	 * @return a AalUapp instance
	 */
	public AalUapp getUapp(String path);

	/**
	 * Gets the USRV representation of the current usrv config xml.
	 * 
	 * @param path
	 *            the path of the usrv config xml.
	 * @return a AalUsrv instance
	 */
	public AalUsrv getUsrv(String path);
}
