package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.DataMap;
import com.xrosstools.xflow.Event;
import com.xrosstools.xflow.EventActivity;
import com.xrosstools.xflow.EventSpec;
import com.xrosstools.xflow.NodeConfigAware;
import com.xrosstools.xflow.XflowContext;

public class TestEventActivity implements EventActivity, NodeConfigAware  {
	public static final String EVENT_ID = "event id";
	private String eventId;
	@Override
	public EventSpec specify(XflowContext context) {
		return new EventSpec(eventId);
	}

	@Override
	public void notify(XflowContext context, Event event) {
		if(!event.getId().equals(eventId))
			throw new IllegalArgumentException();
	}

	@Override
	public void initNodeConfig(DataMap config) {
		eventId = config.get(EVENT_ID);
	}
}
