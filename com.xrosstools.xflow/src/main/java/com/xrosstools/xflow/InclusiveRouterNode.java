package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InclusiveRouterNode extends Node {
	private String[] defaultOutputs;
	private InclusiveRouter router;
	private Map<String, Link> linkMap = new HashMap<>();

	public InclusiveRouterNode(String name) {
		this(name, null);
	}
			
	public InclusiveRouterNode(String name, InclusiveRouter router) {
		super(name);
		this.router = router;
	}

	public void setOutputs(Link[] outputs) {
		if(router == null && outputs.length > 1)
			throw new IllegalArgumentException("Router implementation is reqired for node: " + getId());
			
		super.setOutputs(outputs);
		List<String> defaultOutputList = new ArrayList<>();
		for(Link link: outputs) {
			String linkName = link.getId();
			if(linkMap.containsKey(linkName))
				throw new IllegalArgumentException(String.format("Linke id: \"%s\" is duplicated", linkName));

			linkMap.put(linkName, link);
			if(link.isDefaultLink())
				defaultOutputList.add(link.getId());
		}
		
		defaultOutputs = defaultOutputList.toArray(new String[defaultOutputList.size()]);
	}
	
	public Link getLink(String name) {
		return linkMap.get(name);
	}

	public String[] getDefaultOutputs() {
		return defaultOutputs.clone();
	}

	public void handle(ActiveToken token) {
		if(!token.checkInput())
			return;

		if(getOutputs() == null)
			return;

		if(router == null) {
			token.submit(getOutputs()[0].getTarget());
			return;
		}
		
		String[] ids = router.route(token.getContext());
		RouteToken rt = new RouteToken(this, ids.length);
		for(String id: ids) {
			Link link = getLink(id);
			token.submit(link.getTarget(), rt);
		}
	}
}
