package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.model.Diagram;
import com.xrosstools.idea.gef.util.PropertyEntry;

public class WorkflowDiagram extends Diagram<Workflow> implements PropertyConstants{
    private PropertyEntry<String> description = stringProperty(PROP_DESCRIPTION);
    private PropertyEntry<String> evaluator = stringProperty(PROP_EVALUATOR);

    public WorkflowDiagram() {
        register(evaluator);
        register(description);
    }

    public String getDescription() {
        return description.get();
    }

    public String getEvaluator() {
        return evaluator.get();
    }
}
