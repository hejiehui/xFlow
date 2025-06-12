package com.xrosstools.xflow.sample;

import java.util.concurrent.atomic.AtomicInteger;

import com.xrosstools.xflow.AutoActivity;
import com.xrosstools.xflow.DataMap;
import com.xrosstools.xflow.NodeConfigAware;
import com.xrosstools.xflow.XflowContext;

public class TestAutoActivity implements AutoActivity, NodeConfigAware {
	private int step;
	@Override
	public void execute(XflowContext context) {
		AtomicInteger counter = context.get("counter");
		counter.addAndGet(step);
	}

	@Override
	public void initNodeConfig(DataMap config) {
		step = config.get("step");
	}
}
