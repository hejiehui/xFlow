package com.xrosstools.xflow.sample;

import java.util.concurrent.atomic.AtomicInteger;

import com.xrosstools.xflow.AutoActivity;
import com.xrosstools.xflow.XflowContext;

public class TestAddOne extends TestAdapter implements AutoActivity {
	public static final String PROP_KEY_COUNTER = "counter";

	@Override
	public void execute(XflowContext context) {
		call(context);

		AtomicInteger counter = context.get(PROP_KEY_COUNTER);
		counter.addAndGet(1);
	}
}
