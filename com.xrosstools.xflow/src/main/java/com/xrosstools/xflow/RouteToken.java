package com.xrosstools.xflow;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class RouteToken {
	private RouteResult routerResult;
	private Set<String> mergedRoutes = new CopyOnWriteArraySet<>();

	public RouteToken(RouteResult routerResult, String route) {
		this.routerResult = routerResult;
		this.mergedRoutes.add(route);
	}
	
	public RouteToken(RouteResult routerResult, Set<String> mergedRoutes) {
		this.routerResult = routerResult;
		this.mergedRoutes = mergedRoutes;
	}

	public RouteResult getRouteResult() {
		return routerResult;
	}

	public Set<String> getMergedRoutes() {
		return mergedRoutes;
	}

	public void setMergedRoutes(Set<String> mergedRoutes) {
		this.mergedRoutes = mergedRoutes;
	}
}
