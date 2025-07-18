package com.xrosstools.xflow;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class RouteToken {
	private RouteResult routerResult;
	private Set<String> reachedRoutes = new CopyOnWriteArraySet<>();

	public RouteToken(RouteResult routerResult, String route) {
		this.routerResult = routerResult;
		this.reachedRoutes.add(route);
	}

	public RouteResult getRouteResult() {
		return routerResult;
	}

	public Set<String> getReachedRoutes() {
		return reachedRoutes;
	}

	public void setReachedRoutes(Set<String> reachedRoutes) {
		this.reachedRoutes = reachedRoutes;
	}
}
