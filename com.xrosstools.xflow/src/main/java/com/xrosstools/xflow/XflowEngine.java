package com.xrosstools.xflow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class XflowEngine {
	private static final ExecutorService executor = Executors.newCachedThreadPool();
	private static final ScheduledExecutorService waitExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

	public static void submit(XflowContext context, Node node) {
		executor.submit(new ActiveToken(context, node));
	}
	
	public static void submit(ActiveToken activeToken) {
		executor.submit(activeToken);
	}
	
	public static void schedule(Runnable task, long delay, TimeUnit unit) {
		waitExecutor.schedule(task, delay, unit);
	}
}
