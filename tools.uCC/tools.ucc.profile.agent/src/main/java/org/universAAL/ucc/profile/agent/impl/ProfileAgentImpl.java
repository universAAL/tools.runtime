package org.universAAL.ucc.profile.agent.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.profile.AALService;
import org.universAAL.ontology.profile.AALServiceProfile;
import org.universAAL.ontology.profile.AALSpace;
import org.universAAL.ontology.profile.AALSpaceProfile;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.Profile;
import org.universAAL.ontology.profile.SubProfile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.universAAL.support.utils.service.Arg;
import org.universAAL.support.utils.service.Path;
import org.universAAL.support.utils.service.low.Request;
import org.universAAL.support.utils.service.mid.UtilEditor;
import org.universAAL.ucc.profile.agent.ProfileAgent;
//import org.universaal.ontology.health.owl.HealthProfile;

public class ProfileAgentImpl implements ProfileAgent {
	  private static ServiceCaller caller = null;
	  
	  // TODO: to be updated according to what we need
	  private static final String PROFILE_AGENT_NAMESPACE = "http://ontology.itaca.es/ProfileAGENT.owl#";
	  
	  private static final String OUTPUT_GETPROFILABLE = PROFILE_AGENT_NAMESPACE
		    + "out1";
	  private static final String OUTPUT_GETPROFILE = PROFILE_AGENT_NAMESPACE
		    + "out2";
	  private static final String OUTPUT_GETUSERS = PROFILE_AGENT_NAMESPACE
		    + "out3";
	  private static final String OUTPUT_GETSUBPROFILES = PROFILE_AGENT_NAMESPACE
		    + "out4";
	  private static final String OUTPUT_GETSUBPROFILE = PROFILE_AGENT_NAMESPACE
		    + "out5";
	  // USER_URI=USER_URI_PREFIX + UserID
	  private static final String USER_URI_PREFIX = "urn:org.universAAL.aal_space:test_env#";
	  //static final String CONTEXT_HISTORY_HTL_IMPL_NAMESPACE = "http://ontology.universAAL.org/ContextHistoryHTLImpl.owl#";
	  //private static final String OUTPUT_QUERY_RESULT = CONTEXT_HISTORY_HTL_IMPL_NAMESPACE + "queryResult";
	  
	  private static final String OUTPUT = PROFILE_AGENT_NAMESPACE
			    + "outX";
	  
	  public ProfileAgentImpl(ModuleContext context) {
			caller = new DefaultServiceCaller(context);
	  }
	  
	  /**
	   * add user to CHE via Profiling server
	   * @param user
	   * @return ?
	   */
	  public String addUser(User user) {
	    	System.out.println("Profile Agent: add user with URI: " + user.getURI());
	    	ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
	    	req.addAddEffect(new String[]{ProfilingService.PROP_CONTROLS}, user);
	    	ServiceResponse resp = caller.call(req);
	    	return resp.getCallStatus().name();
	  }
	
	  /**
	   * get user from CHE
	   * @param user
	   * @return User instance, if exists; null, if not exists (registered)
	   */
	  public User getUser(User user) {
			System.out.println("Profile agent: get user: " + user.getURI());
			ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
			req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS}, user);
			req.addRequiredOutput(OUTPUT_GETPROFILABLE, new String[]{ProfilingService.PROP_CONTROLS});
			ServiceResponse resp = caller.call(req);
			if (resp.getCallStatus() == CallStatus.succeeded) {
			    Object out=getReturnValue(resp.getOutputs(), OUTPUT_GETPROFILABLE);
			    if (out != null) {
				return (User)out;
			    } else {
			    	System.out.println("NOTHING!");
			    	return null;
			    }
			}else{
			    System.out.println("other results: " + resp.getCallStatus().name());
			    return null;
			}
	  }
	  
	  public User getUser(String uri) {
		  	System.out.println("Profile agent: get user with URI: " + uri);
		  	User user = new User(uri);
			ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
			req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS}, user);
			req.addRequiredOutput(OUTPUT_GETPROFILABLE, new String[]{ProfilingService.PROP_CONTROLS});
			ServiceResponse resp = caller.call(req);
			if (resp.getCallStatus() == CallStatus.succeeded) {
			    Object out=getReturnValue(resp.getOutputs(), OUTPUT_GETPROFILABLE);
			    if (out != null) {
			    	
					System.out.println("Profile agent: result got is - " + out.toString());
					
					return (User)out;
			    } else {
			    	System.out.println("NOTHING!");
			    	return null;
			    }
			}else{
			    System.out.println("other results: " + resp.getCallStatus().name());
			    return null;
			}
	  }

	  public List<User> getAllUsers() {
		  	System.out.println("Profile Agent: getAllUsers");
			ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
			req.addRequiredOutput(OUTPUT_GETUSERS, new String[]{ProfilingService.PROP_CONTROLS});
			req.addTypeFilter(Path.at(ProfilingService.PROP_CONTROLS).path, User.MY_URI);
			ServiceResponse resp=caller.call(req);
			if (resp.getCallStatus() == CallStatus.succeeded) {
			    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETUSERS);
			    if (out != null) {
			    	System.out.println(out.toString());			    	
			    	// get each user using the uri
			    	List<User> users = new ArrayList();
			    	List outl = (List)out;
			    	Object ur;
			    	//System.out.println("Profile Agent: the output size: " + outl.size());
			    	for (int i=0; i<outl.size(); i++) {
			    		ur = (Object) outl.get(i);
			    		//System.out.println("Has a user " + ur);			    		
			    		User u = getUser(ur.toString());
			    		//System.out.println("Profile Agent: get an user: " + u.getURI());
			    		users.add(u);
			    	}			        	 
			    	return users;
			    } else {
			    	System.out.println("NOTHING!");
			    	return null;
			    }
			}else{
			    System.out.println("Other results: " + resp.getCallStatus().name());
			    return null;
			}
	  }

		public boolean updateUser(User user) {
			System.out.println("Profile Agent: update user: " + user.getURI());
			ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
			req.addChangeEffect(new String[]{ProfilingService.PROP_CONTROLS}, user);
			ServiceResponse resp = caller.call(req);
			System.out.println("The result: " + resp.getCallStatus().name());
			if (resp.getCallStatus() == CallStatus.succeeded) 
				return true;
			else return false;
		}

		public boolean deleteUser(String uri) {
			System.out.println("Profile Agent: delete User: " + uri);
			User user = new User(uri);
			ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
			MergedRestriction r1 = MergedRestriction.getFixedValueRestriction(
				ProfilingService.PROP_CONTROLS, user);
			req.getRequestedService().addInstanceLevelRestriction(r1, new String[]{ProfilingService.PROP_CONTROLS});
			req.addRemoveEffect(new String[]{ProfilingService.PROP_CONTROLS});
			ServiceResponse resp = caller.call(req);
			System.out.println("The result: " + resp.getCallStatus().name());
			if (resp.getCallStatus() == CallStatus.succeeded) 
				return true;
			else return false;
		}
	
	  private ServiceRequest userProfileRequest(User user) {
		        ServiceRequest req = new ServiceRequest(new ProfilingService(), null);
		        req.addValueFilter(new String[] {ProfilingService.PROP_CONTROLS}, user);

		        req.addTypeFilter(new String[] {ProfilingService.PROP_CONTROLS, Profilable.PROP_HAS_PROFILE}, UserProfile.MY_URI);

		        req.addRequiredOutput(OUTPUT_GETPROFILE, new String[] {ProfilingService.PROP_CONTROLS, Profilable.PROP_HAS_PROFILE});

		        return req;
		      }


	
    private Object getReturnValue(List outputs, String expectedOutput) {
    	Object returnValue = null;
    	if (outputs == null)
    		System.out.println("Profile Agent: No results found!");
    	else
    		for (Iterator i = outputs.iterator(); i.hasNext();) {
    			ProcessOutput output = (ProcessOutput) i.next();
    			if (output.getURI().equals(expectedOutput))
    				if (returnValue == null)
    					returnValue = output.getParameterValue();
    				else
    					System.out.println("Profile Agent: redundant return value!");
    			else
    				System.out.println("Profile Agent - output ignored: " + output.getURI());
    		}

    	return returnValue;
    }
    
    public String addUserProfile(UserProfile profile) {
    	System.out.println("Profile Agent: add user Profile: " + profile.getURI());
    	ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
    	req.addAddEffect(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE}, profile);
    	ServiceResponse resp = caller.call(req);
    	return resp.getCallStatus().name();
    }

	public String getUserProfile(User user) {
	 	System.out.println("Profile agent: get user profile for user: " + user.getURI());
	 	ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
	 	req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS}, user);
		req.addRequiredOutput(OUTPUT_GETPROFILE, new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE});
	 	ServiceResponse resp=caller.call(req);
	 	if (resp.getCallStatus() == CallStatus.succeeded) {
	 	    Object out=getReturnValue(resp.getOutputs(), OUTPUT_GETPROFILE);
	 	    if (out != null) {
	 	    	System.out.println("The result: " + out.toString());
	 	    	return out.toString();
	 	    } else {
	 	    	System.out.println("NOTHING!");
	 	    	return null;
	 	    }
	 	}else{
	 	    System.out.println("Other result: " + resp.getCallStatus().name());
	 	    return null;
	 	}
	}


    public String addUserProfile(User user, UserProfile profile) {
    	System.out.println("Profile agent: addProfile to an user");
    	ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
    	req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS}, user);
    	req.addAddEffect(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE}, profile);
    	ServiceResponse resp=caller.call(req);
    	return resp.getCallStatus().name();
    }
    
	public void changeUserProfile(User user, UserProfile userProfile) {
		// TODO Auto-generated method stub
		
	}

	public void removeUserProfile(User user) {
		// TODO Auto-generated method stub
		
	}

	public List getAALSpaceProfiles(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addAALSpaceProfile(User user, AALSpaceProfile aalSpaceProfile) {
		// TODO Auto-generated method stub
		
	}

	public void changeAALSpaceProfile(User user, AALSpaceProfile aalSpaceProfile) {
		// TODO Auto-generated method stub
		
	}

	public void removeAALSpaceProfile(User user) {
		// TODO Auto-generated method stub
		
	}
	
	public String addSubProfile(SubProfile profile) {
		System.out.println("Profile agent: add subProfile" + profile.getPropertyURIs().toString());
		ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
		req.addAddEffect(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE}, profile);
		ServiceResponse resp = caller.call(req);
		return resp.getCallStatus().name();
	}

	public SubProfile getSubProfile(SubProfile profile) {
		System.out.println("Profile Agent: Get SubProfile");
		ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
		req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE}, profile);
		req.addRequiredOutput(OUTPUT_GETSUBPROFILE, new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE});
		ServiceResponse resp = caller.call(req);
		if (resp.getCallStatus() == CallStatus.succeeded) {
		    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETSUBPROFILE);
		    if (out != null) {
		    	System.out.println(out.toString());
		    	return (SubProfile) out;
		    } else {
		    	System.out.println("NOTHING!");
		    	return null;
		    }
		}else{
		    System.out.println("The result is: " + resp.getCallStatus().name());
		    return null;
		}
	    }

	public String getUserSubprofiles(User user) {
		System.out.println("Profile agent: get all Subprofiles for user: " + user.getURI());
		ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
		req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS}, user);
		req.addRequiredOutput(OUTPUT_GETSUBPROFILES, new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE});
		ServiceResponse resp=caller.call(req);
		if (resp.getCallStatus() == CallStatus.succeeded) {
		    Object out=getReturnValue(resp.getOutputs(), OUTPUT_GETSUBPROFILES);
		    if (out != null) {
		    	System.out.println(out.toString());
		    	return out.toString();
		    } else {
		    	System.out.println("NOTHING!");
		    	return null;
		    }
		}else{
		    System.out.println("Other results: " + resp.getCallStatus().name());
		    return null;
		}
	}

//	private String getSubProfile(String urn) {
//		System.out.println("Profile Agent: call GetSubProfile using urn");
//		SubProfile profile = new SubProfile(urn);
//		ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
//		req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE}, profile);
//		req.addRequiredOutput(OUTPUT_GETSUBPROFILE, new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE});
//		ServiceResponse resp = caller.call(req);
//		if (resp.getCallStatus() == CallStatus.succeeded) {
//		    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETSUBPROFILE);
//		    if (out != null) {
//		    	System.out.println(out.toString());
//		    	return out.toString();
//		    } else {
//		    	System.out.println("NOTHING!");
//		    	return "nothing";
//		    }
//		}else{
//		    return resp.getCallStatus().name();
//		}
//	    }
	
	public String addUserSubprofile(User user, SubProfile subProfile) {
		System.out.println("Profile agent: add subProfile for user: " + user.getURI() + " subProfile: " + subProfile.toString());
		ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
	 	req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS}, user);
	 	req.addAddEffect(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE}, subProfile);
		ServiceResponse resp=caller.call(req);
		return resp.getCallStatus().name();
	}

	public void changeUserSubprofile(User user, SubProfile subProfile) {
		// TODO Auto-generated method stub
		
	}

	public void removeUserSubprofile(User user, String subprofile_URI) {
		// TODO Auto-generated method stub
		
	}

	public List getUserSubprofiles(UserProfile profile) {
		System.out.println("Profile Agent: get Subprofiles for userprofile: " + profile.getURI());
		ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
	 	req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE}, profile);
	 	req.addTypeFilter(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE}, SubProfile.MY_URI);
		req.addRequiredOutput(OUTPUT_GETSUBPROFILES, new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE});
		ServiceResponse resp=caller.call(req);
		if (resp.getCallStatus() == CallStatus.succeeded) {
			//System.out.println(resp.getOutputs());
		    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETSUBPROFILES);
		    if (out != null) {
		    	System.out.println(out.toString());
		    	return (List) out;
		    } else {
		    	System.out.println("NOTHING!");
		    	return null;
		    }
		}else{
		    System.out.println("Results: " + resp.getCallStatus().name());
		    return null;
		}
	}

	public String addUserSubprofile(UserProfile userProfile, SubProfile subProfile) {
		System.out.println("Profile Agent: add subprofile for userProfile: " + userProfile.getURI() + " subprofile: " + subProfile.toString());
		ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
	 	req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE}, userProfile);
	 	req.addAddEffect(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,Profile.PROP_HAS_SUB_PROFILE}, subProfile);
		ServiceResponse resp=caller.call(req);
		return resp.getCallStatus().name();
	}
	

	
	/*** use space server ********/
	public String addSpace(AALSpace space) {
		return genericAdd(space, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	public AALSpace getSpace(AALSpace space) {
		String[] path = Path.at(ProfilingService.PROP_CONTROLS).path;
		ServiceResponse resp = caller.call(UtilEditor.requestGet(
				ProfilingService.MY_URI, path, Arg.in(space),
				Arg.out(OUTPUT)));
		if (resp.getCallStatus() == CallStatus.succeeded) {
		    Object out=getReturnValue(resp.getOutputs(),OUTPUT);
		    if (out != null) {
		    	System.out.println(out.toString());
		    	return (AALSpace)out;
		    } else {
		    	System.out.println("NOTHING!");
		    	return null;
		    }
		}else{
		   System.out.println("Other results: " + resp.getCallStatus().name());
		    return null;
		}	  
	}

	public List<AALSpace> getSpaces() {
		Request req=new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.out(OUTPUT));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AALSpace.MY_URI)); //This only works if type is set as instance restriction in serv
		ServiceResponse resp=caller.call(req);
		if (resp.getCallStatus() == CallStatus.succeeded) {
		    Object out=getReturnValue(resp.getOutputs(),OUTPUT);
		    if (out != null) {
		    	System.out.println(out.toString());
		    	// get each device using the uri
		    	List<AALSpace> spaces = new ArrayList();
		    	List outl = (List)out;
		    	Object ur;
		    	for (int i=0; i<outl.size(); i++) {
		    		ur = (Object) outl.get(i);
		    		AALSpace u = getSpace(new AALSpace(ur.toString()));
		    		spaces.add(u);
		    	}			        	 
		    	return spaces;
		    } else {
		    	System.out.println("NOTHING!");
		    	return null;
		    }
		}else{
			System.out.println("Other results: " + resp.getCallStatus().name());
			return null;
		}
	}
	
	public String addSpaceProfile(AALSpaceProfile aalSpaceProfile) {
		return genericAdd(aalSpaceProfile, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	public String getDevice(Device device) {
		return genericGet(device, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_HARDWARE).path);
	}

	public String addDevice(Device device) {
		return genericAdd(device, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_HARDWARE).path);
	}
	
	public String addDevice(Device device, AALSpace space) {
		String result = addDevice(device);
		if (result.equals("call_succeeded"))
			return addDeviceToSpace(device, space);
		else return "Add device fail!";
	}

	public Device getDevice(String uri) {
		Device device;
		Resource res = new Device(uri);
		String[] path = Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_HARDWARE).path;
		ServiceResponse resp = caller.call(UtilEditor.requestGet(
				ProfilingService.MY_URI, path, Arg.in(res),
				Arg.out(OUTPUT)));
		if (resp.getCallStatus() == CallStatus.succeeded) {
		    Object out=getReturnValue(resp.getOutputs(),OUTPUT);
		    if (out != null) {
		    	System.out.println(out.toString());
		    	return (Device)out;
		    } else {
		    	System.out.println("NOTHING!");
		    	return null;
		    }
		}else{
		   System.out.println("Other results: " + resp.getCallStatus().name());
		    return null;
		}	  
		
	}

	public boolean updateDevice(Device device) {
		String result = genericChange(device, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_HARDWARE).path);
		if (result.equals("call_succeeded"))
			return true;
		else return false;
	}

	public boolean deleteDevice(String uri) {
		String result = genericRemove(new Device(uri), Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_HARDWARE).path);
		if (result.equals("call_succeeded"))
			return true;
		else return false;
	}
	
	// Has not been tested - wait for space server!!!
	public List<Device> getAllDevices(AALSpace space) {
		ServiceRequest req=new ServiceRequest(new ProfilingService(),null);
		req.addValueFilter(new String[]{ProfilingService.PROP_CONTROLS}, space);
		req.addTypeFilter(new String[]{ProfilingService.PROP_CONTROLS}, AALSpace.MY_URI);
		req.addRequiredOutput(OUTPUT, new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,AALSpaceProfile.PROP_INSTALLED_HARDWARE});
		req.addTypeFilter(new String[]{ProfilingService.PROP_CONTROLS,Profilable.PROP_HAS_PROFILE,AALSpaceProfile.PROP_INSTALLED_HARDWARE}, Device.MY_URI);
		ServiceResponse resp=caller.call(req);
		if (resp.getCallStatus() == CallStatus.succeeded) {
		    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETUSERS);
		    if (out != null) {
		    	System.out.println(out.toString());			    	
		    	// get each device using the uri
		    	List<Device> devices = new ArrayList();
		    	List outl = (List)out;
		    	Object ur;
		    	//System.out.println("Profile Agent: the output size: " + outl.size());
		    	for (int i=0; i<outl.size(); i++) {
		    		ur = (Object) outl.get(i);
		    		//System.out.println("Has a device " + ur);			    		
		    		Device u = getDevice(ur.toString());
		    		//System.out.println("Profile Agent: get an user: " + u.getURI());
		    		devices.add(u);
		    	}			        	 
		    	return devices;
		    } else {
		    	System.out.println("NOTHING!");
		    	return null;
		    }
		}else{
		    System.out.println("Other results: " + resp.getCallStatus().name());
		    return null;
		}
	}

	public String getSpaceProfile(AALSpaceProfile aalSpaceProfile) {
		return genericGet(aalSpaceProfile, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	public String addDeviceToSpace(Device dev, AALSpace aalSpace) {
		Request req=new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AALSpace.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_HARDWARE), Arg.add(dev));
		ServiceResponse resp=caller.call(req);
		return resp.getCallStatus().name();
	}

	public String getDevicesOfSpace(AALSpace aalSpace) {
		Request req=new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AALSpace.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_HARDWARE), Arg.out(OUTPUT));
		ServiceResponse resp=caller.call(req);
		return getListOfResults(resp);
	}

	public String addService(AALService aalService) {
		return genericAdd(aalService, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	public String getService(AALService aalService) {
		return genericGet(aalService, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	public String getServices() {
		Request req=new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.out(OUTPUT));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AALService.MY_URI)); //This only works if type set as instance restriction in serv
		ServiceResponse resp=caller.call(req);
		return getListOfResults(resp);
	}
	
	public String changeService(AALService aalService) {
		// TODO Auto-generated method stub
		return null;
	}

	public String removeService(AALService aalService) {
		// TODO Auto-generated method stub
		return null;
	}

	public String addServiceProf(AALServiceProfile aalServiceProfile) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServiceProf(AALServiceProfile aalServiceProfile) {
		// TODO Auto-generated method stub
		return null;
	}

	public String changeServiceProf(AALServiceProfile aalServiceProfile) {
		// TODO Auto-generated method stub
		return null;
	}

	public String removeServiceProf(AALServiceProfile aalServiceProfile) {
		// TODO Auto-generated method stub
		return null;
	}

	public String addServicesToSpace(AALSpace aalSpace, AALService serv) {
		Request req=new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AALSpace.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_SERVICES), Arg.add(serv));
		ServiceResponse resp=caller.call(req);
		return resp.getCallStatus().name();
	}

	public String getServicesOfSpace(AALSpace aalSpace) {
		Request req=new Request(new ProfilingService(null));
	 	req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
	 	req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AALSpace.MY_URI));
	 	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_SERVICES), Arg.out(OUTPUT));
	 	ServiceResponse resp=caller.call(req);
	 	return getListOfResults(resp);
	}

	public String getHROfServ(AALService aalService) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHWOfServ(AALService aalService) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAppOfServ(AALService aalService) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*** For uCC/uStore Web service *****/
	public String getAALSpaceProfile() {
		return null;
	}
	
	/**
	 * get user profile for user with userId
	 * (use Profiling server)
	 * 
	 * @return String representation of the User profile
	 * TODO: check with ustore the format
	 * 
	 */
	public String getUserProfile(String userID) {
		    String userURI = USER_URI_PREFIX + userID;
		    User user = new User(userURI);

		    ServiceResponse sr = caller.call(userProfileRequest(user));

		    if (sr.getCallStatus() == CallStatus.succeeded) {
		      try {
		        List outputAsList = sr.getOutput(OUTPUT_GETPROFILE, true);

		        if ((outputAsList == null) || (outputAsList.size() == 0)) {
		          return null;
		        }
		        // TODO: convert the UserProfile result to some structure?
		        return outputAsList.get(0).toString();
		      } catch (Exception e) {
		        return null;
		      }
		    }
			return null;
	}
	
	/**** utility functions *****/
	private String genericAdd(Resource res, String[] path) {
		ServiceResponse resp = caller.call(UtilEditor.requestAdd(
			ProfilingService.MY_URI, path, Arg.add(res)));
		return resp.getCallStatus().name();
	}
	
	private String genericGet(Resource res, String[] path) {
		ServiceResponse resp = caller.call(UtilEditor.requestGet(
			ProfilingService.MY_URI, path, Arg.in(res),
			Arg.out(OUTPUT)));
		
		return getListOfResults(resp);
	}
	
    private String genericChange(Resource res, String[] path) {
    	ServiceResponse resp = caller.call(UtilEditor.requestChange(
    		ProfilingService.MY_URI, path, Arg.change(res)));
		return resp.getCallStatus().name();
    }
	
    private String genericRemove(Resource res, String[] path) {
    	ServiceResponse resp = caller.call(UtilEditor.requestRemove(
			ProfilingService.MY_URI, path, Arg.remove(res)));
		return resp.getCallStatus().name();
    }
    
	private String getListOfResults(ServiceResponse resp){
		if (resp.getCallStatus() == CallStatus.succeeded) {
		    Object out=getReturnValue(resp.getOutputs(),OUTPUT);
		    if (out != null) {
		    	System.out.println(out.toString());
		    	return out.toString();
		    } else {
		    	System.out.println("NOTHING!");
		    	return "nothing";
		    }
		}else{
		    return resp.getCallStatus().name();
		}
	   }

}
