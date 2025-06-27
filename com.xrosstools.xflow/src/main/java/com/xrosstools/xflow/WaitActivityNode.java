package com.xrosstools.xflow;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaitActivityNode extends Node {
	private int count;
	private TimeUnit unit;
	
	public WaitActivityNode(String name, int count, TimeUnit unit) {
		super(name);
		this.count = count;
		this.unit = unit;
	}

	public boolean isSinglePhased() {
		return false;
	}

	public List<ActiveToken> handle(ActiveToken token) {
		if(getOutputs().length == 0)
			XflowEngine.schedule(new TimeoutTask(token, null), count, unit);
		else
			XflowEngine.schedule(new TimeoutTask(token, getOutputs()[0].getTarget()), count, unit);

		return Collections.emptyList();
	}
	
	private static class TimeoutTask implements Runnable {
		private ActiveToken token;
		private Node next;
		private TimeoutTask(ActiveToken token, Node next) {
			this.token = token;
			this.next = next;
		}

		@Override
		public void run() {
			token.getNode().succeed();
			token.submit(next);
		}
	}
}
