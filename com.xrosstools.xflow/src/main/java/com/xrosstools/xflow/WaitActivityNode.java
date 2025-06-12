package com.xrosstools.xflow;

import java.util.concurrent.TimeUnit;

public class WaitActivityNode extends Node {
	private int count;
	private TimeUnit unit;
	public WaitActivityNode(String name, int count, TimeUnit unit) {
		super(name);
		this.count = count;
		this.unit = unit;
	}

	public void handle(ActiveToken token) {
		XflowEngine.schedule(new TimeoutTask(token.getContext(), getOutputs()[0].getTarget()), count, unit);
	}
	
	private static class TimeoutTask implements Runnable {
		private XflowContext context;
		private Node next;
		private TimeoutTask(XflowContext context, Node next) {
			this.context = context;
			this.next = next;
		}

		@Override
		public void run() {
			XflowEngine.submit(context, next);
		}
	}
}
