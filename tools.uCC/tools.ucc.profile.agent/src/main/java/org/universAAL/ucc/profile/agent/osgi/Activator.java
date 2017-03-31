package org.universAAL.ucc.profile.agent.osgi;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.profile.AALSpace;
import org.universAAL.ontology.profile.AALSpaceProfile;
import org.universAAL.ontology.profile.AssistedPerson;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;
//import org.universAAL.ontology.profile.userid.UserIDProfile;
import org.universAAL.ucc.profile.agent.ProfileAgent;
import org.universAAL.ucc.profile.agent.impl.ProfileAgentImpl;
//import org.universaal.ontology.health.owl.HealthProfile;
//import org.universaal.ontology.health.owl.TakeMeasurementActivity;
//import org.universaal.ontology.health.owl.Treatment;

public class Activator implements BundleActivator {

  public static BundleContext context = null;

  private static ModuleContext moduleContext = null;
  
    
  /*
   * (non-Javadoc)
   * 
   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
   */
  public void start(BundleContext context) throws Exception {
	  Activator.context = context;
		Activator.moduleContext = uAALBundleContainer.THE_CONTAINER
			.registerModule(new Object[] { context });
		
		LogUtils.logInfo(moduleContext, Activator.class, "start", "starting Activator");
    this.context.registerService(ProfileAgent.class.getName(), new ProfileAgentImpl(moduleContext), null);

	LogUtils.logInfo(moduleContext, Activator.class, "start", "start testing...");
    //test();
	LogUtils.logInfo(moduleContext, Activator.class, "start", "Test Space server....");
    System.out.println("Test Space server....");
    //testSpaceServer();  
  }


  private void testSpaceServer() {
	  ProfileAgentImpl agent = new ProfileAgentImpl(moduleContext); 
	  
	  String spaceURI = "urn:org.universAAL.aal_space:test_env#my_home_space";
	  String aalSpaceProfileURI = "urn:org.universAAL.aal_space:test_env#some_space_profile";
	  String device1URI = "urn:org.universAAL.aal_space:test_env#Device1";
	  String device2URI = "urn:org.universAAL.aal_space:test_env#Device2";
	  
	  
	  AALSpace space = new AALSpace(spaceURI);
	  //AALSpaceProfile aalSpaceProfile = new AALSpaceProfile(aalSpaceProfileURI);
	  
	  Device device1 = new Device(device1URI);
	  Device device2 = new Device(device2URI);
	  
	    /*test add/get space*/
	  	System.out.println("[TEST] getting all AAL spaces...");
	  	List<AALSpace> spaces = agent.getSpaces();
	  	if (spaces==null) {
	  		// TODO: create the space with appropriate properties from MW
	  		AALSpace myspace = new AALSpace(spaceURI);
	  		agent.addSpace(myspace);	  	
	  		spaces = agent.getSpaces();
	  	}
	  	System.out.println("Result is: " + spaces);
	  	for (int i=0; i<spaces.size(); i++) {
	  		System.out.println("[TEST] has a space: " + spaces.get(i).getURI());
	  	}
	  		    
	    //System.out.println("[TEST] adding AAL space ..." + space.getURI());
	    //System.out.println("Result is: " + agent.addSpace(space));
	    System.out.println("[TEST] getting AAL space..." + space.getURI());
	    System.out.println("Result is: " + agent.getSpace(space));
	    System.out.println("[TEST] getting all AAL spaces...");
	    System.out.println("Result is: " + agent.getSpaces());
	    
	    
	    /*test add/get space profile*/
/*	    System.out.println("[TEST] adding aalspace profile..." + aalSpaceProfile.getURI());
	    System.out.println("Result is: " + agent.addSpaceProfile(aalSpaceProfile));
	    System.out.println("[TEST] getting AAL space profile ..." + aalSpaceProfile.getURI());
	    System.out.println("Result is: " + agent.getSpaceProfile(aalSpaceProfile));
*/
	    /*test add space profile to a space */
	    
	    /*test add/get devices*/
	    System.out.println("[TEST] adding device 1..." + device1.getURI());
	    System.out.println("Result is: " + agent.addDevice(device1));
	    System.out.println("[TEST] adding device 2 to space..." + device2.getURI());
	    System.out.println("Result is: " + agent.addDevice(device2, space));
	    System.out.println("[TEST] getting device 2..." + device2.getURI());
	    Object dret = agent.getDevice(device2URI);
	    if (dret instanceof Device)
	    	System.out.println("Result is a device: " + device2URI);
	    else System.out.println("Result is not a device!");
	    System.out.println("[TEST] updating device 2..." + device2.getURI());
	    System.out.println("Result is: " + agent.updateDevice(device2));
	    System.out.println("[TEST] deleting device 2..." + device2.getURI());
	    System.out.println("Result is: " + agent.deleteDevice(device2URI));
	    System.out.println("[TEST] getting device 2..." + device2.getURI());
	    Object dret2 = agent.getDevice(device2URI);
	    System.out.println("[TEST] the result is: " + dret2);
	    System.out.println("[TEST] adding device 1 to space..." + device1.getURI() + " " + space.getURI());
	    System.out.println("Result is: " + agent.addDeviceToSpace(device1, space)); 
	    //System.out.println("[TEST] adding device 2 to space..." + device2.getURI() + " " + space.getURI());
	    //System.out.println("Result is: " + agent.addDeviceToSpace(device2, space)); 
	    /** This does not work !!!!**/
/*	    System.out.println("[TEST] getting devices of space..." + space.getURI());
	    System.out.println("Result is: " + agent.getDevicesOfSpace(space)); */

	    
  } 


/*
   * (non-Javadoc)
   * 
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception {

  }

  public void test() {
    ProfileAgentImpl agent = new ProfileAgentImpl(moduleContext);

    String userID = "Maria";
    String userURI = "urn:org.universAAL.aal_space:test_env#"+userID;
    String userProfileURI = "urn:org.universAAL.aal_space:test_env#Maria_user_profile";
    String userIDProfileURI = "urn:org.universAAL.aal_space:test_env#Maria_userID_profile";
    //String healthProfileURI = "urn:org.universAAL.aal_space:test_env#Maria_health_profile";
    String aalSpaceProfileURI = "urn:org.universAAL.aal_space:test_env#some_space_profile";
    String device1URI = "urn:org.universAAL.aal_space:test_env#Device1";
    String device2URI = "urn:org.universAAL.aal_space:test_env#Device2";
    String spaceURI = "urn:org.universAAL.aal_space:test_env#space";
    String service1URI = "urn:org.universAAL.aal_space:test_env#Service1";
    String service2URI = "urn:org.universAAL.aal_space:test_env#Service2";
    
    String userID2 = "John";
    String userURI2 = "urn:org.universAAL.aal_space:test_env#"+userID2;
    String userProfileURI2 = "urn:org.universAAL.aal_space:test_env#John_user_profile";
    String userIDProfileURI2 = "urn:org.universAAL.aal_space:test_env#John_userID_profile";
    
    User maria = new User(userURI);
    //AssistedPerson maria = new AssistedPerson(userURI);
    UserProfile userProfile = new UserProfile(userProfileURI);
    AALSpaceProfile aalSpaceProfile = new AALSpaceProfile(aalSpaceProfileURI);
    
    User john = new User(userURI2);
    //AssistedPerson john = new AssistedPerson(userURI2);
    UserProfile userProfile2 = new UserProfile(userProfileURI2); 
    
/*    Device device1 = new Device(device1URI);
    Device device2 = new Device(device2URI);
    
    AALSpace space = new AALSpace(spaceURI);
    
    AALService service1 = new AALService(service1URI);
    AALService service2 = new AALService(service2URI); */
    
    /**** TESTING PROFILING SERVER *************/
    /*test add/get user*/
    System.out.println("[TEST] adding user ..."+maria.getURI());
    System.out.println("Result is: " + agent.addUser(maria));
/*    System.out.println("[TEST] getting user1...");
    System.out.println("Result is: " + agent.getUser(maria)); */
//    System.out.println("[TEST] getting user with uri " + userURI);
//    System.out.println("Result is: " + agent.getUser(userURI));  
    
    System.out.println("[TEST] adding user ..."+john.getURI());
    System.out.println("Result is: " + agent.addUser(john));
//    System.out.println("[TEST] getting user2...");
//    System.out.println("Result is: " + agent.getUser(john));
//    System.out.println("[TEST] getting user with uri " + userURI2);
//    System.out.println("Result is: " + agent.getUser(userURI2));  
    
    String perURI = "urn:org.universAAL.aal_space:test_env#Per";
    //AssistedPerson per = new AssistedPerson(perURI);
    User per = new User(perURI);
    System.out.println("[TEST] adding user ..."+per.getURI());
    System.out.println("Result is: " + agent.addUser(per));
    System.out.println("[TEST] getting user using uri: " + perURI);
    User re = agent.getUser(perURI);
    System.out.println("Result is: " + re);
    if (re instanceof User) 
    	System.out.println("[Activator] Result is an User ");  
    else if (re instanceof AssistedPerson)
    	System.out.println("[Activator] Result is an AssistedPerson ");
    
    /*test add/get user profile*/
/*    System.out.println("[TEST] adding user profile..." + userProfile.toString());
    System.out.println("Result is: " + agent.addUserProfile(userProfile));

    System.out.println("[TEST] adding user profile to user ...");
    System.out.println("Result is: " + agent.addUserProfile(maria, userProfile));

    System.out.println("[TEST] getting user profile...");
    String gottenUserProfile = agent.getUserProfile(maria);
    System.out.println("[TEST] gotten user profile:" + gottenUserProfile); */
    
 /*   System.out.println("[TEST] adding user profile2..." + userProfile2.toString());
    System.out.println("Result is: " + agent.addUserProfile(userProfile2));

    System.out.println("[TEST] adding user profile to user2/john ...");
    System.out.println("Result is: " + agent.addUserProfile(john, userProfile2));  */

    /*test get user profile using userID*/
/*    System.out.println("[TEST] getting user profile with ID..." + userID);
    System.out.println("Result is: " + agent.getUserProfile(userID)); */
    
    /*test get all users*/
    System.out.println("[TEST] getting all users ");
    List<User> users = agent.getAllUsers(); 
    //System.out.println("The result: " + users.toString());
    System.out.println("Has " + users.size() + " user(s)");  
    for (int i=0; i<users.size(); i++) {
    	User u = (User)(users.get(i));
    	System.out.println("Has a user " + u.getURI());    	
    }
    
    
/*    System.out.println("[TEST] deleting an user: " + userURI);
    boolean deleteRes = agent.deleteUser(userURI);
    if (deleteRes) System.out.println("[TEST] the delete is successful");
    else System.out.println("[TEST] the delete is not successful");
    
    System.out.println("[TEST] getting all users ");
    List<User> users2 = agent.getAllUsers(); 
    System.out.println("The result: " + users2.toString());
    
    // testing - actually need some more info to test for the user?
    System.out.println("[TEST] updating user ..."+per.getURI());
    System.out.println("Result is: " + agent.updateUser(per)); */
    
    /*test add/get user subprofile via User */
/*    System.out.println("[TEST] adding user ID subprofile...");
    UserIDProfile userIDProfile = new UserIDProfile(userIDProfileURI);
    userIDProfile.setUSERNAME("Maria");
    userIDProfile.setPASSWORD("Pass");
    System.out.println("Add userId subprofile: " + agent.addSubProfile(userIDProfile));
    System.out.println("Add subprofile to user Maria. " + userIDProfile.getUSERNAME() + "/" + userIDProfile.getPASSWORD());
    System.out.println("Add subprofile to user Maria. Result is: " + agent.addUserSubprofile(maria, userIDProfile));

    //HealthProfile healthProfile = new HealthProfile(healthProfileURI);
    //healthProfile.addTreatment(new TakeMeasurementActivity(healthProfileURI+"treatment"));
    //System.out.println("Add userId subprofile: " + agent.addSubProfile(healthProfile));
    //System.out.println("Add health subprofile to user Maria. " + healthProfile.getPropertyURIs());
    //System.out.println("Add subprofile to user Maria. Result is: " + agent.addUserSubprofile(maria, healthProfile));
    
    
    System.out.println("[TEST] getting user subprofiles...");
    // should be a list of String of urn
    String gotUserIdProfile = agent.getUserSubprofiles(maria);
    System.out.println("[TEST] gotten user ID profiles:" + gotUserIdProfile);
    
    System.out.println("[TEST] get userID profile: " + agent.getUserProfile(gotUserIdProfile));
    
    
 /*   for (int i=0; i<gotUserIdProfile.size(); i++) {
    	if (gotUserIdProfile.get(i) instanceof UserIDProfile) {
    		System.out.println("the username: " + ((UserIDProfile)gotUserIdProfile).getUSERNAME());
    		System.out.println("the password: " + ((UserIDProfile)gotUserIdProfile).getPASSWORD());
    	}
    } */
    
    
    /*test add/get user subprofile via user profile  */
/*    System.out.println("[TEST] adding user ID subprofile...");
    
    System.out.println("Result is: " + agent.addUserSubprofile(userProfile, userIDProfile));
 */   
    //System.out.println("[TEST] adding health subprofile...");
    
   // System.out.println("Result is: " + agent.addUserSubprofile(userProfile, healthProfile)); */

    
    // has some problem when installing the profile agent if running the following code
/*    System.out.println("[TEST] getting user subprofiles...");
    log.info("[TEST] getting user subprofiles...");
    List gotUserIdProfile2 = agent.getUserSubprofiles(userProfile);
    if (gotUserIdProfile2==null) log.info("[TEST] the result is null");
    else {
    	log.info("[TEST] gotten user subprofiles:" + gotUserIdProfile2.toString());
    	System.out.println("[TEST] gotten user subprofiles:" + gotUserIdProfile2.toString());
    }
 
    */
  }



}
