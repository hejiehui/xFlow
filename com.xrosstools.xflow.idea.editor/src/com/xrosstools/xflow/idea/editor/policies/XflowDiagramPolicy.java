package com.xrosstools.xflow.idea.editor.policies;

import com.xrosstools.idea.gef.policies.OrderedContainerEditPolicy;
import com.xrosstools.xflow.idea.editor.model.Xflow;

public class XflowDiagramPolicy extends OrderedContainerEditPolicy {
    public boolean isChildTypeAcceptable(Object model) {
        return model instanceof Xflow;
    }
}
