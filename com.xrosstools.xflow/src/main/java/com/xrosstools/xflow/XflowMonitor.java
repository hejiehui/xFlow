package com.xrosstools.xflow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class XflowMonitor extends JFrame {
	private static final long serialVersionUID = 1L;

	private JList<Xflow> processInstanceList;
    private DefaultListModel<Xflow> listModel;
    private JLabel nameLabel;
    private JLabel statusLabel;
    private JLabel startTimeLabel;
    private JLabel endTimeLabel;
    private JLabel lastTickLabel;
    private JLabel reasonLabel;
    private JList<String> nodeList;
    private DefaultListModel<String> nodeListModel;
    private JTextArea statusTextArea;

    private static final String PENDING_NODES = "Pending nodes";
    private static final String FAILED_NODES = "Failed nodes";
    private static final String ACTIVE_NODES = "Active nodes";
    private static final String EXECUTED_NODES = "Executed nodes";
    private static final String CONTEXT_ENTRIES = "Context entries";
    private static final String RETRY_NODE = "Retry node";

    private JButton suspendButton = new JButton("Suspend");
    private JButton resumeButton = new JButton("Resume");
    private JButton abortButton = new JButton("Abort");
    private JButton activeButton = new JButton(ACTIVE_NODES);
    private JButton failedButton = new JButton(FAILED_NODES);
    private JButton pendingButton = new JButton(PENDING_NODES);
    private JButton executedButton = new JButton(EXECUTED_NODES);
    private JButton contextButton = new JButton(CONTEXT_ENTRIES);
    private JButton retryButton = new JButton(RETRY_NODE);
    
    private TitledBorder nodesTitle = new TitledBorder("Node list");
    private TitledBorder statusTitle = new TitledBorder("Node status");
    
    private Xflow selectedInstance;
    private String selectedNode;

    public XflowMonitor(int refreshRate) {
        setTitle("Xflow instance monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.setBorder(new TitledBorder("Instance list"));

        listModel = new DefaultListModel<>();

        processInstanceList = new JList<>(listModel);
        processInstanceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        processInstanceList.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
	            if (!e.getValueIsAdjusting()) {
	            	selectedInstance = processInstanceList.getSelectedValue();
	            	updateInstanceDetails();
	            }
        	}});

        JScrollPane listScrollPane = new JScrollPane(processInstanceList);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        // Instance status
        JPanel flowMainPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel flowInfoPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        flowInfoPanel.setBorder(new TitledBorder("Instance status"));
        flowInfoPanel.add(new JLabel("Name:"));
        nameLabel = new JLabel("");
        flowInfoPanel.add(nameLabel);
        
        flowInfoPanel.add(new JLabel("Status:"));
        statusLabel = new JLabel("");
        flowInfoPanel.add(statusLabel);
        
        flowInfoPanel.add(new JLabel("Start time:"));
        startTimeLabel = new JLabel("");
        flowInfoPanel.add(startTimeLabel);
        
        flowInfoPanel.add(new JLabel("Last tick:"));
        lastTickLabel = new JLabel("");
        flowInfoPanel.add(lastTickLabel);
        
        flowInfoPanel.add(new JLabel("End time:"));
        endTimeLabel = new JLabel("");
        flowInfoPanel.add(endTimeLabel);
        
        flowInfoPanel.add(new JLabel("Reason:"));
        reasonLabel = new JLabel("");
        flowInfoPanel.add(reasonLabel);

        JPanel flowControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        flowControlPanel.add(suspendButton);
        flowControlPanel.add(resumeButton);
        flowControlPanel.add(abortButton);

        flowMainPanel.add(flowInfoPanel, BorderLayout.CENTER);
        flowMainPanel.add(flowControlPanel, BorderLayout.SOUTH);
        
        JPanel nodeControlMainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        JPanel nodeControlPanel_1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        nodeControlPanel_1.add(contextButton);
        nodeControlPanel_1.add(executedButton);
        nodeControlPanel_1.add(activeButton);

        JPanel nodeControlPanel_2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        nodeControlPanel_2.add(failedButton);
        nodeControlPanel_2.add(pendingButton);
        nodeControlPanel_2.add(retryButton);

        JPanel nodeInfoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        JPanel nodePanel = new JPanel(new BorderLayout());
        nodePanel.setBorder(nodesTitle);
        
        nodeListModel = new DefaultListModel<>();
        nodeList = new JList<>(nodeListModel);
        nodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nodeList.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
	            if (!e.getValueIsAdjusting()) {
	            	String lastSelected = nodeList.getSelectedValue();
	            	selectedNode = lastSelected == null ? selectedNode : lastSelected;
	            	updateNodeStatus();
	            }
        	}});
        
        JScrollPane nodeScrollPane = new JScrollPane(nodeList);
        nodePanel.add(nodeScrollPane, BorderLayout.CENTER);
        
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(statusTitle);
        
        statusTextArea = new JTextArea();
        statusTextArea.setEditable(false);
        JScrollPane statusScrollPane = new JScrollPane(statusTextArea);
        statusPanel.add(statusScrollPane, BorderLayout.CENTER);
        
        nodeControlMainPanel.add(nodeControlPanel_1);
        nodeControlMainPanel.add(nodeControlPanel_2);

        nodeInfoPanel.add(nodePanel);
        nodeInfoPanel.add(statusPanel);

        rightPanel.add(flowMainPanel, BorderLayout.NORTH);
        rightPanel.add(nodeInfoPanel, BorderLayout.CENTER);
        rightPanel.add(nodeControlMainPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        addButtonListeners();

        add(mainPanel);
        
        setTimer(refreshRate);
    }
    
    public void addInstance(Xflow instance) {
    	listModel.addElement(instance);
	}
    
    private void updateInstanceDetails() {
        if (selectedInstance != null) {
            nameLabel.setText(selectedInstance.getId());
            statusLabel.setText(selectedInstance.getStatus().name());
            lastTickLabel.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(selectedInstance.getLastTickTime())));
            reasonLabel.setText(selectedInstance.getAbortReason());
            startTimeLabel.setText(getTime(selectedInstance.getStartTime()));
            endTimeLabel.setText(getTime(selectedInstance.getEndTime()));
            statusTextArea.setText("");
        }
    }
    
    private void updateNodes() {
    	if(currentUpdator != null) {
        	updateNodes(currentUpdator.getNodes());
    	}
    }
    
    private void updateNodeStatus() {
    	if(currentUpdator != null && selectedNode != null) {
            if(nodeListModel.contains(selectedNode)) {
            	nodeList.setSelectedValue(selectedNode, true);
            	statusTitle.setTitle(String.format("%s status", selectedNode));
            	statusTextArea.setText(currentUpdator.getStatus(selectedNode));
            } else {
            	statusTextArea.setText("");
            }
            this.repaint();
    	}
    }
    
    private void addButtonListeners() {
    	updatorMap.put(PENDING_NODES, new NodeUpdator() {
			public List<String> getNodes() {
				return selectedInstance.getPendingNodeIds();
			}
		});
    	
    	updatorMap.put(FAILED_NODES, new NodeUpdator() {
			public List<String> getNodes() {
				return selectedInstance.getFailedNodeIds();
			}

			public String getStatus(String nodeId) {
				return selectedInstance.getFailure(nodeId).toString();
			}
    	});
    	
    	updatorMap.put(ACTIVE_NODES, new NodeUpdator() {
			public List<String> getNodes() {
				return selectedInstance.getActiveNodeIds();
			}
    	});
    	
    	updatorMap.put(CONTEXT_ENTRIES, new NodeUpdator() {
			public List<String> getNodes() {
				return new ArrayList<String>(selectedInstance.getContext().keySet());
			}

			public String getStatus(String nodeId) {
				return String.valueOf(selectedInstance.getContext().get(nodeId));
			}
    	});
    	
    	updatorMap.put(EXECUTED_NODES, new NodeUpdator() {
			public List<String> getNodes() {
				return selectedInstance.getExecutedNodeIds();
			}
    	});
    	
        suspendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedInstance == null) return;
				selectedInstance.suspend();
				selectUpdator(PENDING_NODES);				
			}
		});

        resumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedInstance == null) return;
				selectedInstance.resume();
				selectUpdator(ACTIVE_NODES);
			}
		});

        abortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedInstance == null) return;
				selectedInstance.abort("Triggered by user");
				selectUpdator(ACTIVE_NODES);
			}
		});

        executedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedInstance == null) return;
				selectUpdator(EXECUTED_NODES);
			}
		});

        activeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedInstance == null) return;
				selectUpdator(ACTIVE_NODES);
			}
		});

        failedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedInstance == null) return;
				selectUpdator(FAILED_NODES);
			}
		});

        pendingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedInstance == null) return;
				selectUpdator(PENDING_NODES);
			}
		});
        
        contextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedInstance == null) return;
				selectUpdator(CONTEXT_ENTRIES);
			}
		});
        
        retryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedInstance == null) return;
				if(selectedInstance.getFailedNodeIds().contains(selectedNode))
					selectedInstance.retry(selectedNode);
				selectUpdator(FAILED_NODES);
			}
		});
    }
    
    private void updateNodes(List<String> nodes) {
        nodeListModel.clear();
        for (String node : nodes) {
            nodeListModel.addElement(node);
        }
    }

    private Map<String, NodeUpdator> updatorMap = new HashMap<>();
    
    private NodeUpdator currentUpdator;

    private void selectUpdator(String id) {
    	nodesTitle.setTitle(id);
    	currentUpdator = updatorMap.get(id);
    	updateNodes(currentUpdator.getNodes());
    	selectedNode = null;
    	this.repaint();
    }

    private abstract class NodeUpdator {
    	public abstract List<String> getNodes();

    	public String getStatus(String nodeId) {
    		StringBuilder sb = new StringBuilder();
    		sb.append(String.format("Is active:         %s\n", selectedInstance.isActive(nodeId)));
    		sb.append(String.format("Is failed:         %s\n", selectedInstance.isFailed(nodeId)));
    		sb.append(String.format("Failure:           %s\n", selectedInstance.getFailure(nodeId)));
    		sb.append(String.format("Execution count:   %s\n", selectedInstance.getExecutionCount(nodeId)));
    		sb.append(String.format("Last start time:   %s\n", getTime(selectedInstance.getLastStartTime(nodeId))));
    		sb.append(String.format("Last succeed time: %s\n", getTime(selectedInstance.getLastSucceedTime(nodeId))));
    		sb.append(String.format("Last failed time:  %s\n", getTime(selectedInstance.getLastFailedTime(nodeId))));
			return sb.toString();
		}
    }
    
    private void setTimer(int refreshRate) {
        Timer timer = new Timer(refreshRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateInstanceDetails();
            	updateNodes();
            	updateNodeStatus();
            }
        });
        
        timer.start();        
        //timer.stop();
    }
    
    private String getTime(long time) {
    	return time == 0 ? "N/A" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(time));
    }
}
