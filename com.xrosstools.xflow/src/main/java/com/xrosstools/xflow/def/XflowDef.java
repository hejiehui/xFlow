package com.xrosstools.xflow.def;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xrosstools.xflow.DataMap;
import com.xrosstools.xflow.FlowConfigAware;
import com.xrosstools.xflow.GlobalConfigAware;
import com.xrosstools.xflow.Link;
import com.xrosstools.xflow.Node;
import com.xrosstools.xflow.NodeConfigAware;
import com.xrosstools.xflow.RouterNode;
import com.xrosstools.xflow.TaskActivity;
import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowFactory;
import com.xrosstools.xflow.XflowListener;

public class XflowDef {
	private String name;
	private ImplementationDef<XflowListener> listenerDef;
    private List<NodeDef> nodeDefs;
    private List<LinkDef> linkDefs;
    private DataMap globalConfig;
    private DataMap flowConfig;

	public XflowDef(String name, ImplementationDef<XflowListener> listenerDef, List<NodeDef> nodeDefs, List<LinkDef> linkDefs,
			DataMap globalConfig,
			DataMap flowConfig) {
		this.name = name;
		this.listenerDef = listenerDef;
		this.nodeDefs = nodeDefs;
		this.linkDefs = linkDefs;
		this.globalConfig = globalConfig;
		this.flowConfig = flowConfig;
	}

	public String getName() {
		return name;
	}

	public Xflow create(XflowFactory factory) {
		List<Node> nodes = new LinkedList<>();
		List<RouterNode> routerNodes = new LinkedList<>();

		for(NodeDef nodeDef: nodeDefs) {
			Node node = createNode(nodeDef);
			nodes.add(node);
			if(node instanceof RouterNode) {
				routerNodes.add((RouterNode)node);
			}
		}
	
		Map<Integer, List<Link>> links = new HashMap<>();
		for(LinkDef linkDef: linkDefs) {
			Integer sourceId = linkDef.getSourceIndex();
			Link link = linkDef.create(nodes);
			if(!links.containsKey(sourceId))
				links.put(sourceId, new LinkedList<Link>());
			List<Link> outputs = links.get(sourceId);
			outputs.add(link);
		}
		
		for(Integer sourceId: links.keySet()) {
			if(!links.containsKey(sourceId))
				continue;

			List<Link> outputs = links.get(sourceId);
			nodes.get(sourceId).setOutputs(outputs.toArray(new Link[outputs.size()]));
		}
		
		initRouterNode(routerNodes);
		
		return new Xflow(factory, name, nodes, listenerDef.create());
	}
	
	private void initRouterNode(List<RouterNode> routerNodes) {
		for(RouterNode node: routerNodes) {
			node.visit();
		}
		
		for(RouterNode node: routerNodes) {
			node.checkEnd();
		}
		
		for(RouterNode node: routerNodes) {
			node.displayRouteInfo();
		}
	}
	
	private Node createNode(NodeDef nodeDef) {
		Node node = nodeDef.create();

		node.initGlobalConfig(globalConfig.copy());
		node.initFlowConfig(flowConfig.copy());
		node.initNodeConfig(nodeDef.getConfig().copy());
		
		return node;
	}
}
