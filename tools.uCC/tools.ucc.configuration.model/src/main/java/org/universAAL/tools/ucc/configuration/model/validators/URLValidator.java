package org.universAAL.tools.ucc.configuration.model.validators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.tools.ucc.configuration.model.Activator;
import org.universAAL.tools.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.tools.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.tools.ucc.configuration.model.exceptions.ValidationException;
import org.universAAL.tools.ucc.configuration.model.interfaces.ConfigurationValidator;

public class URLValidator implements ConfigurationValidator {

	public URLValidator() {
	}

	public boolean isValid(ConfigOptionRegistry registry, Value value) {

		if (value == null || "".equals(value.getValue())) {
			return true;
		}
		try {
			int responseCode = getResponseCode(value.getValue());
			LogUtils.logInfo(Activator.getContext(), this.getClass(), "isValid",
					new Object[] { "response code: " + responseCode }, null);

			if (responseCode == 200) {
				return true;
			}
		} catch (MalformedURLException e) {
			LogUtils.logError(Activator.getContext(), this.getClass(), "isValid", new Object[] { e.toString() }, null);

		} catch (IOException e) {
			LogUtils.logError(Activator.getContext(), this.getClass(), "isValid", new Object[] { e.toString() }, null);

		}
		return false;
	}

	public void validate(ConfigOptionRegistry registry, Value value) throws ValidationException {
		if (!isValid(registry, value)) {
			throw new ValidationException("No valid URL");
		}
	}

	public void setAttributes(String[] attributes) {
		// TODO Auto-generated method stub
	}

	public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
		URL u = new URL(urlString);
		HttpURLConnection huc = (HttpURLConnection) u.openConnection();
		huc.setRequestMethod("GET");
		huc.setConnectTimeout(200);
		huc.connect();
		return huc.getResponseCode();
	}

}
