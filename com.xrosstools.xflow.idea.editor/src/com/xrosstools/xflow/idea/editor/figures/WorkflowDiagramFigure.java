package com.xrosstools.xflow.idea.editor.figures;

import com.xrosstools.idea.gef.figures.Figure;
import com.xrosstools.idea.gef.figures.ToolbarLayout;

public class WorkflowDiagramFigure extends Figure {
    public WorkflowDiagramFigure() {
        getInsets().set(50, 50, 50, 0);
        ToolbarLayout layout= new ToolbarLayout();
        layout.setHorizontal(false);
        layout.setSpacing(100);
        layout.setStretchMinorAxis(false);
        layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
        setLayoutManager(layout);
    }
}
