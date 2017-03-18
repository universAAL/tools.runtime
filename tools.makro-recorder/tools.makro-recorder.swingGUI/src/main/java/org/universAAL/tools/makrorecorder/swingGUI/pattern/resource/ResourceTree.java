package org.universAAL.tools.makrorecorder.swingGUI.pattern.resource;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.universAAL.middleware.rdf.Resource;

/**
*
* @author maxim djakow
*/
public class ResourceTree extends JTree {
	
	private static final long serialVersionUID = 1L;

	Object data = null;
	
	Hashtable<Resource, DefaultMutableTreeNode> visitedElements = new Hashtable<Resource, DefaultMutableTreeNode>();
	
	public ResourceTree() {
		super();
	}
	
	public ResourceTree(Object data) {
		super();
		setData(data);
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
		update();
	}
	
	public void update() {
		if(data==null) {
			this.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(), false));
		} else {
			this.setModel(new DefaultTreeModel(createNodeFromObject(data), false));
			visitedElements.clear();
		}
	}
	
	public DefaultMutableTreeNode createNodeFromObject(Object o) {
		DefaultMutableTreeNode node;
		if (o instanceof Resource) {					
			node = createNodeFromResource((Resource)o);
		} else if (o instanceof List<?>) {
			node = new DefaultMutableTreeNode(o.getClass());
			for (Object i : (List<?>)o) {
				node.add(createNodeFromObject(i));
			}
		} else {
			node = new DefaultMutableTreeNode(o.getClass());
			node.add(new DefaultMutableTreeNode(o.toString()));
		}
		return node;
	}
	
	public DefaultMutableTreeNode createNodeFromResource(Resource r) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		
		node.setUserObject(r.getClass());
		node.add(new DefaultMutableTreeNode(r.getURI()));
		
		if(visitedElements.put(r, node)==null) {		
			Enumeration<?> props = r.getPropertyURIs();
		    while (props.hasMoreElements()) {	    	
		    	String propKey = (String) props.nextElement();
				Object propVal = r.getProperty(propKey);
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(propKey);
				node.add(child);
				child.add(createNodeFromObject(propVal));
		    }
		} else {
			node = visitedElements.get(r);
		}
		
		return node;
	}
}
