package com.xrosstools.xflow.imp;

import com.xrosstools.xflow.Event;
import com.xrosstools.xflow.Task;
import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowListener;

public class XflowSystemOutListener implements XflowListener {

	@Override
	public void flowCreated(String flowId, String flowInfo) {
		System.out.println("Flow created: " + flowId);
		System.out.println(flowInfo);
	}

	@Override
	public void flowStarted(String flowId, XflowContext context) {
		System.out.println("Flow started: " + flowId);
	}

	@Override
	public void flowSucceed(String flowId, XflowContext context) {
		System.out.println("Flow succeed: " + flowId);
	}

	@Override
	public void flowSuspended(String flowId, XflowContext context) {
		System.out.println("Flow suspended: " + flowId);
	}

	@Override
	public void flowResumed(String flowId, XflowContext context) {
		System.out.println("Flow resumed: " + flowId);
	}

	@Override
	public void flowRestored(String flowId, XflowContext context) {
		System.out.println("Flow restored: " + flowId);
	}

	@Override
	public void flowFailed(String flowId, XflowContext context) {
		System.out.println("Flow failed: " + flowId);
	}

	@Override
	public void flowAborted(String flowId, XflowContext context, String reason) {
		System.out.println("Flow aborted: " + flowId);
	}

	@Override
	public void nodeStarted(String nodeId, XflowContext context) {
		System.out.println("Node started: " + nodeId);
	}

	@Override
	public void nodeRetried(String nodeId, XflowContext context) {
		System.out.println("Node retried: " + nodeId);
	}

	@Override
	public void nodeSucceed(String nodeId, XflowContext context) {
		System.out.println("Node succeed: " + nodeId);
	}

	@Override
	public void nodePended(String nodeId, XflowContext context) {
		System.out.println("Node pended: " + nodeId);
	}

	@Override
	public void nodeFailed(String nodeId, XflowContext context, Throwable e) {
		System.out.println("Node failed: " + nodeId);
	}

	@Override
	public void nodeRestored(String nodeId, XflowContext context) {
		System.out.println("Node restored: " + nodeId);
	}
	
	@Override
	public void eventNotifyFailed(String nodeId, XflowContext context, Event event, Throwable e) {
		System.out.println("Event notify failed: " + nodeId);
	}

	@Override
	public void taskSubmitFailed(String nodeId, XflowContext context, Task task, Throwable e) {
		System.out.println("Task submit failed: " + nodeId);
	}

	@Override
	public void mergeSubflowFailed(String nodeId, XflowContext context,  XflowContext subFlowContext, Throwable e) {
		System.out.println("Merge subflow failed: " + nodeId);
	}
}
