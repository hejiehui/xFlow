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
import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowFactory;

public class XflowDef {
	private String name;
    private List<NodeDef> nodeDefs;
    private List<LinkDef> linkDefs;
    private DataMap globalConfig;
    private DataMap flowConfig;

	public XflowDef(String name, List<NodeDef> nodeDefs, List<LinkDef> linkDefs,
			DataMap globalConfig,
			DataMap flowConfig) {
		this.name = name;
		this.nodeDefs = nodeDefs;
		this.linkDefs = linkDefs;
		this.globalConfig = globalConfig;
		this.flowConfig = flowConfig;
	}

	public String getName() {
		return name;
	}

	public Xflow create(XflowFactory fafactory) {
		List<Node> nodes = new LinkedList<>();
		for(NodeDef nodeDef: nodeDefs)
			nodes.add(createNode(nodeDef));
	
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
		
		return new Xflow(fafactory, name, nodes);
	}
	
	private Node createNode(NodeDef nodeDef) {
		Node node = nodeDef.create();

		node.initGlobalConfig(globalConfig.copy());
		node.initFlowConfig(flowConfig.copy());
		node.initNodeConfig(nodeDef.getConfig().copy());
		
		return node;
	}
}
