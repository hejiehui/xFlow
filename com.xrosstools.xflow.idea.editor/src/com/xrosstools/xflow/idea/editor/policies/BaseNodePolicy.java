package com.xrosstools.xflow.idea.editor.policies;

import com.xrosstools.idea.gef.commands.CreateConnectionCommand;
import com.xrosstools.idea.gef.policies.NodeEditPolicy;
import com.xrosstools.xflow.idea.editor.commands.CreateLinkCommand;

public class BaseNodePolicy extends NodeEditPolicy {
    public CreateConnectionCommand createCreateConnectionCommand() {
        return new CreateLinkCommand();
    }
}
