package com.xrosstools.xflow.idea.editor.parts;

import com.intellij.openapi.project.Project;
import com.xrosstools.idea.gef.parts.AbstractGraphicalEditPart;
import com.xrosstools.idea.gef.parts.EditPart;
import com.xrosstools.idea.gef.parts.EditPartFactory;
import com.xrosstools.xflow.idea.editor.model.*;

public class XflowPartFactory implements EditPartFactory {
    private Project project;
    private XflowDiagram diagram;

    public XflowPartFactory(Project project) {
        this.project = project;
    }

    public EditPart createEditPart(EditPart parent, Object model) {
        AbstractGraphicalEditPart part = null;

        if (model == null)
            return null;

        if(model instanceof XflowDiagram) {
            part = new XflowDiagramPart();
            diagram = (XflowDiagram)model;
        } else if(model instanceof Xflow) {
            part = new XflowPart();
        } else if(model instanceof StartNode) {
            part = new ImageNodePart();
        } else if(model instanceof EndNode) {
            part = new ImageNodePart();
        } else if(model instanceof ActivityNode) {
            part = new ActivityNodePart();
            ((ActivityNodePart) part).setProject(project);
            if(model instanceof SubFlowNode)
                ((SubFlowNode)model).setDiagram(diagram);
        } else if(model instanceof RouterNode) {
            part = new RouterNodePart();
            ((RouterNodePart) part).setProject(project);
        } else if(model instanceof Link) {
            part = new LinkPart();
        }

        part.setModel(model);
        return part;
    }
}
