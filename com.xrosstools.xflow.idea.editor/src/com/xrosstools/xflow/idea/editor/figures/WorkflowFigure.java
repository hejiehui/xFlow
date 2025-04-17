package com.xrosstools.xflow.idea.editor.figures;

import com.xrosstools.idea.gef.figures.*;
import com.xrosstools.idea.gef.figures.Label;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;

import java.awt.*;

public class WorkflowFigure extends Figure implements PropertyConstants {
    private Label label;
    private RectangleFigure topLine;
    private Figure figure;
    private RectangleFigure bottomLine;

    public WorkflowFigure() {
        figure = new Figure();
        figure.setLayoutManager(new FreeformLayout(INCREMENTAL));
        figure.setMinSize(new Dimension(DEFAULT_WORKFLOW_WIDTH, DEFAULT_WORKFLOW_HEIGHT));

        label = new Label();

        ToolbarLayout layout= new ToolbarLayout();
        layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
        layout.setSpacing(TITLE_SPACE);
        setLayoutManager(layout);

        label.setLabelAlignment(PositionConstants.LEFT);
        label.setForegroundColor(ColorConstants.white);

        topLine = new RectangleFigure();
        topLine.setBackgroundColor(ColorConstants.lightGray);
        topLine.setForegroundColor(ColorConstants.lightGray);

        bottomLine = new RectangleFigure();
        bottomLine.setBackgroundColor(ColorConstants.lightGray);
        bottomLine.setForegroundColor(ColorConstants.lightGray);

        setPanelSize(new Dimension(DEFAULT_WORKFLOW_WIDTH, DEFAULT_WORKFLOW_HEIGHT));

        add(label);
        add(topLine);
        add(figure);
        add(bottomLine);
    }

    public void setName(String name, String toolTip) {
        label.setText(name);
        label.setToolTipText(toolTip);
    }

    public Figure getPanel(){
        return figure;
    }

    public Dimension getPanelSize() {
        return figure.getPreferredSize();
    }

    public void setPanelSize(Dimension size) {
        figure.setPreferredSize(size);
        topLine.setSize(new Dimension(size.width, 1));
        bottomLine.setSize(new Dimension(size.width, 2));
    }
}
