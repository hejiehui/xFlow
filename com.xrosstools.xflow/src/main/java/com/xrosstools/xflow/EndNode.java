package com.xrosstools.xflow;

import java.util.Collections;
import java.util.List;

public class EndNode extends Node {
	public EndNode(String name) {
		super(name);
	}

	public boolean isSinglePhased() {
		return true;
	}

	public List<ActiveToken> handle(ActiveToken token) {
		token.getContext().getFlow().succeed();
		
		ActiveToken parentToken = token.getContext().getParentToken();
		if(parentToken == null)
			return Collections.emptyList();

		SubflowActivityNode parentNode = (SubflowActivityNode)parentToken.getNode();
		parentNode.mergeSubflow();
		
		return Collections.emptyList();
	}
}
