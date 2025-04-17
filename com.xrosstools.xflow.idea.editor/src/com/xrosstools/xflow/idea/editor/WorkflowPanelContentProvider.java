package com.xrosstools.xflow.idea.editor;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.xrosstools.idea.gef.AbstractPanelContentProvider;
import com.xrosstools.idea.gef.ContextMenuProvider;
import com.xrosstools.idea.gef.parts.EditPartFactory;
import com.xrosstools.xflow.idea.editor.menus.WorkflowContextMenuProvider;
import com.xrosstools.xflow.idea.editor.menus.WorkflowOutlineContextMenuProvider;
import com.xrosstools.xflow.idea.editor.model.*;
import com.xrosstools.xflow.idea.editor.parts.WorkflowPartFactory;
import com.xrosstools.xflow.idea.editor.treeparts.WorkflowTreePartFactory;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;

public class WorkflowPanelContentProvider extends AbstractPanelContentProvider<WorkflowDiagram> implements XflowIcons {
    private Project project;
    private VirtualFile virtualFile;
    private WorkflowDiagram diagram;

    private WorkflowDiagramFactory factory = new WorkflowDiagramFactory();

    public WorkflowPanelContentProvider(Project project, VirtualFile virtualFile) {
        super(virtualFile);
        this.project = project;
        this.virtualFile = virtualFile;
    }

    @Override
    public WorkflowDiagram getContent() throws Exception {
        diagram = factory.getFromXML(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(virtualFile.getInputStream()));
        return diagram;
    }

    @Override
    public void saveContent() throws Exception {
        String contentStr = factory.format(factory.convertToXML(diagram));
        virtualFile.setBinaryContent(contentStr.getBytes(virtualFile.getCharset()));
    }

    @Override
    public void buildPalette(JPanel palette) {
        palette.add(createConnectionButton());

        palette.add(createNodeButton("Workflow", XflowIcons.WORKFLOW, Workflow.class));

        for(NodeType type: NodeType.values()) {
            palette.add(createNodeButton(type.getDisplayName(), type.getTypeIcon(), type.getTypeClass()));
        }

//        palette.add(createPaletteButton(generateHelperAction, GENERATE_HELPER_ICON, GENERATE_HELPER));
    }

    private JButton createConnectionButton() {
        JButton btn = new JButton("Link", LINK);
        btn.setPreferredSize(new Dimension(100, 50));
        btn.setContentAreaFilled(false);
        btn.addActionListener(e -> createConnection(new Link()));
        return btn;
    }

    private JButton createNodeButton(String name, Icon icon, final Class nodeClass) {
        JButton btn = new JButton(name, icon);
//        btn.setPreferredSize(new Dimension(100, 50));
        btn.setContentAreaFilled(false);
        btn.addActionListener(e -> {
            try {
                createModel(nodeClass.newInstance());
            }catch (Throwable ex) {
                throw new IllegalArgumentException(nodeClass.getCanonicalName());
            }
        });
        return btn;
    }

    @Override
    public ActionGroup createToolbar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        return actionGroup;
    }
    @Override
    public EditPartFactory createEditPartFactory() {
        return new WorkflowPartFactory(project);
    }

    @Override
    public EditPartFactory createTreePartFactory() {
        return new WorkflowTreePartFactory();
    }

    @Override
    public ContextMenuProvider getContextMenuProvider() {
        return register(new WorkflowContextMenuProvider(project));
    }

    @Override
    public ContextMenuProvider getOutlineContextMenuProvider() {
        return register(new WorkflowOutlineContextMenuProvider(project));
    }
}
