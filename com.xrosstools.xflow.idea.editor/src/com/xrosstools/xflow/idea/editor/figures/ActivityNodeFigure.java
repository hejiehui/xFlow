package com.xrosstools.xflow.idea.editor.figures;

import com.xrosstools.idea.gef.figures.Label;
import com.xrosstools.idea.gef.figures.RoundedRectangle;
import com.xrosstools.idea.gef.figures.Text;
import com.xrosstools.idea.gef.figures.ToolbarLayout;
import com.xrosstools.xflow.idea.editor.model.NodeType;

public class ActivityNodeFigure extends RoundedRectangle {
    private Text label;

    public ActivityNodeFigure(NodeType type) {
        ToolbarLayout layout= new ToolbarLayout();
        layout.setHorizontal(false);
        layout.setSpacing(5);
        layout.setStretchMinorAxis(false);
        layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
        setLayoutManager(layout);
        this.getInsets().set(5,5,5,5);

        Label typeAndName = new Label();
        typeAndName.setIcon(type.getTypeIcon());
        typeAndName.setText(type.getDisplayName());
        add(typeAndName);

        label = new Text();
        add(label);
    }

    public void setText(String text) {
        label.setText(text);
    }
}
