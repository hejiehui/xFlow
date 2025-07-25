package com.xrosstools.xflow.def;

import java.util.List;

import com.xrosstools.xflow.Link;
import com.xrosstools.xflow.Node;

public class LinkDef {
	private String name;
	private int sourceIndex;
	private int targetIndex;
	private boolean defaultLink;

	public LinkDef(String name, int sourceIndex, int targetIndex, boolean defaultLink) {
		this.name = name;
		this.sourceIndex = sourceIndex;
		this.targetIndex = targetIndex;
		this.defaultLink = defaultLink;
	}

	public String getName() {
		return name;
	}

	public int getSourceIndex() {
		return sourceIndex;
	}

	public int getTargetIndex() {
		return targetIndex;
	}

	public boolean isDefaultLink() {
		return defaultLink;
	}

	public Link create(List<Node> nodes) {
		return new Link(name, nodes.get(sourceIndex), nodes.get(targetIndex).addInputCount(), defaultLink);
	}
}
