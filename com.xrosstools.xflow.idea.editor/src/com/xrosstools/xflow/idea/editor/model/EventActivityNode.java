package com.xrosstools.xflow.idea.editor.model;


import com.xrosstools.idea.gef.util.PropertyEntry;

public class EventActivityNode extends ActivityNode {
    private PropertyEntry<String> implementation = stringProperty(PROP_IMPLEMENTATION);

    public EventActivityNode() {
        register(implementation);
    }

    public NodeType getType() {
        return NodeType.EVENT_ACTIVITY;
    }
}
