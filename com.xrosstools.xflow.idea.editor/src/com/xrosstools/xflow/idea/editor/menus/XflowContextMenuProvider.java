package com.xrosstools.xflow.idea.editor.menus;

import com.intellij.openapi.project.Project;
import com.xrosstools.idea.gef.ContentChangeListener;
import com.xrosstools.idea.gef.ContextMenuProvider;
import com.xrosstools.idea.gef.actions.ImplementationUtil;
import com.xrosstools.idea.gef.commands.PropertyChangeCommand;
import com.xrosstools.idea.gef.parts.EditPart;
import com.xrosstools.idea.gef.routers.RouterStyle;
import com.xrosstools.idea.gef.util.ConfigMenuProvider;
import com.xrosstools.xflow.idea.editor.model.*;
import com.xrosstools.xflow.idea.editor.parts.BaseNodePart;
import com.xrosstools.xflow.idea.editor.parts.LinkPart;
import com.xrosstools.xflow.idea.editor.parts.XflowDiagramPart;
import com.xrosstools.xflow.idea.editor.parts.XflowPart;

import javax.swing.*;

public class XflowContextMenuProvider extends ContextMenuProvider implements ContentChangeListener<XflowDiagram>, PropertyConstants {
    private Project project;
    private XflowDiagram diagram;

    public XflowContextMenuProvider(Project project) {
        this.project = project;
    }

    public void contentChanged(XflowDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public JPopupMenu buildContextMenu(Object selected) {
        EditPart part = (EditPart)selected;
        JPopupMenu menu = new JPopupMenu();
        if(part instanceof XflowDiagramPart) {
            buildDiagramContextMenu(menu, (XflowDiagramPart) part);
        } else if(part instanceof XflowPart) {
            buildFlowContextMenu(menu, (XflowPart)part);
        } else if(part instanceof BaseNodePart) {
            buildNodeContextMenu(menu, (BaseNodePart)part);
        } else if(part instanceof LinkPart) {
            buildLinkContextMenu(menu, (LinkPart)part);
        }

        return menu;
    }

    private void buildDiagramContextMenu(JPopupMenu menu, XflowDiagramPart part) {
        XflowDiagram diagram = (XflowDiagram)part.getModel();
        ConfigMenuProvider.buildPropertyMenu(project, menu, PROPERTIES_CATEGORY, "property", diagram);
    }

    private void buildFlowContextMenu(JPopupMenu menu, XflowPart part) {
        Xflow xflow = (Xflow)part.getModel();
        ConfigMenuProvider.buildPropertyMenu(project, menu, PROPERTIES_CATEGORY, "property", xflow);
    }

    private void buildNodeContextMenu(JPopupMenu menu, BaseNodePart part) {
        BaseNode node = (BaseNode)part.getModel();
        if(NodeType.isReferToClass(node.getType())) {
            ImplementationUtil.buildImplementationMenu(project, menu, node, PROP_IMPLEMENTATION, false);
            addSeparator(menu);
        }

        if(NodeType.isReferToClassMethod(node.getType())) {
            ImplementationUtil.buildImplementationMenu(project, menu, node, PROP_IMPLEMENTATION, true);
            addSeparator(menu);
        }

        if(NodeType.isPropertiesSupported(node.getType()))
            ConfigMenuProvider.buildPropertyMenu(project, menu, PROPERTIES_CATEGORY, "property", node);
    }

    private void buildLinkContextMenu(JPopupMenu menu, LinkPart part) {
        Link link = (Link)part.getModel();
        RouterStyle curStyle = link.getStyle();
        for(RouterStyle style: RouterStyle.values()) {
            if(curStyle != style)
                menu.add(createItem(style.getDisplayName(), false, new PropertyChangeCommand(link, PROP_STYLE, curStyle, style)));
        }
    }

}
