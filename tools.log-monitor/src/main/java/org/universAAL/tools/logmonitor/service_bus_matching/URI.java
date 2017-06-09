package org.universAAL.tools.logmonitor.service_bus_matching;

/**
 * 
 * @author Carsten Stockloew
 *
 */
public class URI {

	private String uri;

	public URI(String uri) {
		this.uri = uri;
	}

	public static String get(String uri, boolean shortForm) {
		return new URI(uri).get(shortForm);
	}

	public String get(boolean shortForm) {
		if (shortForm)
			return getShortURI();
		return getURI();
	}

	public String getURI() {
		return uri;
	}

	public String getShortURI() {
		int idx = uri.lastIndexOf('#');
		if (idx == -1)
			return uri;

		return uri.substring(idx);
	}
}
