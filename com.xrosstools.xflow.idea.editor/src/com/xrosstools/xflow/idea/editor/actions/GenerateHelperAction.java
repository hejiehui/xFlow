package com.xrosstools.xflow.idea.editor.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.xrosstools.idea.gef.ContentChangeListener;
import com.xrosstools.idea.gef.actions.AbstractCodeGenerator;
import com.xrosstools.xflow.idea.editor.model.BaseNode;
import com.xrosstools.xflow.idea.editor.model.Xflow;
import com.xrosstools.xflow.idea.editor.model.XflowDiagram;

import java.time.ZonedDateTime;

import static com.xrosstools.idea.gef.actions.CodeGenHelper.*;

public class GenerateHelperAction extends AbstractCodeGenerator implements ContentChangeListener<XflowDiagram> {
    public static final String GENERATE_HELPER = "Helper";
    private static final String CREATE_XFLOW =
            "    public static class %s {\n" +//Model name, constants
            "%s" +
            "        public static Xflow create() {\n" +
            "            return load().create(\"%s\");\n" +
            "        }\n" +
            "    }\n\n";

    private static final String FLOW_COMMENTS =
            "    //%s\n";

    private static final String INVALID_FLOW_NAME =
            "    /*  Error!!! No. %d flow's name is empty. */\n";

    private static final String CONSTANT_COMMENTS =
            "        //%s\n";

    private static final String CONSTANT_DEF =
            "        public static final String %s = \"%s\";\n\n";//label = "Id"

    private static final String NODE_DEF_HEADER =
            "        /*  Node Names */\n";

    private static final String INVALID_NODE_ID =
            "        /*  Error!!! No. %d node's Id is empty. */\n";

    private Project project;
    private VirtualFile file;
    private XflowDiagram diagram;

    public GenerateHelperAction(Project project, VirtualFile file){
        super(project, "Generate model factory");
        this.project = project;
        this.file = file;
    }

    public void contentChanged(XflowDiagram diagram) {
        this.diagram = diagram;
    }

    public String getDefaultFileName() {
        return fileToClassName(file.getNameWithoutExtension());
    }

    @Override
    public String getContent(String packageName, String fileName) {
        StringBuffer codeBuf = getTemplate("/templates/HelperTemplate.txt", this.getClass());
        replace(codeBuf, "!PACKAGE!", packageName);
        replace(codeBuf, "!DESCRIPTION!", getValue(diagram.getDescription()));
        replace(codeBuf, "!LAST_GENERATE_TIME!", ZonedDateTime.now().toString());
        replace(codeBuf, "!FACTORY_CLASS!", fileName);
        replace(codeBuf, "!MODEL_PATH!", findResourcesPath(project, file));

        replace(codeBuf, "!FLOW_DEFINITIONS!", "\n" + generateBody().toString());

        return codeBuf.toString();
    }

    private StringBuffer generateBody() {
        StringBuffer constants = new StringBuffer();

        int i = 0;
        for(Xflow xflow: diagram.getChildren()) {
            i++;
            StringBuffer buf = new StringBuffer();
            if(isEmpty(xflow.getName())) {
                constants.append(String.format(INVALID_FLOW_NAME, i));
                continue;
            }

            int k = 0;
            buf.append(NODE_DEF_HEADER);
            for(BaseNode node: xflow.getChildren()) {
                k++;
                if(isEmpty(node.getId())) {
                    buf.append(String.format(INVALID_NODE_ID, k));
                    continue;
                }

                appendDesc(buf, CONSTANT_COMMENTS, node.getDescription());
                appendLabelId(buf, node.getDisplayText(), node.getId());
            }

            appendDesc(buf, FLOW_COMMENTS, xflow.getDescription());
            String createMachine = String.format(CREATE_XFLOW, toClassName(xflow.getName()), buf.toString(), xflow.getName());
            constants.append(createMachine);

        }

        return constants;
    }

    private void appendLabelId(StringBuffer buf, String label, String id) {
        label = label.contains(" ") ? label.replace(' ', '_') : label;

        buf.append(String.format(CONSTANT_DEF, label.toUpperCase(), id));
    }
}
