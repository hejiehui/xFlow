package com.xrosstools.xflow.idea.editor.treeparts;

import com.xrosstools.idea.gef.treeparts.AbstractNodeContainerTreePart;
import com.xrosstools.xflow.idea.editor.XflowIcons;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;
import com.xrosstools.xflow.idea.editor.model.XflowDiagram;

import javax.swing.*;
import java.util.List;

public class XflowDiagramTreePart extends AbstractNodeContainerTreePart {
    @Override
    public Icon getImage() {
        return XflowIcons.XFLOW_ICON;
    }

    public List getModelChildren() {
        List children = super.getModelChildren();
        children.add(0, ((XflowDiagram) getModel()).values(PropertyConstants.PROPERTIES_CATEGORY));
        return children;
    }
}
