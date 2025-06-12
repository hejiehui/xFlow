package com.xrosstools.xflow;

public class ParallelRouterNode extends Node {
	public ParallelRouterNode(String name) {
		super(name);
	}

	public void handle(ActiveToken token) {
		if(!token.checkInput())
			return;

		RouteToken rt = new RouteToken(this, getOutputs().length);
		for(Link link: getOutputs()) {
			token.submit(link.getTarget(), rt);
		}
	}
}
