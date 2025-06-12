package com.xrosstools.xflow;

public class Link {
	private String id;
	private Node source;
	private Node target;
	private boolean defaultLink;
	private boolean trueLink;

	public Link(String id, Node source, Node target, boolean defaultLink, boolean trueLink) {
		this.id = id;
		this.source = source;
		this.target = target;
		this.defaultLink = defaultLink;
		this.trueLink = trueLink;
	}

	public Node getSource() {
		return source;
	}

	public Node getTarget() {
		return target;
	}

	public String getId() {
		return id;
	}

	public boolean isDefaultLink() {
		return defaultLink;
	}

	public boolean isTrueLink() {
		return trueLink;
	}
}
