package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.PropertyEntry;

import java.util.ArrayList;
import java.util.List;

public class SubFlowNode extends ActivityNode {
    private XflowDiagram diagram;
    private PropertyEntry<String> subFlow = enumProperty(PROP_SUB_FLOW, "", ()-> getCandidates());
    public NodeType getType() {
        return NodeType.SUB_FLOW;
    }

    public SubFlowNode() {
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

    public XflowDiagram getDiagram() {
        return diagram;
    }

    public void setDiagram(XflowDiagram diagram) {
        this.diagram = diagram;
    }
}
