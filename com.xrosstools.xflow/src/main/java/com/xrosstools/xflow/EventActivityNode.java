package com.xrosstools.xflow;

import java.util.concurrent.atomic.AtomicReference;

public class EventActivityNode extends Node {
	private AtomicReference<EventSpec> eventIdRef = new AtomicReference<>();

	private EventActivity activity;

	public EventActivityNode(String id, EventActivity activity) {
		super(id, activity);
		this.activity= activity;
	}

	public void handle(ActiveToken token) {
		eventIdRef.set(activity.specify(token.getContext()).initActivityId(getId()));
	}
	
	public void notify(XflowContext context, Event event) {
		activity.notify(context, event);
		eventIdRef.set(null);
		if(getOutputs().length == 0)
			return;

		XflowEngine.submit(context, getOutputs()[0].getTarget());
	}
	
	public EventSpec getEventSpec() {
		return eventIdRef.get();
	}
}
