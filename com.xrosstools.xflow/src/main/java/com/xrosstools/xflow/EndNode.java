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
		ActiveToken parentToken = token.getContext().getParentToken();

		if(parentToken != null) {
			String parentNodeId = parentToken.getNode().getId();
			parentToken.getContext().getFlow().mergeSubflow(parentNodeId);
		}

		token.getContext().getFlow().succeed();
		return Collections.emptyList();
	}
}
