package com.xrosstools.xflow;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class EventActivityNode extends Node {
	private AtomicReference<EventSpec> eventIdRef = new AtomicReference<>();
	private EventActivity activity;

	public EventActivityNode(String id, EventActivity activity) {
		super(id, activity);
		this.activity= activity;
	}

	public boolean isSinglePhased() {
		return false;
	}
	
	public static void restoreEventSpecs(Map<String, Node> nodes, Map<String, ActiveToken> activeTokenMap, List<EventSpec> eventSpecs) {
		for(EventSpec spec: eventSpecs) {
			String nodeId = spec.getActivityId();
			if(nodeId == null || !nodes.containsKey(nodeId) || !(nodes.get(nodeId) instanceof EventActivityNode))
				throw new IllegalArgumentException("Can not find event activity for " + nodeId);

			EventActivityNode node = (EventActivityNode)nodes.get(nodeId);
			node.restore(activeTokenMap.get(node.getId()), spec);
			activeTokenMap.remove(node.getId());
		}
	}

	private void restore(ActiveToken token, EventSpec eventSpec) {
		restore(token);
		eventIdRef.set(eventSpec);
	}

	public List<ActiveToken> handle(ActiveToken token) {
		eventIdRef.set(activity.specify(token.getContext()).initActivityId(getId()));
		return Collections.emptyList();
	}
	
	public void notify(XflowContext context, Event event) {
		assertNotify();

		synchronized (eventIdRef) {
			assertNotify();

			List<ActiveToken> nextTokens = next(getToken());
			try {
				activity.notify(context, getEventSpec(),  event);
				eventIdRef.set(null);
				succeed(nextTokens);
			} catch (Throwable e) {
				getListener().eventNotifyFailed(getId(), context, event, e);
				failed(e);
				throw e;
			}
		}
	}
	
	private void assertNotify() {
		assertToken();

		if(eventIdRef.get() == null)
			throw new IllegalStateException(String.format("Event node: %s is not activated", getId()));
	}
	
	public EventSpec getEventSpec() {
		return eventIdRef.get();
	}
}
