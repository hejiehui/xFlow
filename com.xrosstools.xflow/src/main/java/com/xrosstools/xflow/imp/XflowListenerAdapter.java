package com.xrosstools.xflow.imp;

import com.xrosstools.xflow.Event;
import com.xrosstools.xflow.Task;
import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowListener;

public class XflowListenerAdapter implements XflowListener {

	@Override
	public void flowCreated(String flowId, String flowInfo) {
	}

	@Override
	public void flowStarted(String flowId, XflowContext context) {
	}

	@Override
	public void flowSucceed(String flowId, XflowContext context) {
	}

	@Override
	public void flowSuspended(String flowId, XflowContext context) {
	}

	@Override
	public void flowResumed(String flowId, XflowContext context) {
	}

	@Override
	public void flowRestored(String flowId, XflowContext context) {
	}

	@Override
	public void flowFailed(String flowId, XflowContext context) {
	}

	@Override
	public void flowAborted(String flowId, XflowContext context, String reason) {
	}

	@Override
	public void nodeStarted(String nodeId, XflowContext context) {
	}

	@Override
	public void nodeRetried(String nodeId, XflowContext context) {
	}

	@Override
	public void nodeSucceed(String nodeId, XflowContext context) {
	}
	
	@Override
	public void nodePended(String nodeId, XflowContext context) {
	}

	@Override
	public void nodeFailed(String nodeId, XflowContext context, Throwable e) {
	}
	
	@Override
	public void nodeRestored(String nodeId, XflowContext context) {
	}
	
	@Override
	public void eventNotifyFailed(String nodeId, XflowContext context, Event event, Throwable e) {
	}

	@Override
	public void taskSubmitFailed(String nodeId, XflowContext context, Task task, Throwable e) {
	}
	
	@Override
	public void mergeSubflowFailed(String nodeId, XflowContext context,  XflowContext subFlowContext, Throwable e) {
	}
}
