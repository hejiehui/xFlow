package com.xrosstools.xflow.imp;

import com.xrosstools.xflow.Event;
import com.xrosstools.xflow.Task;
import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowListener;

public class XflowListenerAdapter implements XflowListener {

	@Override
	public void flowCreated(String flowId) {
	}

	@Override
	public void flowStarted(XflowContext context, String flowId) {
	}

	@Override
	public void flowSucceed(XflowContext context, String flowId) {
	}

	@Override
	public void flowSuspended(XflowContext context, String flowId) {
	}

	@Override
	public void flowResumed(XflowContext context, String flowId) {
	}

	@Override
	public void flowFailed(XflowContext context, String flowId) {
	}

	@Override
	public void flowAborted(XflowContext context, String flowId, String reason) {
	}

	@Override
	public void nodeStarted(XflowContext context, String nodeId) {
	}

	@Override
	public void nodeRetried(XflowContext context, String nodeId) {
	}

	@Override
	public void nodeSucceed(XflowContext context, String nodeId) {
	}
	
	@Override
	public void nodePended(XflowContext context, String nodeId) {
	}

	@Override
	public void nodeFailed(XflowContext context, String nodeId, Throwable e) {
	}
	
	@Override
	public void eventNotifyFailed(XflowContext context, String nodeId, Event event, Throwable e) {
	}

	@Override
	public void taskSubmitFailed(XflowContext context, String nodeId, Task task, Throwable e) {
	}
	
	@Override
	public void mergeSubflowFailed(XflowContext context, String nodeId,  XflowContext subFlowContext, Throwable e) {
	}

	@Override
	public void flowRestored(XflowContext context, String flowId) {
	}
}
