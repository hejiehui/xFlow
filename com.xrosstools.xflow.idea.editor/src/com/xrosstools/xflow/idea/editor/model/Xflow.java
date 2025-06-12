package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.model.Diagram;
import com.xrosstools.idea.gef.util.PropertyEntry;

public class Xflow extends Diagram<BaseNode> implements PropertyConstants{
    private PropertyEntry<String> name = stringProperty(PROP_ID);
    private PropertyEntry<String> description = stringProperty(PROP_DESCRIPTION);

    public Xflow() {
        register(name);
        register(description);
    }

    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getTooltips() {
        return String.format("%s/n%s", getName(), description.get());
    }
}
