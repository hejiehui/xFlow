package com.xrosstools.xflow;

public interface EventActivity {
	EventSpec specify(XflowContext context);

	void notify(XflowContext context, EventSpec spec, Event event);
}
