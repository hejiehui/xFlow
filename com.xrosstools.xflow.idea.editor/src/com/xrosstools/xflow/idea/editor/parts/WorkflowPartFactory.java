package com.xrosstools.xflow.idea.editor.parts;

import com.intellij.openapi.project.Project;
import com.xrosstools.idea.gef.parts.AbstractGraphicalEditPart;
import com.xrosstools.idea.gef.parts.EditPart;
import com.xrosstools.idea.gef.parts.EditPartFactory;
import com.xrosstools.xflow.idea.editor.model.*;

public class WorkflowPartFactory implements EditPartFactory {
    private Project project;
    private WorkflowDiagram diagram;

    public WorkflowPartFactory(Project project) {
        this.project = project;
    }

    public EditPart createEditPart(EditPart parent, Object model) {
        AbstractGraphicalEditPart part = null;

        if (model == null)
            return null;

        if(model instanceof WorkflowDiagram) {
            part = new WorkflowDiagramPart();
            diagram = (WorkflowDiagram)model;
        } else if(model instanceof Workflow) {
            part = new WorkflowPart();
        } else if(model instanceof Workflow) {
            part = new WorkflowPart();
        } else if(model instanceof TaskNode) {
            part = new TaskNodePart();
        } else if(model instanceof RouterNode) {
            part = new RouterNodePart();
        } else if(model instanceof Link) {
            part = new LinkPart();
        }

        part.setModel(model);
        return part;
    }
}
