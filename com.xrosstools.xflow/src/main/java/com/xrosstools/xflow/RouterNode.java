package com.xrosstools.xflow;

import java.util.ArrayList;
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
	
	public RouterNode(String id, Object configurable) {
		super(id, configurable);
	}
	
	/**
	 * @return if current route node produces multiple concurrent active tokens
	 */
	public abstract boolean isDispatcher();
	
	/**
	 * @return if current route node combines multiple concurrent active tokens
	 */
	public abstract boolean isMerger();

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
		if(!isDispatcher())
			return;

		for(Link link: getOutputs()) {
			RouterNode next = findNext(link);
			if(next != null) {
				next.visit(this, link.getId());
			}
		}
	}
	
	public void visit(RouterNode sourceNode, String routeId) {
		if(this == sourceNode)
			return;

		if(isMerger()) {
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
				Set<String> clearedNodes = new HashSet<String>();
				clearedNodes.add(getId());
				clearDownstream(sourceId, clearedNodes);
				return;
			}
		}

		for(Link link: getOutputs()) {
			RouterNode next = findNext(link);
			if(next != null) {
				next.visit(sourceNode, routeId);
			}
		}
	}
	
	private void clearDownstream(String sourceId, Set<String> clearedNodes) {
		for(Link link: getOutputs()) {
			RouterNode next = findNext(link);
			
			if(next == null || next.getId() == sourceId || clearedNodes.contains(next.getId())) 
				continue;

			if(next.isMerger() && next.sourceRouteInfoMap.containsKey(sourceId))
				next.sourceRouteInfoMap.remove(sourceId);

			next.clearDownstream(sourceId, clearedNodes);
			clearedNodes.add(next.getId());
		}
	}

	private RouterNode findNext(Link link) {
		Node next = link.getTarget();
		while(next != null && !(next instanceof RouterNode))
			next = next.getOutputs().length == 0 ? null : next.getOutputs()[0].getTarget();
		return (RouterNode)next;
	}
	
	public boolean isMerged(ActiveToken token) {
		List<RouteToken> routeTokens = token.getRouteTokens();
		if(routeTokens.isEmpty())
			return true;

		List<RouteToken> mergedTokens = new ArrayList<>();
		boolean merged = true;
		for(RouteToken routeToken: routeTokens) {
			if(reach(routeToken)) {
				RouteInfo info = sourceRouteInfoMap.get(routeToken.getRouteResult().getRouterId());
				if(info.isEnd)
					mergedTokens.add(routeToken);
			}else
				merged = false;
		}
		
		for(RouteToken mergedToken: mergedTokens)
			routeTokens.remove(mergedToken);
		
		//If all route are merged
		return merged;
	}
	
	public boolean reach(RouteToken routeToken) {
		RouteResult result = routeToken.getRouteResult();
		if(!sourceRouteInfoMap.containsKey(result.getRouterId()))
			throw new IllegalArgumentException(String.format("Can not find router: %s", result.getRouterId()));

		RouteInfo info = sourceRouteInfoMap.get(result.getRouterId());

		Set<String> mergedRoutes = routeResultMap.get(result);
		if(mergedRoutes == null) {
			mergedRoutes = new CopyOnWriteArraySet<>();
			routeResultMap.put(result, mergedRoutes);
		}

		mergedRoutes.addAll(routeToken.getMergedRoutes());
		
		//Check if all route reached current router
		for(String route: result.getRoutes()) {
			//There is still incoming route
			if(info.routes.contains(route) && !mergedRoutes.contains(route))
				return false;
		}
		
		routeToken.setMergedRoutes(mergedRoutes);
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
	
	public static String initRouterNode(List<Node> nodes) {
		List<RouterNode> routerNodes = new ArrayList<>();

		for(Node node: nodes) {
			if(node instanceof RouterNode) {
				routerNodes.add((RouterNode)node);
			}
		}
		
		for(RouterNode node: routerNodes) {
			node.visit();
		}
		
		for(RouterNode node: routerNodes) {
			node.checkEnd();
		}
		
		StringBuilder sb = new StringBuilder();
		for(RouterNode node: routerNodes) {
			sb.append(node.routeInfo());
		}
		
		return sb.toString();
	}

	public String routeInfo() {
		StringBuilder sb = new StringBuilder();
		if(sourceRouteInfoMap.isEmpty())
			return sb.toString();

		sb.append("Merge router id: " + getId() + "\n");
		for(String id: sourceRouteInfoMap.keySet()) {
			RouteInfo info = sourceRouteInfoMap.get(id);
			sb.append("\tRouter id: " + id + "\n");
			sb.append("\t[");
			for(String route: info.routes) {
				sb.append(route + " ");
			}
			
			sb.append(info.isEnd ? "end" : "");
			sb.append("]\n");
		}
		
		return sb.toString();
	}
}
