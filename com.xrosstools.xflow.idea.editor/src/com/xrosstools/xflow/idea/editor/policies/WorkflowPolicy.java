package com.xrosstools.xflow.idea.editor.policies;

import com.xrosstools.idea.gef.commands.Command;
import com.xrosstools.idea.gef.commands.DeleteFromContainerCommand;
import com.xrosstools.idea.gef.policies.NodeContainerEditPolicy;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.Workflow;
import com.xrosstools.xflow.idea.editor.model.WorkflowDiagram;

public class WorkflowPolicy extends NodeContainerEditPolicy {
    public Command getDeleteCommand() {
        return new DeleteFromContainerCommand<Workflow>().init((WorkflowDiagram)getHost().getParent().getModel(), (Workflow) getHost().getModel());
    }

    public boolean isChildTypeAcceptable(Object model) {
        return model instanceof BaseNode;
    }
}
