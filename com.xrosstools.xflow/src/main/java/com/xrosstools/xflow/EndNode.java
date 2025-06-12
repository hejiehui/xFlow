package com.xrosstools.xflow;

public class EndNode extends Node {
	public EndNode(String name) {
		super(name);
	}

	public void handle(ActiveToken token) {
		token.getContext().getFlow().succeed();
	}
}
