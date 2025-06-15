package com.xrosstools.xflow;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Shall we always keep one running task whenever possible?
 * When current task finished, we calculate tasks next, then we take the first auto task and execute it. 
 * The rest tasks should be scheduled by xflow again
 */
public class ActiveToken implements Runnable {
	private XflowContext context;

	private Node node;
	//We can use Stack instead, because there is no concurrent case
	private ConcurrentLinkedDeque<RouteToken> routeTokens;
	
	public ActiveToken(XflowContext context, Node node) {
		this.context = context;
		this.setNode(node);
		routeTokens = new ConcurrentLinkedDeque<>();
	}

	public ActiveToken(XflowContext context, Node node, Collection<RouteToken> routes) {
		this(context, node);
		routeTokens = new ConcurrentLinkedDeque<>(routes);
	}
	
	public void participate(RouteToken routeToken) {
		routeTokens.addLast(routeToken);
	}

	@Override
	public void run() {
		getNode().handle(this);
	}
	
	public XflowContext getContext() {
		return context;
	}

	public boolean checkInput() {
		if(routeTokens.isEmpty())
			return true;

		RouteToken routeToken = routeTokens.getLast();
		
		if(routeToken.reach() > 0)
			return false;
			
		routeTokens.removeLast();
		return true;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void submit(Node node) {
		XflowEngine.submit(new ActiveToken(context, node, routeTokens));
	}
	
	public void submit(Node node, RouteToken routeToken) {
		ActiveToken activeToken = new ActiveToken(context, node, routeTokens);
		activeToken.participate(routeToken);		
		XflowEngine.submit(activeToken);
	}
}
