package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.InclusiveRouter;
import com.xrosstools.xflow.XflowContext;

public class TestInclusiveRouter extends TestAdapter implements InclusiveRouter {
	public static final String PROP_KEY_PATHES = "pathes";

	@Override
	public String[] route(XflowContext context) {
		call(context);
		if(!context.contains(PROP_KEY_PATHES))
			return null;

		String pathes = context.get(PROP_KEY_PATHES);
		return pathes.split(",");
	}
}
