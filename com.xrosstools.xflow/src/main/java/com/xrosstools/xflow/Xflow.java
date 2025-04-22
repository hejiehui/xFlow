package com.xrosstools.xflow;

import java.nio.channels.IllegalSelectorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Xflow {
	private Map<String, Node> nodes = new HashMap<String, Node>();
	boolean start(XflowContext context) {
		
		throw new IllegalSelectorException();
	}
	XflowContext getContext();
	boolean restore(XflowContext context, List<String> activeTasks);
	List<String> getPendingTasks();
	boolean pause();
	boolean resume();
	boolean cancel(String id);
	boolean remove(String id);
	XflowStatus getStatus();
}
