package com.xrosstools.xflow.sample;

import java.util.concurrent.atomic.AtomicInteger;

import com.xrosstools.xflow.AutoActivity;
import com.xrosstools.xflow.DataMap;
import com.xrosstools.xflow.GlobalConfigAware;
import com.xrosstools.xflow.NodeConfigAware;
import com.xrosstools.xflow.XflowContext;

public class TestAutoActivity extends TestAdapter implements AutoActivity, NodeConfigAware, GlobalConfigAware {
	public static final String PROP_KEY_COUNTER = "counter";
	public static final String PROP_KEY_STEP = "step";

	private int step;
	
	private DataMap config;
	@Override
	public void execute(XflowContext context) {
		call(context);

		AtomicInteger counter = context.get(PROP_KEY_COUNTER);
		counter.addAndGet(step);
		
		if(config.contains("globalA"))
			context.copyFrom(config, "globalA", "globalB", "gBool");
	}

	@Override
	public void initNodeConfig(DataMap config) {
		step = config.get(PROP_KEY_STEP);
	}

	@Override
	public void initGlobalConfig(DataMap config) {
		this.config = config;
	}
}
