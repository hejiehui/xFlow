package com.xrosstools.xflow.idea.editor.treeparts;

import com.xrosstools.idea.gef.parts.AbstractTreeEditPart;
import com.xrosstools.xflow.idea.editor.XflowIcons;
import com.xrosstools.xflow.idea.editor.model.Link;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;

import javax.swing.*;

public class LinkTreePart extends AbstractTreeEditPart implements PropertyConstants {
    @Override
    public String getText() {
        return ((Link)getModel()).getDisplayText();
    }

    @Override
    public Icon getImage() {
        return XflowIcons.LINK_ICON;
    }
}
