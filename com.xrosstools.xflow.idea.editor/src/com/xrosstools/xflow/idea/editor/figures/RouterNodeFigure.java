package com.xrosstools.xflow.idea.editor.figures;

import com.xrosstools.idea.gef.figures.*;
import com.xrosstools.xflow.idea.editor.model.NodeType;

public class RouterNodeFigure extends Rhombus {
    private static final int TOP = 10;
    private static final int LEFT = 10;
    private static final int BOTTOM= 10;
    private static final int RIGHT = 10;
    private IconFigure icon;

    public RouterNodeFigure(NodeType type) {
        ToolbarLayout layout= new ToolbarLayout();
        layout.setHorizontal(false);
        layout.setStretchMinorAxis(false);
        layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
        setLayoutManager(layout);
        this.getInsets().set(TOP,LEFT,BOTTOM,RIGHT);

        icon = new IconFigure(type.getTypeIcon());
        add(icon);
    }
}
