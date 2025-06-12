package com.xrosstools.xflow;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TaskActivityNode extends Node {
	private TaskActivity taskActivity;
	private AtomicReference<List<Task>> tasksRef = new AtomicReference<>();

	public TaskActivityNode(String id, TaskActivity taskActivity) {
		super(id, taskActivity);
		this.taskActivity = taskActivity;
	}

	public void findTasks(List<Task> taskList, String assignee) {
		if(tasksRef.get() == null)
			return;

		for(Task task: tasksRef.get())
			if(Objects.equals(assignee, task.getAssignee()) && !task.isSubmitted())
				taskList.add(task);
	}
	
	public void submit(XflowContext context, Task task) {
		if(task.isSubmitted())
			throw new IllegalArgumentException(String.format("Task %s is already submitted.", task.getId()));

		boolean completed = false;
		
		synchronized(this) {
			if(tasksRef.get() == null)
				return;

			completed = taskActivity.submit(context, tasksRef.get(), task);
			task.setSubmitted();
			if(completed)
				tasksRef.set(null);
		}

		if(completed && getOutputs().length == 1)
			XflowEngine.submit(context, getOutputs()[0].getTarget());
	}

	public void handle(ActiveToken token) {
		List<Task> tasks = taskActivity.create(token.getContext());
		for(Task task: tasks)
			task.initActivityId(getId());
		tasksRef.set(tasks);
	}
}
