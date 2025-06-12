package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.PropertyEntry;

import java.util.concurrent.TimeUnit;

public class WaitActivityNode extends ActivityNode {
    private PropertyEntry<Integer> delay = intProperty(PROP_DELAY);
    private PropertyEntry<TimeUnit> timeUnit = enumProperty(PROP_TIME_UNIT, DEFAULT_TIME_UNIT, TimeUnit.values());

    public WaitActivityNode() {
        register(delay);
        register(timeUnit);
    }
    public NodeType getType() {
        return NodeType.WAIT_ACTIVITY;
    }
}
