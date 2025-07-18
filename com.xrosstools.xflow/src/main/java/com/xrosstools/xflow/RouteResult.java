package com.xrosstools.xflow;

import java.util.HashSet;
import java.util.Set;

public class RouteResult {
	private String routerId;
	private Set<String> routes = new HashSet<>();

	public RouteResult(String routerId, String[] routes) {
		this.routerId = routerId;
		for(String route: routes)
			this.routes.add(route);
	}

	public String getRouterId() {
		return routerId;
	}

	public Set<String> getRoutes() {
		return routes;
	}
	
	public RouteToken createToken(String route) {
		return new RouteToken(this, route);
	}
}
