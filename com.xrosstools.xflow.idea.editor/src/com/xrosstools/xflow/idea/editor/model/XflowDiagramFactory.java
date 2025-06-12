package com.xrosstools.xflow.idea.editor.model;

import com.xrosstools.idea.gef.util.ConfigXmlAccessor;
import com.xrosstools.idea.gef.util.PropertyEntrySource;
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

public class XflowDiagramFactory implements PropertyConstants {
    private static final String XFLOW = "xflow";
    private static final String FLOW = "flow";

    private static final String NAME = PROP_ID.toLowerCase();
    private static final String DESCRIPTION = PROP_DESCRIPTION.toLowerCase();
    private static final String EVALUATOR = PROP_EVALUATOR.toLowerCase();

    private static final String NODES = "nodes";
    private static final String LOCATION = "location";

    private static final String LINKS = "links";
    private static final String LINK = "link";
    private static final String SOURCE_INDEX = "source_index";
    private static final String TARGET_INDEX = "target_index";

    String PROPERTIES = PROPERTIES_CATEGORY.toLowerCase();
    String PROPERTY = "property";

    private PropertySourceXmlRegister<Object> register = new PropertySourceXmlRegister<>();
    private ConfigXmlAccessor propertyAccessor = new ConfigXmlAccessor(PROPERTIES_CATEGORY, PROPERTY);

    public XflowDiagramFactory() {
        register.register(NodeType.START).attributes(PROP_ID).nodes(PROP_DESCRIPTION);
        register.register(NodeType.END).attributes(PROP_ID).nodes(PROP_DESCRIPTION);

        register.register(NodeType.AUTO_ACTIVITY).attributes(PROP_ID, PROP_LABEL, PROP_IMPLEMENTATION).nodes(PROP_DESCRIPTION);
        register.register(NodeType.TASK_ACTIVITY).attributes(PROP_ID, PROP_LABEL, PROP_IMPLEMENTATION).nodes(PROP_DESCRIPTION);
        register.register(NodeType.EVENT_ACTIVITY).attributes(PROP_ID, PROP_LABEL, PROP_IMPLEMENTATION).nodes(PROP_DESCRIPTION);
        register.register(NodeType.WAIT_ACTIVITY).attributes(PROP_ID, PROP_LABEL, PROP_DELAY, PROP_TIME_UNIT).nodes(PROP_DESCRIPTION);
        register.register(NodeType.SUB_FLOW).attributes(PROP_ID, PROP_LABEL).nodes(PROP_DESCRIPTION);

        register.register(NodeType.BINARY_ROUTER).attributes(PROP_ID, PROP_LABEL, PROP_IMPLEMENTATION).nodes(PROP_DESCRIPTION);
        register.register(NodeType.EXCLUSIVE_ROUTER).attributes(PROP_ID, PROP_LABEL, PROP_IMPLEMENTATION).nodes(PROP_DESCRIPTION);
        register.register(NodeType.INCLUSIVE_ROUTER).attributes(PROP_ID, PROP_LABEL, PROP_IMPLEMENTATION).nodes(PROP_DESCRIPTION);
        register.register(NodeType.PARALLEL_ROUTER).attributes(PROP_ID, PROP_LABEL).nodes(PROP_DESCRIPTION);

        register.register(Link.class).attributes(PROP_ID, PROP_LABEL, PROP_DEFAULT_LINK, PROP_TRUE_LINK, PROP_STYLE).nodes(PROP_DESCRIPTION);
    }

    public XflowDiagram getFromXML(Document doc) {
        Node root = doc.getElementsByTagName(XFLOW).item(0);
        XflowDiagram diagram = new XflowDiagram();

        diagram.setPropertyValue(PROP_DESCRIPTION, getChildNodeText(root, DESCRIPTION));
        diagram.setPropertyValue(PROP_EVALUATOR, getAttribute(root, EVALUATOR));
        readProperties(root, diagram);

        diagram.getChildren().addAll(readFlows(getValidChildNodes(root, FLOW)));

        return diagram;
    }

    private List<Xflow> readFlows(List<Node> nodes) {
        List<Xflow> xflows = new ArrayList<>();

        for(int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Xflow xflow = new Xflow();

            xflow.setPropertyValue(PROP_ID, getAttribute(node, NAME));
            xflow.setPropertyValue(PROP_DESCRIPTION, getChildNodeText(node, DESCRIPTION));
            readProperties(node, xflow);

            xflow.setChildren(readNodes(getChildNode(node, NODES)));
            linkNode(xflow.getChildren(), getChildNode(node, LINKS));

            xflows.add(xflow);
        }

        return xflows;
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

                if(NodeType.isPropertiesSupported(type))
                    readProperties(nodeNode, node);
            } else
                throw new IllegalArgumentException(type.name());

            nodes.add(node);
        }

        return nodes;
    }

    private void readProperties(Node nodeNode, PropertyEntrySource source) {
        Node propertiesNode = getChildNode(nodeNode, PROPERTIES);
        if(propertiesNode != null)
            propertyAccessor.readProperties(propertiesNode, source);
    }

    private void linkNode(List<BaseNode> nodes, Node docNode) {
        List<Node> nodeNodes = getValidChildNodes(docNode);
        for (int i = 0; i < nodeNodes.size(); i++) {
            Node connectionNode = nodeNodes.get(i);

            BaseNode parent = nodes.get(getIntAttribute(connectionNode, SOURCE_INDEX));
            BaseNode child = nodes.get(getIntAttribute(connectionNode, TARGET_INDEX));

            Link link = new Link(parent, child);
            register.readProperties(Link.class, connectionNode, link);
        }
    }

    public Document convertToXML(XflowDiagram diagram) {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement(XFLOW);
            doc.appendChild(root);

            root.appendChild(createNode(doc, DESCRIPTION, diagram.getDescription()));
            root.setAttribute(EVALUATOR, diagram.getEvaluator());

            Element propertiesNode = createNode(doc, root, PROPERTIES);
            propertyAccessor.writeProperties(doc, propertiesNode, diagram);

            writeFlows(doc, root, diagram.getChildren());

            return doc;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void writeFlows(Document doc, Element nodes, List<Xflow> xflows) {
        for(Xflow xflow: xflows) {
            Element workFlowNode = createNode(doc, nodes, FLOW);
            workFlowNode.setAttribute(NAME, xflow.getName());
            workFlowNode.appendChild(createNode(doc, DESCRIPTION, xflow.getDescription()));
            propertyAccessor.writeProperties(doc, createNode(doc, workFlowNode, PROPERTIES), xflow);

            writeNodes(doc, createNode(doc, workFlowNode, NODES), xflow.getChildren());
            writeLinks(doc, createNode(doc, workFlowNode, LINKS), xflow.getChildren());
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

                if(NodeType.isPropertiesSupported(type))
                    propertyAccessor.writeProperties(doc, createNode(doc, node, PROPERTIES), baseNode);
            } else
                throw new IllegalArgumentException(type.name());
        }
    }

    private void writeLinks(Document doc, Element linksNode, List<BaseNode> nodes) {
        for (BaseNode node: nodes) {
            for(Link conn: node.getOutputs()) {
                Element connNode = createNode(doc, linksNode, LINK);
                register.writeProperties(doc, Link.class, connNode, conn);
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
