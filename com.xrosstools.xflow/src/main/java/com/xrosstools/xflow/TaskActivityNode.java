package com.xrosstools.xflow;

import java.util.Collections;
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

	public boolean isSinglePhased() {
		return false;
	}

	public void findTasks(List<Task> taskList, String assignee) {
		if(tasksRef.get() == null)
			return;

		for(Task task: tasksRef.get())
			if(Objects.equals(assignee, task.getAssignee()) && !task.isSubmitted())
				taskList.add(task);
	}
	
	public void submit(XflowContext context, Task task) {
		assertSubmit(task);

		ActiveToken next = this.singleNext(getToken());
		synchronized(tasksRef) {
			assertSubmit(task);

			boolean completed;
			try {
				completed = taskActivity.submit(context, tasksRef.get(), task);
			} catch (Throwable e) {
				getListener().taskSubmitFailed(context, getId(), task, e);
				throw e;
			}
			task.setSubmitted();
			if(completed) {
				tasksRef.set(null);
				succeed();
				XflowEngine.submit(next);
			}
		}
	}
	
	private void assertSubmit(Task task) {
		assertToken();

		if(task.isSubmitted())
			throw new IllegalArgumentException(String.format("Task %s is already submitted.", task.getId()));

		if(tasksRef.get() == null)
			throw new IllegalStateException(String.format("Event node: %s is not wait for event", getId()));
	}

	public List<ActiveToken> handle(ActiveToken token) {
		List<Task> tasks = taskActivity.create(token.getContext());
		for(Task task: tasks)
			task.initActivityId(getId());
		tasksRef.set(tasks);
		return Collections.emptyList();
	}
}
