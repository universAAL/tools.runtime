package org.universAAL.tools.ucc.windows;

import java.util.ResourceBundle;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class DeploymentInformationView extends Window {
	private Tree tree;
	private HorizontalSplitPanel hp;
	private final String base;
	private ResourceBundle bundle;
	private Button ok;
	private Button cancel;

	public DeploymentInformationView(UccUI app) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		setCaption(bundle.getString("deploy.info.header"));
		hp = new HorizontalSplitPanel();
		hp.setSizeFull();
		hp.setStyleName(Reindeer.SPLITPANEL_SMALL);
		hp.setLocked(true);
		hp.setSplitPosition(200, Sizeable.UNITS_PIXELS);
		tree = new Tree();
		tree.setImmediate(true);
		tree.setNewItemsAllowed(false);
		tree.setNullSelectionAllowed(false);
		ok = new Button(bundle.getString("ok.button"));
		cancel = new Button(bundle.getString("cancel.button"));
		setContent(hp);
		setWidth("1024px");
		setHeight("600px");
		center();
		setModal(true);
		setClosable(false);
	}

	public VerticalLayout createSecondComponent(DeployStrategyView stratView, DeployConfigView confView) {
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setMargin(true);
		vl.setSpacing(true);
		vl.addComponent(stratView);
		vl.addComponent(confView);
		HorizontalLayout hol = new HorizontalLayout();
		hol.setSpacing(true);
		hol.setMargin(true);
		hol.addComponent(ok);
		hol.addComponent(cancel);
		vl.addComponent(hol);
		vl.setComponentAlignment(hol, Alignment.BOTTOM_CENTER);
		hp.setSecondComponent(vl);
		return vl;
	}

	public void createFirstComponent(Component c) {
		hp.setFirstComponent(c);
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public HorizontalSplitPanel getHp() {
		return hp;
	}

	public void setHp(HorizontalSplitPanel hp) {
		this.hp = hp;
	}

	public Button getOk() {
		return ok;
	}

	public void setOk(Button ok) {
		this.ok = ok;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

}
