package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.Event;
import com.xrosstools.xflow.Task;
import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowListener;

public class TestSystemOutListener extends TestAdapter implements XflowListener {
	@Override
	public void flowCreated(String flowId, String flowInfo) {
		System.out.println("Flow created: " + flowId);
		System.out.println(flowInfo);
	}

	@Override
	public void flowStarted(String flowId, XflowContext context) {
		System.out.println("Flow started: " + flowId);
		flowCallback(context, flowStarted);
	}

	@Override
	public void flowSucceed(String flowId, XflowContext context) {
		System.out.println("Flow succeed: " + flowId);
		flowCallback(context, flowSucceed);
	}

	@Override
	public void flowSuspended(String flowId, XflowContext context) {
		System.out.println("Flow suspended: " + flowId);
		flowCallback(context, flowSuspended);
	}

	@Override
	public void flowResumed(String flowId, XflowContext context) {
		System.out.println("Flow resumed: " + flowId);
		flowCallback(context, flowResumed);
	}

	@Override
	public void flowRestored(String flowId, XflowContext context) {
		System.out.println("Flow restored: " + flowId);
		flowCallback(context, flowRestored);
	}

	@Override
	public void flowFailed(String flowId, XflowContext context) {
		System.out.println("Flow failed: " + flowId);
		flowCallback(context, flowFailed);
	}

	@Override
	public void flowAborted(String flowId, XflowContext context, String reason) {
		System.out.println("Flow aborted: " + flowId);
		flowCallback(context, flowAborted);
	}

	@Override
	public void nodeStarted(String nodeId, XflowContext context) {
		System.out.println("Node started: " + nodeId);
		nodeCallback(context, nodeStarted, nodeId);
	}

	@Override
	public void nodeRetried(String nodeId, XflowContext context) {
		System.out.println("Node retried: " + nodeId);
		nodeCallback(context, nodeRetried, nodeId);
	}

	@Override
	public void nodeSucceed(String nodeId, XflowContext context) {
		System.out.println("Node succeed: " + nodeId);
		nodeCallback(context, nodeSucceed, nodeId);
	}

	@Override
	public void nodePended(String nodeId, XflowContext context) {
		System.out.println("Node pended: " + nodeId);
		nodeCallback(context, nodePended, nodeId);
	}

	@Override
	public void nodeFailed(String nodeId, XflowContext context, Throwable e) {
		System.out.println("Node failed: " + nodeId);
		nodeCallback(context, nodeFailed, nodeId);
	}

	@Override
	public void nodeRestored(String nodeId, XflowContext context) {
		System.out.println("Node restored: " + nodeId);
		nodeCallback(context, nodeRestored, nodeId);
	}

	@Override
	public void eventNotifyFailed(String nodeId, XflowContext context, Event event, Throwable e) {
		System.out.println("Event notify failed: " + nodeId);
		nodeCallback(context, eventNotifyFailed, nodeId);
	}

	@Override
	public void taskSubmitFailed(String nodeId, XflowContext context, Task task, Throwable e) {
		System.out.println("Task submit failed: " + nodeId);
		nodeCallback(context, taskSubmitFailed, nodeId);
	}

	@Override
	public void mergeSubflowFailed(String nodeId, XflowContext context,  XflowContext subFlowContext, Throwable e) {
		System.out.println("Merge subflow failed: " + nodeId);
		nodeCallback(context, mergeSubflowFailed, nodeId);
	}
}
