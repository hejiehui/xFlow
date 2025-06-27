package com.xrosstools.xflow;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SubflowActivityNode extends Node {
	private String subflowId;
	private SubflowActivity activity;
	private XflowFactory factory;
	private AtomicReference<XflowContext> subflowContextRef = new AtomicReference<>();

	public SubflowActivityNode(String id, String subflowId, SubflowActivity activity, XflowFactory factory) {
		super(id);
		this.subflowId = subflowId;
		this.activity = activity;
		this.factory = factory;
	}

	public boolean isSinglePhased() {
		return false;
	}
	
	public XflowContext getSubflowContext() {
		return subflowContextRef.get();
	}

	@Override
	public List<ActiveToken> handle(ActiveToken token) {
		Xflow subFlow = factory.create(subflowId);

		XflowContext subflowContext = activity.createContext(token.getContext());
		subflowContext.setFlow(subFlow);
		subflowContext.setParentToken(token);

		subFlow.start(subflowContext);

		subflowContextRef.set(subflowContext);
		return Collections.emptyList();
	}

	public void mergeSubflow() {
		assertMerge();

		synchronized (subflowContextRef) {
			assertMerge();

			ActiveToken token = getToken();
			token.clearFailure();
			XflowContext parentContext = token.getContext();
			XflowContext subflowContext = subflowContextRef.get();
			ActiveToken next = this.singleNext(getToken());

			try {
				activity.mergeSubflow(parentContext, subflowContext);
				succeed();
			} catch (Exception e) {
				getListener().mergeSubflowFailed(parentContext, subflowId, subflowContext, e);
				failed(e);
				throw e;
			}
			subflowContextRef.set(null);
			XflowEngine.submit(next);
		}
	}
	
	private void assertMerge() {
		assertToken();

		if(subflowContextRef.get() == null)
			throw new IllegalStateException(String.format("No subflow context found for node: ", subflowId));
	}
}
