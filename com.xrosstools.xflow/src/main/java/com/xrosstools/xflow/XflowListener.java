package com.xrosstools.xflow;

public interface XflowListener {
	void flowCreated(String flowId);
	void flowStarted(XflowContext context, String flowId);
	void flowSucceed(XflowContext context, String flowId);
	void flowSuspended(XflowContext context, String flowId);
	void flowResumed(XflowContext context, String flowId);
	void flowRestored(XflowContext context, String flowId);
	
	/**
	 * When there is no more active token, and flow is still running.
	 * Trigger by flow monitor.
	 */
	void flowFailed(XflowContext context, String flowId);
	
	/**
	 * When there is unrecoverable reason.
	 * Trigger by user.
	 */
	void flowAborted(XflowContext context, String flowId, String reason);
	
	void nodeStarted(XflowContext context, String nodeId);
	void nodeRetried(XflowContext context, String nodeId);
	void nodeSucceed(XflowContext context, String nodeId);
	void nodePended(XflowContext context, String nodeId);
	void nodeFailed(XflowContext context, String nodeId, Throwable e);
	
	void eventNotifyFailed(XflowContext context, String nodeId, Event event, Throwable e);
	void taskSubmitFailed(XflowContext context, String nodeId, Task task, Throwable e);
	void mergeSubflowFailed(XflowContext context, String nodeId,  XflowContext subFlowContext, Throwable e);
}
