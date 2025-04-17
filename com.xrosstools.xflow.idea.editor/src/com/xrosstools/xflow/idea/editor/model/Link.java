package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.model.NodeConnection;

public class Link extends NodeConnection<BaseNode, BaseNode> {
    public Link(){}

    public Link(BaseNode parent, BaseNode child) {
        super(parent, child);
    }
}
