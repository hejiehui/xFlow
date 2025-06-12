package com.xrosstools.xflow;

import java.util.Date;

public class EventSpec extends DataMap {
	private String activityId;
	private String eventId;
	private Date dueDate;
	
	public EventSpec(String eventId) {
		this.eventId = eventId;
	}

	public String getEventId() {
		return eventId;
	}
	
	/**
	 * The system will initialize this
	 * @param activityId
	 */
	public EventSpec initActivityId(String activityId) {
		this.activityId = activityId;
		return this;
	}
	
	/**
	 * The developer needs to initialize this
	 * @param batchId
	 */
	public EventSpec initDueDate(Date dueDate) {
		this.dueDate = dueDate;
		return this;
	}
	
	public String getActivityId() {
		return activityId;
	}

	public Date getDueDate() {
		return dueDate;
	}
	
	public Event create() {
		return new Event(eventId).initActivityId(activityId);
	}
}
