package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.model.Diagram;
import com.xrosstools.idea.gef.util.PropertyEntry;

public class XflowDiagram extends Diagram<Xflow> implements PropertyConstants{
    private PropertyEntry<String> description = stringProperty(PROP_DESCRIPTION);
    private PropertyEntry<String> evaluator = stringProperty(PROP_EVALUATOR);

    public XflowDiagram() {
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
