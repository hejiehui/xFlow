package com.xrosstools.xflow;

public interface XflowManager {
	String[] getInstanceIds(String flowName);
	
	String[] getPendingTasks(String instanceId);
	
	XflowContext getContext(String instanceId);
	
	void save(String instanceId, XflowContext context, String[] pendingTasks);
}
