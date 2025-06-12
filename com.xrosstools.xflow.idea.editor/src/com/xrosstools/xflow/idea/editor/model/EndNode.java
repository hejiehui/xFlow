package com.xrosstools.xflow.idea.editor.model;

public class EndNode extends BaseNode {
    public EndNode() {
        setId("end");
        setOutputLimit(0);
    }

    public NodeType getType() {
        return NodeType.END;
    }
}
