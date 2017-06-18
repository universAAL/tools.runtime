package org.universAAL.tools.logmonitor.test;

import org.universAAL.middleware.bus.junit.BusTestCase;
import org.universAAL.middleware.bus.permission.AccessControl;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.middleware.managers.api.DistributedMWEventHandler;
import org.universAAL.middleware.managers.distributedmw.api.DistributedBusMemberManager;
import org.universAAL.middleware.managers.distributedmw.api.DistributedLogManager;
import org.universAAL.middleware.managers.distributedmw.impl.DistributedMWManagerImpl;
import org.universAAL.middleware.owl.Enumeration;
import org.universAAL.middleware.owl.Intersection;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.TypeURI;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.tracker.IBusMemberRegistry;
//import org.universAAL.ontology.lighting.ElectricLight;
//import org.universAAL.ontology.lighting.LightSource;
//import org.universAAL.ontology.lighting.Lighting;
//import org.universAAL.ontology.lighting.LightingOntology;
//import org.universAAL.ontology.location.LocationOntology;
//import org.universAAL.ontology.location.indoor.Room;
//import org.universAAL.ontology.phThing.PhThingOntology;
//import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.tools.logmonitor.Activator;
import org.universAAL.tools.logmonitor.BusMemberMonitor;

import java.awt.HeadlessException;

import org.universAAL.container.JUnit.JUnitContainer;
import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.container.JUnit.JUnitModuleContext.LogLevel;

public class Test extends BusTestCase {
	static boolean isSetUp = false;
	static DefaultServiceCaller caller;

	public void tearDown() {
		// don't do anything here so we don't have to set up again
		// List<BusMember> l = new LinkedList<BusMember>();
		// int i = 0;
		// while (true) {
		// try {
		// Thread.sleep(20);
		// l.add(new DefaultServiceCaller(mc));
		// i++;
		// if (i % 3 == 0) {
		// // remove one
		// //BusMember m = l.remove((int) (Math.random() * l.size()));
		// BusMember m = l.remove(l.size()-1);
		// System.out.println(" -- removing member: " + m.getURI());
		// m.close();
		// }
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	public void setUp() throws Exception {
		if (isSetUp)
			return;

		super.setUp();
		isSetUp = true;

		// OntologyManagement.getInstance().register(mc, new
		// LocationOntology());
		// OntologyManagement.getInstance().register(mc, new ShapeOntology());
		// OntologyManagement.getInstance().register(mc, new PhThingOntology());
		// OntologyManagement.getInstance().register(mc, new
		// LightingOntology());

		mc.setAttribute(AccessControl.PROP_MODE, "none");
		mc.setAttribute(AccessControl.PROP_MODE_UPDATE, "always");

		Activator.mc = mc;
		//((JUnitModuleContext) mc).setLogLevel(LogLevel.DEBUG);

		// init bus tracker
		org.universAAL.middleware.tracker.impl.Activator.fetchParams = new Object[] {
				IBusMemberRegistry.class.getName() };
		ModuleContext mcTracker = new JUnitModuleContext();
		org.universAAL.middleware.tracker.impl.Activator actTracker = new org.universAAL.middleware.tracker.impl.Activator();
		actTracker.start(mcTracker);

		// init distributed MW
		Object[] parBMLMgmt = new Object[] { DistributedBusMemberManager.class.getName() };
		Object[] parLLMgmt = new Object[] { DistributedLogManager.class.getName() };
		Object[] parEvtH = new Object[] { DistributedMWEventHandler.class.getName() };
		DistributedMWManagerImpl mm = new DistributedMWManagerImpl(mc, parBMLMgmt, parBMLMgmt, parLLMgmt, parLLMgmt,
				parEvtH, parEvtH);

		// start log monitor
		try {
			Activator a = new Activator();
			a.start();
			((JUnitContainer) mc.getContainer()).registerLogListener(Activator.lm);
		} catch (HeadlessException e) {
			System.out.println(" -- HeadlessException");
		}
	}

	// public static ContextEventPattern[] providedEvents(Lighting theServer) {
	// MergedRestriction predicateRestriction = MergedRestriction
	// .getFixedValueRestriction(ContextEvent.PROP_RDF_PREDICATE,
	// LightSource.PROP_SOURCE_BRIGHTNESS);
	// MergedRestriction objectRestriction = MergedRestriction
	// .getAllValuesRestrictionWithCardinality(
	// ContextEvent.PROP_RDF_OBJECT, new Enumeration(
	// new Integer[] { new Integer(0),
	// new Integer(100) }), 1, 1);
	// LightSource[] myLights = new LightSource[] {
	// new LightSource("lamp1", ElectricLight.lightBulb, new Room(
	// "room1")),
	// new LightSource("lamp2", ElectricLight.ledLamp, new Room(
	// "room2")) };
	//
	// MergedRestriction subjectRestriction = MergedRestriction
	// .getAllValuesRestrictionWithCardinality(
	// ContextEvent.PROP_RDF_SUBJECT,
	// new Enumeration(myLights), 1, 1);
	// ContextEventPattern cep1 = new ContextEventPattern();
	// cep1.addRestriction(subjectRestriction);
	// cep1.addRestriction(predicateRestriction);
	// cep1.addRestriction(objectRestriction);
	// Intersection xsection = new Intersection();
	// xsection.addType(new TypeURI(LightSource.MY_URI, false));
	// xsection.addType(MergedRestriction.getFixedValueRestriction(
	// LightSource.PROP_HAS_TYPE, ElectricLight.lightBulb));
	// xsection.addType(MergedRestriction
	// .getAllValuesRestrictionWithCardinality(
	// LightSource.PROP_PHYSICAL_LOCATION, Room.MY_URI, 1, 1));
	// subjectRestriction = MergedRestriction
	// .getAllValuesRestrictionWithCardinality(
	// ContextEvent.PROP_RDF_SUBJECT, xsection, 1, 1);
	// ContextEventPattern cep2 = new ContextEventPattern();
	// cep2.addRestriction(subjectRestriction);
	// cep2.addRestriction(predicateRestriction);
	// cep2.addRestriction(objectRestriction);
	//
	// return new ContextEventPattern[] { cep1, cep2 };
	// }

	public void testAddScript() {
		LogUtils.logDebug(mc, this.getClass(), "method", "msg");
		caller = new DefaultServiceCaller(mc);

		// Lighting theServer = new Lighting();
		// ContextEventPattern[] cep = providedEvents(theServer);
		// ContextProvider info = new ContextProvider("TestContextProvider");
		// info.setType(ContextProviderType.controller);
		// info.setProvidedEvents(cep);
		// ContextPublisher cp = new DefaultContextPublisher(mc, info);
		//
		// ContextSubscriber cs = new ContextSubscriber(mc, cep) {
		// @Override
		// public void handleContextEvent(ContextEvent event) {
		// }
		//
		// @Override
		// public void communicationChannelBroken() {
		// }
		// };

		// BusMemberListener l =
		// org.universAAL.tools.logmonitor.bus_member.LogMonitor.busMemberListener;
		// l.

		// System.out.println(Activator.serialize(cep[0]));
		// System.out.println(Activator.serialize(cep[1]));

		// try {
		// Thread.sleep(2000000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		caller.close();
		// cp.close();
	}
}
