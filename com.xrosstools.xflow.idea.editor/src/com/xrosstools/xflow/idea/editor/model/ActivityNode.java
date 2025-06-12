package com.xrosstools.xflow.idea.editor.model;

public abstract class ActivityNode extends BaseNode {
    public ActivityNode() {
        setOutputLimit(1);
    }
}
