package com.xrosstools.xflow;

import java.util.concurrent.atomic.AtomicInteger;

public class RouteToken {
	//TODO change to node index
	private Node router;
	private AtomicInteger counter = new AtomicInteger();

	public RouteToken(Node router, int count) {
		this.router = router;
		counter.set(count);
	}

	public Node getRouter() {
		return router;
	}

	public int reach() {
		return counter.decrementAndGet();
	}
}
