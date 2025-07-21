package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InclusiveRouterNode extends RouterNode {
	private String[] defaultOutputs;
	private InclusiveRouter router;

	public InclusiveRouterNode(String name) {
		this(name, null);
	}
			
	public InclusiveRouterNode(String name, InclusiveRouter router) {
		super(name, router);
		this.router = router;
	}

	public boolean isSinglePhased() {
		return true;
	}

	public boolean isDispatcher() {
		return router != null;
	}

	public boolean isMerger() {
		return getInputCount() > 1;
	}

	public void setOutputs(Link[] outputs) {
		if(router == null && outputs.length > 1)
			throw new IllegalArgumentException("Router implementation is reqired for node: " + getId());
			
		super.setOutputs(outputs);
		initLinkMap(outputs);
		List<String> defaultOutputList = new ArrayList<>();
		for(Link link: outputs) {
			if(link.isDefaultLink())
				defaultOutputList.add(link.getId());
		}
		
		defaultOutputs = defaultOutputList.toArray(new String[defaultOutputList.size()]);
	}
	
	public String[] getDefaultOutputs() {
		return defaultOutputs.clone();
	}

	public List<ActiveToken> handle(ActiveToken token) {
		if(isMerger())
			if(!isMerged(token))
				return Collections.emptyList();

		return isDispatcher() ? getNextTokens(token, router.route(token.getContext())) : next(token);
	}
}
