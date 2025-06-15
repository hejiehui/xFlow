package com.xrosstools.xflow;

public class SubflowActivityNode extends Node {
	private String subflowId;
	private SubflowActivity activity;
	private XflowFactory factory;

	public SubflowActivityNode(String id, String subflowId, SubflowActivity activity, XflowFactory factory) {
		super(id);
		this.subflowId = subflowId;
		this.activity = activity;
		this.factory = factory;
	}

	@Override
	public void handle(ActiveToken token) {
		Xflow subFlow = factory.create(subflowId);

		XflowContext subFlowContext = activity.createContext(token.getContext());
		subFlowContext.setFlow(subFlow);
		subFlowContext.setParentToken(token);
		
		subFlow.start(subFlowContext);
	}
	
	public void finish(XflowContext subflowContext) {
		ActiveToken token = subflowContext.getParentToken();
		activity.mergeContext(subflowContext.getParentToken().getContext(), subflowContext);
		
		if(getOutputs().length == 0)
			return;

		token.submit(getOutputs()[0].getTarget());
	}	
}
