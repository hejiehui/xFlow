package com.xrosstools.xflow.idea.editor.figures;

import com.xrosstools.idea.gef.figures.*;
import com.xrosstools.xflow.idea.editor.model.NodeType;

public class RouterNodeFigure extends RoundedRectangle {
    private IconFigure icon;

    public RouterNodeFigure(NodeType type) {
        ToolbarLayout layout= new ToolbarLayout();
        layout.setHorizontal(false);
        layout.setSpacing(5);
        layout.setStretchMinorAxis(false);
        layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
        setLayoutManager(layout);
        this.getInsets().set(5,5,5,5);

        Label typeAndName = new Label();
        typeAndName.setIcon(type.getTypeIcon());
        add(typeAndName);

        icon = new IconFigure();
        add(icon);
    }
}
