package org.universAAL.tools.makrorecorder.osgi;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Vector;


import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
//import org.universAAL.tools.makrorecorder.gui.MainGUI;
import org.universAAL.tools.makrorecorder.osgi.pattern.Pattern;

/**
*
* @author maxim djakow
*/
public class MakroRecorder extends Observable {
    
       
    private static final String patternBaseDir = "Pattern/";
    
    private Vector<File> patternDirs;
        
    private HashMap<String,Pattern> pattern = null;
    
    private DefaultServiceCaller sc = null;
    
    public MakroRecorder(ModuleContext context) {
        pattern = new HashMap<String, Pattern>();
        patternDirs = new Vector<File>();
        patternDirs.add(new File(patternBaseDir));
        sc = new DefaultServiceCaller(Activator.getModuleContext());
        for(File f : patternDirs)
        	f.mkdir();
    }

    public static String getPatternBaseDir() {
        return patternBaseDir;
    }
    
    public Vector<File> getPatternDirs() {
		return patternDirs;
	}
    
    public HashMap<String, Pattern> getAllPattern() {
        return pattern;
    }
    
    public Collection<String> getAllPatternNames() {
        return pattern.keySet();
    }
    
    public Pattern getPatternByName(String name) {
        return pattern.get(name);
    }
    
    public boolean addPattern(Pattern newPattern) {
        if(pattern != null && newPattern != null) {
        	pattern.put(newPattern.getName(),newPattern);
        	if(countObservers()>0){
    	        setChanged();
    	        notifyObservers("Pattern added");
    	    } 
        	return true;
        }
        return false;
    }
    
    public boolean removePattern(String name) {
    	if(pattern != null && name != null) {    		
    		Pattern removedPattern = pattern.remove(name);
    		if(removedPattern != null) {
	    		removedPattern.deactivate();
	    		File file = removedPattern.getFile();
				if(file.exists()) {
					file.delete();
					if(countObservers()>0){
		    	        setChanged();
		    	        notifyObservers("Pattern removed");
		    	    } 
				}
    		}
    	}
    	return false;
    }
    
    public boolean savePattern(Pattern newPattern) {    	
    	addPattern(newPattern);
    	newPattern.saveToFile();
    	return true;
    }
    
    public void saveToFiles() {
        for (String key : getAllPatternNames()) {
            getPatternByName(key).saveToFile();
        }
    }
    
    public void loadFromFiles() {
    	for(File d : patternDirs) {
	    	 File[] files = d.listFiles();
	    	 if(files != null) {
		    	 for(File f : files) {
		    		 if(f.toString().endsWith(Pattern.patternFileType)) {
		    			 addPattern(loadFromFile(f));
		    		 }
		    	 }
	    	 }
    	}
	}
    
    public Pattern loadFromFile(File f) {
    	return Pattern.loadFromFile(f);
    }
    
    public boolean sendOut(Resource r) {
    	if(r instanceof ServiceRequest) {
			return sc.call((ServiceRequest)r).getCallStatus() == CallStatus.succeeded;
		} else if(r instanceof ContextEvent) {
			
		}
    	return false;
    }
    
    public void reload() {
    	pattern.clear();
    	loadFromFiles();
	}
}
