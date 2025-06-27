package com.xrosstools.xflow.sample;

import java.util.concurrent.atomic.AtomicInteger;

import com.xrosstools.xflow.AutoActivity;
import com.xrosstools.xflow.DataMap;
import com.xrosstools.xflow.NodeConfigAware;
import com.xrosstools.xflow.XflowContext;

public class TestAutoActivity extends TestAdapter implements AutoActivity, NodeConfigAware {
	public static final String PROP_KEY_COUNTER = "counter";
	public static final String PROP_KEY_STEP = "step";

	private int step;
	@Override
	public void execute(XflowContext context) {
		call(context);

		AtomicInteger counter = context.get(PROP_KEY_COUNTER);
		counter.addAndGet(step);
	}

	@Override
	public void initNodeConfig(DataMap config) {
		step = config.get(PROP_KEY_STEP);
	}
}
