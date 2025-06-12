package com.xrosstools.xflow;

public class StartNode extends Node {
	public StartNode(String name) {
		super(name);
	}
	
	public void handle(ActiveToken token) {
		if(getOutputs().length == 0)
			return;

		token.submit(getOutputs()[0].getTarget());
	}
}
