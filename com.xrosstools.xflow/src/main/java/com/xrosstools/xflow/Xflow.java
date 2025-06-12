package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The flow will schedule its pending tasks into TaskEngine.
 * It will not running within a thread.
 * Every task needs to check flow status before running. If it
 * is suspended, the the task should return directly and be moved to pending task queue.
 */
public class Xflow {
	private XflowFactory fafactory;
	private String id;
	private Map<String, Node> nodes = new HashMap<String, Node>();
	private Queue<Node> pendingNodes = new ConcurrentLinkedQueue<>();
	private Map<String, Exception> failedTasks = new ConcurrentHashMap<>();
	
	private XflowContext context;
	private AtomicReference<XflowStatus> statusRef = new AtomicReference<>();
	
	public Xflow(XflowFactory fafactory, String flowId, List<Node> nodeList) {
		this.id = flowId;
		for(Node node: nodeList) {
			String id = node.getId();
			if(nodes.containsKey(id))
				throw new IllegalArgumentException(String.format("Node id : \"%s\" is duplicated", id));
			nodes.put(id, node);
		}
		statusRef.set(XflowStatus.CREATED);
	}

	public String getId() {
		return id;
	}

	public void start(XflowContext context) {
		this.context = context;
		context.setFlow(this);
		for(Node node: nodes.values())
			if(node instanceof StartNode) {
				XflowEngine.submit(context, node);
				return;
			}

		throw new IllegalArgumentException("No start node found");
	}
	
	public XflowContext getContext() {
		return context;
	}

	/**
	 * TODO How to restore tasks? or do we allow this?
	 * @param context
	 * @param pendingActivities
	 */
	public void restore(XflowContext context, Set<String> pendingActivityIds) {
		this.context = context;
		for(String id: pendingActivityIds) {
			Node node = nodes.get(id);
			XflowEngine.submit(context, node);
		}
	}
	
	public List<String> getPendingNodes() {
		return null;//new ArrayList<String>(pendingTasks.keySet());
	}

	public Map<String, Exception> getFailedTasks() {
		return failedTasks;
	}

	public void suspend() {
		if(getStatus() != XflowStatus.RUNNING)
			throw new IllegalStateException("Flow is not running. Current status is " + getStatus());

		statusRef.set(XflowStatus.SUSPENDED);
	}

	public void resume() {
		if(getStatus() != XflowStatus.SUSPENDED)
			throw new IllegalStateException("Flow is not suspended. Current status is " + getStatus());

		statusRef.set(XflowStatus.RUNNING);
	}
	
	public void succeed() {
		statusRef.set(XflowStatus.SUCCEED);
	}
	
	public void fail(Exception ex) {
		statusRef.set(XflowStatus.FAILED);
	}
	
	public void pending(Node node) {
		pendingNodes.add(node);
	}

	public void retry(String nodeId) {
		if(isSuspended())
			throw new IllegalStateException("Xflow %s is suspended");
		XflowEngine.submit(context, nodes.get(nodeId));
	}
	
	public boolean isSuspended() {
		return statusRef.get() == XflowStatus.SUSPENDED;
	}
	
	public boolean isEnded() {
		return statusRef.get() == XflowStatus.SUCCEED || statusRef.get() == XflowStatus.FAILED;
	}
	
	public XflowStatus getStatus() {
		return statusRef.get();
	}
	
	/**
	 * If assignee is null, it will return all task that is not assigned.
	 * @param assignee
	 * @return
	 */
	public List<Task> getTasks(String assignee) {
		List<Task> taskList = new ArrayList<>();
		for(Node node: nodes.values()) {
			if(node instanceof TaskActivityNode)
				((TaskActivityNode)node).findTasks(taskList, assignee);
		}
		return taskList;
	}
	
	public void submit(Task task) {
		Node node = nodes.get(task.getActivityId());
		if(!(node instanceof TaskActivityNode))
			throw new IllegalArgumentException(String.format("Node %s is not a task activity.", task.getActivityId()));

		((TaskActivityNode)node).submit(context, task);
	}
	
	public List<EventSpec> getEventSpecs() {
		List<EventSpec> eventSpecs = new ArrayList<>();
		for(Node node: nodes.values()) {
			if(node instanceof EventActivityNode) {
				EventSpec eventSpec = ((EventActivityNode)node).getEventSpec();
				if(eventSpec != null)
					eventSpecs.add(eventSpec);
			}
		}
		return eventSpecs;
	}
	
	public void notify(Event event) {
		Node node = nodes.get(event.getActivityId());
		if(!(node instanceof EventActivityNode))
			throw new IllegalArgumentException(String.format("Node %s is not a event activity.", event.getActivityId()));

		((EventActivityNode)node).notify(context, event);
	}
}
