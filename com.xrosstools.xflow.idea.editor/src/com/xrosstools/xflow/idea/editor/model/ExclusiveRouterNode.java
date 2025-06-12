package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.PropertyEntry;

public class ExclusiveRouterNode extends RouterNode {
    private PropertyEntry<String> implementation = stringProperty(PROP_IMPLEMENTATION);

    public ExclusiveRouterNode() {
        register(implementation);
    }

    public NodeType getType() {
        return NodeType.EXCLUSIVE_ROUTER;
    }
}
