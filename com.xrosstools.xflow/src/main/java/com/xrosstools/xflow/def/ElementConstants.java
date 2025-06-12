package com.xrosstools.xflow.def;

/**
 * All element names in XML model
 */
public interface ElementConstants {
    String XFLOW = "xflow";
    String FLOW = "flow";

	String NODES = "nodes";

	String START_NODE = "start";
	String END_NODE = "end";

	String AUTO_ACTIVITY_NODE = "auto_activity";
	String TASK_ACTIVITY_NODE = "task_activity";
	String EVENT_ACTIVITY_NODE = "event_activity";
	String WAIT_ACTIVITY_NODE = "wait_activity";

	String BINARY_ROUTER_NODE = "binary_router";
	String INCLUSIVE_ROUTER_NODE = "inclusive_router";
	String EXCLUSIVE_ROUTER_NODE = "exclusive_router";
	String PARALLE_ROUTER_NODE = "parallel_router";

    String LINKS = "links";
    String LINK = "link";

    String PROP_ID = "id";
    String PROP_LABEL = "Label";
    String PROP_DESCRIPTION = "Description";
    String PROP_LOCATION = "location";
    String PROP_IMPLEMENTATION = "implementation";
    String PROP_SOURCE_INDEX = "source_index";
    String PROP_TARGET_INDEX = "target_index";
    String PROP_DEFAULT_LINK = "default_link";
    String PROP_TRUE_LINK = "true_link";
    
    String PROP_DELAY = "delay";
    String PROP_TIME_UNIT = "time_unit";
    
    String SEPARATOR = "::";

    String PROP_PROPERTIES = "properties";
    String PROP_PROPERTY = "property";
    String PROP_KEY = "key";
    String PROP_VALUE = "value";
    String PROP_TYPE = "type";
}
