package org.universAAL.tools.ucc.startup.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.universAAL.tools.ucc.database.Activator;
import org.universAAL.tools.ucc.startup.api.Setup;
import org.universAAL.tools.ucc.startup.model.UccUsers;
import org.universAAL.tools.ucc.startup.model.UserAccountInfo;

import javax.xml.bind.JAXB;

/**
 *
 * @author Nicole Merkle
 *
 */
public class SetupImpl implements Setup {

	public void saveUsers(List<UserAccountInfo> users) {
		UccUsers all = new UccUsers();
		all.setUser(users);
		JAXB.marshal(all, Activator.getUserxml());
	}

	public List<UserAccountInfo> getUsers() {
		UccUsers users = JAXB.unmarshal(Activator.getUserxml(), UccUsers.class);
		return users.getUser();
	}

	public void updateUser(UserAccountInfo user) {
		System.err.println("In update user");
		List<UserAccountInfo> all = getUsers();
		List<UserAccountInfo> up = new ArrayList<UserAccountInfo>();
		for (UserAccountInfo ua : all) {
			if (ua.getName().equals(user.getName())) {
				UserAccountInfo temp = new UserAccountInfo();
				temp.setChecked(user.isChecked());
				temp.setName(user.getName());
				temp.setPassword(user.getPassword());
				temp.setRole(user.getRole());
				System.err.println("Database: " + user.getRole().size() + " " + user.getRole());
				up.add(temp);
			} else {
				up.add(ua);
			}
		}
		UccUsers in = new UccUsers();
		in.setUser(up);
		JAXB.marshal(in, Activator.getUserxml());
	}

	public void deleteUser(UserAccountInfo user) {
		List<UserAccountInfo> users = getUsers();
		List<UserAccountInfo> list = new ArrayList<UserAccountInfo>();
		for (UserAccountInfo ui : users) {
			if (!ui.getName().equals(user.getName()) || !ui.getPassword().equals(user.getPassword())) {
				// UserAccountInfo u = new UserAccountInfo();
				// u.setChecked(ui.isChecked());
				// u.setName(ui.getName());
				// u.setPassword(ui.getPassword());
				// u.setRole(ui.getRole());
				list.add(ui);
			}
		}
		UccUsers del = new UccUsers();
		del.setUser(list);
		JAXB.marshal(del, Activator.getUserxml());

	}

	public void deleteAllUsers() {
		// TODO Auto-generated method stub
	}

	public void saveUser(UserAccountInfo user) {
		List<UserAccountInfo> temp = getUsers();
		List<UserAccountInfo> saving = new ArrayList<UserAccountInfo>();
		boolean flag = false;
		if (!temp.isEmpty()) {
			for (UserAccountInfo uai : temp) {
				saving.add(uai);
				if (uai.getName().equals(user.getName())) {
					flag = true;
					saving.remove(uai);
				}

			}
			if (flag == false) {
				saving.add(user);
			}
		} else {
			saving.add(user);
		}
		saveUsers(saving);

	}

}
