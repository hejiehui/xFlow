package com.xrosstools.xflow.idea.editor.commands;

import com.xrosstools.idea.gef.commands.CreateConnectionCommand;
import com.xrosstools.idea.gef.routers.RouterStyle;
import com.xrosstools.xflow.idea.editor.model.ActivityNode;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.Link;
import com.xrosstools.xflow.idea.editor.model.RouterNode;

public class CreateLinkCommand extends CreateConnectionCommand {
    public void postExecute() {
        Link link = (Link)getConnection();
        link.setStyle(choseStyle());
    }

    private RouterStyle choseStyle() {
        BaseNode source = (BaseNode)getSource();
        BaseNode target = (BaseNode)getTarget();

        if(source instanceof RouterNode)
            return RouterStyle.VERTICAL_RIGHT_ANGLE;
        if(source instanceof ActivityNode && target instanceof RouterNode )
            return RouterStyle.HORIZONTAL_RIGHT_ANGLE;

        return RouterStyle.HORIZONTAL_LIGHTNING;
    }
}
