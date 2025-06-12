package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.PropertyEntry;

public class AutoActivityNode extends ActivityNode {
    private PropertyEntry<String> implementation = stringProperty(PROP_IMPLEMENTATION);

    public AutoActivityNode() {
        register(implementation);
    }

    public NodeType getType() {
        return NodeType.AUTO_ACTIVITY;
    }
}
