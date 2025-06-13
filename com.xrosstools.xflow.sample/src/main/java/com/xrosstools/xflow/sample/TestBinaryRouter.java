package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.BinaryRouter;
import com.xrosstools.xflow.XflowContext;

public class TestBinaryRouter implements BinaryRouter {
	public static final String PROP_KEY_RESULT = "result";
	
	@Override
	public boolean route(XflowContext context) {
		return context.get(PROP_KEY_RESULT);
	}
}
