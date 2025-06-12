package com.xrosstools.xflow;

import java.util.HashMap;
import java.util.Map;

public class ExclusiveRouterNode extends Node {
	private ExclusiveRouter router;
	private Map<String, Link> linkMap = new HashMap<>();

	public ExclusiveRouterNode(String name, ExclusiveRouter router) {
		super(name);
		this.router = router;
	}
	
	public void setOutputs(Link[] outputs) {
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
		String id = router.route(token.getContext());
		Link link = getLink(id);
		token.submit(link.getTarget());
	}
}
