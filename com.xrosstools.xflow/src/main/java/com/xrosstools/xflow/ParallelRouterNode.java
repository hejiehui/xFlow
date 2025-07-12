package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParallelRouterNode extends Node {
	public ParallelRouterNode(String name) {
		super(name);
	}

	public boolean isSinglePhased() {
		return true;
	}

	public List<ActiveToken> handle(ActiveToken token) {
		if(!token.checkInput())
			return Collections.emptyList();

		if(getOutputs().length == 0)
			return Collections.emptyList();

		RouteToken rt = new RouteToken(this.getId(), getOutputs().length);
		List<ActiveToken> nextTokens = new ArrayList<>(getOutputs().length);
		for(Link link: getOutputs()) {
			nextTokens.add(token.next(link.getTarget(), rt));
		}

		return nextTokens;
	}
}
