package com.xrosstools.xflow.sample;

import java.util.concurrent.atomic.AtomicInteger;

import com.xrosstools.xflow.SubflowActivity;
import com.xrosstools.xflow.XflowContext;

public class TestSubflowActivity extends TestAdapter implements SubflowActivity {
	public static final String COUNT = "count";
	public static final String ERROR = "error";

	@Override
	public XflowContext createContext(XflowContext parentContext) {
		call(parentContext);

		XflowContext subflowContext = new XflowContext();
		
		if(parentContext.contains(ERROR))
			injectException(subflowContext, (Exception)subflowContext.get(ERROR));
		
		if(parentContext.contains(SUB_FLOW_SUSPEND))
			injectSuspend(subflowContext, nodeStarted, START_NODE);
		
		int count = parentContext.get(COUNT);
		subflowContext.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(count));
		
		return subflowContext;
	}

	@Override
	public void mergeSubflow(XflowContext parentContext, XflowContext subFlowContext) {
		call(parentContext);

		AtomicInteger counter = subFlowContext.get(TestAutoActivity.PROP_KEY_COUNTER);
		parentContext.put(COUNT, counter.get());
	}
}
