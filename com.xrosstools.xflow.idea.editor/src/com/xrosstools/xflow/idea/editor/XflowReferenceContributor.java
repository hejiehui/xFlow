package com.xrosstools.xflow.idea.editor;

import com.intellij.psi.PsiReferenceRegistrar;
import com.xrosstools.idea.gef.references.AbstractReferenceContributor;
import com.xrosstools.xflow.idea.editor.model.NodeType;
import com.xrosstools.xflow.idea.editor.model.PropertyConstants;

public class XflowReferenceContributor extends AbstractReferenceContributor implements PropertyConstants {
    //Be careful about the upper/lower letter
    private static final String IMPLEMENTATION = "implementation";
    public XflowReferenceContributor() {
        super(XflowFileType.EXTENSION);
    }

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        for(NodeType type: NodeType.REFER_TO_CLASS)
            registerAttr(registrar, type.getNodeName(), IMPLEMENTATION, false);

        for(NodeType type: NodeType.REFER_TO_CLASS_METHOD)
            registerAttr(registrar, type.getNodeName(), IMPLEMENTATION, true);
    }
}
