package org.universAAL.tools.makrorecorder.swingGUI.pattern.resource;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.tools.makrorecorder.swingGUI.Activator;

/**
*
* @author maxim djakow
*/
public class ResourceInfoPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private Resource resource;
	
	private JLabel infoLabel;
	private JLabel plainInfoLabel;
	private ResourceTree resourceTree;
	
	public ResourceInfoPanel() {
		super();
		init();
		load(null);
	}
	
	public ResourceInfoPanel(Resource resource) {
		super();
		init();
		load(resource);
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public void load(Resource r) {
		setResource(r);
		reload();
	}
	
	public void reload() {
    	if(resource == null) {
    		infoLabel.setText("");
    		plainInfoLabel.setText("");
            resourceTree.setData(null);
            resourceTree.setVisible(false);
    	} else {
    		infoLabel.setText(genInfo(resource));
    		plainInfoLabel.setText(genPlainInfo(resource));
            resourceTree.setData(resource);
            resourceTree.setVisible(true);
    	}
    }
	
	private void init() {

		setLayout(new BorderLayout());
		
		JTabbedPane tp = new JTabbedPane();
		tp.setBackground(Color.WHITE);
		
        infoLabel = new JLabel();
        infoLabel.setVerticalAlignment(SwingConstants.TOP);
        JScrollPane infoLabelsp = new JScrollPane(infoLabel);
        infoLabelsp.setBorder(BorderFactory.createEmptyBorder());
        
        plainInfoLabel = new JLabel();
        plainInfoLabel.setVerticalAlignment(SwingConstants.TOP);
        JScrollPane plainInfoLabelsp = new JScrollPane(plainInfoLabel);
        plainInfoLabelsp.setBorder(BorderFactory.createEmptyBorder());
        
        resourceTree = new ResourceTree();
        resourceTree.setBackground(this.getBackground());
        JScrollPane resourceTreesp = new JScrollPane(resourceTree);
        resourceTreesp.setBorder(BorderFactory.createEmptyBorder());
        
        tp.addTab("Resource Information", infoLabelsp);
        tp.addTab("Resource Tree", resourceTreesp);
        tp.addTab("Plain View", plainInfoLabelsp);
        
        add(tp,BorderLayout.CENTER);
	}

    public static String genInfo(Resource r) {
    	String info = "<html><body style=\"padding:10px\">";
    	if(r instanceof ServiceRequest)
    		info += genInfo((ServiceRequest)r);
    	else if(r instanceof ContextEvent)
    		info += genInfo((ContextEvent)r);
    	info += "</body></html>";
    	return info;
    }
    
    public static String genInfo(ContextEvent ce) {
    	String info = "";
    	info += "<h2>Context-Event</h2>";
    	info += "<br/>Subject:";
    	info += "<ul><li>"+ce.getRDFSubject().toString()+"</li></ul>";
    	info += "<br/>Predicate:";
    	info += "<ul><li>"+ce.getRDFPredicate().toString()+"</li></ul>";
    	info += "<br/>Object:";
    	info += "<ul><li>"+ce.getRDFObject().toString()+"</li></ul>";
    	return info;
    }  
    
    public static String genInfo(ServiceRequest sr) {
    	//return "<h2>Service-Request</h2><pre>"+Activator.getSerializer().serialize(sr)+"</pre>";
    	String info = "";
    	info += "<h2>Service-Request</h2>";
    	info += "<br/>Requested Service:";
    	info += "<ul><li>"+sr.getRequestedService().getType()+"</li></ul>";
    	Resource effects[] = sr.getRequiredEffects();
    	if(effects.length>0) {
	    	info += "<br/>Required Effects:";
	    	info += "<ul>";
	    	for (Resource effect : effects) {
				for (String type : effect.getTypes()) {
					info += "<li>"+shortURI(type)+" "+effect.getProperty("http://ontology.universAAL.org/Service.owl#propertyValue").toString()+"</li>";
				}
			}
    	}
    	info += "</ul>";
    	Resource outputs[] = sr.getRequiredOutputs();
    	if(outputs.length>0) {
	    	info += "<br/>Required Outputs:";
	    	info += "<ul>";
	    	for (Resource output : outputs) {
				for (String type : output.getTypes()) {
					info += "<li>"+shortURI(type)+"</li>";
				}    	
			}
	    	info += "</ul>";
    	}
    	
    	return info;
    }
    
    public static String genPlainInfo(Resource r) {
    	String info = "<html><body style=\"padding:10px\">";
    	info += "<pre>"+Activator.getSerializer().serialize(r)+"</pre>";
    	info += "</body></html>";
    	return info;
	}
    
    public static String shortURI(String s) {
    	return s.substring(s.indexOf("#")+1);
    }
    
}
