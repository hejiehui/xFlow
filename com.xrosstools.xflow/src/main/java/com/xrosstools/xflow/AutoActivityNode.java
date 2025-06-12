package com.xrosstools.xflow;

public class AutoActivityNode extends Node {
	private AutoActivity activity;

	public AutoActivityNode(String id, AutoActivity activity) {
		super(id, activity);
		this.activity= activity;
	}

	public void handle(ActiveToken token) {
		activity.execute(token.getContext());
		if(getOutputs().length == 0)
			return;

		token.submit(getOutputs()[0].getTarget());
	}
}
