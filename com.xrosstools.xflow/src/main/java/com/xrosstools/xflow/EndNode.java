package com.xrosstools.xflow;

public class EndNode extends Node {
	public EndNode(String name) {
		super(name);
	}

	public void handle(ActiveToken token) {
		token.getContext().getFlow().succeed();
		
		ActiveToken parentToken = token.getContext().getParentToken();
		if(parentToken == null)
			return;

		SubflowActivityNode parentNode = (SubflowActivityNode)parentToken.getNode();
		parentNode.finish(token.getContext()); 
	}
}
