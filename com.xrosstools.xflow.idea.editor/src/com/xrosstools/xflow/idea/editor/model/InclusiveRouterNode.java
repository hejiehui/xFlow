package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.PropertyEntry;

public class InclusiveRouterNode extends RouterNode {
    private PropertyEntry<String> implementation = stringProperty(PROP_IMPLEMENTATION);

    public InclusiveRouterNode() {
        register(implementation);
    }

    public NodeType getType() {
        return NodeType.INCLUSIVE_ROUTER;
    }
}
