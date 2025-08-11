package com.xrosstools.xflow;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class XflowEngine {
	private static ExecutorService executor;
	private static ScheduledExecutorService waitExecutor;

	static {
		executor = Executors.newCachedThreadPool();
		waitExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
            	shutdown();
            }
        }));
	}
	
	public static void shutdown() {
		System.out.println("Shutdown XflowEngine");
        shutdown(executor);
        shutdown(waitExecutor);
	}

	private static void shutdown(ExecutorService service) {
		service.shutdown();
        try {
            if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
            	service.shutdownNow();
            }
        } catch (InterruptedException e) {
        	service.shutdownNow();
        }            
	}

	public static void submit(XflowContext context, Node node) {
		submit(new ActiveToken(context, node));
	}
	
	public static void submit(ActiveToken activeToken) {
		if(activeToken == null)
			return;

		activeToken.getContext().getFlow().tick();
		executor.submit(activeToken);
	}
	
	public static void submit(List<ActiveToken> activeTokens) {
		for(ActiveToken token: activeTokens)
			submit(token);
	}

	public static void schedule(Runnable task, long delay, TimeUnit unit) {
		waitExecutor.schedule(task, delay, unit);
	}
}
