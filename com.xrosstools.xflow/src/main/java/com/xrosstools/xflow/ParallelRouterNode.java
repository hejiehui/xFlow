package com.xrosstools.xflow;

import java.util.Collections;
import java.util.List;

public class ParallelRouterNode extends RouterNode {
	private String[] ids;

	public ParallelRouterNode(String name) {
		super(name);
	}

	public boolean isSinglePhased() {
		return true;
	}

	public boolean isDispatcher() {
		return getOutputs().length > 1;
	}
	
	public boolean isMerger() {
		return getInputCount() > 1;
	}
	
	public void setOutputs(Link[] outputs) {
		super.setOutputs(outputs);
		initLinkMap(outputs);
		ids = getLinkMap().keySet().toArray(new String[outputs.length]);
	}

	public List<ActiveToken> handle(ActiveToken token) {
		if(isMerger())
			if(!isMerged(token))
				return Collections.emptyList();

		return isDispatcher() ? getNextTokens(token, ids) : next(token);
	}
}
