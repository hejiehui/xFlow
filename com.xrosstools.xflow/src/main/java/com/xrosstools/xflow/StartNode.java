package com.xrosstools.xflow;

import java.util.List;

public class StartNode extends Node {
	public StartNode(String name) {
		super(name);
	}
	
	public boolean isSinglePhased() {
		return true;
	}

	public List<ActiveToken> handle(ActiveToken token) {
		return next(token);
	}
}
