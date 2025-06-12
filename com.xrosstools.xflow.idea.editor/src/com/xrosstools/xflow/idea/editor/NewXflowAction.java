package com.xrosstools.xflow.idea.editor;

import com.xrosstools.idea.gef.DefaultNewModelFileAction;

public class NewXflowAction extends DefaultNewModelFileAction {
    public NewXflowAction() {
        super("Xross Flow", XflowFileType.EXTENSION, XflowIcons.XFLOW_ICON, "new_xflow_file", "/templates/template.xflow");
    }
}
