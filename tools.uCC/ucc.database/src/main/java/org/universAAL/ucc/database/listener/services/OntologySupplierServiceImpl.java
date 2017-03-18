package org.universAAL.ucc.database.listener.services;

import java.util.ArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import org.universAAL.ucc.database.aalspace.DataAccess;
import org.universAAL.ucc.database.listener.interfaces.OntologyChangedListener;
import org.universAAL.ucc.database.listener.interfaces.OntologySupplierService;
import org.universAAL.ucc.model.jaxb.OntologyInstance;

public class OntologySupplierServiceImpl implements OntologySupplierService {
	private static ArrayList<OntologyChangedListener>listeners = new ArrayList<OntologyChangedListener>();
	
	public void addListener(OntologyChangedListener listener) {
		listeners.add(listener);

	}

	public void removeListener(OntologyChangedListener listener) {
		for(OntologyChangedListener l : listeners) {
			if(l.equals(listener)) {
				listeners.remove(l);
			}
		}

	}


	public ArrayList<OntologyInstance> getOntology(String flat) {
		BundleContext context = FrameworkUtil.getBundle(getClass()).getBundleContext();
		ServiceReference ref = context.getServiceReference(DataAccess.class.getName());
		DataAccess access = (DataAccess)context.getService(ref);
			ArrayList<OntologyInstance> ont = access.getFormFields(flat);
		
		return ont;
	}

	public static ArrayList<OntologyChangedListener> getListeners() {
		return listeners;
	}

	public static void setListeners(ArrayList<OntologyChangedListener> listeners) {
		OntologySupplierServiceImpl.listeners = listeners;
	}
	
	

}
