package com.xrosstools.xflow.idea.editor.treeparts;

import com.xrosstools.idea.gef.treeparts.AbstractNodeContainerTreePart;
import com.xrosstools.xflow.idea.editor.XflowIcons;

import javax.swing.*;

public class WorkflowDiagramTreePart extends AbstractNodeContainerTreePart {
    @Override
    public Icon getImage() {
        return XflowIcons.XFLOW;
    }
}
