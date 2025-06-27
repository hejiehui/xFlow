package com.xrosstools.xflow;

import java.util.Collections;
import java.util.List;
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

	public List<ActiveToken> handle(ActiveToken token) {
		eventIdRef.set(activity.specify(token.getContext()).initActivityId(getId()));
		return Collections.emptyList();
	}
	
	public void notify(XflowContext context, Event event) {
		assertNotify();

		synchronized (eventIdRef) {
			assertNotify();

			ActiveToken next = this.singleNext(getToken());
			try {
				activity.notify(context, event);
				eventIdRef.set(null);
				succeed();
			} catch (Throwable e) {
				getListener().eventNotifyFailed(context, getId(), event, e);
				failed(e);
				throw e;
			}
			XflowEngine.submit(next);
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
