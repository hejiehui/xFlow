package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.PropertyEntry;

public class TaskActivityNode extends ActivityNode {

    private PropertyEntry<String> implementation = stringProperty(PROP_IMPLEMENTATION);

    public TaskActivityNode() {
        register(implementation);
    }

    public NodeType getType() {
        return NodeType.TASK_ACTIVITY;
    }
}
