package com.xrosstools.xflow.idea.editor.parts;

import com.xrosstools.idea.gef.figures.Figure;
import com.xrosstools.idea.gef.parts.AbstractNodeContainerEditPart;
import com.xrosstools.idea.gef.parts.EditPolicy;
import com.xrosstools.xflow.idea.editor.figures.WorkflowDiagramFigure;
import com.xrosstools.xflow.idea.editor.policies.XflowDiagramPolicy;

public class XflowDiagramPart extends AbstractNodeContainerEditPart {
    protected EditPolicy createEditPolicy() {
        return new XflowDiagramPolicy();
    }

    protected Figure createFigure() {
        return new WorkflowDiagramFigure();
    }
}
