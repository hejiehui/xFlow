package com.xrosstools.xflow;

import java.util.Map;
import java.util.Set;

public class ActiveTokenRecord {
	private String nodeId;
	private Map<Integer, Set<String>> routeRecords;
	
	public ActiveTokenRecord(String nodeId, Map<Integer, Set<String>> routeRecords) {
		this.nodeId = nodeId;
		this.routeRecords = routeRecords;
	}

	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public Map<Integer, Set<String>> getRouteRecords() {
		return routeRecords;
	}
	public void setRouteRecords(Map<Integer, Set<String>> routeRecords) {
		this.routeRecords = routeRecords;
	}
}
