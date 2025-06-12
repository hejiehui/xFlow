package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.PropertyEntry;

public class BinaryRouterNode extends RouterNode {
    private PropertyEntry<String> implementation = stringProperty(PROP_IMPLEMENTATION);

    public BinaryRouterNode() {
        register(implementation);
        setOutputLimit(2);
    }

    public NodeType getType() {
        return NodeType.BINARY_ROUTER;
    }
}
