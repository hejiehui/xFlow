package com.xrosstools.xflow.idea.editor.policies;

import com.xrosstools.idea.gef.policies.OrderedContainerEditPolicy;
import com.xrosstools.xflow.idea.editor.model.Workflow;

public class WorkflowDiagramPolicy extends OrderedContainerEditPolicy {
    public boolean isChildTypeAcceptable(Object model) {
        return model instanceof Workflow;
    }
}
