package com.xrosstools.xflow;

public interface ActivityListener {
	void activitySucceed(String flowId, String flowInstId, String activityId, String instId);
	void activityFailed(String flowId, String flowInstId, String activityId, String instId);

}
