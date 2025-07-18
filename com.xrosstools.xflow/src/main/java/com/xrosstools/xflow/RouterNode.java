package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class RouterNode extends Node {
	private static class RouteInfo {
		Set<String> routes = new HashSet<>();
		boolean isEnd = false;
	}
	
	//Read only map
	private Map<String, RouteInfo> sourceRouteInfoMap = new HashMap<>();
	
	private Map<RouteResult, Set<String>> routeResultMap = new ConcurrentHashMap<>();
	
	private Map<String, Link> linkMap = new HashMap<>();

	public RouterNode(String id) {
		super(id);
	}
	
	public abstract boolean isSource();

	public void checkEnd() {
		//End of visit
		if(getOutputs().length == 0 || getOutputs().length ==1 && findNext(getOutputs()[0]) == null) {
			//Make end
			for(RouteInfo info: sourceRouteInfoMap.values())
				info.isEnd = true;
		}
	}
	
	public Map<String, Link> getLinkMap() {
		return linkMap;
	}


	public Link getLink(String name) {
		return linkMap.get(name);
	}
	
	public void initLinkMap(Link[] outputs) {
		for(Link link: outputs) {
			String linkName = link.getId();
			if(linkMap.containsKey(linkName))
				throw new IllegalArgumentException(String.format("Linke id: \"%s\" is duplicated", linkName));

			linkMap.put(linkName, link);
		}
	}

	public void visit() {
		if(!isSource())
			return;

		for(Link link: getOutputs()) {
			RouterNode next = findNext(link);
			if(next != null) {
				next.visit(this, link.getId());
			}
		}
	}
	
	public void visit(RouterNode sourceNode, String routeId) {
		String sourceId = sourceNode.getId();
		RouteInfo info = sourceRouteInfoMap.get(sourceId);
		if(info == null) {
			info = new RouteInfo();
			sourceRouteInfoMap.put(sourceId, info);
		}
		
		//Already visited
		if(info.routes.contains(routeId))
			return;

		info.routes.add(routeId);
		
		//All routes merged
		if(sourceNode.getOutputs().length == info.routes.size()) {
			info.isEnd = true;
			//Clear for ended router
			clearDownstream(sourceId);
		} else {
			for(Link link: getOutputs()) {
				RouterNode next = findNext(link);
				if(next != null) {
					next.visit(sourceNode, routeId);
				}
			}
		}
	}
	
	private void clearDownstream(String sourceId) {
		for(Link link: getOutputs()) {
			RouterNode next = findNext(link);
			if(next != null && next.sourceRouteInfoMap.containsKey(sourceId)) {
				next.sourceRouteInfoMap.remove(sourceId);
				next.clearDownstream(sourceId);
			}
		}
	}

	private RouterNode findNext(Link link) {
		Node next = link.getTarget();
		while(next != null && !(next instanceof RouterNode))
			next = next.getOutputs().length == 0 ? null : next.getOutputs()[0].getTarget();
		return (RouterNode)next;
	}
	
	public boolean checkInput(ActiveToken token) {
		Deque<RouteToken> routeTokens = token.getRouteTokens();
		if(routeTokens.isEmpty())
			return true;

		RouteToken routeToken = routeTokens.getLast();
		
		if(!reach(routeToken))
			return false;
			
		RouteInfo info = sourceRouteInfoMap.get(routeToken.getRouteResult().getRouterId());
		if(info.isEnd)
			routeTokens.removeLast();

		return true;
	}
	
	public boolean reach(RouteToken routeToken) {
		RouteResult result = routeToken.getRouteResult();
		if(!sourceRouteInfoMap.containsKey(result.getRouterId()))
			throw new IllegalArgumentException(String.format("Can not find router: %s", result.getRouterId()));

		RouteInfo info = sourceRouteInfoMap.get(result.getRouterId());

		Set<String> reachedRoutes = routeResultMap.get(result);
		if(reachedRoutes == null) {
			reachedRoutes = new CopyOnWriteArraySet<>();
			routeResultMap.put(result, reachedRoutes);
		}

		reachedRoutes.addAll(routeToken.getReachedRoutes());
		
		//Check if all route reached current router
		for(String route: result.getRoutes()) {
			//There is still incoming route
			if(info.routes.contains(route) && !reachedRoutes.contains(route))
				return false;
		}
		
		routeToken.setReachedRoutes(reachedRoutes);
		routeResultMap.remove(result);

		return true;
	}
	
	public List<ActiveToken> getNextTokens(ActiveToken token, String[] ids) {
		RouteResult result = new RouteResult(getId(), ids);

		List<ActiveToken> nextTokens = new ArrayList<>(ids.length);
		for(String id: ids) {
			Link link = getLink(id);
			nextTokens.add(token.next(link.getTarget(), result.createToken(id)));
		}
		return nextTokens;	
	}
	
	public void displayRouteInfo() {
		System.out.println("Current router id: " + getId());
		for(String id: sourceRouteInfoMap.keySet()) {
			RouteInfo info = sourceRouteInfoMap.get(id);
			System.out.println("\tRouter id: " + id);
			System.out.print("\t[");
			for(String route: info.routes) {
				System.out.print(route + " ");
			}
			System.out.println("]");
		}
	}
}
