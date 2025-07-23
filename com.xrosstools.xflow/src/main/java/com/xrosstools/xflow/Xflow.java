package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The flow will schedule its pending tasks into TaskEngine.
 * It will not running within a thread.
 * Every task needs to check flow status before running. If it
 * is suspended, the the task should return directly and be moved to pending task queue.
 */
public class Xflow {
	private String id;
	private Map<String, Node> nodes = new HashMap<String, Node>();
	private XflowListener listener;

	private Queue<ActiveToken> pendingNodes = new ConcurrentLinkedQueue<>();
	
	private XflowContext context;
	private AtomicReference<XflowStatus> statusRef = new AtomicReference<>(XflowStatus.CREATED);
	private AtomicLong lastTickRef = new AtomicLong(-1);
	
	public Xflow(XflowFactory factory, String flowId, List<Node> nodeList, XflowListener listener) {
		this.id = flowId;
		for(Node node: nodeList) {
			String id = node.getId();
			if(nodes.containsKey(id))
				throw new IllegalArgumentException(String.format("Node id : \"%s\" is duplicated", id));
			nodes.put(id, node);
			node.setListener(listener);
		}
		this.listener = listener;
		listener.flowCreated(flowId);
	}

	public String getId() {
		return id;
	}
	
	public XflowContext getContext() {
		return context;
	}
	
	public XflowStatus getStatus() {
		return statusRef.get();
	}
	
	public XflowListener getListener() {
		return listener;
	}

	public void start(XflowContext context) {
		reqire(XflowStatus.CREATED);

		this.context = context;
		context.setFlow(this);
		for(Node node: nodes.values())
			if(node instanceof StartNode) {
				changeTo(XflowStatus.CREATED, XflowStatus.RUNNING);
				XflowEngine.submit(context, node);
				listener.flowStarted(context, getId());
				return;
			}

		throw new IllegalArgumentException("No start node found");
	}
	
	/**
	 * This only works when all nodes are inactive, except task and event node.
	 * And current flow is not a sub flow.
	 * @return internal state of current xflow.
	 */
	public XflowRecorder specify() {
		reqire(XflowStatus.SUSPENDED);
		
		if(context.getParentToken() != null)
			throw new IllegalStateException("Specify is not supported for subflow");

		long lastTick = getLastTick();
		
		List<Task> tasks = new ArrayList<>();
		List<EventSpec> eventSpecs = new ArrayList<>();
		List<RouteResult> routeResults = new ArrayList<>();
		List<ActiveTokenRecord> tokenRecorders = new ArrayList<>();

		for(Node node: nodes.values()) {
			if(!node.isActive())
				continue;

			if(node instanceof TaskActivityNode)
				tasks.addAll(((TaskActivityNode)node).getTasks());
			else if(node instanceof EventActivityNode)
				eventSpecs.add(((EventActivityNode)node).getEventSpec());
			else
				throw new IllegalStateException(String.format("Node %s is still running.", node.getId()));
			
			tokenRecorders.add(new ActiveTokenRecord(node.getId(), specifyToken(routeResults, node.getToken())));
		}
		
		for(ActiveToken token: pendingNodes)
			tokenRecorders.add(new ActiveTokenRecord(token.getNode().getId(), specifyToken(routeResults, token)));

		XflowRecorder flowRecorder = new XflowRecorder(routeResults, tokenRecorders);
		
		flowRecorder.setEventSpecs(eventSpecs);
		flowRecorder.setTasks(tasks);

		if(lastTick != getLastTick())
			throw new IllegalStateException("Running node detected during specifying.");

		return flowRecorder;
	}
	
	private Map<Integer, Set<String>> specifyToken(List<RouteResult> routeResults, ActiveToken activeToken) {
		Map<Integer, Set<String>> recorder = new LinkedHashMap<>();

		List<RouteToken> curRouteTokens = activeToken.getRouteTokens();
		for(RouteToken token: curRouteTokens) {
			RouteResult result = token.getRouteResult();
			if(!routeResults.contains(result))
				routeResults.add(result);
			recorder.put(routeResults.indexOf(result), token.getMergedRoutes());
		}
		
		return recorder;
	}

	/**
	 * Restore an xflow instance from a recorder
	 */
	public void restore(XflowContext context, XflowRecorder flowRecorder) {
		reqire(XflowStatus.CREATED);

		if(flowRecorder.getTokenRecorders().isEmpty())
			throw new IllegalArgumentException("There is no active node to be restored");
		
		this.context = context;
		context.setFlow(this);

		//Validate and initialize active token
		List<RouteResult> routeResults = flowRecorder.getRouteResults();
		Map<String, ActiveToken> activeTokenMap = new HashMap<>();
		for(ActiveTokenRecord recorder: flowRecorder.getTokenRecorders()) {
			String nodeId = recorder.getNodeId();
			if(!nodes.containsKey(nodeId))
				throw new NoSuchElementException("There is no active node called " + nodeId);
			
			List<RouteToken> curRouteTokens = new ArrayList<>();
			Map<Integer, Set<String>> routeRecords = recorder.getRouteRecords();
			
			for(Integer resultIndex: routeRecords.keySet()) {
				RouteToken routeToken = new RouteToken(routeResults.get(resultIndex), routeRecords.get(resultIndex));
				curRouteTokens.add(routeToken);
			}
			activeTokenMap.put(nodeId, new ActiveToken(context, nodes.get(nodeId), curRouteTokens));
		}

		//Restore task node
		TaskActivityNode.restoreTasks(nodes, activeTokenMap, flowRecorder.getTasks());
		
		//Restore event node
		EventActivityNode.restoreEventSpecs(nodes, activeTokenMap, flowRecorder.getEventSpecs());

		changeTo(XflowStatus.CREATED, XflowStatus.RUNNING);

		for(ActiveToken token: activeTokenMap.values()) {
			XflowEngine.submit(token);
		}
		
		listener.flowRestored(context, getId());
	}
	
	public void suspend() {
		changeTo(XflowStatus.RUNNING, XflowStatus.SUSPENDED);
		listener.flowSuspended(context, id);
	}

	public void resume() {
		changeTo(XflowStatus.SUSPENDED, XflowStatus.RUNNING);
		
		ActiveToken next;
		while((next = pendingNodes.poll()) != null)
			XflowEngine.submit(next);

		listener.flowResumed(context, id);
	}

	public void succeed() {
		changeTo(XflowStatus.RUNNING, XflowStatus.SUCCEED);
		listener.flowSucceed(context, id);
	}
	
	public void abort(String reason) {
		changeTo(XflowStatus.RUNNING, XflowStatus.ABORTED);
		listener.flowAborted(context, id, reason);
	}

	/**
	 * When there is no active token.
	 */
	public void fail() {
		changeTo(XflowStatus.RUNNING, XflowStatus.FAILED);
		listener.flowFailed(context, id);
	}
	
	public void pending(ActiveToken token) {
		pendingNodes.add(token);
		listener.nodePended(context, token.getNode().getId());
	}
	
	public void pending(List<ActiveToken> tokens) {
		if(tokens == null)
			return;
		pendingNodes.addAll(tokens);
		for(ActiveToken token: tokens)
			listener.nodePended(context, token.getNode().getId());
	}

	public void retry(String nodeId) {
		reqire(XflowStatus.RUNNING);
		nodes.get(nodeId).retry();
		listener.nodeRetried(context, id);
	}
	
	public boolean isSuspended() {
		return XflowStatus.SUSPENDED == statusRef.get();
	}
	
	public boolean isRunning() {
		return XflowStatus.RUNNING == statusRef.get();
	}
	
	public boolean isEnded() {
		XflowStatus cur = getStatus();
		return XflowStatus.SUCCEED == cur || 
				XflowStatus.FAILED == cur ||
				XflowStatus.ABORTED == cur;
	}
	
	public boolean isFailed() {
		if(statusRef.get() == XflowStatus.SUCCEED || statusRef.get() == XflowStatus.ABORTED)
			return false;
			
		if(statusRef.get() == XflowStatus.FAILED)
			return true;

		long lastTick = getLastTick();
		for(Node node: nodes.values())
			if(node.isActive())
				return false;
		
		if(lastTick != getLastTick())
			return false;

		statusRef.set(XflowStatus.FAILED);
		return true;
	}
	
	public List<String> getActiveNodeIds() {
		List<String> ids = new ArrayList<>();
		for(Node node: nodes.values())
			if(node.isActive())
				ids.add(node.getId());
		return ids;
	}
	
	public List<String> getFailedNodeIds() {
		List<String> ids = new ArrayList<>();
		for(Node node: nodes.values())
			if(node.isFailed())
				ids.add(node.getId());
		return ids;
	}
	
	public List<String> getPendingNodeIds() {
		List<String> ids = new ArrayList<>();
		List<ActiveToken> pendingNodesSnap = new ArrayList<>(pendingNodes);

		for(ActiveToken token: pendingNodesSnap)
			ids.add(token.getNode().getId());
		return ids;
	}

	public boolean isActive(String nodeId) {
		return nodes.get(nodeId).isActive();
	}
	
	public boolean isFailed(String nodeId) {
		return nodes.get(nodeId).isFailed();
	}
	
	public Throwable getFailure(String nodeId) {
		return nodes.get(nodeId).getFailure();
	}
	
	public XflowContext getSubflowContext(String nodeId) {
		return ((SubflowActivityNode)nodes.get(nodeId)).getSubflowContext();
	}
	
	public Xflow getSubflow(String nodeId) {
		return getSubflowContext(nodeId).getFlow();
	}
	public void mergeSubflow(String nodeId) {
		reqire(XflowStatus.RUNNING);
		((SubflowActivityNode)nodes.get(nodeId)).mergeSubflow();
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
	
	/**
	 * Submit will not cause task activity fail. 
	 * @param task
	 */
	public void submit(Task task) {
		reqire(XflowStatus.RUNNING);
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
		reqire(XflowStatus.RUNNING);
		Node node = nodes.get(event.getActivityId());
		if(!(node instanceof EventActivityNode))
			throw new IllegalArgumentException(String.format("Node %s is not a event activity.", event.getActivityId()));

		((EventActivityNode)node).notify(context, event);
	}
	
	private void reqire(XflowStatus validStatus) {
		XflowStatus cur = statusRef.get();
		if(cur == validStatus)
			return;
			
		throw new IllegalStateException(String.format("Flow is not in %s. Current status is %s", validStatus.name(), cur.name()));
	}
	
	private void changeTo(XflowStatus expected, XflowStatus next) {
		reqire(expected);
		statusRef.compareAndSet(expected, next);
	}
	
	public void tick() {
		lastTickRef.set(System.currentTimeMillis());
	}
	
	public long getLastTick() {
		return lastTickRef.get();
	}
}
