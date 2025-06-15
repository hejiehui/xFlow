package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.PropertyEntry;

import java.util.ArrayList;
import java.util.List;

public class SubflowActivityNode extends ActivityNode {
    private PropertyEntry<String> implementation = stringProperty(PROP_IMPLEMENTATION);
    private XflowDiagram diagram;
    private PropertyEntry<String> subFlow = enumProperty(PROP_SUBFLOW, "", ()-> getCandidates());

    public NodeType getType() {
        return NodeType.SUBFLOW_ACTIVITY;
    }

    public SubflowActivityNode() {
        register(implementation);
        register(subFlow);
    }
    private String[] getCandidates() {

        List<String> names = new ArrayList<>();
        for(Xflow xflow: diagram.getChildren()){
            if(xflow.getName() == null || xflow.getName().trim().isEmpty())
                continue;
            names.add(xflow.getName());
        }
        return names.toArray(new String[names.size()]);
    }

    public void setDiagram(XflowDiagram diagram) {
        this.diagram = diagram;
    }
}
