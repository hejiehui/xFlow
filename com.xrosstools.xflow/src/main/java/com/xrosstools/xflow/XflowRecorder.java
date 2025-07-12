package com.xrosstools.xflow;

import java.util.List;
import java.util.Map;

public class XflowRecorder {
	private List<RouteToken> routeTokens;
	
	private Map<String, List<Integer>> tokenRecorders;
	
	private List<Task> tasks;
	
	private List<EventSpec> eventSpecs;
	
	public XflowRecorder(List<RouteToken> routeTokens, Map<String, List<Integer>> tokenRecorders) {
		this.routeTokens = routeTokens;
		this.tokenRecorders = tokenRecorders;
	}

	public List<RouteToken> getRouteTokens() {
		return routeTokens;
	}

	public Map<String, List<Integer>> getTokenRecorders() {
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
