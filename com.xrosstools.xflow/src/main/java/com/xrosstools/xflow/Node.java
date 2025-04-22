package com.xrosstools.xflow;

import java.util.Map;

public interface Node {
	String getId();
	Map<String, Link> getOutputs();
}
