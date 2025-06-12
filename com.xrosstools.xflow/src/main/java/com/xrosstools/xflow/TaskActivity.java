package com.xrosstools.xflow;

import java.util.List;

public interface TaskActivity {
	/**
	 * @return id that differentiates tasks created each time  
	 */
	List<Task> create(XflowContext context);

	/**
	 * @return true if activity completed
	 */
	boolean submit(XflowContext context, List<Task> tasks, Task task);
}
