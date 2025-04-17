package com.xrosstools.xflow.idea.editor;

import com.xrosstools.idea.gef.DefaultNewModelFileAction;

public class NewXflowAction extends DefaultNewModelFileAction {
    public NewXflowAction() {
        super("Xross Workflow", XflowFileType.EXTENSION, XflowIcons.XFLOW, "new_xflow_file", "/templates/template.xflow");
    }
}
