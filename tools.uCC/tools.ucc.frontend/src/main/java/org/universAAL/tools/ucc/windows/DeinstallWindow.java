package org.universAAL.tools.ucc.windows;

import java.util.List;
import java.util.ResourceBundle;

import org.universAAL.tools.ucc.model.AppItem;
import org.universAAL.tools.ucc.model.RegisteredService;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DeinstallWindow extends Window implements Window.CloseListener {
	private Table list;
	private Button del;
	private Button cancel;
	private String base;
	private ResourceBundle bundle;

	public DeinstallWindow(List<RegisteredService> srv) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		setCaption(bundle.getString("uninstallation.header"));
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		list = new Table(bundle.getString("services.table"));
		list.setImmediate(true);
		list.setSelectable(true);
		list.setWidth("100%");
		list.addContainerProperty("serviceId", String.class, null);
		list.addContainerProperty("appId", String.class, null);
		list.addContainerProperty("menuName", String.class, null);
		list.addContainerProperty("userID", String.class, null);
		list.addContainerProperty("provider", String.class, null);
		// list.addContainerProperty("bundleId", String.class, null);
		// list.addContainerProperty("version", String.class, null);
		list.setVisibleColumns(
				new String[] { "serviceId", "appId"/* , "bundleId", "version" */ });
		list.setColumnHeader("serviceId", "Service-ID");
		list.setColumnHeader("appId", "App-ID");
		list.setColumnHeader("menuName", "Menu Name");
		list.setColumnHeader("userID", "User ID");
		list.setColumnHeader("provider", "Provider-Website");
		// list.setColumnHeader("bundleId", "Bundle-ID");
		// list.setColumnHeader("version", "Bundle-Version");
		list.setSortDisabled(true);
		list.setMultiSelect(false);

		Container beanContainer = new BeanItemContainer<AppItem>(AppItem.class);

		for (RegisteredService item : srv) {
			if (!item.getServiceId().equals("")) {
				for (String app : item.getAppId()) {
					AppItem ai = new AppItem();
					ai.setServiceId(item.getServiceId());
					ai.setAppId(app);
					ai.setMenuName(item.getMenuName());
					ai.setUserID(item.getUserID());
					ai.setProvider(item.getProvider());
					beanContainer.addItem(ai);
				}
			}
		}
		list.setContainerDataSource(beanContainer);

		vl.addComponent(list);
		vl.setComponentAlignment(list, Alignment.MIDDLE_CENTER);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		del = new Button(bundle.getString("uninstall.usrv"));
		cancel = new Button(bundle.getString("close.button"));
		hl.addComponent(del);
		hl.addComponent(cancel);
		vl.addComponent(hl);
		vl.setComponentAlignment(hl, Alignment.BOTTOM_CENTER);
		setWidth("550px");
		setHeight("500px");
		center();
		setContent(vl);
	}

	public Table getList() {
		return list;
	}

	public void setList(Table list) {
		this.list = list;
	}

	public Button getDel() {
		return del;
	}

	public void setDel(Button del) {
		this.del = del;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public void windowClose(CloseEvent e) {
		close();

	}

}
