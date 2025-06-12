package com.xrosstools.xflow.idea.editor.model;

public class StartNode extends BaseNode {
    public StartNode() {
        setId("start");
        setInputLimit(0);
    }

    public NodeType getType() {
        return NodeType.START;
    }
}
