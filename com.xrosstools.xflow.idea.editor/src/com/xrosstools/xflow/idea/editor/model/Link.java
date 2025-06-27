package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.model.NodeConnection;
import com.xrosstools.idea.gef.routers.RouterStyle;
import com.xrosstools.idea.gef.util.PropertyEntry;
import com.xrosstools.idea.gef.util.RouterStylePropertyDescriptor;

public class Link extends NodeConnection<BaseNode, BaseNode> implements PropertyConstants {
    private PropertyEntry<String> id = stringProperty(PROP_ID);
    private PropertyEntry<String> label = stringProperty(PROP_LABEL);
    private PropertyEntry<String> description = stringProperty(PROP_DESCRIPTION);
    private PropertyEntry<Boolean> defaultLink = booleanProperty(PROP_DEFAULT_LINK, false);
    private PropertyEntry<Boolean> trueLink = booleanProperty(PROP_TRUE_LINK, false);
    private PropertyEntry<RouterStyle> style = property(PROP_STYLE, RouterStyle.DEFAULT).setDescriptor(new RouterStylePropertyDescriptor(PROP_STYLE));

    private PropertyEntry<Integer> distance = intProperty(PROP_DISTANCE, 50);

    public Link(){
        register();
    }

    public Link(BaseNode parent, BaseNode child) {
        super(parent, child);
        register();
    }

    private void register() {
        register(id);
        register(label);
        register(description);
        register(defaultLink, ()-> getSource() instanceof InclusiveRouterNode);
        register(trueLink, ()-> getSource() instanceof BinaryRouterNode);
        register(style);
    }

    public String getDisplayText() {
        String labelVal = label.get();
        String idVal = id.get();
        return labelVal == null || labelVal.length() == 0 ? (idVal == null ? "" : idVal) : labelVal;
    }

    public RouterStyle getStyle() {
        return style.get();
    }

    public void setStyle(RouterStyle style) {
        this.style.set(style);
    }

    public Integer getDistance() {
        return distance.get();
    }

    public void setDistance(Integer _distance) {
        distance.set(_distance);
    }
}
