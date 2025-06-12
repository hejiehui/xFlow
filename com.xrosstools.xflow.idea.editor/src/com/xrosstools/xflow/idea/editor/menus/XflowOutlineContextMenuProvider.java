package com.xrosstools.xflow.idea.editor.menus;

import com.intellij.openapi.project.Project;
import com.xrosstools.idea.gef.ContentChangeListener;
import com.xrosstools.idea.gef.ContextMenuProvider;
import com.xrosstools.xflow.idea.editor.model.XflowDiagram;

import javax.swing.*;

public class XflowOutlineContextMenuProvider extends ContextMenuProvider implements ContentChangeListener<XflowDiagram> {
    private Project project;
    private XflowDiagram diagram;

    public XflowOutlineContextMenuProvider(Project project) {
        this.project = project;
    }

    public void contentChanged(XflowDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public JPopupMenu buildContextMenu(Object selected) {
        JPopupMenu menu = new JPopupMenu();
        return menu;
    }
}
