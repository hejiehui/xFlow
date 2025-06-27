package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowContext;

public class TestAdapter {
	public static final String INTERNAL_TASK = "internal_task";
	
	public static final String AUTO_ACTIVITY_NODE = "a";
	public static final String TASK_ACTIVITY_NODE = "aaaabbb";
	public static final String EVENT_ACTIVITY_NODE = "event activity";
	public static final String BINARY_ROUTER_NODE = "b1";
	public static final String INCLUSIVE_ROUTER_NODE = "r1";
	public static final String SUBFLOW_ACTIVITY_NODE = "f1";
	public static final String SUBFLOW_ID = "parallel router";
	public static final String SUBFLOW_AUTO_ACTIVITY_ID_1 = "a1";
	public static final String SUBFLOW_AUTO_ACTIVITY_ID_2 = "a2";
	public static final String SUBFLOW_AUTO_ACTIVITY_ID_3 = "a3";

	public static void injectException(XflowContext context, final Throwable e) {
		context.put(INTERNAL_TASK, new Runnable() {			
			@Override
			public void run() {
				throw (RuntimeException)e;
			}
		});
	}
	
	public static void restoreNormal(XflowContext context) {
		context.put(INTERNAL_TASK, new Runnable() {			
			@Override
			public void run() {
			}
		});
	}
	
	public static void injectWait(XflowContext context, long dur) {
		context.put(INTERNAL_TASK, new Runnable() {			
			@Override
			public void run() {
				try {
					Thread.sleep(dur);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void call(XflowContext context) {
		Runnable internalTask = context.get(INTERNAL_TASK);
		if(internalTask != null)
			internalTask.run();
	}
	
	public void sleep1() throws Exception {
		Thread.sleep(1);
	}

	public void sleep() throws Exception {
		Thread.sleep(5);
	}
	
	public void waitToEnd(Xflow f) throws Exception {
		while(!f.isEnded())
			sleep1();
	}

	public void waitToActive(Xflow f, String nodeId) throws Exception {
		while(!f.isActive(nodeId))
			sleep1();
	}

	public void waitToFail(Xflow f, String nodeId) throws Exception {
		while(!f.isFailed(nodeId))
			sleep1();
	}
}
