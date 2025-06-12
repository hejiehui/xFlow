package com.xrosstools.xflow.idea.editor.parts;

import com.xrosstools.idea.gef.figures.Figure;
import com.xrosstools.idea.gef.parts.AbstractGraphicalEditPart;
import com.xrosstools.xflow.idea.editor.figures.ActivityNodeFigure;
import com.xrosstools.xflow.idea.editor.model.BaseNode;

import java.awt.*;

public class ActivityNodePart extends BaseNodePart {
    @Override
    protected Figure createFigure() {
        return new ActivityNodeFigure(((BaseNode) getModel()).getType());
    }

    @Override
    protected void refreshVisuals() {
        BaseNode node = (BaseNode) getModel();
        ActivityNodeFigure figure = (ActivityNodeFigure)getFigure();

        figure.setText(node.getDisplayText());
        figure.setToolTipText(((BaseNode) getModel()).getTooltips());

        Rectangle rectangle = new Rectangle(node.getLocation(), figure.getPreferredSize());
        ((AbstractGraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), rectangle);
    }
}