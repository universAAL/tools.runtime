package org.universAAL.tools.makrorecorder.osgi.pattern;

import java.io.File;
import java.util.Observable;
import java.util.Vector;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.tools.makrorecorder.osgi.Activator;
import org.universAAL.tools.makrorecorder.osgi.MakroRecorder;

/**
*
* @author maxim djakow
*/
public class Pattern extends Observable{
	
	public static final String patternFileType = ".xml";
	
	private String name = "";
	private String description = "";
	
	private transient Vector<Resource> input = new Vector<Resource>();
	
	private transient Vector<Resource> output = new Vector<Resource>();
	
	private boolean active = false;
	
	private transient BusRecorder busRecorder = new BusRecorder(this);
	
	private transient PatternListener listener = null;
	
	public Pattern() {
		
	}
	
	public Pattern(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	@SuppressWarnings("unchecked")
	public Pattern clone() {
    	Pattern ret = new Pattern();
    	ret.name = name;
    	ret.description = description;
    	ret.input = (Vector<Resource>) input.clone();
    	ret.output = (Vector<Resource>) output.clone();
    	return ret;
    }	
    
	
	public void activate() {
		if(listener == null)
			listener = new PatternListener(this);
		listener.activate();
		active = true;
	}		
	
	public void deactivate() {
		if(listener != null)
			listener.deactivate();
		active = false;
		listener = null;
	}
	
	public void startRecording() {
		busRecorder.start();
	}
	
	public void stopRecording() {
		busRecorder.stop();
	}
	
	public boolean sendOutput() {		
		for(Resource r : output) {
			if(r instanceof ServiceRequest) {
				return new DefaultServiceCaller(Activator.getModuleContext()).call((ServiceRequest)r).getCallStatus() == CallStatus.succeeded;
			} else if(r instanceof ContextEvent) {
				
			}
		}
    	return false;
	}
	
	public File getFile() {
		return new File(MakroRecorder.getPatternBaseDir()+getName()+patternFileType);
	}
        
    public void saveToFile() {
        saveToFile(getFile());
    }
        
    public void saveToFile(File file) {
    	PatternXMLParser.genXML(this);
    }   
    
        
    public static Pattern loadFromFile(File file) {
    	
    	Pattern ret = null;
        ret = PatternXMLParser.genPattern(file);
        ret.busRecorder = new BusRecorder(ret);
        return ret;
    }    
        
    public boolean isActive() {
		return active;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Vector<Resource> getInput() {
		return input;
	}
	
	public Vector<Resource> getOutput() {
		return output;
	}
	
	public void addInput(Resource r) {
		input.add(r);
		if(countObservers()>0){
	        setChanged();
	        notifyObservers("new input");
	    }
	}
	
	public void addOutput(Resource r) {
		output.add(r);
		if(countObservers()>0){
	        setChanged();
	        notifyObservers("new output");
	    }
	}
}
