package com.xrosstools.xflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xrosstools.xflow.def.DataTypeEnum;
import com.xrosstools.xflow.def.ElementConstants;
import com.xrosstools.xflow.def.ImplementationDef;
import com.xrosstools.xflow.def.LinkDef;
import com.xrosstools.xflow.def.NodeDef;
import com.xrosstools.xflow.def.XflowDef;

public class XflowFactory implements ElementConstants {
    private static final ConcurrentHashMap<String, XflowFactory> factories = new ConcurrentHashMap<>();

    private DataMap config;
	private Map<String, XflowDef> flowDefs = new HashMap<>();
	
	public Xflow create(String name) {
		if(!flowDefs.containsKey(name))
			throw new IllegalArgumentException("Can not find flow: " + name);
		return flowDefs.get(name).create(this);
	}
	
	public static XflowFactory load(URL url) {
	    String path = url.toString();
	    
	    if(isLoaded(url.toString()))
	        return factories.get(path);
	    
        try {
			return getFactory(path, load(url.openStream()));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * It will first check model file from file path, if it does not exist, it will try classpath then. 
	 * @param path path of the model file
	 */
	public static XflowFactory load(String path) {
        if(isLoaded(path))
            return factories.get(path);
        
		InputStream in;
		File f = new File(path);
		try {
			if(f.exists())
				in = new FileInputStream(f);
			else {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				if (classLoader == null) {
					classLoader = XflowFactory.class.getClassLoader();
				}
				in = classLoader.getResource(path).openStream();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
        
        return getFactory(path, load(in));
	}
	
	private static XflowFactory getFactory(String path, XflowFactory factory) {
		XflowFactory oldFactory = factories.putIfAbsent(path, factory);        
        return oldFactory == null ? factory : oldFactory;
	}
	
	public static XflowFactory load(InputStream in) {
		XflowFactory factory = null;
		try{
			Document doc= DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			factory = getFromDocument(doc);
		} catch(Throwable e) {
			throw new IllegalStateException(e);
		}finally {
			try{
				if(in != null)
					in.close();
			}catch(Throwable e1){
			}
		}
		return factory;
	}
	
	private static boolean isLoaded(String path) {
	    return factories.containsKey(path);
	}
    
	private static XflowFactory getFromDocument(Document doc) throws Exception {
		XflowFactory factory = new XflowFactory();
		factory.readDiagram(doc);
		return factory;
	}

	private void readDiagram(Document doc) throws Exception {
		Node root = doc.getElementsByTagName(XFLOW).item(0);
		config = readConfig(root);
		
		List<Node> flowNodes = getValidChildNodes(root, FLOW);
        for (int i = 0; i < flowNodes.size(); i++) {
        	Node flowNode = flowNodes.get(i);
        	XflowDef flowDef = readFlow(doc, flowNode);
        	String flowId = flowDef.getName();

        	if(flowDefs.containsKey(flowId))
    			throw new IllegalArgumentException(String.format("Flow id \"%s\" is duplicated.", flowId));

        	flowDefs.put(flowId, flowDef);
        }		
	}

	private XflowDef readFlow(Document doc, Node flowNode) {
    	DataMap flowConfig = readConfig(flowNode);
		List<NodeDef> nodeDefs = readNodes(getChildNode(flowNode, NODES));
		List<LinkDef> linkDefs = readLinks(getChildNode(flowNode, LINKS));
		return new XflowDef(getAttribute(flowNode, PROP_ID), nodeDefs, linkDefs, config, flowConfig);
	}

	private List<NodeDef> readNodes(Node nodesNode) {
        List<Node> nodeNodes = getValidChildNodes(nodesNode);
        if (nodeNodes.size() == 0)
            return Collections.emptyList();


		List<NodeDef> nodes = new ArrayList<>();
        for (int i = 0; i < nodeNodes.size(); i++) {
            Node nodeNode = nodeNodes.get(i);
            String nodeName = nodeNode.getNodeName();
            String name = getAttribute(nodeNode, PROP_ID);
            String implementation = getAttribute(nodeNode, PROP_IMPLEMENTATION);

            NodeDef def;
            switch (nodeName) {
            case START_NODE:
            	def = NodeDef.startNodeDef(name);
				break;
            case END_NODE:
            	def = NodeDef.endNodeDef(name);
				break;
            case AUTO_ACTIVITY_NODE:
            	def = NodeDef.activityNodeDef(name, new ImplementationDef<AutoActivity>(implementation));
				break;
            case TASK_ACTIVITY_NODE:
            	def = NodeDef.taskActivityNodeDef(name, new ImplementationDef<TaskActivity>(implementation));
				break;
            case EVENT_ACTIVITY_NODE:
            	def = NodeDef.eventActivityNodeDef(name, new ImplementationDef<EventActivity>(implementation));
				break;
            case WAIT_ACTIVITY_NODE:
            	int delay = getIntAttribute(nodeNode, PROP_DELAY);
            	TimeUnit unit = TimeUnit.valueOf(getAttribute(nodeNode, PROP_TIME_UNIT));
            	def = NodeDef.waitActivityNodeDef(name, delay, unit);
				break;
            case BINARY_ROUTER_NODE:
            	def = NodeDef.binaryRouteNodeDef(name, implementation == null ? null : new ImplementationDef<BinaryRouter>(implementation));
				break;
            case INCLUSIVE_ROUTER_NODE:
            	def = NodeDef.inclusiveRouteNodeDef(name, implementation == null ? null : new ImplementationDef<InclusiveRouter>(implementation));
				break;
            case EXCLUSIVE_ROUTER_NODE:
            	def = NodeDef.exclusiveRouteNodeDef(name, implementation == null ? null : new ImplementationDef<ExclusiveRouter>(implementation));
				break;
            case PARALLE_ROUTER_NODE:
            	def = NodeDef.paralleRouterNodeDef(name);
				break;
			default:
				throw new IllegalArgumentException(String.format("Node name: \"%s\" is unknown", nodeName));
			}

            def.setConfig(readConfig(nodeNode));
            nodes.add(def);
        }

		return nodes;
	}
	
	private List<LinkDef> readLinks(Node linksNode) {
        List<Node> linkNodes = getValidChildNodes(linksNode);
        if (linkNodes.size() == 0)
            return Collections.emptyList();


		List<LinkDef> linkDefs = new ArrayList<>();
        for (int i = 0; i < linkNodes.size(); i++) {
            Node linkNode = linkNodes.get(i);
            String id = getAttribute(linkNode, PROP_ID);
            int sourceIndex = getIntAttribute(linkNode, PROP_SOURCE_INDEX);
            int targetIndex = getIntAttribute(linkNode, PROP_TARGET_INDEX);
            boolean defaultLink = Boolean.parseBoolean(getAttribute(linkNode, PROP_DEFAULT_LINK));
            boolean trueLink = Boolean.parseBoolean(getAttribute(linkNode, PROP_TRUE_LINK));

            linkDefs.add(new LinkDef(id, sourceIndex, targetIndex, defaultLink, trueLink));
        }

        return linkDefs;
	}
	
	private DataMap readConfig(Node parentNode) {
		Node propertiesNode = getChildNode(parentNode, PROP_PROPERTIES);
		if(propertiesNode == null) return new DataMap();

		List<Node> entryNodes = getValidChildNodes(propertiesNode, PROP_PROPERTY);
		DataMap config = new DataMap();
		
		for(Node entryNode: entryNodes) {
			String key = getAttribute(entryNode, PROP_KEY);
			String type = getAttribute(entryNode, PROP_TYPE);
			String valueStr = getAttribute(entryNode, PROP_VALUE);
			config.put(key, DataTypeEnum.findByDisplayName(type).parse(valueStr));
		}
			
		return config;
	}
	
    private int getIntAttribute(Node node, String attributeName){
        return Integer.parseInt(getAttribute(node, attributeName));
    }
    
    private String getAttribute(Node node, String attributeName){
        NamedNodeMap map = node.getAttributes();
        for(int i = 0; i < map.getLength(); i++){
            if(attributeName.equals(map.item(i).getNodeName()))
                return map.item(i).getNodeValue();
        }
        
        return null;
    }
    
    private boolean isValidNode(Node node) {
        return !node.getNodeName().equals("#text");
    }
    
    private boolean isValidNode(Node node, String name) {
		return node.getNodeName().equals(name);
	}
    
    private List<Node> getValidChildNodes(Node node) {
        List<Node> nl = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
             for(int i = 0; i < nodeList.getLength(); i++){
            if(isValidNode(nodeList.item(i)))
                nl.add(nodeList.item(i));
        }
        return nl;
    }
    
	private List<Node> getValidChildNodes(Node node, String name) {
		List<Node> nl = new ArrayList<>();
		if(node == null)
			return nl;

		NodeList nodeList = node.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++){
			if(isValidNode(nodeList.item(i), name))
				nl.add(nodeList.item(i));
		}
		return nl;
	}

	private Node getChildNode(Node node, String name) {
        List<Node> children = getValidChildNodes(node);
        Node found = null;

        for(int i = 0; i < children.size(); ++i) {
            if (((Node)children.get(i)).getNodeName().equalsIgnoreCase(name)) {
                found = (Node)children.get(i);
                break;
            }
        }

        return found;
    }
}
