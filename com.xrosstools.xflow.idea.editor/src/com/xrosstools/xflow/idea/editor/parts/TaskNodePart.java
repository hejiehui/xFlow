package com.xrosstools.xflow.idea.editor.parts;

import com.xrosstools.idea.gef.figures.Figure;
import com.xrosstools.idea.gef.parts.AbstractGraphicalEditPart;
import com.xrosstools.xflow.idea.editor.figures.TaskNodeFigure;
import com.xrosstools.xflow.idea.editor.model.BaseNode;

import java.awt.*;

public class TaskNodePart extends BaseNodePart {
    @Override
    protected Figure createFigure() {
        return new TaskNodeFigure(((BaseNode) getModel()).getType());
    }

    @Override
    protected void refreshVisuals() {
        BaseNode node = (BaseNode) getModel();
        TaskNodeFigure figure = (TaskNodeFigure)getFigure();

        figure.setText(node.getName());
        figure.setToolTipText(((BaseNode) getModel()).getTooltips());

        Rectangle rectangle = new Rectangle(node.getLocation(), figure.getPreferredSize());
        ((AbstractGraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), rectangle);
    }
}