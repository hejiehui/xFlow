package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	public static void restoreTasks(Map<String, Node> nodes, Map<String, ActiveToken> activeTokenMap, List<Task> allTasks) {
		Map<String, List<Task>> taskMap = new HashMap<>();
		for(Task task: allTasks) {
			String nodeId = task.getActivityId();
			if(nodeId == null || !nodes.containsKey(nodeId) || !(nodes.get(nodeId) instanceof TaskActivityNode))
				throw new IllegalArgumentException("Can not find task activity for " + nodeId);

			List<Task> tasks = taskMap.get(nodeId);
			if(tasks == null) {
				tasks = new ArrayList<Task>();
				taskMap.put(nodeId, tasks);
			}
			
			tasks.add(task);
		}
		
		//Initialize task node
		for(Map.Entry<String, List<Task>> entry: taskMap.entrySet()) {
			TaskActivityNode node = (TaskActivityNode)nodes.get(entry.getKey());
			node.restore(activeTokenMap.get(node.getId()), entry.getValue());
			activeTokenMap.remove(node.getId());
		}
	}

	private void restore(ActiveToken token, List<Task> tasks) {
		restore(token);
		tasksRef.set(tasks);
	}

	public List<Task> getTasks() {
		if(tasksRef.get() == null)
			return Collections.emptyList();
		
		return new ArrayList<Task>(tasksRef.get());
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
				getListener().taskSubmitFailed(getId(), context, task, e);
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
