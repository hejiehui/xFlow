package com.xrosstools.xflow.imp;

import com.xrosstools.xflow.Event;
import com.xrosstools.xflow.Task;
import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowListener;

public class XflowSystemOutListener implements XflowListener{

	@Override
	public void flowCreated(String flowId) {
		System.out.println("Flow created: " + flowId);
	}

	@Override
	public void flowStarted(XflowContext context, String flowId) {
		System.out.println("Flow started: " + flowId);
	}

	@Override
	public void flowSucceed(XflowContext context, String flowId) {
		System.out.println("Flow succeed: " + flowId);
	}

	@Override
	public void flowSuspended(XflowContext context, String flowId) {
		System.out.println("Flow suspended: " + flowId);
	}

	@Override
	public void flowResumed(XflowContext context, String flowId) {
		System.out.println("Flow resumed: " + flowId);
	}

	@Override
	public void flowRestored(XflowContext context, String flowId) {
		System.out.println("Flow restored: " + flowId);
	}

	@Override
	public void flowFailed(XflowContext context, String flowId) {
		System.out.println("Flow failed: " + flowId);
	}

	@Override
	public void flowAborted(XflowContext context, String flowId, String reason) {
		System.out.println("Flow aborted: " + flowId);
	}

	@Override
	public void nodeStarted(XflowContext context, String nodeId) {
		System.out.println("Node started: " + nodeId);
	}

	@Override
	public void nodeSucceed(XflowContext context, String nodeId) {
		System.out.println("Node succeed: " + nodeId);
	}

	@Override
	public void nodeFailed(XflowContext context, String nodeId, Throwable e) {
		System.out.println("Node failed: " + nodeId);
	}

	@Override
	public void eventNotifyFailed(XflowContext context, String nodeId, Event event, Throwable e) {
		System.out.println("Event notify failed: " + nodeId);
	}

	@Override
	public void taskSubmitFailed(XflowContext context, String nodeId, Task task, Throwable e) {
		System.out.println("Task submit failed: " + nodeId);
	}

	@Override
	public void mergeSubflowFailed(XflowContext context, String nodeId,  XflowContext subFlowContext, Throwable e) {
		System.out.println("Merge subflow failed: " + nodeId);
	}
}
