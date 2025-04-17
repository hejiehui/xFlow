package com.xrosstools.xflow.idea.editor;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface XflowIcons {
    Icon XFLOW = IconLoader.getIcon("/icons/xflow.png", XflowIcons.class);
    Icon WORKFLOW = IconLoader.getIcon("/icons/workflow.png", XflowIcons.class);
    Icon LINK = IconLoader.getIcon("/icons/link.png", XflowIcons.class);
    Icon AUTO_TASK_ICON = IconLoader.getIcon("/icons/auto_task.png", XflowIcons.class);
    Icon MANUAL_TASK_ICON = IconLoader.getIcon("/icons/manual_task.png", XflowIcons.class);
    Icon VALIDATOR_ICON = IconLoader.getIcon("/icons/validator.png", XflowIcons.class);
    Icon LOCATOR_ICON = IconLoader.getIcon("/icons/locator.png", XflowIcons.class);
    Icon DISPATCHER_ICON = IconLoader.getIcon("/icons/dispatcher.png", XflowIcons.class);
    Icon TIMER_ICON = IconLoader.getIcon("/icons/Wait.png", XflowIcons.class);

    Icon SEQUENCE_ICON = IconLoader.getIcon("/icons/Sequence.png", XflowIcons.class);
    Icon SELECTOR_ICON = IconLoader.getIcon("/icons/Selector.png", XflowIcons.class);
    Icon PARALLEL_ICON = IconLoader.getIcon("/icons/Parallel.png", XflowIcons.class);

    Icon INVERTER_ICON = IconLoader.getIcon("/icons/Inverter.png", XflowIcons.class);
    Icon REPEAT_ICON = IconLoader.getIcon("/icons/Repeat.png", XflowIcons.class);
    Icon RETRY_ICON = IconLoader.getIcon("/icons/Retry.png", XflowIcons.class);
    Icon FORCE_SUCCESS_ICON = IconLoader.getIcon("/icons/ForceSuccess.png", XflowIcons.class);
    Icon FORCE_FAILURE_ICON = IconLoader.getIcon("/icons/ForceFailure.png", XflowIcons.class);

    Icon ACTION_ICON = IconLoader.getIcon("/icons/Action.png", XflowIcons.class);
    Icon CONDITION_ICON = IconLoader.getIcon("/icons/Condition.png", XflowIcons.class);
    Icon FIXED_STATUS_ICON = IconLoader.getIcon("/icons/FixedStatus.png", XflowIcons.class);
    Icon SLEEP_ICON = IconLoader.getIcon("/icons/Sleep.png", XflowIcons.class);
    Icon SUBTREE_ICON = IconLoader.getIcon("/icons/Subtree.png", XflowIcons.class);

    Icon GENERATE_HELPER_ICON = IconLoader.getIcon("/icons/generate_helper.png", XflowIcons.class);
}
