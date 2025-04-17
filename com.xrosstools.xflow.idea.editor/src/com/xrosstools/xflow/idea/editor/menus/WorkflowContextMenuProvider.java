package com.xrosstools.xflow.idea.editor.menus;

import com.intellij.openapi.project.Project;
import com.xrosstools.idea.gef.ContentChangeListener;
import com.xrosstools.idea.gef.ContextMenuProvider;
import com.xrosstools.xflow.idea.editor.model.WorkflowDiagram;

import javax.swing.*;

public class WorkflowContextMenuProvider extends ContextMenuProvider implements ContentChangeListener<WorkflowDiagram> {
    private Project project;
    private WorkflowDiagram diagram;

    public WorkflowContextMenuProvider(Project project) {
        this.project = project;
    }

    public void contentChanged(WorkflowDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public JPopupMenu buildContextMenu(Object selected) {
        JPopupMenu menu = new JPopupMenu();
        return menu;
    }
}
