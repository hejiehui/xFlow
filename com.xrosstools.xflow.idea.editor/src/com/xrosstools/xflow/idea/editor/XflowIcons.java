package com.xrosstools.xflow.idea.editor;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface XflowIcons {
    Icon XFLOW_ICON = IconLoader.getIcon("/icons/xflow.png", XflowIcons.class);
    Icon FLOW_ICON = IconLoader.getIcon("/icons/flow.png", XflowIcons.class);
    Icon START_ICON = IconLoader.getIcon("/icons/start.png", XflowIcons.class);
    Icon END_ICON = IconLoader.getIcon("/icons/end.png", XflowIcons.class);
    Icon LINK_ICON = IconLoader.getIcon("/icons/link.png", XflowIcons.class);

    Icon AUTO_ACTIVITY_ICON = IconLoader.getIcon("/icons/auto_activity.png", XflowIcons.class);
    Icon TASK_ACTIVITY_ICON = IconLoader.getIcon("/icons/task_activity.png", XflowIcons.class);
    Icon EVENT_ACTIVITY_ICON = IconLoader.getIcon("/icons/event_activity.png", XflowIcons.class);
    Icon WAIT_ACTIVITY_ICON = IconLoader.getIcon("/icons/wait_activity.png", XflowIcons.class);
    Icon SUBFLOW_ACTIVITY_ICON = IconLoader.getIcon("/icons/subflow_activity.png", XflowIcons.class);

    Icon INCLUSIVE_ROUTER__ICON = IconLoader.getIcon("/icons/inclusive_router.png", XflowIcons.class);
    Icon EXCLUSIVE_ROUTER_ICON = IconLoader.getIcon("/icons/exclusive_router.png", XflowIcons.class);
    Icon PARALLEL_ROUTER_ICON = IconLoader.getIcon("/icons/parallel_router.png", XflowIcons.class);
    Icon BINARY_ROUTER_ICON = IconLoader.getIcon("/icons/binary_router.png", XflowIcons.class);

    Icon PROPERTIES_ICON = IconLoader.getIcon("/icons/properties.png", XflowIcons.class);
    Icon PROPERTY_ICON = IconLoader.getIcon("/icons/property.png", XflowIcons.class);

    Icon GENERATE_HELPER_ICON = IconLoader.getIcon("/icons/generate_helper.png", XflowIcons.class);
}
