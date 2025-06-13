package com.xrosstools.xflow.idea.editor.menus;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import com.xrosstools.idea.gef.ContentChangeListener;
import com.xrosstools.idea.gef.ContextMenuProvider;
import com.xrosstools.idea.gef.actions.ImplementationUtil;
import com.xrosstools.idea.gef.actions.InputTextCommandAction;
import com.xrosstools.idea.gef.commands.CreatePropertyCommand;
import com.xrosstools.idea.gef.commands.PropertyChangeCommand;
import com.xrosstools.idea.gef.parts.EditPart;
import com.xrosstools.idea.gef.routers.RouterStyle;
import com.xrosstools.idea.gef.util.ConfigMenuProvider;
import com.xrosstools.idea.gef.util.DataTypeEnum;
import com.xrosstools.idea.gef.util.PropertyEntrySource;
import com.xrosstools.xflow.idea.editor.model.*;
import com.xrosstools.xflow.idea.editor.parts.BaseNodePart;
import com.xrosstools.xflow.idea.editor.parts.LinkPart;
import com.xrosstools.xflow.idea.editor.parts.XflowDiagramPart;
import com.xrosstools.xflow.idea.editor.parts.XflowPart;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        if(NodeType.isPropertiesSupported(node.getType())) {
            addDefinedPropertiesActions(project, menu, node);
            ConfigMenuProvider.buildPropertyMenu(project, menu, PROPERTIES_CATEGORY, "property", node);
        }
    }

    private void buildLinkContextMenu(JPopupMenu menu, LinkPart part) {
        Link link = (Link)part.getModel();
        RouterStyle curStyle = link.getStyle();
        for(RouterStyle style: RouterStyle.values()) {
            if(curStyle != style)
                menu.add(createItem(style.getDisplayName(), false, new PropertyChangeCommand(link, PROP_STYLE, curStyle, style)));
        }
    }

    private void addDefinedPropertiesActions(Project project, JPopupMenu menu, BaseNode node) {
        List<String> propKeys = new ArrayList<>();
        Set<String> keys = node.keySet(PROPERTIES_CATEGORY);

        String implementation = (String) node.get(PROP_IMPLEMENTATION).get();
        if (ImplementationUtil.isEmpty(implementation))
            return;

        try {
            String className = ImplementationUtil.getClassName(implementation);
            PsiClass type = ImplementationUtil.findClass(project, className);

            if (null != type) {
                for (PsiField f : type.getFields()) {

                    if (f.getNameIdentifier().getText().startsWith(PROPERTY_KEY_PREFIX) && f.getType().getPresentableText().equals("String")) {
                        String text = f.getText();
                        int start = text.indexOf('"');
                        if (start <= 0)
                            continue;

                        int end = text.indexOf('"', start + 1);
                        text = text.substring(start + 1, end);
                        if (!keys.contains(text))
                            propKeys.add(text);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (propKeys.size() == 0)
            return;

        JMenu subSetValue = new JMenu("Predefined properties");
        for (String key : propKeys) {
            JMenu createProperty = new JMenu(key);

            for (String typeName : DataTypeEnum.CONFIGURABLE_NAMES) {
                CreatePropertyCommand cmd = new CreatePropertyCommand(PROPERTIES_CATEGORY, node, DataTypeEnum.findByDisplayName(typeName));
                cmd.setInputText(key);
                createProperty.add(createItem(typeName, false, cmd));
            }
            subSetValue.add(createProperty);
        }
        menu.add(subSetValue);
    }
}
