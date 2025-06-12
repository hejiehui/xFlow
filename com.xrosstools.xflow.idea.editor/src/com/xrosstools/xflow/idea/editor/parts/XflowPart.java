package com.xrosstools.xflow.idea.editor.parts;

import com.xrosstools.idea.gef.figures.Figure;
import com.xrosstools.idea.gef.parts.AbstractNodeContainerEditPart;
import com.xrosstools.idea.gef.parts.EditPolicy;
import com.xrosstools.xflow.idea.editor.figures.WorkflowFigure;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;
import com.xrosstools.xflow.idea.editor.model.Xflow;
import com.xrosstools.xflow.idea.editor.policies.XflowPolicy;

import java.awt.*;
import java.beans.PropertyChangeEvent;

public class XflowPart extends AbstractNodeContainerEditPart implements PropertyConstants {
    protected Figure createFigure() {
        return new WorkflowFigure();
    }

    @Override
    public Figure getContentPane() {
        return ((WorkflowFigure)getFigure()).getPanel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Xflow workflow = (Xflow)getModel();
        ((WorkflowFigure)getFigure()).setName(workflow.getName(), workflow.getTooltips());
    }

    @Override
    protected EditPolicy createEditPolicy() {
        return new XflowPolicy();
    }

    @Override
    protected void refreshVisuals() {
        Xflow node = (Xflow) getModel();
        WorkflowFigure figure = (WorkflowFigure)getFigure();

        figure.setName(node.getName(), node.getDescription());
        figure.setPanelSize(checkConstraint());
    }

    private Dimension checkConstraint() {
        /**
         * This is special to idea GEF, because IDEA GEF use absolute coordination. So the
         * x and y are started from absolute location instead of 0.
         */
        Point smLoc = getContentPane().getLocation();
        int x = smLoc.x;
        int y = smLoc.y;
        for(Object child: getChildren()) {
            BaseNodePart part = (BaseNodePart)child;
            Point loc = part.getFigure().getLocation();
            Dimension size = part.getFigure().getSize();
            x = Math.max(x, loc.x + size.width);
            y = Math.max(y, loc.y + size.height);
        }

        return new Dimension(
                Math.max(DEFAULT_WORKFLOW_WIDTH, x + INCREMENTAL - smLoc.x),
                Math.max(DEFAULT_WORKFLOW_HEIGHT, y + INCREMENTAL - smLoc.y));
    }
}
