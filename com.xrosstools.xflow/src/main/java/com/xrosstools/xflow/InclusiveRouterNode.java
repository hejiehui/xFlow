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
		super(name);
		this.router = router;
	}

	public boolean isSinglePhased() {
		return true;
	}

	public boolean isSource() {
		return router != null;
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
		if(!checkInput(token))
			return Collections.emptyList();

		if(router == null) {
			return next(token);
		}
		
		String[] ids = router.route(token.getContext());
		return getNextTokens(token, ids);
	}
}
