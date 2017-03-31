package org.universAAL.ucc.startup.api.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.universAAL.ucc.startup.api.Setup;
import org.universAAL.ucc.startup.model.UccUsers;
import org.universAAL.ucc.startup.model.UserAccountInfo;
import javax.xml.bind.JAXB;

/**
 * 
 * @author Nicole Merkle
 *
 */
public class SetupImpl implements Setup {

	public void saveUsers(List<UserAccountInfo> users, String file) {
		UccUsers all = new UccUsers();
		all.setUser(users);
		JAXB.marshal(all, new File(file));
	}

	public List<UserAccountInfo> getUsers(String file) {
		UccUsers users = JAXB.unmarshal(new File(file), UccUsers.class);
		return users.getUser();
	}

	public void  updateUser(UserAccountInfo user, String file) {
		System.err.println("In update user");
		List<UserAccountInfo> all = getUsers(file);
		List<UserAccountInfo>up = new ArrayList<UserAccountInfo>();
		for(UserAccountInfo ua : all) {
			if(ua.getName().equals(user.getName())) {
				UserAccountInfo temp = new UserAccountInfo();
				temp.setChecked(user.isChecked());
				temp.setName(user.getName());
				temp.setPassword(user.getPassword());
				temp.setRole(user.getRole());
				System.err.println("Database: "+user.getRole().size()+" "+user.getRole());
				up.add(temp);
			} else {
				up.add(ua);
			}
		}
		UccUsers in = new UccUsers();
		in.setUser(up);
		JAXB.marshal(in, new File(file));
	}

	public void deleteUser(UserAccountInfo user, String file) {
		List<UserAccountInfo> users = getUsers(file);
		List<UserAccountInfo>list = new ArrayList<UserAccountInfo>();
		for(UserAccountInfo ui : users) {
			if(!ui.getName().equals(user.getName()) || !ui.getPassword().equals(user.getPassword())) {
//				UserAccountInfo u = new UserAccountInfo();
//				u.setChecked(ui.isChecked());
//				u.setName(ui.getName());
//				u.setPassword(ui.getPassword());
//				u.setRole(ui.getRole());
				list.add(ui);
			}
		}
		UccUsers del = new UccUsers();
		del.setUser(list);
		JAXB.marshal(del, new File(file));
	
	}

	public void deleteAllUsers(String file) {
		// TODO Auto-generated method stub
	}

	public void saveUser(UserAccountInfo user, String file) {
		List<UserAccountInfo> temp = getUsers(file);
		List<UserAccountInfo>saving = new ArrayList<UserAccountInfo>();
		boolean flag = false;
		if(!temp.isEmpty()) {
		for(UserAccountInfo uai : temp) {
			saving.add(uai);
			if(uai.getName().equals(user.getName())) {
				flag = true;
				saving.remove(uai);
			} 
				
		}
		if(flag == false) {
			saving.add(user);
		}
		} else {
			saving.add(user);
		}
		saveUsers(saving, file);

	}

}
