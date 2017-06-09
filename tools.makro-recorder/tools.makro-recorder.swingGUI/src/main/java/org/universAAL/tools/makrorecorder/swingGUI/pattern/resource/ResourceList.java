package org.universAAL.tools.makrorecorder.swingGUI.pattern.resource;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;

/**
 *
 * @author maxim djakow
 */
public class ResourceList extends JList {

	private static final long serialVersionUID = 1L;
	private Vector<Resource> resources;

	public ResourceList(Vector<Resource> resources) {
		this.resources = resources;
		init();
		reload();
	}

	private void init() {
		setModel(new DefaultListModel());
		setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = getSelectedIndex();
					Resource r = EditResourceGUI.edit(resources.get(index));
					if (r != null) {
						resources.set(index, r);
						reload();
					}
				}
			}
		});
		setDragEnabled(true);
		setDropMode(DropMode.INSERT);
		setTransferHandler(new TransferHandler() {
			private static final long serialVersionUID = 1L;

			public boolean canImport(TransferHandler.TransferSupport info) {
				if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					return false;
				}
				return true;
			}

			protected Transferable createTransferable(JComponent c) {
				return new StringSelection((String) ((JList) c).getSelectedValue());
			}

			public int getSourceActions(JComponent c) {
				return TransferHandler.MOVE;
			}

			public boolean importData(TransferHandler.TransferSupport info) {
				if (!info.isDrop()) {
					return false;
				}

				int seleIndex = ((JList) info.getComponent()).getSelectedIndex();
				int dropIndex = ((JList.DropLocation) info.getDropLocation()).getIndex();

				if (seleIndex > -1) {
					Resource r = resources.get(seleIndex);
					resources.remove(seleIndex);
					if (seleIndex < dropIndex)
						dropIndex--;
					resources.insertElementAt(r, dropIndex);
					reload();
				}
				return true;
			}
		});
	}

	public void reload() {
		DefaultListModel dlm = (DefaultListModel) getModel();
		dlm.clear();
		for (Resource r : resources) {
			dlm.addElement(shortResourceInfo(r));
		}
	}

	public Vector<Resource> getResources() {
		return resources;
	}

	public void setResources(Vector<Resource> resources) {
		this.resources = resources;
	}

	public Resource getSelectedResource() {
		return resources.get(getSelectedIndex());
	}

	public void removeSelection() {
		removeSelectionInterval(getSelectedIndex(), getSelectedIndex());
	}

	public static String shortURI(String s) {
		return s.substring(s.indexOf("#") + 1);
	}

	public static String shortResourceInfo(Resource r) {
		String ret = "";
		if (r instanceof ContextEvent) {
			ContextEvent ce = (ContextEvent) r;
			ret += shortURI(ce.getRDFSubject().toString()) + " ";
			ret += shortURI(ce.getRDFPredicate()) + " ";
			ret += shortURI(ce.getRDFObject().toString());
		} else if (r instanceof ServiceRequest) {
			ServiceRequest sr = (ServiceRequest) r;
			ret += shortURI(sr.getRequestedService().getType());
			for (Resource effect : sr.getRequiredEffects()) {
				for (String type : effect.getTypes()) {
					ret += " " + shortURI(type);
				}
				ret += " " + effect.getProperty("http://ontology.universAAL.org/Service.owl#propertyValue").toString();
			}
			for (Resource output : sr.getRequiredOutputs()) {
				for (String type : output.getTypes()) {
					ret += " " + shortURI(type);
				}
			}

		} else {
			ret += r.getClass() + ": " + r.toString();
		}
		return ret;
	}
}
