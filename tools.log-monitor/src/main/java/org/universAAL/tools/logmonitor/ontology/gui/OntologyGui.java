/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.ontology.gui;

import java.awt.BorderLayout;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.Resource;

/**
 * The main frame.
 * 
 * @author Carsten Stockloew
 */
public class OntologyGui extends JPanel implements TreeSelectionListener {

    private static final long serialVersionUID = 1L;

    /**
     * The graphics panel where the content is drawn.
     */
    OntologyPane pane = new OntologyPane();

    private JTree tree;
    private DefaultMutableTreeNode root = null;
    DefaultTreeModel treeModel;

    private Map<String, DefaultMutableTreeNode> onts = new Hashtable<String, DefaultMutableTreeNode>();
    private DefaultMutableTreeNode mwOnts;
    private DefaultMutableTreeNode ontOnts;
    private DefaultMutableTreeNode appOnts;

    /**
     * Create the main frame.
     */
    public OntologyGui() {
	this.setLayout(new BorderLayout());
	// Message overview
	root = new DefaultMutableTreeNode("root");
	treeModel = new DefaultTreeModel(root);
	tree = new JTree(treeModel);
	tree.setRootVisible(false);
	tree.addTreeSelectionListener(this);

	// overall view
	JScrollPane scrollPaneLeft = new JScrollPane(tree);
	JScrollPane scrollPaneRight = new JScrollPane(pane);

	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		scrollPaneLeft, scrollPaneRight);
	splitPane.setDividerLocation(0.4);
	add(splitPane, BorderLayout.CENTER);

	mwOnts = new DefaultMutableTreeNode("universAAL Middleware Ontologies");
	ontOnts = new DefaultMutableTreeNode("universAAL Ontologies");
	appOnts = new DefaultMutableTreeNode("Application Ontologies");
	root.add(appOnts);
	root.add(ontOnts);
	root.add(mwOnts);
	treeModel.reload(root);
    }

    public void add(final String ontURI) {
	if (ontURI == null)
	    return;
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		// add a tree node
		DefaultMutableTreeNode ontNode = onts.get(ontURI);
		if (ontNode == null) {
		    // ontology not yet available
		    ontNode = new DefaultMutableTreeNode(ontURI);

		    DefaultMutableTreeNode group = appOnts;

		    String module = OntologyManagement.getInstance()
			    .getRegisteringModuleID(ontURI);
		    if (ontURI.toLowerCase().startsWith(
			    Resource.uAAL_NAMESPACE_PREFIX.toLowerCase())) {
			if (module.startsWith("mw."))
			    group = mwOnts;
			else if (module.startsWith("ont."))
			    group = ontOnts;
		    }
		    // System.out.println("Ontology: " + ontURI + "  " +
		    // module);

		    group.add(ontNode);
		    treeModel.reload(group);
		    expand(group);

		    onts.put(ontURI, ontNode);
		}
	    }
	});
    }

    public void remove(final String ontURI) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		// remove from gui
		DefaultMutableTreeNode node = onts.get(ontURI);
		treeModel.removeNodeFromParent(node);

		// remove ontology
		onts.remove(ontURI);
	    }
	});
    }

    private void expand(DefaultMutableTreeNode node) {
	TreeNode[] path = node.getPath();
	TreePath treePath = new TreePath(path);
	tree.expandPath(treePath);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
	final JTree tree = (JTree) e.getSource();
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
			.getLastSelectedPathComponent();
		// String selectedNodeName = selectedNode.toString();
		if (node == null) {
		    pane.show(null);
		    return;
		}

		if (onts.containsValue(node)) {
		    // get the member ID from the node
		    // this could be done more efficient
		    String ontURI = null;
		    for (String id : onts.keySet()) {
			DefaultMutableTreeNode tmp = onts.get(id);
			if (node == tmp) {
			    ontURI = id;
			}
		    }
		    // get member data and show it
		    if (ontURI != null) {
			pane.show(ontURI);
		    }
		}
	    }
	});
    }
}
