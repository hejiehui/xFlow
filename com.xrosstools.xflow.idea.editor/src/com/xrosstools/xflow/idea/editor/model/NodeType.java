package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.xflow.idea.editor.XflowIcons;

import javax.swing.*;

public enum NodeType implements XflowIcons {
    AUTO_TASK(AutoTask.class, AUTO_TASK_ICON),
    MANUAL_TASK(ManualTask.class, MANUAL_TASK_ICON),

    VALIDATOR(Validator.class, VALIDATOR_ICON),
    LOCATOR(Locator.class, LOCATOR_ICON),
    DISPATCHER(Dispatcher.class, DISPATCHER_ICON),
    ;


    private String displayName;
    private String nodeName;
    private Class typeClass;
    private Icon typeIcon;

    NodeType(Class typeClass, Icon typeIcon) {
        this.typeClass = typeClass;
        this.typeIcon = typeIcon;
        nodeName = name().toLowerCase();

        displayName = Character.toUpperCase(nodeName.charAt(0)) + nodeName.substring(1);
        displayName = displayName.replace("_", " ");
    }


    public Icon getTypeIcon() {
        return typeIcon;
    }

    public Class getTypeClass() {
        return typeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public static NodeType findByNodeName(String nodeName) {
        for(NodeType type: values()) {
            if(type.getNodeName().equals(nodeName))
                return type;
        }
        throw new IllegalArgumentException(nodeName);
    }
}
