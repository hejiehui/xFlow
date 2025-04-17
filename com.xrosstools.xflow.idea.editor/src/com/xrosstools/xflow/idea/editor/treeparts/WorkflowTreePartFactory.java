package com.xrosstools.xflow.idea.editor.treeparts;

import com.xrosstools.idea.gef.parts.AbstractTreeEditPart;
import com.xrosstools.idea.gef.parts.EditPart;
import com.xrosstools.idea.gef.parts.EditPartFactory;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.Workflow;
import com.xrosstools.xflow.idea.editor.model.WorkflowDiagram;

public class WorkflowTreePartFactory implements EditPartFactory {
    public EditPart createEditPart(EditPart parent, Object model) {
        AbstractTreeEditPart part = null;
        if(model == null)
            return part;

        if(model instanceof WorkflowDiagram)
            part = new WorkflowDiagramTreePart();
        else if(model instanceof Workflow)
            part = new WorkflowTreePart();
        else if(model instanceof BaseNode)
            part = new BaseNodeTreePart();

        part.setModel(model);
        return part;
    }
}
