package com.xrosstools.xflow.idea.editor.parts;

import com.xrosstools.idea.gef.figures.Figure;
import com.xrosstools.idea.gef.parts.AbstractGraphicalEditPart;
import com.xrosstools.xflow.idea.editor.figures.RouterNodeFigure;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.RouterNode;

import java.awt.*;

public class RouterNodePart extends BaseNodePart {
    @Override
    protected Figure createFigure() {
        return new RouterNodeFigure(((BaseNode)getModel()).getType());
    }

    @Override
    protected void refreshVisuals() {
        RouterNode node = (RouterNode) getModel();
        RouterNodeFigure figure = (RouterNodeFigure)getFigure();

        figure.setToolTipText(((BaseNode) getModel()).getTooltips());

        Rectangle rectangle = new Rectangle(node.getLocation(), figure.getPreferredSize());
        ((AbstractGraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), rectangle);
    }
}
