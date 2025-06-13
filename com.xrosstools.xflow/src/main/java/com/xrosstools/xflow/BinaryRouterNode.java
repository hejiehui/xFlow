package com.xrosstools.xflow;

import java.util.HashMap;
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

	public void handle(ActiveToken token) {
		if(getOutputs() == null)
			return;

		if(router == null) {
			token.submit(getOutputs()[0].getTarget());
			return;
		}

		Boolean id = router.route(token.getContext());
		Link link = getLink(id);
		token.submit(link.getTarget());
	}
}
