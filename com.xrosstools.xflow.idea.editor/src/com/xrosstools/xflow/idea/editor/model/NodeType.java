package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.xflow.idea.editor.XflowIcons;

import javax.swing.*;

public enum NodeType implements XflowIcons {
    START(StartNode.class, START_ICON),
    END(EndNode.class, END_ICON),

    AUTO_ACTIVITY(AutoActivityNode.class, AUTO_ACTIVITY_ICON),
    TASK_ACTIVITY(TaskActivityNode.class, TASK_ACTIVITY_ICON),
    EVENT_ACTIVITY(EventActivityNode.class, EVENT_ACTIVITY_ICON),
    WAIT_ACTIVITY(WaitActivityNode.class, WAIT_ACTIVITY_ICON),
    SUB_FLOW(SubFlowNode.class, SUB_FLOW_ICON),

    BINARY_ROUTER(BinaryRouterNode.class, BINARY_ROUTER_ICON),
    INCLUSIVE_ROUTER(InclusiveRouterNode.class, INCLUSIVE_ROUTER__ICON),
    EXCLUSIVE_ROUTER(ExclusiveRouterNode.class, EXCLUSIVE_ROUTER_ICON),
    PARALLEL_ROUTER(ParallelRouterNode.class, PARALLEL_ROUTER_ICON),
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

    public static final NodeType[] REFER_TO_CLASS_METHOD = new NodeType[] {
            AUTO_ACTIVITY,
            BINARY_ROUTER,
            INCLUSIVE_ROUTER,
            EXCLUSIVE_ROUTER,
    };

    public static final NodeType[] REFER_TO_CLASS = new NodeType[] {
            TASK_ACTIVITY,
            EVENT_ACTIVITY,
    };

    public static boolean isPropertiesSupported(NodeType type) {
        return isReferToClassMethod(type) || isReferToClass(type);
    }

    public static boolean isReferToClass(NodeType type) {
        for(NodeType test: REFER_TO_CLASS)
            if(test == type)
                return true;

        return false;
    }

    public static boolean isReferToClassMethod(NodeType type) {
        for(NodeType test: REFER_TO_CLASS_METHOD)
            if(test == type)
                return true;

        return false;
    }
}
