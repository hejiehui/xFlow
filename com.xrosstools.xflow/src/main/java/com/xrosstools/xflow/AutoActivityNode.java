package com.xrosstools.xflow;

import java.util.List;

public class AutoActivityNode extends Node {
	private AutoActivity activity;

	public AutoActivityNode(String id, AutoActivity activity) {
		super(id, activity);
		this.activity= activity;
	}
	
	public boolean isSinglePhased() {
		return true;
	}

	public List<ActiveToken> handle(ActiveToken token) {
		activity.execute(token.getContext());
		return next(token);
	}
}
