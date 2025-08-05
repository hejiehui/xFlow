package com.xrosstools.xflow;

import java.util.List;

public class XflowRecorder {
	private String instanceId;
	
	private List<RouteResult> routeResults;
	
	private List<ActiveTokenRecord> tokenRecorders;
	
	private List<Task> tasks;
	
	private List<EventSpec> eventSpecs;
	
	public XflowRecorder(String instanceId, List<RouteResult> routeResults, List<ActiveTokenRecord> tokenRecorders) {
		this.instanceId = instanceId;
		this.routeResults = routeResults;
		this.tokenRecorders = tokenRecorders;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public List<RouteResult> getRouteResults() {
		return routeResults;
	}

	public List<ActiveTokenRecord> getTokenRecorders() {
		return tokenRecorders;
	}
	
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<EventSpec> getEventSpecs() {
		return eventSpecs;
	}

	public void setEventSpecs(List<EventSpec> eventSpecs) {
		this.eventSpecs = eventSpecs;
	}
}
