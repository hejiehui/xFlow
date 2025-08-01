package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Node implements NodeHandler {
	private final static Link[] EMPTY = new Link[0];
	private Object configurable;
	private String id;
	private int inputCount;
	private Link[] outputs = EMPTY;
	private AtomicReference<ActiveToken> tokenRef = new AtomicReference<>();
	private XflowListener listener;
	
	public abstract boolean isSinglePhased();
	
	public Node(String id) {
		this(id, null);
	}
	
	public Node(String id, Object configurable) {
		this.id = id;
		this.configurable = configurable;
		this.outputs = EMPTY;
	}

	public String getId() {
		return id;
	}
	
	public int getInputCount() {
		return inputCount;
	}

	public Node addInputCount() {
		inputCount++;
		return this;
	}

	public boolean isActive() {
		return getToken() != null;
	}
	
	public boolean isFailed() {
		ActiveToken token = getToken();
		return token != null && token.getFailure() != null;
	}
	
	public Throwable getFailure() {
		ActiveToken token = getToken();
		return token == null ? null : token.getFailure();
	}
	
	public XflowListener getListener() {
		return listener;
	}

	public void setListener(XflowListener listener) {
		this.listener = listener;
	}

	public ActiveToken getToken() {
		return tokenRef.get();
	}

	/**
	 * Make sure only one token runs at a time
	 * @param token active token that will execute current node
	 * @return true if token get the permit
	 */
	public boolean start(ActiveToken token) {
		//When node fails, the token will not be released
		if(token == tokenRef.get())
			return true;

		if(!tokenRef.compareAndSet(null, token))
			return false;
		
		try {
			listener.nodeStarted(id, token.getContext());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return true;
	}
	
	public void restore(ActiveToken token) {
		tokenRef.set(token);
		
		try {
			listener.nodeRestored(id, token.getContext());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void releaseToken() {
		tokenRef.set(null);
	}
	
	public void assertToken() {
		if(getToken() == null)
			throw new IllegalStateException(String.format("Active token is reqired for %s", id));
	}

	public void setOutputs(Link[] outputs) {
		this.outputs = outputs == null ? EMPTY : outputs.clone();
	}

	public Link[] getOutputs() {
		return outputs.clone();
	}

	public void initNodeConfig(DataMap config) {
		if(configurable != null && configurable instanceof NodeConfigAware)
			((NodeConfigAware)configurable).initNodeConfig(config);
	}

	public void initFlowConfig(DataMap config) {
		if(configurable != null && configurable instanceof FlowConfigAware)
			((FlowConfigAware)configurable).initFlowConfig(config);
	}

	public void initGlobalConfig(DataMap config) {
		if(configurable != null && configurable instanceof GlobalConfigAware)
			((GlobalConfigAware)configurable).initGlobalConfig(config);
	}
	
	public void succeed(List<ActiveToken> nextTokens) {
		ActiveToken token = getToken();
		Xflow flow = token.getContext().getFlow();

		token.setFailure(null);
		
		releaseToken();
		try {
			listener.nodeSucceed(id, token.getContext());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		//tok and check
		if(flow.isEnded())
			return;
			
		if(flow.isSuspended()) {
			flow.pending(nextTokens);
			return;
		}
		
		XflowEngine.submit(nextTokens);
		flow.tok();
		flow.checkStatus();

	}
	
	public void failed(Throwable ex) {
		ActiveToken token = getToken();
		token.setFailure(ex);

		try {
			listener.nodeFailed(id, token.getContext(), ex);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public List<ActiveToken> next(ActiveToken token, Node node) {
		List<ActiveToken> nodes = new ArrayList<>(1);
		nodes.add(token.next(node));
		return nodes;
	}

	public List<ActiveToken> next(ActiveToken token) {
		if(getOutputs().length == 0)
			return Collections.emptyList();

		List<ActiveToken> nodes = new ArrayList<>(1);
		nodes.add(token.next(getOutputs()[0].getTarget()));
		return nodes;
	}
	
	public ActiveToken singleNext(ActiveToken token) {
		return getOutputs().length == 0 ? null : token.next(getOutputs()[0].getTarget());
	}
	
	public void retry() {
		assertToken();
		getToken().clearFailure();
		handle(this, isSinglePhased());
	}
	
	public void handle(NodeHandler handle, boolean isLastPhase) {
		assertToken();

		try {
			List<ActiveToken> nextTokens = handle.handle(getToken());
			if(isLastPhase)
				succeed(nextTokens);
		} catch(Throwable e) {
			failed(e);
			throw e;
		}
	}
}
