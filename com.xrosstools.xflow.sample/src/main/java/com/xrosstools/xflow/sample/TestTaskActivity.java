package com.xrosstools.xflow.sample;

import java.util.ArrayList;
import java.util.List;

import com.xrosstools.xflow.DataMap;
import com.xrosstools.xflow.FlowConfigAware;
import com.xrosstools.xflow.NodeConfigAware;
import com.xrosstools.xflow.Task;
import com.xrosstools.xflow.TaskActivity;
import com.xrosstools.xflow.XflowContext;

public class TestTaskActivity implements TaskActivity, NodeConfigAware, FlowConfigAware {
	private int count;
	private String assignee;

	@Override
	public void initFlowConfig(DataMap config) {
		assignee = config.get("assignee");
	}

	@Override
	public void initNodeConfig(DataMap config) {
		count = config.get("count");		
	}

	@Override
	public List<Task> create(XflowContext context) {
		List<Task> tasks = new ArrayList<>();
		for(int i = 0; i < count; i++) {
			tasks.add(new FeedbackTask("id_" + i, assignee));
		}
		return tasks;
	}

	@Override
	public boolean submit(XflowContext context, List<Task> tasks, Task task) {
		return ((FeedbackTask)task).getFeedback().equals("OK");
	}
}
