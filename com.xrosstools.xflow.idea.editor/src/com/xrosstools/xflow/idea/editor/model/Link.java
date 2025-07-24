package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.model.NodeConnection;
import com.xrosstools.idea.gef.routers.RouterStyle;
import com.xrosstools.idea.gef.util.*;

public class Link extends NodeConnection<BaseNode, BaseNode> implements PropertyConstants {
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private PropertyEntry<String> id = stringProperty(PROP_ID);
    private PropertyEntry<String> label = stringProperty(PROP_LABEL);
    private PropertyEntry<String> description = stringProperty(PROP_DESCRIPTION);
    private PropertyEntry<Boolean> defaultLink = booleanProperty(PROP_DEFAULT_LINK, false);
    private PropertyEntry<RouterStyle> style = property(PROP_STYLE, RouterStyle.DEFAULT).setDescriptor(new RouterStylePropertyDescriptor(PROP_STYLE));

    private PropertyEntry<Integer> distance = intProperty(PROP_DISTANCE, 50);

    public Link(){
        register();
    }

    public Link(BaseNode source, BaseNode target) {
        super(source, target);
        register();
    }

    private void register() {
        register(id);
        updateIdDescriptor();
        register(label);
        register(description);
        register(defaultLink, ()-> getSource() instanceof InclusiveRouterNode);
        register(style);
    }

    public void setSource(BaseNode _source) {
        super.setSource(_source);
        if(_source != null && id != null)
            updateIdDescriptor();
    }

    private void updateIdDescriptor() {
        IPropertyDescriptor descriptor = id.getDescriptor();
        if(getSource() instanceof BinaryRouterNode && descriptor instanceof StringPropertyDescriptor) {
            id.setDescriptor(new ListPropertyDescriptor(new String[]{FALSE, TRUE}));
            if(getSource().getOutputs().size() == 1) {
                String theOtherLinkId = (String)getSource().getOutputs().get(0).get(PROP_ID).get();
                id.set(TRUE.equals(theOtherLinkId) ? FALSE : TRUE);
            }else
                id.set(TRUE);
        }else if(!(getSource() instanceof BinaryRouterNode) && descriptor instanceof ListPropertyDescriptor)
            id.setDescriptor(DataTypeEnum.STRING.createDescriptor());
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
