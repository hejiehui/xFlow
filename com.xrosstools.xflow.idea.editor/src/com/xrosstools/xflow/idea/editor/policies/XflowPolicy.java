package com.xrosstools.xflow.idea.editor.policies;

import com.xrosstools.idea.gef.commands.Command;
import com.xrosstools.idea.gef.commands.DeleteFromContainerCommand;
import com.xrosstools.idea.gef.policies.NodeContainerEditPolicy;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.Xflow;
import com.xrosstools.xflow.idea.editor.model.XflowDiagram;

public class XflowPolicy extends NodeContainerEditPolicy {
    public Command getDeleteCommand() {
        return new DeleteFromContainerCommand<Xflow>().init((XflowDiagram)getHost().getParent().getModel(), (Xflow) getHost().getModel());
    }

    public boolean isChildTypeAcceptable(Object model) {
        return model instanceof BaseNode;
    }
}
