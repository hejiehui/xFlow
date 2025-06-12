package com.xrosstools.xflow.idea.editor.parts;

import com.xrosstools.idea.gef.figures.Figure;
import com.xrosstools.idea.gef.figures.IconFigure;
import com.xrosstools.idea.gef.parts.AbstractGraphicalEditPart;
import com.xrosstools.xflow.idea.editor.model.BaseNode;

import java.awt.*;

public class ImageNodePart extends BaseNodePart {
    @Override
    protected Figure createFigure() {
        return new IconFigure(((BaseNode)getModel()).getType().getTypeIcon());
    }

    @Override
    protected void refreshVisuals() {
        BaseNode node = (BaseNode) getModel();
        IconFigure figure = (IconFigure)getFigure();

        figure.setToolTipText(((BaseNode) getModel()).getTooltips());

        Rectangle rectangle = new Rectangle(node.getLocation(), figure.getPreferredSize());
        ((AbstractGraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), rectangle);
    }
}
