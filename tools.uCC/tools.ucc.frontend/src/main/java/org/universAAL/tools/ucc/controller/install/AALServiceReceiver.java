package org.universAAL.tools.ucc.controller.install;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.universAAL.tools.ucc.service.manager.Activator;

import com.vaadin.ui.Upload.Receiver;

public class AALServiceReceiver implements Receiver {

	public OutputStream receiveUpload(String filename, String mimeType) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(Activator.getTempUsrvFiles(), filename));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		return os;
	}
}
