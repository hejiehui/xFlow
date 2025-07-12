package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.Collections;
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

	public boolean isSinglePhased() {
		return true;
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

	public List<ActiveToken> handle(ActiveToken token) {
		if(!token.checkInput())
			return Collections.emptyList();

		if(router == null) {
			return next(token);
		}
		
		String[] ids = router.route(token.getContext());
		RouteToken rt = new RouteToken(this.getId(), ids.length);
		List<ActiveToken> nextTokens = new ArrayList<>(ids.length);
		for(String id: ids) {
			Link link = getLink(id);
			nextTokens.add(token.next(link.getTarget(), rt));
		}
		return nextTokens;
	}
}
