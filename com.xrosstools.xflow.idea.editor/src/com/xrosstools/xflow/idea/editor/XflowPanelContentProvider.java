package com.xrosstools.xflow.idea.editor;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.xrosstools.idea.gef.AbstractPanelContentProvider;
import com.xrosstools.idea.gef.ContextMenuProvider;
import com.xrosstools.idea.gef.parts.EditPartFactory;
import com.xrosstools.xflow.idea.editor.actions.GenerateHelperAction;
import com.xrosstools.xflow.idea.editor.menus.XflowContextMenuProvider;
import com.xrosstools.xflow.idea.editor.menus.XflowOutlineContextMenuProvider;
import com.xrosstools.xflow.idea.editor.model.*;
import com.xrosstools.xflow.idea.editor.parts.XflowPartFactory;
import com.xrosstools.xflow.idea.editor.treeparts.XflowTreePartFactory;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;

public class XflowPanelContentProvider extends AbstractPanelContentProvider<XflowDiagram> implements XflowIcons {
    private Project project;
    private VirtualFile virtualFile;
    private XflowDiagram diagram;

    private XflowDiagramFactory factory = new XflowDiagramFactory();

    public XflowPanelContentProvider(Project project, VirtualFile virtualFile) {
        super(virtualFile);
        this.project = project;
        this.virtualFile = virtualFile;
    }

    @Override
    public XflowDiagram getContent() throws Exception {
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

        palette.add(createNodeButton("Xflow", XflowIcons.FLOW_ICON, Xflow.class));

        for(NodeType type: NodeType.values()) {
            palette.add(createNodeButton(type.getDisplayName(), type.getTypeIcon(), type.getTypeClass()));
        }

        palette.add(createPaletteButton(register(new GenerateHelperAction(project, virtualFile)), GENERATE_HELPER_ICON, GenerateHelperAction.GENERATE_HELPER));
    }

    private JButton createConnectionButton() {
        JButton btn = new JButton("Link", LINK_ICON);
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
        return new XflowPartFactory(project);
    }

    @Override
    public EditPartFactory createTreePartFactory() {
        return new XflowTreePartFactory();
    }

    @Override
    public ContextMenuProvider getContextMenuProvider() {
        return register(new XflowContextMenuProvider(project));
    }

    @Override
    public ContextMenuProvider getOutlineContextMenuProvider() {
        return register(new XflowOutlineContextMenuProvider(project));
    }
}
