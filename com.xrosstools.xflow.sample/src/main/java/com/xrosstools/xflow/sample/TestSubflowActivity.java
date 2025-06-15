package com.xrosstools.xflow.sample;

import java.util.concurrent.atomic.AtomicInteger;

import com.xrosstools.xflow.SubflowActivity;
import com.xrosstools.xflow.XflowContext;

public class TestSubflowActivity implements SubflowActivity {
	public static final String COUNT = "count";

	@Override
	public XflowContext createContext(XflowContext parentContext) {
		XflowContext subflowContext = new XflowContext();
		int count = parentContext.get(COUNT);
		subflowContext.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(count));
		return subflowContext;
	}

	@Override
	public void mergeContext(XflowContext parentContext, XflowContext subFlowContext) {
		AtomicInteger counter = subFlowContext.get(TestAutoActivity.PROP_KEY_COUNTER);
		parentContext.put(COUNT, counter.get());
	}
}
