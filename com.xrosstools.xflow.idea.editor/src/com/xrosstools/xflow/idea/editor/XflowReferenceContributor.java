package com.xrosstools.xflow.idea.editor;

import com.intellij.psi.PsiReferenceRegistrar;
import com.xrosstools.idea.gef.references.AbstractReferenceContributor;
import com.xrosstools.xflow.idea.editor.model.NodeType;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;

public class XflowReferenceContributor extends AbstractReferenceContributor implements PropertyConstants {
    public XflowReferenceContributor() {
        super(XflowFileType.EXTENSION);
    }

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        for(NodeType type: NodeType.REFER_TO_CLASS)
            registerAttr(registrar, type.getNodeName(), PROP_IMPLEMENTATION, false);

        for(NodeType type: NodeType.REFER_TO_CLASS_METHOD)
            registerAttr(registrar, type.getNodeName(), PROP_IMPLEMENTATION, true);
    }
}
