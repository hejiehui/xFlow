package com.xrosstools.xflow.idea.editor.parts;

import com.xrosstools.idea.gef.figures.*;
import com.xrosstools.idea.gef.parts.AbstractNodeConnectionEditPart;
import com.xrosstools.idea.gef.routers.BendpointConnectionRouter;
import com.xrosstools.idea.gef.routers.CommonStyleLocator;
import com.xrosstools.idea.gef.routers.CommonStyleRouter;
import com.xrosstools.idea.gef.routers.RouterStyle;
import com.xrosstools.xflow.idea.editor.model.ActivityNode;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.Link;
import com.xrosstools.xflow.idea.editor.model.RouterNode;

public class LinkPart extends AbstractNodeConnectionEditPart {
    private Link link;
    private Label label;
    private CommonStyleRouter router;
    private CommonStyleLocator locator;
    protected Figure createFigure() {
        link = (Link)getModel();
        Connection conn = new Connection();
        conn.setTargetDecoration(new ArrowDecoration());
        conn.setRouter(new BendpointConnectionRouter());
        conn.setForegroundColor(ColorConstants.black);

        router = new CommonStyleRouter(link.getStyle());
        conn.setRouter(router);

        label = new Label();
        label.setText(link.getDisplayText());
        label.setOpaque(true);

        locator = new CommonStyleLocator(label);
        conn.add(label, locator);

        return conn;
    }

    protected void refreshVisuals() {
        label.setText(link.getDisplayText());
        router.setStyle(link.getStyle());
        locator.setStyle(link.getStyle());
    }
}
