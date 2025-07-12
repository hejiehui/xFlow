package com.xrosstools.xflow;

import java.util.concurrent.atomic.AtomicInteger;

public class RouteToken {
	private String routerId;
	private AtomicInteger counter = new AtomicInteger();

	public RouteToken(String routerId, int count) {
		this.routerId = routerId;
		counter.set(count);
	}

	public String getRouterId() {
		return routerId;
	}

	public int reach() {
		return counter.decrementAndGet();
	}
	
	public int getCount() {
		return counter.get();
	}
}
