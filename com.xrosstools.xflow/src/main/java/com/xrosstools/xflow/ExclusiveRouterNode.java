package com.xrosstools.xflow;

import java.util.HashMap;
import java.util.Map;

public class ExclusiveRouterNode extends Node {
	private ExclusiveRouter router;
	private Map<String, Link> linkMap = new HashMap<>();

	public ExclusiveRouterNode(String name) {
		this(name, null);
	}

	public ExclusiveRouterNode(String name, ExclusiveRouter router) {
		super(name);
		this.router = router;
	}
	
	public void setOutputs(Link[] outputs) {
		if(router == null && outputs.length > 1)
			throw new IllegalArgumentException("Router implementation is reqired for node: " + getId());
			
		super.setOutputs(outputs);
		for(Link link: outputs) {
			String linkName = link.getId();
			if(linkMap.containsKey(linkName))
				throw new IllegalArgumentException(String.format("Linke id: \"%s\" is duplicated", linkName));

			linkMap.put(linkName, link);
		}
	}
	
	public Link getLink(String name) {
		return linkMap.get(name);
	}

	public void handle(ActiveToken token) {
		if(getOutputs() == null)
			return;

		if(router == null) {
			token.submit(getOutputs()[0].getTarget());
			return;
		}

		String id = router.route(token.getContext());
		Link link = getLink(id);
		
		if(link == null)
			throw new IllegalArgumentException(String.format("Linke id: \"%s\" is undefined", id));
		
		token.submit(link.getTarget());
	}
}
