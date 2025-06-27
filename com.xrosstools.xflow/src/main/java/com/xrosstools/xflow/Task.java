package com.xrosstools.xflow;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Task is an assignment that requires third part to perform. Third party
 * refers to human, timer, other system, event or message source, etc.
 * No matter what kind of third party is, they all need to provide task 
 * with whatever information it needs, like data, or signal which the flow need but 
 * can not generate by itself.
 * 
 * When parent activity complete, all unfinished task will be cancelled.
 */
public class Task extends DataMap {
	private String activityId;

	private String id;
	private String assignee;
	private AtomicReference<Boolean> submittedRef = new AtomicReference<>(Boolean.FALSE);
	
	private Date dueDate;
	
	public Task(String id, String assignee) {
		this.id = id;
		this.assignee = assignee;
	}

	public String getId() {
		return id;
	}
	
	/**
	 * The system will initialize this
	 * @param activityId
	 */
	public Task initActivityId(String activityId) {
		this.activityId = activityId;
		return this;
	}

	/**
	 * The developer needs to initialize this
	 * @param batchId
	 */
	public void initDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getActivityId() {
		return activityId;
	}
	
	public Date getDueDate() {
		return dueDate;
	}

	public String getAssignee() {
		return assignee;
	}
	
	//TODO need to listen change event
	public void reassign(String assignee) {
		this.assignee = assignee;
	}

	public boolean isSubmitted() {
		return submittedRef.get();
	}
	
	public void setSubmitted() {
		submittedRef.set(Boolean.TRUE);
	}
	
	public void clearSubmitFlag() {
		submittedRef.set(Boolean.FALSE);
	}
}
