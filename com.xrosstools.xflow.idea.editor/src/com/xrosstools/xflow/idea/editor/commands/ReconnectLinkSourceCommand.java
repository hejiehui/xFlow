package com.xrosstools.xflow.idea.editor.commands;

import com.xrosstools.idea.gef.commands.ReconnectSourceCommand;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;

public class ReconnectLinkSourceCommand extends ReconnectSourceCommand implements PropertyConstants {
    private String oldId;
    private String newId;
    public void execute() {
        oldId = (String)getConnection().get(PROP_ID).get();
        super.execute();
        newId = (String)getConnection().get(PROP_ID).get();
    }

    public void undo() {
        super.undo();
        getConnection().get(PROP_ID).set(oldId);
    }
}
