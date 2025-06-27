package com.xrosstools.xflow;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinaryRouterNode extends Node {
	private BinaryRouter router;
	private Map<Boolean, Link> linkMap = new HashMap<>();

	public BinaryRouterNode(String name) {
		this(name, null);
	}

	public BinaryRouterNode(String name, BinaryRouter router) {
		super(name);
		this.router = router;
	}
	
	public boolean isSinglePhased() {
		return true;
	}

	public void setOutputs(Link[] outputs) {
		if(router == null && outputs.length == 2)
			throw new IllegalArgumentException("Router implementation is reqired for node: " + getId());
			
		super.setOutputs(outputs);
		for(Link link: outputs) {
			Boolean linkName = link.isTrueLink();
			if(linkMap.containsKey(linkName))
				throw new IllegalArgumentException(String.format("Linke id: \"%s\" is duplicated", linkName));

			linkMap.put(linkName, link);
		}
	}

	public Link getLink(boolean value) {
		return linkMap.get(value);
	}

	public List<ActiveToken> handle(ActiveToken token) {
		if(router == null) {
			return next(token);
		}

		Boolean id = router.route(token.getContext());
		Link link = getLink(id);

		if(link == null)
			throw new IllegalArgumentException(String.format("Linke id: \"%s\" is undefined", id));

		return next(token, link.getTarget());
	}
}
