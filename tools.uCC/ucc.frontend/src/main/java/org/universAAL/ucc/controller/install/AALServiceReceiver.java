package org.universAAL.ucc.controller.install;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.universAAL.ucc.service.manager.Activator;

import com.vaadin.ui.Upload.Receiver;

public class AALServiceReceiver implements Receiver {
	private final static String dir = "tempUsrvFiles";

	public OutputStream receiveUpload(String filename, String mimeType) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(Activator.getModuleConfigHome().getAbsolutePath() +"/"+dir + "/"+filename);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		return os;

	}

}
