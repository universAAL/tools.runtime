package org.universAAL.tools.ucc.model.preferences;

/**
 * The preferences contains the setup information of uCC. It has a admin user
 * with a password, a ip to access the uCC, an ip to communicate with the uStore
 * and a language setting parameter.
 * 
 * @author Nicole Merkle
 * 
 */
public class Preferences {
	private String admin = "";
	private String pwd = "";
	private String uccIp = "";
	private String shopIp = "";
	private String uccPort = "";
	// private String wsPort = "";
	private String language = "";

	public Preferences() {
	}

	public Preferences(String admin, String pwd, String uccIP, String shopIP, String uccPort, String wsPort,
			String lang) {
		this.admin = admin;
		this.pwd = pwd;
		this.uccIp = uccIP;
		this.shopIp = shopIP;
		this.uccPort = uccPort;
		// this.wsPort = wsPort;
		this.language = lang;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUccIp() {
		return uccIp;
	}

	public void setUccIp(String uccIp) {
		this.uccIp = uccIp;
	}

	public String getShopIp() {
		return shopIp;
	}

	public void setShopIp(String shopIp) {
		this.shopIp = shopIp;
	}

	public String getUccPort() {
		return uccPort;
	}

	public void setUccPort(String uccPort) {
		this.uccPort = uccPort;
	}

	// public String getWsPort() {
	// return wsPort;
	// }
	//
	// public void setWsPort(String wsPort) {
	// this.wsPort = wsPort;
	// }

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
