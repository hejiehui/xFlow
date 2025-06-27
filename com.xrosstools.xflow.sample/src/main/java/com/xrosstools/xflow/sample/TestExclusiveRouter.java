package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.ExclusiveRouter;
import com.xrosstools.xflow.XflowContext;

public class TestExclusiveRouter extends TestAdapter implements ExclusiveRouter {
	public static final String PROP_KEY_PATH = "path";

	@Override
	public String route(XflowContext context) {
		call(context);
		return context.get(PROP_KEY_PATH);
	}
}
