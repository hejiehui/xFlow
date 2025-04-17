package com.xrosstools.xflow.idea.editor.treeparts;

import com.xrosstools.idea.gef.parts.AbstractTreeEditPart;
import com.xrosstools.xflow.idea.editor.model.BaseNode;

import javax.swing.*;

public class BaseNodeTreePart extends AbstractTreeEditPart {
    @Override
    public String getText() {
        return ((BaseNode)getModel()).getName();
    }

    @Override
    public Icon getImage() {
        return ((BaseNode)getModel()).getType().getTypeIcon();
    }
}
