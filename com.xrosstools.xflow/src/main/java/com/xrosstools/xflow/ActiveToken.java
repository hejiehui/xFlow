package com.xrosstools.xflow;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Shall we always keep one running task whenever possible?
 * When current task finished, we calculate tasks next, then we take the first auto task and execute it. 
 * The rest tasks should be scheduled by xflow again
 */
public class ActiveToken implements Runnable {
	private XflowContext context;

	private Node node;
	//We can use Stack instead, because there is no concurrent case
	private List<RouteToken> routeTokens;
	
	private AtomicReference<Throwable> failureRef = new AtomicReference<>();
	
	public ActiveToken(XflowContext context, Node node) {
		this.context = context;
		this.setNode(node);
		routeTokens = new CopyOnWriteArrayList<>();
	}

	public ActiveToken(XflowContext context, Node node, Collection<RouteToken> routes) {
		this(context, node);
		routeTokens = new CopyOnWriteArrayList<>(routes);
	}
	
	public List<RouteToken> getRouteTokens() {
		return routeTokens;
	}

	public void participate(RouteToken routeToken) {
		routeTokens.add(routeToken);
	}

	public void clearFailure() {
		setFailure(null);
	}

	public void setFailure(Throwable e) {
		failureRef.set(e);
	}

	public Throwable getFailure() {
		return failureRef.get();
	}

	@Override
	public void run() {
		Xflow flow = context.getFlow();

		do {
			if(flow.isEnded())
				return;

			if(flow.isSuspended()) {
				flow.pending(this);
				return;
			}
		} while(!node.start(this));

		List<ActiveToken> nextTokens = node.handle(node, node.isSinglePhased());

		if(node.isFailed())
			return;

		if(!node.isSinglePhased())
			return;

		if(flow.isEnded())
			return;
			
		if(flow.isSuspended()) {
			flow.pending(nextTokens);
			return;
		}
		
		XflowEngine.submit(nextTokens);
//
//		//Check fail after token executed
//		flow.isFailed();
	}

	public XflowContext getContext() {
		return context;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public ActiveToken next(Node node) {
		return new ActiveToken(context, node, routeTokens);
	}
	
	public ActiveToken next(Node node, RouteToken routeToken) {
		ActiveToken activeToken = new ActiveToken(context, node, routeTokens);
		activeToken.participate(routeToken);		
		return activeToken;
	}

	public void submit(Node node) {
		if(node != null)
			XflowEngine.submit(new ActiveToken(context, node, routeTokens));
	}
}
