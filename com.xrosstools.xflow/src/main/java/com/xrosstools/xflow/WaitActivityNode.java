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
		XflowEngine.schedule(new TimeoutTask(token, next(token)), count, unit);
		return Collections.emptyList();
	}
	
	private static class TimeoutTask implements Runnable {
		private ActiveToken token;
		private List<ActiveToken> nextTokens;
		private TimeoutTask(ActiveToken token, List<ActiveToken> nextTokens) {
			this.token = token;
			this.nextTokens= nextTokens;
		}

		@Override
		public void run() {
			token.getNode().succeed(nextTokens);
		}
	}
}
