package com.xrosstools.xflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExclusiveRouterNode extends RouterNode {
	private ExclusiveRouter router;
	private Map<String, Link> linkMap = new HashMap<>();

	public ExclusiveRouterNode(String name) {
		this(name, null);
	}

	public ExclusiveRouterNode(String name, ExclusiveRouter router) {
		super(name, router);
		this.router = router;
	}
	
	public boolean isSinglePhased() {
		return true;
	}

	public boolean isDispatcher() {
		return false;
	}

	public boolean isMerger() {
		return false;
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

	public List<ActiveToken> handle(ActiveToken token) {
		if(router == null)
			return next(token);

		String id = router.route(token.getContext());
		Link link = getLink(id);
		
		if(link == null)
			throw new IllegalArgumentException(String.format("Linke id: \"%s\" is undefined", id));
		
		return next(token, link.getTarget());
	}
}
