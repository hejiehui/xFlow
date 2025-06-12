package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.model.Node;
import com.xrosstools.idea.gef.util.PropertyEntry;
import com.xrosstools.idea.gef.util.PropertyEntrySource;

import java.awt.*;

public abstract class BaseNode extends Node<Link> implements PropertyConstants {
    private PropertyEntry<String> id = stringProperty(PROP_ID);
    private PropertyEntry<String> label = stringProperty(PROP_LABEL);
    private PropertyEntry<String> description = stringProperty(PROP_DESCRIPTION);

    private PropertyEntrySource config = new PropertyEntrySource();

    private Point location;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
        firePropertyChange(PROP_LOCATION, null, location);
    }

    public BaseNode() {
        register(id);
        register(label);
        register(description);
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public abstract NodeType getType();

    public String getDisplayText() {
        String labelVal = label.get();
        String idVal = id.get();
        return labelVal != null && labelVal.length() == 0 ? (idVal == null ? "" : idVal) : labelVal;
    }

    public String getTooltips() {
        return String.format("%s/n%s", getDisplayText(), description.get());
    }
}
