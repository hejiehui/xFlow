package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowContext;

public class TestAdapter {
	public static final String INTERNAL_TASK = "internal_task";
	public static final String SUB_FLOW_SUSPEND = "sub_flow_suspend";
	
	public static final String START_NODE = "start";
	public static final String END_NODE = "end";
	
	public static final String AUTO_ACTIVITY_NODE = "a";
	public static final String TASK_ACTIVITY_NODE = "aaaabbb";
	public static final String EVENT_ACTIVITY_NODE = "event activity";
	public static final String WAIT_ACTIVITY_NODE = "w1";
	public static final String BINARY_ROUTER_NODE = "b1";
	public static final String INCLUSIVE_ROUTER_NODE = "r1";
	public static final String PARALLEL_ROUTER_NODE = "r1";
	public static final String SUBFLOW_ACTIVITY_NODE = "f1";
	public static final String SUBFLOW_ID = "parallel router";
	public static final String SUBFLOW_AUTO_ACTIVITY_ID_1 = "a1";
	public static final String SUBFLOW_AUTO_ACTIVITY_ID_2 = "a2";
	public static final String SUBFLOW_AUTO_ACTIVITY_ID_3 = "a3";
	
	
	public static final String CALL_BACK = "call back";
	public static final String NODE_ID = "node id";
	public static final String FLOW_TASK = "flow_task";
	
	public static final String flowCreated = "flowCreated";
	public static final String flowStarted = "flowStarted";
	public static final String flowSucceed = "flowSucceed";
	public static final String flowSuspended = "flowSuspended";
	public static final String flowResumed = "flowResumed";
	public static final String flowRestored = "flowRestored";
	
	public static final String flowFailed = "flowFailed";
	public static final String flowAborted = "flowAborted";
	
	public static final String nodeStarted = "nodeStarted";
	public static final String nodeRetried = "nodeRetried";
	public static final String nodeSucceed = "nodeSucceed";
	public static final String nodePended = "nodePended";
	public static final String nodeFailed = "nodeFailed";
	public static final String nodeRestored = "nodeRestored";
	
	public static final String eventNotifyFailed = "eventNotifyFailed";
	public static final String taskSubmitFailed = "taskSubmitFailed";
	public static final String mergeSubflowFailed = "mergeSubflowFailed";

	public static void injectSuspend(final XflowContext context) {
		context.put(INTERNAL_TASK, new Runnable() {			
			@Override
			public void run() {
				System.out.println("internal suspend");
				context.getFlow().suspend();
			}
		});
	}

	public static void injectSuspend(final XflowContext context, final String callback) {
		context.put(CALL_BACK, callback);
		context.put(FLOW_TASK, new Runnable() {			
			@Override
			public void run() {
				System.out.println("internal suspend at " + callback);
				context.getFlow().suspend();
			}
		});
	}

	public static void injectSuspend(final XflowContext context, final String callback, final String nodeId) {
		context.put(CALL_BACK, callback);
		context.put(nodeId, new Runnable() {			
			@Override
			public void run() {
				System.out.println("internal suspend at " + callback + " node: " + nodeId);
				context.getFlow().suspend();
			}
		});
	}

	public static void injectException(final XflowContext context, final Throwable e) {
		context.put(INTERNAL_TASK, new Runnable() {			
			@Override
			public void run() {
				throw (RuntimeException)e;
			}
		});
	}
	
	public static void restoreNormal(final XflowContext context) {
		context.put(INTERNAL_TASK, new Runnable() {			
			@Override
			public void run() {
			}
		});
	}
	
	public static void injectWait(final XflowContext context, final long dur) {
		context.put(INTERNAL_TASK, new Runnable() {			
			@Override
			public void run() {
				try {
					System.out.println("Sleep for " + dur);
					Thread.sleep(dur);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void call(XflowContext context) {
		if(!context.contains(INTERNAL_TASK))
			return;

		Runnable internalTask = context.get(INTERNAL_TASK);
		if(internalTask != null)
			internalTask.run();
	}
	
	public void nodeCallback(XflowContext context, String callback, String nodeId) {
		if(nodeId == null || !context.contains(nodeId))
			return;

		Runnable internalTask = context.get(nodeId);
		if(internalTask != null && callback.equals(context.get(CALL_BACK)))
			internalTask.run();
	}
	
	public void flowCallback(XflowContext context, String callback) {
		if(!context.contains(FLOW_TASK))
			return;

		Runnable internalTask = context.get(FLOW_TASK);
		if(internalTask != null && callback.equals(context.get(CALL_BACK)))
			internalTask.run();
	}

	
	public void sleep1() throws Exception {
		Thread.sleep(1);
	}

	public void sleep() throws Exception {
		Thread.sleep(5);
	}
	
	public void sleep(long dur) throws Exception {
		Thread.sleep(dur);
	}
	
	public void waitToSuspend(Xflow f) throws Exception {
		int i = 30;
		while(!f.isSuspended() && i-- > 0)
			sleep1();

//		System.out.println("sleeped: " + i);
	}

	public void waitToEnd(Xflow f) throws Exception {
		int i = 100;
		while(!f.isEnded() && i-- > 0)
			sleep1();
	}

	public void waitToActive(Xflow f, String nodeId) throws Exception {
		int i = 20;
		while(!f.isActive(nodeId) && i-- > 0)
			sleep1();
	}

	public void waitToInactive(Xflow f, String nodeId) throws Exception {
		int i = 20;
		while(f.isActive(nodeId) && i-- > 0)
			sleep1();
	}

	public void waitToFail(Xflow f, String nodeId) throws Exception {
		int i = 20;
		while(!f.isFailed(nodeId) && i-- > 0)
			sleep1();
	}
}
