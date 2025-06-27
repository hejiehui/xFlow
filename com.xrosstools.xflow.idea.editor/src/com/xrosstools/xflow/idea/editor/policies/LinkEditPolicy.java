package com.xrosstools.xflow.idea.editor.policies;

import com.xrosstools.idea.gef.commands.Command;
import com.xrosstools.idea.gef.parts.AbstractConnectionEditPart;
import com.xrosstools.idea.gef.policies.NodeConnectionEditPolicy;
import com.xrosstools.xflow.idea.editor.commands.AdjustLinkCommand;

public class LinkEditPolicy extends NodeConnectionEditPolicy {
    public Command getAdjustConnectionCommand(AbstractConnectionEditPart connectionPart) {
        return new AdjustLinkCommand(connectionPart);
    }
}
