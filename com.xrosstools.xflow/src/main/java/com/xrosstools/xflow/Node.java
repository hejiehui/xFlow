package com.xrosstools.xflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.xrosstools.xflow.imp.XflowListenerAdapter;

public abstract class Node implements NodeHandler {
	private final static Link[] EMPTY = new Link[0];
	private Object configurable;
	private String id;
	private Link[] outputs = EMPTY;
	private AtomicReference<ActiveToken> tokenRef = new AtomicReference<>();
	private XflowListener listener = new XflowListenerAdapter();
	
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
	public boolean isActive() {
		return getToken() != null;
	}
	
	public boolean isFailed() {
		ActiveToken token = getToken();
		return token != null && token.getFailure() != null;
	}
	
	public Throwable getLastFailure() {
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
	 * @param token
	 * @return
	 */
	public boolean start(ActiveToken token) {
		//When node fails, the token will not be released
		if(token == tokenRef.get())
			return true;

		synchronized (tokenRef) {
			if(tokenRef.get() == null) {
				tokenRef.set(token);
				try {
					listener.nodeStarted(token.getContext(), id);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return true;
			} else
				return false;
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
	
	public void succeed() {
		ActiveToken token = getToken();
		token.setFailure(null);
		
		releaseToken();
		try {
			listener.nodeSucceed(token.getContext(), id);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void failed(Throwable ex) {
		ActiveToken token = getToken();
		token.setFailure(ex);

		try {
			listener.nodeFailed(token.getContext(), id, ex);
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
		getToken().clearFailure();
		XflowEngine.submit(handle(this, isSinglePhased()));
	}
	
	public List<ActiveToken> handle(NodeHandler handle, boolean isLastPhase) {
		assertToken();

		try {
			List<ActiveToken> nextTokens = handle.handle(getToken());
			if(isLastPhase) {
				succeed();
				return nextTokens;
			}
		} catch(Throwable e) {
			failed(e);
			throw e;
		}

		return Collections.emptyList();
	}
}
