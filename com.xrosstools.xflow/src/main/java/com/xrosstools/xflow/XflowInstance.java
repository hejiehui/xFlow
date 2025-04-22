package com.xrosstools.xflow;

import java.util.List;

public interface XflowInstance {
	String getId();
	XflowContext getContext();
	List<ManualTask> getManualTasks();
}
