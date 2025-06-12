package com.xrosstools.xflow.def;

import java.lang.reflect.Method;

import com.xrosstools.xflow.AutoActivity;
import com.xrosstools.xflow.BinaryRouter;
import com.xrosstools.xflow.ExclusiveRouter;
import com.xrosstools.xflow.InclusiveRouter;
import com.xrosstools.xflow.XflowContext;

public class MethodWrapper {
	public final Class<?>[] parameterClasses = new Class[] {XflowContext.class};

	protected Object instance;
	protected Method method;
	
	Object invoke(Object...params) {
		try {
			return method.invoke(instance, params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class ActivityMethodWrapper extends MethodWrapper implements AutoActivity {
		@Override
		public void execute(XflowContext context) {
			invoke(context);
		}
	}
	
	public static class BinaryRouteMethodWrapper extends MethodWrapper implements BinaryRouter {
		@Override
		public boolean route(XflowContext context) {
			return (boolean)invoke(context);
		}
	}
	
	public static class InclusiveRouteMethodWrapper extends MethodWrapper implements InclusiveRouter {
		@Override
		public String[] route(XflowContext context) {
			return (String[])invoke(context);
		}
	}
	
	public static class ExclusiveRouteMethodWrapper extends MethodWrapper implements ExclusiveRouter {
		public String route(XflowContext context) {
			return (String)invoke(context);
		}
	}
}
