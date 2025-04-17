package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.model.Node;
import com.xrosstools.idea.gef.util.PropertyEntry;

import java.awt.*;

public abstract class BaseNode extends Node<Link> implements PropertyConstants{
    private PropertyEntry<String> name = stringProperty(PROP_NAME);
    private PropertyEntry<String> description = stringProperty(PROP_DESCRIPTION);
    private Point location;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
        firePropertyChange(PROP_LOCATION, null, location);
    }

    public BaseNode() {
        register(name);
        register(description);
    }

    public abstract NodeType getType();

    public String getName() {
        return name.get();
    }

    public String getTooltips() {
        return String.format("%s/n%s", getName(), description.get());
    }
}
