package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.Event;
import com.xrosstools.xflow.Task;
import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowListener;

public class TestSystemOutListener extends TestAdapter implements XflowListener {
	@Override
	public void flowCreated(String flowId) {
		System.out.println("Flow created: " + flowId);
	}

	@Override
	public void flowStarted(XflowContext context, String flowId) {
		System.out.println("Flow started: " + flowId);
		flowCallback(context, flowStarted);
	}

	@Override
	public void flowSucceed(XflowContext context, String flowId) {
		System.out.println("Flow succeed: " + flowId);
		flowCallback(context, flowSucceed);
	}

	@Override
	public void flowSuspended(XflowContext context, String flowId) {
		System.out.println("Flow suspended: " + flowId);
		flowCallback(context, flowSuspended);
	}

	@Override
	public void flowResumed(XflowContext context, String flowId) {
		System.out.println("Flow resumed: " + flowId);
		flowCallback(context, flowResumed);
	}

	@Override
	public void flowRestored(XflowContext context, String flowId) {
		System.out.println("Flow restored: " + flowId);
		flowCallback(context, flowRestored);
	}

	@Override
	public void flowFailed(XflowContext context, String flowId) {
		System.out.println("Flow failed: " + flowId);
		flowCallback(context, flowFailed);
	}

	@Override
	public void flowAborted(XflowContext context, String flowId, String reason) {
		System.out.println("Flow aborted: " + flowId);
		flowCallback(context, flowAborted);
	}

	@Override
	public void nodeStarted(XflowContext context, String nodeId) {
		System.out.println("Node started: " + nodeId);
		nodeCallback(context, nodeStarted, nodeId);
	}

	@Override
	public void nodeRetried(XflowContext context, String nodeId) {
		System.out.println("Node retried: " + nodeId);
		nodeCallback(context, nodeRetried, nodeId);
	}

	@Override
	public void nodeSucceed(XflowContext context, String nodeId) {
		System.out.println("Node succeed: " + nodeId);
		nodeCallback(context, nodeSucceed, nodeId);
	}

	@Override
	public void nodePended(XflowContext context, String nodeId) {
		System.out.println("Node pended: " + nodeId);
		nodeCallback(context, nodePended, nodeId);
	}

	@Override
	public void nodeFailed(XflowContext context, String nodeId, Throwable e) {
		System.out.println("Node failed: " + nodeId);
		nodeCallback(context, nodeFailed, nodeId);
	}

	@Override
	public void eventNotifyFailed(XflowContext context, String nodeId, Event event, Throwable e) {
		System.out.println("Event notify failed: " + nodeId);
		nodeCallback(context, eventNotifyFailed, nodeId);
	}

	@Override
	public void taskSubmitFailed(XflowContext context, String nodeId, Task task, Throwable e) {
		System.out.println("Task submit failed: " + nodeId);
		nodeCallback(context, taskSubmitFailed, nodeId);
	}

	@Override
	public void mergeSubflowFailed(XflowContext context, String nodeId,  XflowContext subFlowContext, Throwable e) {
		System.out.println("Merge subflow failed: " + nodeId);
		nodeCallback(context, mergeSubflowFailed, nodeId);
	}
}
