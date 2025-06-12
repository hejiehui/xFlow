package com.xrosstools.xflow.idea.editor.treeparts;

import com.xrosstools.idea.gef.treeparts.AbstractNodeContainerTreePart;
import com.xrosstools.xflow.idea.editor.XflowIcons;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;
import com.xrosstools.xflow.idea.editor.model.Xflow;

import javax.swing.*;
import java.util.List;

public class XflowTreePart extends AbstractNodeContainerTreePart {
    @Override
    public String getText() {
        return ((Xflow)getModel()).getName();
    }

    @Override
    public Icon getImage() {
        return XflowIcons.FLOW_ICON;
    }

    public List getModelChildren() {
        List children = super.getModelChildren();
        children.add(0, ((Xflow) getModel()).values(PropertyConstants.PROPERTIES_CATEGORY));
        return children;
    }
}
