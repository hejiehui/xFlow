package com.xrosstools.xflow;

import java.util.Map;

public interface ManualTask {
	String getAssignee();
	
	boolean execute(XflowContext ctx, String actor, Map<String, ?> request) throws Exception;
}
