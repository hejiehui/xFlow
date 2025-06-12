package com.xrosstools.xflow;

public abstract class Node {
	private final static Link[] EMPTY = new Link[0];
	private Object configurable;
	private String id;
	private Link[] outputs;
	
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
	
	public abstract void handle(ActiveToken token);
}
