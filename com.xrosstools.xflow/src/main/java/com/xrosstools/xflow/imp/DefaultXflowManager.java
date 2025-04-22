package com.xrosstools.xflow.imp;

import java.util.concurrent.ConcurrentHashMap;

import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowManager;

public class DefaultXflowManager implements XflowManager {
	private ConcurrentHashMap<String, InstanceContext> cache = new ConcurrentHashMap<>();

	@Override
	public String[] getInstanceIds(String flowName) {
		return cache.keySet().toArray(new String[0]);
	}

	@Override
	public String[] getPendingTasks(String instanceId) {
		return cache.get(instanceId).pendingTasks;
	}

	@Override
	public XflowContext getContext(String instanceId) {
		return cache.get(instanceId).context;
	}

	@Override
	public void save(String instanceId, XflowContext context, String[] pendingTasks) {
		cache.put(instanceId, new InstanceContext(context, pendingTasks));
	}

	private static class InstanceContext {
		XflowContext context;
		String[] pendingTasks;
		
		InstanceContext(XflowContext context, String[] pendingTasks) {
			this.context = context;
			this.pendingTasks = pendingTasks;
		}
	}
}
