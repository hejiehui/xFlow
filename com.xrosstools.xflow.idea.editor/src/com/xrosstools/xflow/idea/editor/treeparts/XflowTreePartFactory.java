package com.xrosstools.xflow.idea.editor.treeparts;

import com.xrosstools.idea.gef.parts.AbstractTreeEditPart;
import com.xrosstools.idea.gef.parts.EditPart;
import com.xrosstools.idea.gef.parts.EditPartFactory;
import com.xrosstools.idea.gef.treeparts.CollectionTreePart;
import com.xrosstools.idea.gef.treeparts.PropertyEntryTreePart;
import com.xrosstools.idea.gef.util.PropertyEntry;
import com.xrosstools.xflow.idea.editor.XflowIcons;
import com.xrosstools.xflow.idea.editor.model.*;

import java.util.Collection;

public class XflowTreePartFactory implements EditPartFactory {
    public EditPart createEditPart(EditPart parent, Object model) {
        AbstractTreeEditPart part = null;
        if(model == null)
            return part;

        if(model instanceof XflowDiagram)
            part = new XflowDiagramTreePart();
        else if(model instanceof Xflow)
            part = new XflowTreePart();
        else if(model instanceof BaseNode)
            part = new BaseNodeTreePart();
        else if(model instanceof Link)
            part = new LinkTreePart();
        else if(model instanceof Collection)
            part = new CollectionTreePart(PropertyConstants.PROPERTIES_CATEGORY, XflowIcons.PROPERTIES_ICON, (Collection)model);
        else if(model instanceof PropertyEntry)
            part = new PropertyEntryTreePart(XflowIcons.PROPERTY_ICON);

        part.setModel(model);
        return part;
    }
}
