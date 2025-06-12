package com.xrosstools.xflow;

/**
 * A common representation for message, signal, event, email, timer, etc.
 */
public class Event extends DataMap {
	private String activityId;
	private String id;
	
	public Event(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public Event initActivityId(String activityId) {
		this.activityId = activityId;
		return this;
	}

	public String getActivityId() {
		return activityId;
	}
}

