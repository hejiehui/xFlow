package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.Task;

public class FeedbackTask extends Task {
	private String feedback;
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public FeedbackTask(String id, String assignee) {
		super(id, assignee);
	}
}
