package org.universAAL.tools.makrorecorder.osgi.pattern;

import java.util.Iterator;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.tools.makrorecorder.osgi.Activator;

/**
* 
* @author maxim djakow
*/
public class PatternListener extends ContextSubscriber {

	private Pattern pattern = null;
	private Iterator<Resource> input = null;
	private ContextEvent waitForElement = null;
	
	public PatternListener(Pattern pattern) {
		super(Activator.getModuleContext(), new ContextEventPattern[]{});
		this.pattern = pattern;
	}
	
	@Override
	public void communicationChannelBroken() {
		pattern.deactivate();
	}

	@Override
	public void handleContextEvent(ContextEvent ce) {
		if(waitForElement != null) {
			if(		waitForElement.getRDFSubject().equals(ce.getRDFSubject()) &&
					waitForElement.getRDFPredicate().equals(ce.getRDFPredicate()) &&
					waitForElement.getRDFObject().equals(ce.getRDFObject())) {
				if(input.hasNext()) {
					waitForElement = (ContextEvent)input.next();
				} else {
					pattern.sendOutput();
					input = pattern.getInput().iterator();
					if(input.hasNext())
						waitForElement = (ContextEvent)input.next();
				}
			}
		}
	}
	
	public void activate() {		
		input = pattern.getInput().iterator();		
		if(input.hasNext())
			waitForElement = (ContextEvent)input.next();
		addNewRegParams(new ContextEventPattern[]{new ContextEventPattern()});
	}
	
	public void deactivate() {
		removeMatchingRegParams(new ContextEventPattern[]{new ContextEventPattern()});
		
		input = null;
		super.close();
	}	
}
