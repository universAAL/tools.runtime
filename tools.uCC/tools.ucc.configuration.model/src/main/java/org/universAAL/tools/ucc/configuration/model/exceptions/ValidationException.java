package org.universAAL.tools.ucc.configuration.model.exceptions;

/**
 *
 * This exception will be thrown if any exception accures during the execution
 * of the validators.
 *
 * @author Sebastian.Schoebinge
 *
 */

@SuppressWarnings("serial")
public class ValidationException extends Exception {

	public ValidationException(String msg) {
		super(msg);
	}

}
