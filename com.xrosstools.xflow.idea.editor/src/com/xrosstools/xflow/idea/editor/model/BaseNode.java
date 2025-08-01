package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.model.Node;
import com.xrosstools.idea.gef.util.PropertyEntry;
import com.xrosstools.idea.gef.util.PropertyEntrySource;
import org.apache.commons.lang.text.StrBuilder;

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

    public String getId() {
        return id.get();
    }

    public String getDescription() {
        return description.get();
    }

    public abstract NodeType getType();

    public String getDisplayText() {
        String labelVal = label.get();
        String idVal = id.get();
        return labelVal == null || labelVal.length() == 0 ? (idVal == null ? "" : idVal) : labelVal;
    }

    public String getTooltips() {
        StrBuilder sb = new StrBuilder();
        if(getDisplayText() != null)
            sb.append(getDisplayText());

        if(description.get() != null)
            sb.append(sb.size() == 0 ? description.get() : "\n" +  description.get());
        return sb.toString();
    }
}
