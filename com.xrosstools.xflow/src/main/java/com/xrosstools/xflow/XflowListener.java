package com.xrosstools.xflow;

public interface XflowListener {
	void flowCreated(String flowId, String flowInstId);
	void flowStared(String flowId, String flowInstId);
	void flowSucceed(String flowId, String flowInstId);
	void flowSuspended(String flowId, String flowInstId);
	void flowResumed(String flowId, String flowInstId);
	void flowFailed(String flowId, String flowInstId, Throwable e);
}
