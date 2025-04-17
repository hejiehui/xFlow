package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.PropertySourceXmlRegister;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static com.xrosstools.idea.gef.util.XmlHelper.*;

public class WorkflowDiagramFactory implements PropertyConstants {
    private static final String XFLOW = "xflow";
    private static final String WORKFLOWS = "workflows";
    private static final String WORKFLOW = "workflow";

    private static final String NAME = PROP_NAME.toLowerCase();
    private static final String DESCRIPTION = PROP_DESCRIPTION.toLowerCase();
    private static final String EVALUATOR = PROP_EVALUATOR.toLowerCase();

    private static final String NODES = "nodes";
    private static final String LOCATION = "location";

    private static final String LINKS = "links";
    private static final String LINK = "link";
    private static final String SOURCE_INDEX = "source_index";
    private static final String TARGET_INDEX = "target_index";

    private PropertySourceXmlRegister<NodeType> register = new PropertySourceXmlRegister<>();

    public WorkflowDiagramFactory() {
        register.register(NodeType.AUTO_TASK).attributes(PROP_NAME).nodes(PROP_DESCRIPTION);
        register.register(NodeType.MANUAL_TASK).attributes(PROP_NAME).nodes(PROP_DESCRIPTION);
        register.register(NodeType.VALIDATOR).attributes(PROP_NAME).nodes(PROP_DESCRIPTION);
        register.register(NodeType.LOCATOR).attributes(PROP_NAME).nodes(PROP_DESCRIPTION);
        register.register(NodeType.DISPATCHER).attributes(PROP_NAME).nodes(PROP_DESCRIPTION);
    }

    public WorkflowDiagram getFromXML(Document doc) {
        Node root = doc.getElementsByTagName(XFLOW).item(0);
        WorkflowDiagram diagram = new WorkflowDiagram();

        diagram.setPropertyValue(PROP_DESCRIPTION, getChildNodeText(root, DESCRIPTION));
        diagram.setPropertyValue(PROP_EVALUATOR, getAttribute(root, EVALUATOR));

        diagram.getChildren().addAll(readWorkflows(getChildNode(root, WORKFLOWS)));

        return diagram;
    }

    private List<Workflow> readWorkflows(Node workflowsNode) {
        List<Workflow> workflows = new ArrayList<>();
        List<Node> nodes = getValidChildNodes(workflowsNode);

        for(int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Workflow workflow = new Workflow();

            workflow.setPropertyValue(PROP_NAME, getAttribute(node, NAME));
            workflow.setPropertyValue(PROP_DESCRIPTION, getChildNodeText(node, DESCRIPTION));

            workflow.setChildren(readNodes(getChildNode(node, NODES)));
            linkNode(workflow.getChildren(), getChildNode(node, LINKS));

            workflows.add(workflow);
        }

        return workflows;
    }

    private List<BaseNode> readNodes(Node docNode) {
        List<BaseNode> nodes = new ArrayList<>();
        List<Node> nodeNodes = getValidChildNodes(docNode);

        for(int i = 0; i < nodeNodes.size(); i++) {
            BaseNode node = null;
            Node nodeNode = nodeNodes.get(i);
            NodeType type = NodeType.findByNodeName(nodeNode.getNodeName());

            try {
                node = (BaseNode) type.getTypeClass().newInstance();
            } catch (Throwable e) {
                throw new IllegalArgumentException(type.getTypeClass().getCanonicalName());
            }

            if(register.contains(type)) {
                register.readProperties(type, nodeNode, node);
                String[] location = getAttribute(nodeNode, LOCATION).split(",");
                node.setLocation(new Point(Integer.parseInt(location[0]), Integer.parseInt(location[1])));
            } else
                throw new IllegalArgumentException(type.name());

            nodes.add(node);
        }

        return nodes;
    }

    private void linkNode(List<BaseNode> nodes, Node docNode) {
        List<Node> nodeNodes = getValidChildNodes(docNode);
        for (int i = 0; i < nodeNodes.size(); i++) {
            Node connectionNode = nodeNodes.get(i);

            BaseNode parent = nodes.get(getIntAttribute(connectionNode, SOURCE_INDEX));
            BaseNode child = nodes.get(getIntAttribute(connectionNode, TARGET_INDEX));
            new Link(parent, child);
        }
    }

    public Document convertToXML(WorkflowDiagram diagram) {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement(XFLOW);
            doc.appendChild(root);

            root.appendChild(createNode(doc, DESCRIPTION, diagram.getDescription()));
            root.setAttribute(EVALUATOR, diagram.getEvaluator());

            Element nodes = createNode(doc, root, WORKFLOWS);
            writeWorkflows(doc, nodes, diagram.getChildren());

            return doc;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void writeWorkflows(Document doc, Element nodes, List<Workflow> workflows) {
        for(Workflow workflow: workflows) {
            Element workFlowNode = createNode(doc, nodes, WORKFLOW);
            workFlowNode.setAttribute(NAME, workflow.getName());
            workFlowNode.appendChild(createNode(doc, DESCRIPTION, workflow.getDescription()));

            writeNodes(doc, createNode(doc, workFlowNode, NODES), workflow.getChildren());
            writeLinks(doc, createNode(doc, workFlowNode, LINKS), workflow.getChildren());
        }
    }

    private void writeNodes(Document doc, Element nodesNode, List<BaseNode> nodes) {
        for(BaseNode baseNode: nodes) {
            String nodeName = baseNode.getType().name().toLowerCase();
            Element node = createNode(doc, nodesNode, nodeName);
            NodeType type = baseNode.getType();

            if(register.contains(type)) {
                register.writeProperties(doc, type, node, baseNode);
                node.setAttribute(LOCATION, String.format("%d,%d", baseNode.getLocation().x, baseNode.getLocation().y));
            } else
                throw new IllegalArgumentException(type.name());
        }
    }

    private void writeLinks(Document doc, Element linksNode, List<BaseNode> nodes) {
        for (BaseNode node: nodes) {
            for(Link conn: node.getOutputs()) {
                Element connNode = createNode(doc, linksNode, LINK);
                connNode.setAttribute(SOURCE_INDEX, String.valueOf(nodes.indexOf(node)));
                connNode.setAttribute(TARGET_INDEX, String.valueOf(nodes.indexOf(conn.getTarget())));
            }
        }
    }

    public static String format(Document doc) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerFactory tFactory =TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(out);
        transformer.transform(source, result);

        // To make well formated document
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(new ByteArrayInputStream(out.toByteArray()));

        XMLWriter writer = null;
        StringWriter stringWriter = new StringWriter();
        OutputFormat format = new OutputFormat(" ", true);
        writer = new XMLWriter(stringWriter, format);
        writer.write(document);
        writer.flush();
        return stringWriter.toString();
    }
}
