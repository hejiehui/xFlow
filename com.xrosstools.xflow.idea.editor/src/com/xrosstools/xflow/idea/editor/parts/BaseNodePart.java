package com.xrosstools.xflow.idea.editor.parts;

import com.intellij.openapi.project.Project;
import com.xrosstools.idea.gef.actions.ImplementationUtil;
import com.xrosstools.idea.gef.figures.AbstractAnchor;
import com.xrosstools.idea.gef.figures.CommonStyleAnchor;
import com.xrosstools.idea.gef.parts.AbstractConnectionEditPart;
import com.xrosstools.idea.gef.parts.AbstractNodeEditPart;
import com.xrosstools.idea.gef.parts.EditPolicy;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.Link;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;
import com.xrosstools.xflow.idea.editor.policies.BaseNodePolicy;

public abstract class BaseNodePart extends AbstractNodeEditPart implements PropertyConstants {
    private Project project;
    private CommonStyleAnchor sourceAnchor;
    private CommonStyleAnchor targetAnchor;

    @Override
    protected EditPolicy createEditPolicy() {
        return new BaseNodePolicy();
    }

    @Override
    public void performAction() {
        BaseNode node = (BaseNode) getModel();

        String impl = (String)node.getPropertyValue(PROP_IMPLEMENTATION);

        if(!ImplementationUtil.isEmpty(impl))
            ImplementationUtil.openImpl(project, impl);
    }

    public AbstractAnchor getSourceConnectionAnchor(AbstractConnectionEditPart connectionEditPart) {
        if(sourceAnchor == null)
            sourceAnchor = new CommonStyleAnchor(getFigure(), true);
        sourceAnchor.setStyle(((Link)connectionEditPart.getModel()).getStyle());
        return sourceAnchor;
    }

    public AbstractAnchor getTargetConnectionAnchor(AbstractConnectionEditPart connectionEditPart) {
        if(targetAnchor  == null)
            targetAnchor = new CommonStyleAnchor(getFigure(), false);
        targetAnchor.setStyle(((Link)connectionEditPart.getModel()).getStyle());
        return targetAnchor;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
