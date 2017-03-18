package org.universAAL.tools.makrorecorder.swingGUI.pattern;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.tools.makrorecorder.osgi.pattern.Pattern;
import org.universAAL.tools.makrorecorder.swingGUI.Activator;

/**
 *
 * @author maxim djakow
 */
public class PatternEditFrame extends javax.swing.JFrame implements Observer{
	
	
	private static final long serialVersionUID = 1L;
	
	private javax.swing.JPanel RecordingPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton recordButton;
    private javax.swing.JLabel recordingLabel;
    
    private PatternPanel patternPanel;
	
	private Pattern pattern = null;
	
    public PatternEditFrame() {    	
        this.pattern = new Pattern();
        initComponents();
        readPattern();
        pattern.addObserver(this); 
    }

    public PatternEditFrame(Pattern pattern) {        
        this.pattern = pattern;
        initComponents();
        readPattern();
        pattern.addObserver(this); 
    }
    
      
    private void readPattern() {
        nameTextField.setText(pattern.getName());
        descriptionTextArea.setText(pattern.getDescription());
        readInputs();
        readOutputs();
    }
    
    public void readInputs() {
    	patternPanel.inputsListReload();
    }
    
    public void readOutputs() {
    	patternPanel.outputsListReload();
    }
    
    public static String shortResourceInfo(Resource r) {
    	String ret = "";
    	
    	if(r instanceof ContextEvent) {
    		ContextEvent ce = (ContextEvent)r;
    		ret += shortURI(ce.getRDFSubject().toString())+" ";
        	ret += shortURI(ce.getRDFPredicate())+" ";
        	ret += shortURI(ce.getRDFObject().toString());
    	}else if(r instanceof ServiceRequest) {
    		ServiceRequest sr = (ServiceRequest)r;
    		ret += shortURI(sr.getRequestedService().getType());
    		for (Resource effect : sr.getRequiredEffects()) {
    			for (String type : effect.getTypes()) {
    				ret += " "+shortURI(type);
				}    			
			}
    		for (Resource output : sr.getRequiredOutputs()) {
    			for (String type : output.getTypes()) {
    				ret += " "+shortURI(type);
				}    	
			}
    		
    	} else {
    		ret+= r.getClass()+": "+r.toString();
    	}
    	return ret;
    }
    
    public static String shortURI(String s) {
    	return s.substring(s.indexOf("#")+1);
    }
    
    private void savePattern() {
    	if(nameTextField.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(this, "Please enter Name", "info", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	if(Activator.getMakroRecorder().getAllPatternNames().contains(nameTextField.getText())) {
    		if (JOptionPane.showConfirmDialog(
    				null, 
    				"Pattern '"+nameTextField.getText()+"' already exist.\nOverwrite?", 
    				"Save pattern", 
    				JOptionPane.YES_NO_OPTION
    				) != JOptionPane.YES_OPTION)
    			return;
    	}
        pattern.setName(nameTextField.getText());
        pattern.setDescription(descriptionTextArea.getText());
        if(Activator.getMakroRecorder().savePattern(pattern)) {
        	Activator.getMakroRecorder().reload();
        	this.dispose();
        }
    }


    private void initComponents() {
    	addWindowListener(new WindowAdapter() {
    	      public void windowClosing(WindowEvent e) {
    	    	  pattern.stopRecording();
    	      }
    	    });
    	
        java.awt.GridBagConstraints gridBagConstraints;
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit Pattern");
        setBounds(new java.awt.Rectangle(200, 200, 1000, 700));
        setMinimumSize(new java.awt.Dimension(500, 400));
        setPreferredSize(new java.awt.Dimension(1000, 700));
        getContentPane().setLayout(new java.awt.GridBagLayout());        
        
        patternPanel = new PatternPanel(pattern);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight =2;
        gridBagConstraints.gridwidth =2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 7, 0);
        getContentPane().add(patternPanel, gridBagConstraints);
        
        infoPanel = new JPanel();
        infoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Pattern Information"));
        infoPanel.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        infoPanel.add(new JLabel("Name:"), gridBagConstraints);
        
        nameTextField = new JTextField();
        nameTextField.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        infoPanel.add(nameTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        infoPanel.add(new JLabel("Description:"), gridBagConstraints);

        descriptionTextArea = new JTextArea();
        descriptionTextArea.setColumns(20);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setRows(5);
        descriptionTextArea.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        infoPanel.add(new JScrollPane(descriptionTextArea), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        infoPanel.setMinimumSize(new Dimension(200, 400));
        getContentPane().add(infoPanel, gridBagConstraints);

        controlPanel = new JPanel();
        controlPanel.setLayout(new java.awt.GridBagLayout());

        RecordingPanel = new JPanel();
        RecordingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Recording"));
        RecordingPanel.setLayout(new java.awt.GridBagLayout());

        recordButton = new JButton();
        recordButton.setForeground(new java.awt.Color(51, 102, 0));
        recordButton.setText("Start");
        recordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if(recordButton.getForeground().equals(new Color(128, 25, 0))) {
                    recordButton.setForeground(new Color(25, 128, 0));
                    recordButton.setText("Start");
                    pattern.stopRecording();
                    recordingLabel.setText("Recording stoped");
                } else {
                    recordButton.setForeground(new Color(128, 25, 0));
                    recordButton.setText("Stop");
                    pattern.startRecording();
                    recordingLabel.setText("Recording...");
                }
            }
        });
        RecordingPanel.add(recordButton, new java.awt.GridBagConstraints());

        recordingLabel = new JLabel();
        recordingLabel.setText("Recording stoped");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 10);
        RecordingPanel.add(recordingLabel, gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
        RecordingPanel.add(new JLabel("Status:"), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        controlPanel.add(RecordingPanel, gridBagConstraints);

        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	pattern.stopRecording();
            	dispose();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        controlPanel.add(cancelButton, gridBagConstraints);

        saveButton = new JButton();
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePattern();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        controlPanel.add(saveButton, gridBagConstraints);
        
        resetButton = new JButton();
        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	pattern = Pattern.loadFromFile(pattern.getFile());
            	readPattern();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        controlPanel.add(resetButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(controlPanel, gridBagConstraints);

        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/universAAL.gif")).getImage());
        
        pack();
    }

	public void update(Observable o, Object arg) {
		readPattern();
	}
}
