package com.xrosstools.xflow.idea.editor.treeparts;

import com.xrosstools.idea.gef.parts.AbstractTreeEditPart;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.NodeType;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BaseNodeTreePart extends AbstractTreeEditPart implements PropertyConstants {
    @Override
    public String getText() {
        return ((BaseNode)getModel()).getDisplayText();
    }

    @Override
    public Icon getImage() {
        return ((BaseNode)getModel()).getType().getTypeIcon();
    }

    public List getModelChildren() {
        List children = new ArrayList();
        BaseNode node = (BaseNode)getModel();

        if(NodeType.isPropertiesSupported(node.getType()))
            children.add(node.values(PROPERTIES_CATEGORY));

        children.addAll(node.getOutputs());
        return children;
    }
}
