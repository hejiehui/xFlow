package com.xrosstools.xflow;

public class XflowContext extends DataMap {
	private ActiveToken parentToken;
	private Xflow flow;

	public void setFlow(Xflow flow) {
		this.flow = flow;
	}
	
	public Xflow getFlow() {
		return flow;
	}

	public ActiveToken getParentToken() {
		return parentToken;
	}

	public void setParentToken(ActiveToken parentToken) {
		this.parentToken = parentToken;
	}
}
