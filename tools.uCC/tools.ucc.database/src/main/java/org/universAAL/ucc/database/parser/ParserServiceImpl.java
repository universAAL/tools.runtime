package org.universAAL.ucc.database.parser;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.universAAL.middleware.managers.deploy.uapp.model.AalUapp;
import org.universAAL.ucc.model.usrv.AalUsrv;

public class ParserServiceImpl implements ParserService {

	public AalUapp getUapp(String path) {
		JAXBContext jc = null;
		Unmarshaller um = null;
		AalUapp uapp = null;
		try {
			// jc =
			// JAXBContext.newInstance("org.universAAL.middleware.deploymanager.uapp.model",
			// AalUapp.class.getClassLoader());
			jc = JAXBContext.newInstance(org.universAAL.middleware.managers.deploy.uapp.model.ObjectFactory.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		try {
			um = jc.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		try {
			uapp = (AalUapp) um.unmarshal(new File(path));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return uapp;
	}

	public AalUsrv getUsrv(String path) {
		JAXBContext jc = null;
		Unmarshaller um = null;
		AalUsrv usrv = null;
		try {
			// jc = JAXBContext.newInstance("org.universAAL.ucc.model.usrv",
			// AalUsrv.class.getClassLoader());
			jc = JAXBContext.newInstance(org.universAAL.ucc.model.usrv.ObjectFactory.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		try {
			um = jc.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		try {
			usrv = (AalUsrv) um.unmarshal(new File(path));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return usrv;
	}

}
