package com.xrosstools.xflow.idea.editor.treeparts;

import com.xrosstools.idea.gef.treeparts.AbstractNodeContainerTreePart;
import com.xrosstools.xflow.idea.editor.XflowIcons;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.Workflow;

import javax.swing.*;

public class WorkflowTreePart extends AbstractNodeContainerTreePart {
    @Override
    public String getText() {
        return ((Workflow)getModel()).getName();
    }

    @Override
    public Icon getImage() {
        return XflowIcons.WORKFLOW;
    }
}
