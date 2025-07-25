package com.xrosstools.xflow;

public interface XflowListener {
	void flowCreated(String flowId, String flowInfo);
	void flowStarted(String flowId, XflowContext context);
	void flowSucceed(String flowId, XflowContext context);
	void flowSuspended(String flowId, XflowContext context);
	void flowResumed(String flowId, XflowContext context);
	void flowRestored(String flowId, XflowContext context);
	
	/**
	 * When there is no more active token, and flow is still running.
	 * Trigger by flow monitor.
	 */
	void flowFailed(String flowId, XflowContext context);
	
	/**
	 * When there is an unrecoverable reason.
	 * Trigger by user.
	 */
	void flowAborted(String flowId, XflowContext context, String reason);
	
	void nodeStarted(String nodeId, XflowContext context);
	void nodeRetried(String nodeId, XflowContext context);
	void nodeSucceed(String nodeId, XflowContext context);
	void nodePended(String nodeId, XflowContext context);
	void nodeFailed(String nodeId, XflowContext context, Throwable e);
	void nodeRestored(String nodeId, XflowContext context);
	
	void eventNotifyFailed(String nodeId, XflowContext context, Event event, Throwable e);
	void taskSubmitFailed(String nodeId, XflowContext context, Task task, Throwable e);
	void mergeSubflowFailed(String nodeId, XflowContext context,  XflowContext subFlowContext, Throwable e);
}
